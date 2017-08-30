package net.scero.test.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

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
        String result;
        HttpStatus httpStatus;
        try {
            result = "Ejecutado";
            httpStatus = HttpStatus.OK;
        } catch (Exception e) {
            result = e.toString();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<String>(result, new HttpHeaders(), httpStatus);        
    }

    @RequestMapping(method = RequestMethod.GET, value = "/storage")
    public ResponseEntity<String> storageEndpoint(HttpServletRequest request) {
        String result;
        HttpStatus httpStatus;
        try {
            File file = new File("prueba.txt");
            if (!file.exists()) {
                file.createNewFile();
                result = "File creado";
            } else if (!file.canRead()) {
                result = "No se puede leer";
            } else if (!file.canWrite()) {
                result = "No se puede escribir";
            } else {
                BufferedWriter oWriter = new BufferedWriter(new FileWriter(file));
                oWriter.write ("Un pollo <br/>");
                oWriter.close();
                
                StringBuilder sb = new StringBuilder();
                BufferedReader oReader = new BufferedReader(new FileReader(file));
                String line = oReader.readLine();
                while(line != null){
                    sb.append(line);
                    line = oReader.readLine();
                }
                oReader.close();
                result = sb.toString();
            }
            httpStatus = HttpStatus.OK;
        } catch (Exception e) {
            result = e.toString();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<String>(result, new HttpHeaders(), httpStatus);                
    }

    //---- Private Methods ----//
}
