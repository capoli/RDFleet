package com.realdolmen.rdfleet.service;

import com.realdolmen.rdfleet.domain.Pack;
import com.realdolmen.rdfleet.repositories.PackRepository;
import com.realdolmen.rdfleet.service.util.ValidDomainObjectFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PackServiceTest {
    private PackRepository packRepositoryMock;
    private PackService packService;
    private List<Pack> packList;
    private Pack pack1;

    @Before
    public void initialize(){
        packRepositoryMock = mock(PackRepository.class);

        packService = new PackService();
        packService.setPackRepository(packRepositoryMock);

        pack1 = ValidDomainObjectFactory.createPack();
        Pack pack2 = ValidDomainObjectFactory.createPack();
        packList = new ArrayList<>(Arrays.asList(pack1, pack2));
        when(packRepositoryMock.findAll()).thenReturn(packList);
        when(packRepositoryMock.findOne(1l)).thenReturn(pack1);
    }

    @Test
    public void testFindAll(){
        assertEquals(packList, packService.findAllPacks());
        verify(packRepositoryMock).findAll();
    }

    @Test
    public void testFindById(){
        assertEquals(pack1, packService.findPackById(1l));
        verify(packRepositoryMock).findOne(1l);
    }

    @Test
    public void testFindByIdNoneFound(){
        assertNull(packService.findPackById(2l));
        verify(packRepositoryMock).findOne(2l);
    }

    @Test
    public void testCreatePack(){
        packService.createPack(pack1);
        verify(packRepositoryMock).save(pack1);
    }

}
