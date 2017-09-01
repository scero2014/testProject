package net.scero.test.controllers.swagger;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.scero.test.ws.TestRQ;
import net.scero.test.ws.TestRS;

@Validated
@Controller
@RequestMapping(value = "/1.0", produces = { MediaType.APPLICATION_JSON_VALUE }, headers = "Accept=application/json")
@Api(value = "Ejemplo de controlador Swagger JSON")
public class SwaggerJsonExampleController {
    /**
     * Ejemplo de controlador Swagger con respuesta Json
     * 
     * @return HTTP status
     */
    @RequestMapping(method = RequestMethod.POST, value = "/exampleJsonEndPoint/{nombre}")
    @ApiOperation(value = "Explicación de que hace", notes = "Notas varias")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Classification successfully created"), @ApiResponse(code = 404,
            message = "Client not found"), @ApiResponse(code = 409, message = "Classification already exists"), @ApiResponse(code = 422,
                    message = "Mandatory fields missing"), @ApiResponse(code = 500, message = "Failure") })
    public ResponseEntity<Void> create(@PathVariable("nombre") String nombre, @Valid @RequestBody TestRQ testRQ) {
        HttpStatus status;
        try {
            status = HttpStatus.CREATED;
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Void>(status);

    }

    /**
     * Devuelve la lista de clasificaciones
     * 
     * @param seqClient Id de cliente
     * @param classification
     * 
     * @return Clasificaciones asociadas al cliente
     */
    @ApiOperation(
        value = "Get a list of classifications",
        notes = "Clasificaciones asociados al cliente",
        response = TestRS.class,
        responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Clasification list returned"),
        @ApiResponse(code = 204, message = "Clasification list not found"),
        @ApiResponse(code = 404, message = "Client not found"),
        @ApiResponse(code = 500, message = "Failure")
    })
    @RequestMapping(value = "/exampleJsonEndPoint/{nombre}", method = RequestMethod.GET)
    public ResponseEntity<List<TestRS>> getList(
        @PathVariable("nombre") String nombre) {
        HttpStatus status;
        List<TestRS> results;
        try {
            results = new ArrayList<TestRS>();
            TestRS testRS = new TestRS();
            testRS.setStatus(0);
            testRS.setMessage("Mensaje 1"); 
            results.add(testRS);
            
            testRS = new TestRS();
            testRS.setStatus(2);
            testRS.setMessage("Mensaje 2");
            results.add(testRS);
            
            status = HttpStatus.OK;
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            results = null;
        }

        return new ResponseEntity<List<TestRS>>(results, status);
    }

}
