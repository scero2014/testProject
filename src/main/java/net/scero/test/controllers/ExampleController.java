package net.scero.test.controllers;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ExampleController {
    //---- Variables ----//

    //---- Constructors ----//

    //---- Public Methods ----//
    @RequestMapping(method = RequestMethod.GET, value = "/")
    @ResponseBody
    public ResponseEntity<String> defaultEndpoint() {
        HttpHeaders headers = new HttpHeaders();
        String result;
        HttpStatus httpStatus;
        try {
            result = "Hola mundo";
            httpStatus = HttpStatus.OK;
        } catch (Exception e) {
            result = e.toString();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<String>(result, headers, httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/aop")
    @ResponseBody
    public ResponseEntity<String> aopTestEndpoint(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        String result;
        HttpStatus httpStatus;
        try {
            result = "Aop example";
            httpStatus = HttpStatus.OK;
        } catch (Exception e) {
            result = e.toString();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<String>(result, headers, httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/secure")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<String> securityTestEndpoint(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        String result;
        HttpStatus httpStatus;
        try {
            result = "Ejecutado";
            httpStatus = HttpStatus.OK;
        } catch (Exception e) {
            result = e.toString();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<String>(result, headers, httpStatus);        
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/ip")
    @RolesAllowed("ROLE_IP")
    public ResponseEntity<String> securityIpTestEndpoint(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        String result;
        HttpStatus httpStatus;
        try {
            result = "Ejecutado";
            httpStatus = HttpStatus.OK;
        } catch (Exception e) {
            result = e.toString();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<String>(result, headers, httpStatus);        
    }
    
    //---- Private Methods ----//
}
