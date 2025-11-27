# WSO2 SOAP Client (Apache Axis 1.x)

Biblioteca para padronizar chamadas SOAP (WSO2) usando Apache Axis 1.x. Fornece criação simplificada de `Call`, configuração via builder e suporte opcional a autenticação básica.

## Requisitos
- Java 8+
- Maven 3+

## Instalação
Adicione ao `pom.xml` (exemplo de coordenadas locais):
```xml
<dependency>
    <groupId>com.leandrosnazareth</groupId>
    <artifactId>wso2-soap-client</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Principais classes
- `Wso2SoapClientProperties`: configurações imutáveis (URL do WSDL, namespace, timeouts, credenciais).
- `Wso2SoapCredentials`: usuário e senha opcionais (HTTP Basic).
- `Wso2SoapClient`: cliente reutilizável para criar `Call` e invocar operações (`invokeSingle`, `invokeList`).
- `SoapParameter`: definição de parâmetros (nome, tipo `QName`, modo IN/OUT/INOUT).
- `SoapClientException`: exceção de alto nível para falhas de cliente.

## Exemplo mínimo (sem autenticação)
```java
Wso2SoapClient client = new Wso2SoapClient(
    Wso2SoapClientProperties.builder("https://meu-wso2/servico?wsdl")
        .namespaceUri("http://meu-namespace")
        .build()
);

List<Unidade> unidades = client.invokeList(
    "PesquisarUnidades",           // operação
    "RowUnidade",                  // elemento de retorno
    Unidade.class,                 // classe do item
    List.of(SoapParameter.in("v_Lotacao", org.apache.axis.Constants.XSD_STRING)),
    codUnidade                     // argumentos
);
```

## Exemplo com autenticação básica
```java
Wso2SoapClient client = new Wso2SoapClient(
    Wso2SoapClientProperties.builder("https://meu-wso2/servico?wsdl")
        .namespaceUri("http://meu-namespace")
        .basicAuth("usuario", "senha")
        .build()
);
```

## Ajustando timeouts
```java
Wso2SoapClientProperties props = Wso2SoapClientProperties.builder("https://meu-wso2/servico?wsdl")
    .namespaceUri("http://meu-namespace")
    .connectionTimeoutMillis(5_000) // timeout de conexão
    .readTimeoutMillis(30_000)      // timeout de leitura
    .build();
```

## Personalizando o Call (headers, SOAPAction, etc.)
Use o segundo construtor para passar um `Consumer<Call>`:
```java
Wso2SoapClient client = new Wso2SoapClient(props, call -> {
    call.setSOAPActionURI("MinhaSoapAction");
    call.addHeader(new SOAPHeaderElement("http://meu-namespace", "TraceId", "abc-123"));
});
```

## Mapeando tipos customizados
`invokeList` registra `BeanDeserializerFactory` para o `returnType` informado. Certifique-se de que a classe possua getters/setters ou anotações JAXB compatíveis com o Axis.

## Tratamento de erros
- Exceções checked de rede (ex.: `RemoteException`) são encapsuladas em `SoapClientException`.
- Configure logs (SLF4J) para inspecionar erros detalhados.

## Publicação
1. Ajuste `groupId`, `artifactId`, `version` no `pom.xml`.
2. Configure o repositório interno no `settings.xml`.
3. Execute: `mvn clean deploy`.
