package com.banpais.api.command.config.client;

import com.banpais.soap.client.BancoPort;
import com.banpais.soap.client.BancoPortService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

@Configuration
public class SoapClientConfig {
    
    @Value("${soap.service.url}")
    private String serviceUrl;

    @Bean
    public BancoPort bancoPort() {
        try {
            BancoPortService service = new BancoPortService();
            return service.getBancoPortSoap11();
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el cliente SOAP", e);
        }
    }
}