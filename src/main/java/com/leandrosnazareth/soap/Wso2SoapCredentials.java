package com.leandrosnazareth.soap;

/**
 * Credenciais opcionais (HTTP Basic) para chamadas WSO2/Axis.
 */
public final class Wso2SoapCredentials {

    private final String username;
    private final String password;

    public Wso2SoapCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
