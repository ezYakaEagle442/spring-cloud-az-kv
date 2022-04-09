# spring-cloud-az-kv

Read [Spring Cloud Azure KV Secret Sample](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-cloud-azure_v4.0.0/keyvault/spring-cloud-azure-starter-keyvault-secrets/property-source)

0. prepare the KV

you need to create a KV in Azure and set the following secrets:

* FOO
* MYSQL-SERVER-FULL-NAME


1. az login with Azure CLI

So you don't need to have any credential in your application properties

```
az login
```

then build and run your spring boot application

```shell
mvn clean package -DskipTests
java -jar target/spring-cloud-azure-starter-keyvault-secrets-sample-single-property-source-1.0.0.jar --spring.profiles.active=azure "--spring.cloud.azure.keyvault.secret.property-sources[0].endpoint=YOUR_KEY_VAULT_ENDPOINT"
```

