package com.banpais.api.config;

import java.util.List;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
//import org.springframework.ws.config.annotation.EnableWsConfiguration; // ELIMINAR ESTA LÍNEA - No es necesaria
import org.springframework.ws.config.annotation.WsConfigurer; // Import CORRECTO
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.adapter.method.MethodArgumentResolver;
import org.springframework.ws.server.endpoint.adapter.method.MethodReturnValueHandler;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig implements WsConfigurer { // Implementa WsConfigurer

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "banco") // El nombre del bean es importante. Será parte de la URL del WSDL.
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema bancoSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("BancoPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://www.example.com/banco");
        wsdl11Definition.setSchema(bancoSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema bancoSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/banco.xsd")); // Ruta a tu archivo XSD
    }

    @Override
    public void addInterceptors(List<EndpointInterceptor> list) {

    }

    @Override
    public void addArgumentResolvers(List<MethodArgumentResolver> list) {

    }

    @Override
    public void addReturnValueHandlers(List<MethodReturnValueHandler> list) {

    }
}
