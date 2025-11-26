package com.leandrosnazareth.soap;

/**
 * Configurações imutáveis do cliente SOAP WSO2.
 */
public final class Wso2SoapClientProperties {

    private final String wsdlUrl;
    private final String namespaceUri;
    private final int connectionTimeoutMillis;
    private final int readTimeoutMillis;

    private Wso2SoapClientProperties(Builder builder) {
        this.wsdlUrl = builder.wsdlUrl;
        this.namespaceUri = builder.namespaceUri;
        this.connectionTimeoutMillis = builder.connectionTimeoutMillis;
        this.readTimeoutMillis = builder.readTimeoutMillis;
    }

    public String getWsdlUrl() {
        return wsdlUrl;
    }

    public String getNamespaceUri() {
        return namespaceUri;
    }

    public int getConnectionTimeoutMillis() {
        return connectionTimeoutMillis;
    }

    public int getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    public static Builder builder(String wsdlUrl) {
        return new Builder(wsdlUrl);
    }

    public static final class Builder {
        private final String wsdlUrl;
        private String namespaceUri;
        private int connectionTimeoutMillis = 10_000;
        private int readTimeoutMillis = 60_000;

        private Builder(String wsdlUrl) {
            this.wsdlUrl = wsdlUrl;
        }

        public Builder namespaceUri(String namespaceUri) {
            this.namespaceUri = namespaceUri;
            return this;
        }

        public Builder connectionTimeoutMillis(int connectionTimeoutMillis) {
            this.connectionTimeoutMillis = connectionTimeoutMillis;
            return this;
        }

        public Builder readTimeoutMillis(int readTimeoutMillis) {
            this.readTimeoutMillis = readTimeoutMillis;
            return this;
        }

        public Wso2SoapClientProperties build() {
            if (wsdlUrl == null || wsdlUrl.trim().isEmpty()) {
                throw new IllegalArgumentException("wsdlUrl é obrigatório");
            }
            return new Wso2SoapClientProperties(this);
        }
    }
}
