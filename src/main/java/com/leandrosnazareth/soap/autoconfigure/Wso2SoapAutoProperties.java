package com.leandrosnazareth.soap.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wso2.soap")
public class Wso2SoapAutoProperties {

    /**
     * URL completa do WSDL.
     */
    private String wsdlUrl;

    /**
     * Namespace usado nas operações (opcional; pode usar o WSDL).
     */
    private String namespaceUri;

    /**
     * Timeout de conexão em milissegundos.
     */
    private int connectionTimeoutMillis = 10_000;

    /**
     * Timeout de leitura em milissegundos.
     */
    private int readTimeoutMillis = 60_000;

    /**
     * Usuário para Basic Auth (opcional).
     */
    private String username;

    /**
     * Senha para Basic Auth (opcional).
     */
    private String password;

    public String getWsdlUrl() {
        return wsdlUrl;
    }

    public void setWsdlUrl(String wsdlUrl) {
        this.wsdlUrl = wsdlUrl;
    }

    public String getNamespaceUri() {
        return namespaceUri;
    }

    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    public int getConnectionTimeoutMillis() {
        return connectionTimeoutMillis;
    }

    public void setConnectionTimeoutMillis(int connectionTimeoutMillis) {
        this.connectionTimeoutMillis = connectionTimeoutMillis;
    }

    public int getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    public void setReadTimeoutMillis(int readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
