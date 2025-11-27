package com.leandrosnazareth.soap.autoconfigure;

import com.leandrosnazareth.soap.Wso2SoapClient;
import com.leandrosnazareth.soap.Wso2SoapClientProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@EnableConfigurationProperties(Wso2SoapAutoProperties.class)
@ConditionalOnProperty(prefix = "wso2.soap", name = "wsdl-url")
public class Wso2SoapAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Wso2SoapClientProperties wso2SoapClientProperties(Wso2SoapAutoProperties props) {
        Wso2SoapClientProperties.Builder builder = Wso2SoapClientProperties.builder(props.getWsdlUrl())
                .namespaceUri(props.getNamespaceUri())
                .connectionTimeoutMillis(props.getConnectionTimeoutMillis())
                .readTimeoutMillis(props.getReadTimeoutMillis());
        if (StringUtils.hasText(props.getUsername())) {
            builder.basicAuth(props.getUsername(), props.getPassword());
        }
        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public Wso2SoapClient wso2SoapClient(Wso2SoapClientProperties properties) {
        return new Wso2SoapClient(properties);
    }
}
