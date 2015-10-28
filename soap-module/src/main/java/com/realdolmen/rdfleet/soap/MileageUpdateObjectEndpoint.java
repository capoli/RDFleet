package com.realdolmen.rdfleet.soap;

import com.realdolmen.rdfleet.web_services.MileageUpdateObject;
import com.realdolmen.rdfleet.web_services.MileageUpdateRequest;
import com.realdolmen.rdfleet.web_services.MileageUpdateResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * Created by JSTAX29 on 28/10/2015.
 */
@Endpoint
public class MileageUpdateObjectEndpoint {
    private static final String NAMESPACE_URI = "http://rdfleet.realdolmen.com/web-services";

    //wsdl: http://localhost:8080/ws/mileageUpdateObjects.wsdl
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "MileageUpdateRequest")
    @ResponsePayload
    public MileageUpdateResponse updateMileage(@RequestPayload MileageUpdateRequest mileageUpdateRequest){
        MileageUpdateResponse response = new MileageUpdateResponse();

        try{
            MileageUpdateObject mileageUpdateObject = mileageUpdateRequest.getMileageUpdateObject();

            response.setResult("Mileage was successfully added. Mileage received: " + mileageUpdateObject.getMileageDriven());
        }catch (Exception e){
            response.setResult("Mileage could not be added.");
        }

        return response;
    }
}
