package com.realdolmen.rdfleet.repositories;

import com.realdolmen.rdfleet.config.JpaConfig;
import com.realdolmen.rdfleet.domain.*;
import com.realdolmen.rdfleet.util.ValidDomainObjectFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JpaConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;
    private Order order;

    @Before
    public void initialize() {
        order = ValidDomainObjectFactory.createOrder();
    }

    @Test
    public void testSaveOrder() {
        orderRepository.save(order);
        Assert.assertNotNull(order.getId());
    }

    @Test(expected = ConstraintViolationException.class)
    public void testDateOrderedNull() {
        order.setDateOrdered(null);
        orderRepository.save(order);
    }

    @Test
    public void testDateOrderedToday() {
        order.setDateOrdered(LocalDate.now());
        orderRepository.save(order);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testDateOrderedFuture() {
        order.setDateOrdered(LocalDate.now().plusDays(1));
        orderRepository.save(order);
    }

    @Test
    public void testDateOrderedPast() {
        order.setDateOrdered(LocalDate.now().minusDays(1));
        orderRepository.save(order);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testAmountPaidByEmployeeNull() {
        order.setAmountPaidByEmployee(null);
        orderRepository.save(order);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testAmountPaidByEmployeeNegative() {
        order.setAmountPaidByEmployee(BigDecimal.valueOf(-52));
        orderRepository.save(order);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testAmountPaidByCompanyNull() {
        order.setAmountPaidByCompany(null);
        orderRepository.save(order);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testAmountPaidByCompanyNegative() {
        order.setAmountPaidByCompany(BigDecimal.valueOf(-500));
        orderRepository.save(order);
    }

    @Test
    public void testDateReceivedToday() {
        order.setDateReceived(LocalDate.now());
        orderRepository.save(order);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testDateReceivedFuture() {
        order.setDateReceived(LocalDate.now().plusDays(1));
        orderRepository.save(order);
    }

    @Test
    public void testDateReceivedPast() {
        order.setDateReceived(LocalDate.now().minusDays(1));
        orderRepository.save(order);
    }
}
