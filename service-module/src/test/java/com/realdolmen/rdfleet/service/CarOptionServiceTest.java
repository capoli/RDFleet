package com.realdolmen.rdfleet.service;

import com.realdolmen.rdfleet.domain.CarOption;
import com.realdolmen.rdfleet.repositories.CarOptionRepository;
import com.realdolmen.rdfleet.service.util.ValidDomainObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * Created by JSTAX29 on 9/11/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class CarOptionServiceTest {
    private CarOptionRepository carOptionRepositoryMock;
    private CarOptionService carOptionService;
    private List<CarOption> carOptionList;
    private CarOption carOption1;

    @Before
    public void initialize(){
        carOptionRepositoryMock = mock(CarOptionRepository.class);

        carOptionService = new CarOptionService();
        carOptionService.setCarOptionRepository(carOptionRepositoryMock);

        carOption1 = ValidDomainObjectFactory.createCarOption("Trekhaak");
        CarOption carOption2 = ValidDomainObjectFactory.createCarOption("Automatiek");
        carOptionList = new ArrayList<>(Arrays.asList(carOption1, carOption2));
        when(carOptionRepositoryMock.findAll()).thenReturn(carOptionList);
        when(carOptionRepositoryMock.findOne(1l)).thenReturn(carOption1);
    }

    @Test
    public void testFindAll(){
        assertEquals(carOptionList, carOptionService.findAllCarOptions());
        verify(carOptionRepositoryMock).findAll();
    }

    @Test
    public void testFindById(){
        assertEquals(carOption1, carOptionService.findCarOptionById(1l));
        verify(carOptionRepositoryMock).findOne(1l);
    }

    @Test
    public void testFindByIdNoneFound(){
        assertNull(carOptionService.findCarOptionById(2l));
        verify(carOptionRepositoryMock).findOne(2l);
    }
    
    @Test
    public void testCreateCarOption(){
        carOptionService.createCarOption(carOption1);
        verify(carOptionRepositoryMock).save(carOption1);
    }


}
