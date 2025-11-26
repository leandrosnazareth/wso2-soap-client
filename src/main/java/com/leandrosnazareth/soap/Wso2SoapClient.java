package com.leandrosnazareth.soap;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Cliente SOAP baseado em Apache Axis, focado em integrações WSO2.
 *
 * Exemplo de uso para lista de objetos:
 * <pre>
 * Wso2SoapClient client = new Wso2SoapClient(Wso2SoapClientProperties.builder(wsdl).namespaceUri(ns).build());
 *
 * List<Unidade> unidades = client.invokeList(
 *         "PesquisarUnidades",
 *         "RowUnidade",
 *         Unidade.class,
 *         List.of(SoapParameter.in("v_Lotacao", org.apache.axis.Constants.XSD_STRING)),
 *         codUnidade);
 * </pre>
 */
public class Wso2SoapClient {

    private static final Logger log = LoggerFactory.getLogger(Wso2SoapClient.class);
    private final Service service;
    private final Wso2SoapClientProperties properties;
    private final Consumer<Call> callCustomizer;

    public Wso2SoapClient(Wso2SoapClientProperties properties) {
        this(properties, call -> {
        });
    }

    public Wso2SoapClient(Wso2SoapClientProperties properties, Consumer<Call> callCustomizer) {
        this.service = new Service();
        this.properties = properties;
        this.callCustomizer = callCustomizer == null ? call -> {
        } : callCustomizer;
    }

    public Call newCall(String operationName) {
        try {
            Call call = (Call) service.createCall();
            URL endpointUrl = new URL(properties.getWsdlUrl());
            call.setTargetEndpointAddress(endpointUrl);
            String namespace = properties.getNamespaceUri() != null ? properties.getNamespaceUri() : properties.getWsdlUrl();
            call.setOperationName(new QName(namespace, operationName));

            // timeouts: conexão e leitura
            call.setProperty("axis.connection.timeout", properties.getConnectionTimeoutMillis());
            call.setTimeout(properties.getReadTimeoutMillis());

            if (properties.getCredentials() != null) {
                call.setUsername(properties.getCredentials().getUsername());
                call.setPassword(properties.getCredentials().getPassword());
            }

            callCustomizer.accept(call);
            return call;
        } catch (ServiceException e) {
            throw new SoapClientException("Falha ao criar o call SOAP para operação " + operationName, e);
        } catch (MalformedURLException e) {
            throw new SoapClientException("URL de WSDL inválida: " + properties.getWsdlUrl(), e);
        }
    }

    public <T> T invokeSingle(String operationName,
                              QName returnQName,
                              Class<T> returnType,
                              List<SoapParameter> parameters,
                              Object... args) {
        Call call = prepareCall(operationName, returnQName, returnType, parameters);
        Object result = invokeInternal(call, args);
        return returnType.cast(result);
    }

    public <T> List<T> invokeList(String operationName,
                                  String rowElementName,
                                  Class<T> elementType,
                                  List<SoapParameter> parameters,
                                  Object... args) {
        QName rowQName = new QName(properties.getNamespaceUri() == null ? "" : properties.getNamespaceUri(), rowElementName);
        Call call = prepareCall(operationName, rowQName, elementType, parameters);
        Object response = invokeInternal(call, args);
        if (response == null) {
            return Collections.emptyList();
        }
        if (response instanceof List) {
            return (List<T>) response;
        }
        return Collections.singletonList(elementType.cast(response));
    }

    private Call prepareCall(String operationName,
                             QName returnQName,
                             Class<?> returnType,
                             List<SoapParameter> parameters) {
        Call call = newCall(operationName);
        call.setReturnType(returnQName, returnType);
        call.registerTypeMapping(returnType, returnQName, null, new BeanDeserializerFactory(returnType, returnQName));
        if (parameters != null) {
            parameters.stream().filter(Objects::nonNull).forEach(p -> call.addParameter(p.getName(), p.getType(), p.getMode()));
        }
        return call;
    }

    private Object invokeInternal(Call call, Object... args) {
        try {
            return call.invoke(args);
        } catch (RemoteException e) {
            log.error("Erro remoto ao invocar operação SOAP {}", call.getOperationName(), e);
            throw new SoapClientException("Erro remoto na operação SOAP: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new SoapClientException("Falha ao invocar operação SOAP: " + e.getMessage(), e);
        }
    }
}
