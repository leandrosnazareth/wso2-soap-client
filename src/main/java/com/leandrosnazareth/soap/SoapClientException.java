package com.leandrosnazareth.soap;

/**
 * Exceção de alto nível para falhas no cliente SOAP.
 */
public class SoapClientException extends RuntimeException {

    public SoapClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public SoapClientException(String message) {
        super(message);
    }
}
