package com.realdolmen.rdfleet.scheduling;

import com.realdolmen.rdfleet.config.JpaConfig;
import com.realdolmen.rdfleet.domain.CarStatus;
import com.realdolmen.rdfleet.domain.RdEmployee;
import com.realdolmen.rdfleet.repositories.RdEmployeeRepository;
import com.realdolmen.rdfleet.scheduling.util.ValidDomainObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ScheduledTasksTest {

    private RdEmployeeRepository rdEmployeeRepositoryMock;
    private JavaMailSender javaMailSenderMock;

    private ScheduledTasks scheduledTasks;

    private RdEmployee rdEmployee;

    @Before
    public void initialize() {
        rdEmployeeRepositoryMock = mock(RdEmployeeRepository.class);
        javaMailSenderMock = mock(JavaMailSender.class);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSenderMock.createMimeMessage()).thenReturn(mimeMessage);

        scheduledTasks = new ScheduledTasks();
        scheduledTasks.setJavaMailSender(javaMailSenderMock);
        scheduledTasks.setRdEmployeeRepository(rdEmployeeRepositoryMock);

        rdEmployee = ValidDomainObjectFactory.createRdEmployee();
    }

    @Test
    public void testCarRenewalNotificationMileageReached() throws MessagingException {
        rdEmployee.getCurrentOrder().getOrderedCar().setMileage(170_000);
        when(rdEmployeeRepositoryMock.findAllEmployeesInService()).thenReturn(new ArrayList<>(Collections.singletonList(rdEmployee)));

        scheduledTasks.checkForNeededCarRenewals();

        verify(javaMailSenderMock).send(any(MimeMessage.class));
    }


    @Test
    public void testCarRenewalNotificationMileageNotReached() throws MessagingException {
        rdEmployee.getCurrentOrder().getOrderedCar().setMileage(84_654);
        when(rdEmployeeRepositoryMock.findAllEmployeesInService()).thenReturn(new ArrayList<>(Collections.singletonList(rdEmployee)));

        scheduledTasks.checkForNeededCarRenewals();

        verify(javaMailSenderMock, times(0)).send(any(MimeMessage.class));
    }

    @Test
    public void testCarRenewalNotificationDateReached() throws MessagingException {
        rdEmployee.getCurrentOrder().setDateReceived(LocalDate.now().minusYears(4).minusDays(1));
        when(rdEmployeeRepositoryMock.findAllEmployeesInService()).thenReturn(new ArrayList<>(Collections.singletonList(rdEmployee)));

        scheduledTasks.checkForNeededCarRenewals();

        verify(javaMailSenderMock).send(any(MimeMessage.class));
    }


    @Test
    public void testCarRenewalNotificationDateNotReached() throws MessagingException {
        rdEmployee.getCurrentOrder().setDateReceived(LocalDate.now().minusYears(3));
        when(rdEmployeeRepositoryMock.findAllEmployeesInService()).thenReturn(new ArrayList<>(Collections.singletonList(rdEmployee)));

        scheduledTasks.checkForNeededCarRenewals();

        verify(javaMailSenderMock, times(0)).send(any(MimeMessage.class));
    }

    @Test
    public void testCarRenewalNotificationNoNotifications() throws MessagingException {
        when(rdEmployeeRepositoryMock.findAllEmployeesInService()).thenReturn(new ArrayList<>(Arrays.asList(rdEmployee, rdEmployee)));

        scheduledTasks.checkForNeededCarRenewals();

        verify(javaMailSenderMock, times(0)).send(any(MimeMessage.class));
    }

    @Test
    public void testCarRenewalNotificationCarNotInUse() throws MessagingException {
        rdEmployee.getCurrentOrder().getOrderedCar().setCarStatus(CarStatus.NOT_USED);
        rdEmployee.getCurrentOrder().getOrderedCar().setMileage(170_000);
        when(rdEmployeeRepositoryMock.findAllEmployeesInService()).thenReturn(new ArrayList<>(Collections.singletonList(rdEmployee)));

        scheduledTasks.checkForNeededCarRenewals();

        verify(javaMailSenderMock, times(0)).send(any(MimeMessage.class));
    }

    @Test
    public void testCarRenewalNotificationMultipleMileagesReached() throws MessagingException {
        RdEmployee rdEmployee1 = ValidDomainObjectFactory.createRdEmployee();
        rdEmployee1.setEmail("Another@Employee.com");
        rdEmployee1.getCurrentOrder().getOrderedCar().setMileage(184_684);
        RdEmployee rdEmployee2 = ValidDomainObjectFactory.createRdEmployee();
        rdEmployee2.setEmail("Different@Employee.com");
        rdEmployee2.getCurrentOrder().getOrderedCar().setMileage(174_684);
        RdEmployee rdEmployee3 = ValidDomainObjectFactory.createRdEmployee();
        rdEmployee3.setEmail("AnotherDifferent@Employee.com");
        rdEmployee3.getCurrentOrder().getOrderedCar().setMileage(84_684);

        when(rdEmployeeRepositoryMock.findAllEmployeesInService()).thenReturn(new ArrayList<>(Arrays.asList(rdEmployee, rdEmployee1, rdEmployee2, rdEmployee3)));

        scheduledTasks.checkForNeededCarRenewals();

        verify(javaMailSenderMock, times(2)).send(any(MimeMessage.class));
    }
}
