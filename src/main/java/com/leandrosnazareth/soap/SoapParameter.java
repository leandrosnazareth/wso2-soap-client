package com.leandrosnazareth.soap;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

/**
 * Representa um parâmetro de operação SOAP.
 */
public final class SoapParameter {

    private final String name;
    private final QName type;
    private final ParameterMode mode;

    private SoapParameter(String name, QName type, ParameterMode mode) {
        this.name = name;
        this.type = type;
        this.mode = mode;
    }

    public static SoapParameter in(String name, QName type) {
        return new SoapParameter(name, type, ParameterMode.IN);
    }

    public static SoapParameter out(String name, QName type) {
        return new SoapParameter(name, type, ParameterMode.OUT);
    }

    public static SoapParameter inOut(String name, QName type) {
        return new SoapParameter(name, type, ParameterMode.INOUT);
    }

    public String getName() {
        return name;
    }

    public QName getType() {
        return type;
    }

    public ParameterMode getMode() {
        return mode;
    }
}
