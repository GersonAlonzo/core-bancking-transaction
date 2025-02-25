package com.banpais.api.exceptions;

public class TramaInvalidaException extends RuntimeException {
    
    public TramaInvalidaException(String message) {
        super(message);
    }
    
    public TramaInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static TramaInvalidaException longitudInvalida(int esperada, int recibida) {
        return new TramaInvalidaException(
            String.format("Longitud de trama inválida. Esperada: %d, Recibida: %d", esperada, recibida)
        );
    }
    
    public static TramaInvalidaException campoInvalido(String nombreCampo, String valor, int longitudEsperada) {
        return new TramaInvalidaException(
            String.format("Campo %s inválido. Valor: [%s], Longitud esperada: %d", 
                nombreCampo, valor, longitudEsperada)
        );
    }
}