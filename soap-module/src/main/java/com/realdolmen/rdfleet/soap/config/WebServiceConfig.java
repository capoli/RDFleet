package com.realdolmen.rdfleet.soap.config;

import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j.callback.SimplePasswordValidationCallbackHandler;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.util.List;
import java.util.Properties;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {
    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/ws/*");
    }

    @Bean(name = "mileageUpdateObjects")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema mileageUpdateObjectsSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("MileageUpdateObjectsPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://rdfleet.realdolmen.com/web-services");
        wsdl11Definition.setSchema(mileageUpdateObjectsSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema mileageUpdateObjectsSchema() {
        return new SimpleXsdSchema(new ClassPathResource("MileageUpdateObject.xsd"));
    }

//    @Bean(name = "wsSecurityInterceptor")
//    public Wss4jSecurityInterceptor wsSecurityInterceptor(){
//        Wss4jSecurityInterceptor securityInterceptor = new Wss4jSecurityInterceptor();
//        securityInterceptor.setValidationCallbackHandler(simplePasswordValidationCallbackHandler());
//        securityInterceptor.setValidationActions("UsernameToken");
//        return securityInterceptor;
//    }
//
//    @Override
//    public void addInterceptors(List<EndpointInterceptor> interceptors) {
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        interceptors.add(wsSecurityInterceptor());
//    }
//
//    @Bean(name = "callbackHandler")
//    public SimplePasswordValidationCallbackHandler simplePasswordValidationCallbackHandler(){
//        SimplePasswordValidationCallbackHandler callbackHandler = new SimplePasswordValidationCallbackHandler();
//        Properties properties = new Properties();
//        properties.setProperty("total", "test");
//        callbackHandler.setUsers(properties);
//        return callbackHandler;
//    }
}