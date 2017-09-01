package net.scero.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.ws.config.annotation.EnableWs;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Lanza aplicación con Spring-Boot
 * 
 * @author jnogueira
 * @data 2017-06-28
 *
 */
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableSwagger2
@EnableWs
public class ApplicationTest {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationTest.class, args);
    }
}
