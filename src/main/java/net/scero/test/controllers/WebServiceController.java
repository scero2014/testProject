package net.scero.test.controllers;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import lombok.extern.slf4j.Slf4j;
import net.scero.test.configurations.WebserviceConfig;
import net.scero.test.ws.GetCountryRequest;
import net.scero.test.ws.TestRQ;
import net.scero.test.ws.TestRS;

@Endpoint
@Slf4j
public class WebServiceController {
    //---- Variables ----//

    //---- Constructors ----//

    //---- Public Methods ----//
    @PayloadRoot(namespace = WebserviceConfig.NAMESPACE_URI, localPart = "getCountryRequest")
    @ResponsePayload
    public TestRS test(@RequestPayload GetCountryRequest request) {
        log.info("Llega una petición a WS");
        log.info(request.toString());

        TestRQ testRQ = request.getTestRQ();
        TestRS testRS = new TestRS();
        if (testRQ.getNumber() > 0) {
            testRS.setStatus(0);
            testRS.setMessage("Hola " + testRQ.getName());
        } else {
            testRS.setStatus(-1);
        }

        log.info("Hay respuesta del WS");
        log.info(testRS.toString());
        return testRS;
    }
    //---- Private Methods ----//
}
