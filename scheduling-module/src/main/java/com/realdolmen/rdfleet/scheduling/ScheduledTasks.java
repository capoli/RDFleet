package com.realdolmen.rdfleet.scheduling;

import com.realdolmen.rdfleet.domain.*;
import com.realdolmen.rdfleet.repositories.CarRepository;
import com.realdolmen.rdfleet.repositories.EmployeeCarRepository;
import com.realdolmen.rdfleet.repositories.OrderRepository;
import com.realdolmen.rdfleet.repositories.RdEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by JSTAX29 on 28/10/2015.
 */
@Component
public class ScheduledTasks {
    @Autowired
    private RdEmployeeRepository rdEmployeeRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    public void setRdEmployeeRepository(RdEmployeeRepository rdEmployeeRepository) {
        this.rdEmployeeRepository = rdEmployeeRepository;
    }

    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Will run the car renewal notification process every saturday at 2am.
     * When a car has reached 160.000km or when the car has been owned for 4 years, the employee should be notified he should get a new car.
     */
//    @Scheduled(cron = "0 0 2 * * SAT")
    @Scheduled(fixedRate = (1000 * 60 * 15)) //15 minutes for testing purposes
    public void checkForNeededCarRenewals() throws MessagingException {
        List<RdEmployee> allEmployeesInService = rdEmployeeRepository.findAllEmployeesInService();

        for (RdEmployee employee : allEmployeesInService) {
            Order currentOrder = employee.getCurrentOrder();
            if (currentOrder == null) continue;
            EmployeeCar orderedCar = currentOrder.getOrderedCar();
            if (orderedCar == null) continue;

            //If the car is not being used, skip it.
            if (orderedCar.getCarStatus() != CarStatus.IN_USE) continue;

            //If the car was received longer than 4 years ago, send a notification
            LocalDate fourYearsAgo = LocalDate.now().minusYears(4);
            if (currentOrder.getDateReceived().isBefore(fourYearsAgo)) {
                String message = String.format("<p>Hello %s %s,</p>\n" +
                                "<p>It's time to order a new car! You have owned your car (%s) for more than 4 years.</p>",
                        employee.getFirstName(), employee.getLastName(), orderedCar.getLicensePlate());

                sendMail(employee.getEmail(), "Time to order a new car!", message);
                //Continue, we sent a message.
                continue;
            }

            //If the car has over 160.000km, also send a notification
            if (orderedCar.getMileage() > 160_000) {
                String message = String.format("<p>Hello %s %s,</p>\n" +
                                "<p>It's time to order a new car! Your car (%s) has driven more than 160,000km. %,dkm to be more precise.</p>",
                        employee.getFirstName(), employee.getLastName(), orderedCar.getLicensePlate(), orderedCar.getMileage());

                sendMail(employee.getEmail(), "Time to order a new car!", message);
            }
        }
    }

    /**
     * Helper method to send an email.
     *
     * @param to      the receiver of the email
     * @param subject the subject of the email
     * @param body    the body of the email
     * @throws MessagingException
     */
    private void sendMail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setSubject(subject);
        helper.setTo(to);
        helper.setText(body, true);
        javaMailSender.send(message);
    }
}
