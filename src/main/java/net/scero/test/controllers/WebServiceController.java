package net.scero.test.controllers;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import net.scero.test.configurations.WebserviceConfig;
import net.scero.test.ws.TestRQ;
import net.scero.test.ws.TestRS;

@Endpoint
public class WebServiceController {
    //---- Variables ----//

    //---- Constructors ----//

    //---- Public Methods ----//
    @PayloadRoot(namespace = WebserviceConfig.NAMESPACE_URI, localPart = "operationTestRequest")
    @ResponsePayload
    public TestRS getCountry(@RequestPayload TestRQ testRQ) {
        TestRS testRS = new TestRS();
        if (testRQ.getNumber() > 0) {
            testRS.setStatus(0);
            testRS.setMessage("Hola " + testRQ.getName());
        } else {
            testRS.setStatus(-1);
        }

        return testRS;
    }
    //---- Private Methods ----//
}
