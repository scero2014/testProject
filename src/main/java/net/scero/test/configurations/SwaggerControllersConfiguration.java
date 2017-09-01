package net.scero.test.configurations;

import org.springframework.context.annotation.Configuration;

import net.scero.test.controllers.swagger.SwaggerJsonExampleController;

/**
 * Controladores empleados por Swagger
 *
 * @author jnogueira
 * @version 1.0
 * @date 14/09/2016
 */
@Configuration
public class SwaggerControllersConfiguration {
    // **** Recursos ****/

    // **** Beans ****/
    public SwaggerJsonExampleController swaggerJsonExampleController() {
        return new SwaggerJsonExampleController();
    }
}
