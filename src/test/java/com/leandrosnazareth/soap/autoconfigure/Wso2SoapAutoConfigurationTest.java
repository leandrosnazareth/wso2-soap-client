package com.leandrosnazareth.soap.autoconfigure;

import com.leandrosnazareth.soap.Wso2SoapClient;
import com.leandrosnazareth.soap.Wso2SoapClientProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class Wso2SoapAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(Wso2SoapAutoConfiguration.class);

    @Test
    void shouldCreateBeansWhenWsdlConfigured() {
        contextRunner
                .withPropertyValues(
                        "wso2.soap.wsdl-url=https://meu-wso2/servico?wsdl",
                        "wso2.soap.namespace-uri=http://ns",
                        "wso2.soap.connection-timeout-millis=5000",
                        "wso2.soap.read-timeout-millis=20000",
                        "wso2.soap.username=user",
                        "wso2.soap.password=secret"
                )
                .run(ctx -> {
                    Wso2SoapClient client = ctx.getBean(Wso2SoapClient.class);
                    Wso2SoapClientProperties props = ctx.getBean(Wso2SoapClientProperties.class);

                    assertNotNull(client);
                    assertEquals("https://meu-wso2/servico?wsdl", props.getWsdlUrl());
                    assertEquals("http://ns", props.getNamespaceUri());
                    assertEquals(5000, props.getConnectionTimeoutMillis());
                    assertEquals(20000, props.getReadTimeoutMillis());
                    assertEquals("user", props.getCredentials().getUsername());
                    assertEquals("secret", props.getCredentials().getPassword());
                });
    }

    @Test
    void shouldNotCreateBeansWithoutWsdl() {
        contextRunner.run(ctx -> assertFalse(ctx.containsBean("wso2SoapClient")));
    }
}
