package com.leandrosnazareth.soap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.apache.axis.Constants;
import org.apache.axis.client.Call;
import org.junit.jupiter.api.Test;

class Wso2SoapClientTest {

    private static final String NAMESPACE = "http://meu-namespace";
    private static final String WSDL_URL = "http://localhost/servico?wsdl";

    @Test
    void invokeListShouldConfigureReturnTypeAndParameters() throws AxisFault {
        FakeCall call = new FakeCall(Arrays.asList("A", "B"));

        Wso2SoapClientProperties props = Wso2SoapClientProperties.builder(WSDL_URL)
                .namespaceUri(NAMESPACE)
                .build();

        Wso2SoapClient client = new TestClient(props, call);

        List<String> result = client.invokeList(
                "PesquisarUnidades",
                "RowUnidade",
                String.class,
                Collections.singletonList(SoapParameter.in("v_Lotacao", Constants.XSD_STRING)),
                "123");

        assertEquals(Arrays.asList("A", "B"), result);
        assertEquals(new QName(NAMESPACE, "RowUnidade"), call.returnQName);
        assertEquals(String.class, call.returnType);
        assertEquals(1, call.parameters.size());
        assertEquals("v_Lotacao", call.parameters.get(0).name);
        assertEquals(Constants.XSD_STRING, call.parameters.get(0).type);
        assertEquals(javax.xml.rpc.ParameterMode.IN, call.parameters.get(0).mode);
        assertEquals(1, call.invokeCount);
    }

    @Test
    void invokeListShouldWrapSingleResultInList() throws AxisFault {
        FakeCall call = new FakeCall("SINGLE");

        Wso2SoapClientProperties props = Wso2SoapClientProperties.builder(WSDL_URL)
                .namespaceUri(NAMESPACE)
                .build();

        Wso2SoapClient client = new TestClient(props, call);

        List<String> result = client.invokeList(
                "Op",
                "Item",
                String.class,
                Collections.emptyList(),
                "arg");

        assertEquals(Collections.singletonList("SINGLE"), result);
        assertEquals(1, call.invokeCount);
    }

    @Test
    void newCallShouldApplyCredentialsAndTimeouts() {
        Wso2SoapClientProperties props = Wso2SoapClientProperties.builder(WSDL_URL)
                .namespaceUri(NAMESPACE)
                .connectionTimeoutMillis(5000)
                .readTimeoutMillis(20000)
                .basicAuth("user", "pass")
                .build();

        Wso2SoapClient client = new Wso2SoapClient(props);
        Call call = client.newCall("TestOperation");

        assertNotNull(call);
        assertEquals("user", call.getUsername());
        assertEquals("pass", call.getPassword());
        assertEquals(20000, call.getTimeout());
        assertEquals(5000, call.getProperty("axis.connection.timeout"));
    }

    private static class TestClient extends Wso2SoapClient {
        private final Call call;

        TestClient(Wso2SoapClientProperties props, Call call) {
            super(props, c -> {
            });
            this.call = call;
        }

        @Override
        public Call newCall(String operationName) {
            return call;
        }
    }

    private static class FakeCall extends Call {
        QName returnQName;
        Class<?> returnType;
        java.util.List<Parameter> parameters = new java.util.ArrayList<>();
        Object response;
        int invokeCount;

        FakeCall(Object response) throws AxisFault {
            super(new org.apache.axis.client.Service());
            this.response = response;
        }

        @Override
        public void setReturnType(QName qName, Class clazz) {
            this.returnQName = qName;
            this.returnType = clazz;
        }

        @Override
        public void addParameter(String name, QName type, javax.xml.rpc.ParameterMode mode) {
            this.parameters.add(new Parameter(name, type, mode));
        }

        @Override
        public Object invoke(Object[] params) {
            this.invokeCount++;
            return response;
        }

        private static class Parameter {
            final String name;
            final QName type;
            final javax.xml.rpc.ParameterMode mode;

            Parameter(String name, QName type, javax.xml.rpc.ParameterMode mode) {
                this.name = name;
                this.type = type;
                this.mode = mode;
            }
        }
    }
}
