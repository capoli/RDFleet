package com.realdolmen.rdfleet.repositories;

import com.realdolmen.rdfleet.config.JpaConfig;
import com.realdolmen.rdfleet.domain.CarOption;
import com.realdolmen.rdfleet.util.ValidDomainObjectFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ConstraintViolationException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JpaConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CarOptionRepositoryTest {
    @Autowired
    private CarOptionRepository carOptionRepository;


    @Test(expected = ConstraintViolationException.class)
    public void testDescriptionIsNull(){
        CarOption carOption = ValidDomainObjectFactory.createCarOption(null);
        carOptionRepository.save(carOption);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testDescriptionIsEmpty(){
        CarOption carOption = ValidDomainObjectFactory.createCarOption("");
        carOptionRepository.save(carOption);
    }

    @Test
    public void testCarOptionOk(){
        CarOption trekhaak = ValidDomainObjectFactory.createCarOption("Trekhaak");
        carOptionRepository.save(trekhaak);
        Assert.assertNotNull(trekhaak.getId());
    }
}
