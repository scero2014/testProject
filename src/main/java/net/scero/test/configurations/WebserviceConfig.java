package net.scero.test.configurations;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import org.springframework.xml.xsd.XsdSchemaCollection;
import org.springframework.xml.xsd.commons.CommonsXsdSchemaCollection;


@EnableWs
@Configuration
public class WebserviceConfig extends WsConfigurerAdapter {
    public static final String NAMESPACE_URI = "http://net.scero.test.ws";

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/ws/*");
    }

    @Bean(name = "countries")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema countriesSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("operationTest");
        wsdl11Definition.setLocationUri("/ws/");
        wsdl11Definition.setTargetNamespace(NAMESPACE_URI);
        wsdl11Definition.setSchemaCollection(collectionSchema());
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema countriesSchema() {
        //        CommonsXsdSchemaCollection schemes = new CommonsXsdSchemaCollection();
        //        schemes.setXsds(new ClassPathResource("schema.xsd"), new ClassPathResource("operations.xsd"));
        //        
        //        return schemes;
        return new SimpleXsdSchema(new ClassPathResource("schema.xsd"));
    }
    
    @Bean
    public XsdSchemaCollection collectionSchema() {
        CommonsXsdSchemaCollection collection = new CommonsXsdSchemaCollection(new Resource[] { new ClassPathResource("schema.xsd") });
        collection.setInline(true);
        return collection;
    }
}
