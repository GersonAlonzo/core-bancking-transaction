//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v3.0.0 
// Visite https://eclipse-ee4j.github.io/jaxb-ri 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2025.02.24 a las 08:01:11 AM CST 
//


package com.example.banco;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="trama" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "trama"
})
@XmlRootElement(name = "registrarMovimientoRequest")
public class RegistrarMovimientoRequest {

    @XmlElement(required = true)
    protected String trama;

    /**
     * Obtiene el valor de la propiedad trama.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrama() {
        return trama;
    }

    /**
     * Define el valor de la propiedad trama.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrama(String value) {
        this.trama = value;
    }

}
