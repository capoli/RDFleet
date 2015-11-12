package com.realdolmen.rdfleet.soap;

import com.realdolmen.rdfleet.config.JpaConfig;
import com.realdolmen.rdfleet.domain.*;
import com.realdolmen.rdfleet.repositories.EmployeeCarRepository;
import com.realdolmen.rdfleet.web_services.MileageUpdateObject;
import com.realdolmen.rdfleet.web_services.MileageUpdateRequest;
import com.realdolmen.rdfleet.web_services.MileageUpdateResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MileageUpdateLogicTest {

    private EmployeeCarRepository employeeCarRepositoryMock;
    private MileageUpdateObjectEndpoint mileageUpdateObjectEndpoint;
    private EmployeeCar employeeCar1;
    private EmployeeCar employeeCar2;
    private EmployeeCar employeeCar3;
    private MileageUpdateObject mileageUpdateObject1;
    private MileageUpdateRequest mileageUpdateRequest;

    @Before
    public void initialize() {
        employeeCarRepositoryMock = mock(EmployeeCarRepository.class);
        mileageUpdateObjectEndpoint = new MileageUpdateObjectEndpoint();
        mileageUpdateObjectEndpoint.setEmployeeCarRepository(employeeCarRepositoryMock);

        employeeCar1 = createEmployeeCar();
        employeeCar2 = createEmployeeCar();
        employeeCar2.setLicensePlate("1-XYZ-865");
        employeeCar3 = createEmployeeCar();
        employeeCar3.setLicensePlate("PLK-568");

        mileageUpdateObject1 = new MileageUpdateObject();
        mileageUpdateObject1.setLicensePlate(employeeCar1.getLicensePlate());
        mileageUpdateObject1.setMileageDriven(employeeCar1.getMileage() + 156);

        MileageUpdateObject mileageUpdateObject2 = new MileageUpdateObject();
        mileageUpdateObject2.setLicensePlate(employeeCar2.getLicensePlate());
        mileageUpdateObject2.setMileageDriven(employeeCar2.getMileage() + 485);

        MileageUpdateObject mileageUpdateObject3 = new MileageUpdateObject();
        mileageUpdateObject3.setLicensePlate(employeeCar3.getLicensePlate());
        mileageUpdateObject3.setMileageDriven(employeeCar3.getMileage() + 214);

        mileageUpdateRequest = new MileageUpdateRequest();
        mileageUpdateRequest.getMileageUpdateObject().add(mileageUpdateObject1);
        mileageUpdateRequest.getMileageUpdateObject().add(mileageUpdateObject2);
        mileageUpdateRequest.getMileageUpdateObject().add(mileageUpdateObject3);

        when(employeeCarRepositoryMock.findByLicensePlateIgnoreCase(employeeCar1.getLicensePlate())).thenReturn(employeeCar1);
        when(employeeCarRepositoryMock.findByLicensePlateIgnoreCase(employeeCar2.getLicensePlate())).thenReturn(employeeCar2);
        when(employeeCarRepositoryMock.findByLicensePlateIgnoreCase(employeeCar3.getLicensePlate())).thenReturn(employeeCar3);
    }

    @Test
    public void testUpdateMileageNormalCase() {
        MileageUpdateResponse mileageUpdateResponse = mileageUpdateObjectEndpoint.updateMileage(mileageUpdateRequest);

        assertTrue(mileageUpdateResponse.getFailedUpdates().isEmpty());
        assertEquals(3, mileageUpdateResponse.getSuccessfulUpdates().size());

        verify(employeeCarRepositoryMock, times(3)).save(any(EmployeeCar.class));
    }

    @Test
    public void testUpdateMileageOneCarNotFound() {
        when(employeeCarRepositoryMock.findByLicensePlateIgnoreCase(employeeCar2.getLicensePlate())).thenReturn(null);

        MileageUpdateResponse mileageUpdateResponse = mileageUpdateObjectEndpoint.updateMileage(mileageUpdateRequest);

        assertEquals(1, mileageUpdateResponse.getFailedUpdates().size());
        assertEquals(2, mileageUpdateResponse.getSuccessfulUpdates().size());

        verify(employeeCarRepositoryMock, times(2)).save(any(EmployeeCar.class));
    }

    @Test
    public void testUpdateMileageDoubleMileageForCarTakesHighest() {
        MileageUpdateObject mileageUpdateObject4 = new MileageUpdateObject();
        mileageUpdateObject4.setLicensePlate(employeeCar3.getLicensePlate());
        mileageUpdateObject4.setMileageDriven(employeeCar3.getMileage() + 100);
        mileageUpdateRequest.getMileageUpdateObject().add(mileageUpdateObject4);

        //This adds a fourth update to the request. But it should be ignored since a higher was already updated.
        MileageUpdateResponse mileageUpdateResponse = mileageUpdateObjectEndpoint.updateMileage(mileageUpdateRequest);

        //An update that did not overwrite another update is not classified as a failure.
        assertEquals(0, mileageUpdateResponse.getFailedUpdates().size());
        assertEquals(3, mileageUpdateResponse.getSuccessfulUpdates().size());

        verify(employeeCarRepositoryMock, times(3)).save(any(EmployeeCar.class));
    }


    @Test
    public void testUpdateMileageDoubleMileageForCarTakesHighestAndOverwrites() {
        MileageUpdateObject mileageUpdateObject4 = new MileageUpdateObject();
        mileageUpdateObject4.setLicensePlate(employeeCar3.getLicensePlate());
        mileageUpdateObject4.setMileageDriven(employeeCar3.getMileage() + 500);
        mileageUpdateRequest.getMileageUpdateObject().add(mileageUpdateObject4);

        //This adds a fourth update to the request. It's higher than the previous update, so it should overwrite. Doing nothing more than an additional success message.
        MileageUpdateResponse mileageUpdateResponse = mileageUpdateObjectEndpoint.updateMileage(mileageUpdateRequest);

        //An update that did not overwrite another update is not classified as a failure.
        assertEquals(0, mileageUpdateResponse.getFailedUpdates().size());
        assertEquals(4, mileageUpdateResponse.getSuccessfulUpdates().size());

        verify(employeeCarRepositoryMock, times(3)).save(any(EmployeeCar.class));
    }

    @Test
    public void testUpdateMileageMileageGivenNotHigherThanCurrentMileage() {
        //Remove the first update object, we will look for an invalid updateobject for car 1
        mileageUpdateRequest.getMileageUpdateObject().remove(mileageUpdateObject1);

        MileageUpdateObject mileageUpdateObject4 = new MileageUpdateObject();
        mileageUpdateObject4.setLicensePlate(employeeCar1.getLicensePlate());
        mileageUpdateObject4.setMileageDriven(employeeCar1.getMileage() - 100);
        mileageUpdateRequest.getMileageUpdateObject().add(mileageUpdateObject4);

        MileageUpdateResponse mileageUpdateResponse = mileageUpdateObjectEndpoint.updateMileage(mileageUpdateRequest);

        assertEquals(1, mileageUpdateResponse.getFailedUpdates().size());
        assertEquals(2, mileageUpdateResponse.getSuccessfulUpdates().size());

        verify(employeeCarRepositoryMock, times(2)).save(any(EmployeeCar.class));
    }


    private Car createCar() {
        Car car = new Car();
        car.setFunctionalLevel(2);
        car.setMake("Audi");
        car.setModel("A1");
        car.setAmountDowngrade(BigDecimal.valueOf(2315.25));
        car.setFuelType(FuelType.DIESEL);
        car.setAmountUpgrade(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP));
        car.setListPrice(BigDecimal.valueOf(25343.22));
        car.setTyreType(TyreType.ALUMINIUM);
        car.setTimeOfDeliveryInDays(Duration.ofDays(150));
        return car;
    }

    private EmployeeCar createEmployeeCar() {
        EmployeeCar employeeCar = new EmployeeCar();
        employeeCar.setSelectedCar(createCar());
        CarOption a = new CarOption();
        a.setDescription("Trekhaak");
        CarOption b = new CarOption();
        b.setDescription("Parkeersensor");
        employeeCar.setCarOptions(Arrays.asList(a, b));
        employeeCar.setCarStatus(CarStatus.IN_USE);
        employeeCar.setLicensePlate("1-FGH-468");
        employeeCar.setMileage(86_564);
        return employeeCar;
    }
}
