package com.banpais.api.command.config.client;

import com.banpais.api.config.SoapFaultException;
import org.apache.cxf.binding.soap.SoapFault;

import java.util.Map;

public abstract class BaseSoapClient<T, R> {
    protected final TramaTransformacionService tramaTransformacionService;
    protected final String nombreTrama;

    public BaseSoapClient(TramaTransformacionService tramaTransformacionService, String nombreTrama) {
        this.tramaTransformacionService = tramaTransformacionService;
        this.nombreTrama = nombreTrama;
    }

    protected abstract T prepararSoapRequest(String trama);
    protected abstract R invocarServicioSoap(T soapRequest);

    public R ejecutarOperacion(Map<String, Object> campos) {
        try {
            // Generar la trama usando el servicio de transformación
            String trama = tramaTransformacionService.generarTrama(campos, nombreTrama);
            
            // Preparar el request SOAP
            T soapRequest = prepararSoapRequest(trama);
            
            // Debug para verificar la trama
            System.out.println("Trama generada: [" + trama + "]");
            System.out.println("Longitud total: " + trama.length());
            
            // Invocar el servicio SOAP
            return invocarServicioSoap(soapRequest);
        } catch (SoapFault fault) {
            String faultCode = fault.getFaultCode() != null ? fault.getFaultCode().getLocalPart() : "Unknown";
            String faultString = fault.getReason() != null ? fault.getReason() : "Unknown SOAP Fault";
            throw new SoapFaultException("Error SOAP: " + faultCode + " - " + faultString, fault);
        } catch (Exception e) {
            throw new RuntimeException("Error al invocar el servicio SOAP: " + e.getMessage(), e);
        }
    }
}