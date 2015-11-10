package com.realdolmen.rdfleet.service;

import com.realdolmen.rdfleet.domain.Car;
import com.realdolmen.rdfleet.domain.CarOption;
import com.realdolmen.rdfleet.repositories.CarOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class CarOptionService {
    private CarOptionRepository carOptionRepository;

    @Autowired
    public void setCarOptionRepository(CarOptionRepository carOptionRepository) {
        this.carOptionRepository = carOptionRepository;
    }

    public List<CarOption> findAllCarOptions() {
        List<CarOption> carOptions = carOptionRepository.findAll();
        if (carOptions.isEmpty())
            throw new IllegalArgumentException("There are no caroptions available");
        return carOptions;
    }

    public List<CarOption> findAllCarOptionsByTowingBracketPossibility(boolean towingBracketPossibility) {
        List<CarOption> carOptions = findAllCarOptions();
        if (!towingBracketPossibility) {
            Iterator<CarOption> iterator = carOptions.iterator();
            while (iterator.hasNext()) {
                CarOption carOption = iterator.next();
                if (carOption.getDescription().toLowerCase().contains("trekhaak")) {
                    iterator.remove();
                }
            }
        }
        return carOptions;
    }

    public CarOption findCarOptionById(Long carOptionId) {
        return carOptionRepository.findOne(carOptionId);
    }

    public void createCarOption(CarOption carOption) {
        carOptionRepository.save(carOption);
    }
}
