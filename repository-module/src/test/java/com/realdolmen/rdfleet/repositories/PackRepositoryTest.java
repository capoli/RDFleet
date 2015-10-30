package com.realdolmen.rdfleet.repositories;

import com.realdolmen.rdfleet.config.JpaConfig;
import com.realdolmen.rdfleet.domain.Pack;
import com.realdolmen.rdfleet.util.ValidDomainObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ConstraintViolationException;

/**
 * Created by JSTAX29 on 30/10/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JpaConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PackRepositoryTest {
    @Autowired
    private PackRepository packRepository;
    private Pack pack;

    @Before
    public void initialize(){
        pack = ValidDomainObjectFactory.createPack();
    }

    @Test(expected = ConstraintViolationException.class)
    public void testNameNull(){
        pack.setName(null);
        packRepository.save(pack);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testNameBlank(){
        pack.setName("");
        packRepository.save(pack);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testItemsNull(){
        pack.setItems(null);
        packRepository.save(pack);
    }
}
