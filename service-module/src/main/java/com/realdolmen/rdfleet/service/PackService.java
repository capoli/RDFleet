package com.realdolmen.rdfleet.service;

import com.realdolmen.rdfleet.domain.Pack;
import com.realdolmen.rdfleet.repositories.PackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by JSTAX29 on 6/11/2015.
 */
@Service
public class PackService {
    private PackRepository packRepository;

    @Autowired
    public void setPackRepository(PackRepository packRepository) {
        this.packRepository = packRepository;
    }

    public List<Pack> findAllPacks(){
        return packRepository.findAll();
    }

    public Pack findPackById(Long id){
        return packRepository.findOne(id);
    }

    public void createPack(Pack pack) {
        packRepository.save(pack);
    }
}
