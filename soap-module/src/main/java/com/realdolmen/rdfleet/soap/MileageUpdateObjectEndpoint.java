package com.realdolmen.rdfleet.soap;

import com.realdolmen.rdfleet.domain.EmployeeCar;
import com.realdolmen.rdfleet.repositories.EmployeeCarRepository;
import com.realdolmen.rdfleet.web_services.MileageUpdateObject;
import com.realdolmen.rdfleet.web_services.MileageUpdateRequest;
import com.realdolmen.rdfleet.web_services.MileageUpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JSTAX29 on 28/10/2015.
 * A webservice that allowed updating the mileage of cars that are in the database.
 * The EndPoint can be reached through the wsdl on the url http://&lt;url&gt;/ws/mileageUpdateObjects.wsdl
 */
@Endpoint
public class MileageUpdateObjectEndpoint {
    private static final String NAMESPACE_URI = "http://rdfleet.realdolmen.com/web-services";
    private EmployeeCarRepository employeeCarRepository;

    @Autowired
    public void setEmployeeCarRepository(EmployeeCarRepository employeeCarRepository) {
        this.employeeCarRepository = employeeCarRepository;
    }

    //wsdl: http://localhost:8080/ws/mileageUpdateObjects.wsdl
    /**
     * Will receive an XML request to update the mileage of cars. These are a list of multiple license plate with a corresponding mileage.
     * Cars in the database should be updated to have the highest mileage that has been provided in the list.
     * When an update fails, e.g. a car that isn't found or the mileage given is lower than their previous mileage, an error message will be added to the reponse.
     * The other updates will not fail however and will be persisted.
     * @param mileageUpdateRequest the request that contains multiple license plates with mileages
     * @return a response that holds what cars have been updated and which have gone wrong.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "MileageUpdateRequest")
    @ResponsePayload
    public MileageUpdateResponse updateMileage(@RequestPayload MileageUpdateRequest mileageUpdateRequest) {
        MileageUpdateResponse response = new MileageUpdateResponse();
        List<MileageUpdateObject> mileageUpdateObjects = mileageUpdateRequest.getMileageUpdateObject();

        Map<EmployeeCar, Integer> carMap = new HashMap<>();

        //Iterate over all received objects
        for (MileageUpdateObject mileageUpdateObject : mileageUpdateObjects) {
            String licensePlate = mileageUpdateObject.getLicensePlate().toUpperCase();

            EmployeeCar employeeCar = employeeCarRepository.findByLicensePlateIgnoreCase(licensePlate.trim());

            //If the car isn't found, an error occurred.
            if (employeeCar == null) {
                response.getFailedUpdates().add((String.format("The car with license plate %s was not found in our system.\n", licensePlate)));
                continue;
            }

            //If the car is being updated but the provided mileage is lower than the previous mileage, an error occurred.
            if (employeeCar.getMileage() > mileageUpdateObject.getMileageDriven()) {
                response.getFailedUpdates().add(String.format("The car with license plate %s has driven %dkm, but was provided a lower mileage (%dkm).\n", licensePlate, employeeCar.getMileage(), mileageUpdateObject.getMileageDriven()));
                continue;
            }

            //If the map doesn't contain the current car, add it
            if (!carMap.containsKey(employeeCar)) {
                carMap.put(employeeCar, mileageUpdateObject.getMileageDriven());
                response.getSuccessfulUpdates().add(String.format("The car with license plate %s has updated its mileage to %d", licensePlate, mileageUpdateObject.getMileageDriven()));
            } else {
                //If the map does contain the current car, check if the new mileage is higher than the other.
                if (carMap.get(employeeCar) < mileageUpdateObject.getMileageDriven()) {
                    //If it is, add it to the map. It should be updated
                    carMap.put(employeeCar, mileageUpdateObject.getMileageDriven());
                    response.getSuccessfulUpdates().add(String.format("The car with license plate %s has updated its mileage to %d", licensePlate, mileageUpdateObject.getMileageDriven()));
                }
            }
        }

        //Update all the cars that were added to the carmap for updating.
        for (Map.Entry<EmployeeCar, Integer> carEntry : carMap.entrySet()) {
            carEntry.getKey().setMileage(carEntry.getValue());
            //Single saving is good for testing
            employeeCarRepository.save(carEntry.getKey());
        }

        //Saving the whole set is probably better for performance.
        //Problem with tests: I can't verify how many entities were saved.
//        employeeCarRepository.save(carMap.keySet());

        return response;
    }
}
