package com.realdolmen.rdfleet.service;

import com.realdolmen.rdfleet.domain.CarOption;
import com.realdolmen.rdfleet.repositories.CarOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarOptionService {
    private CarOptionRepository carOptionRepository;

    @Autowired
    public void setCarRepository(CarOptionRepository carOptionRepository) {
        this.carOptionRepository = carOptionRepository;
    }

    //TODO: test
    public List<CarOption> findAllCarOptions() {
        List<CarOption> carOptions = carOptionRepository.findAll();
        if(carOptions.size() == 0) throw new IllegalArgumentException("There are no caroptions available");
        return carOptions;
    }

    public CarOption findCarOptionById(Long carOptionId){
        return carOptionRepository.findOne(carOptionId);
    }

    public void createCarOption(CarOption carOption) {
        carOptionRepository.save(carOption);
    }
}
