# spring-cloud-az-kv

Read [https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-cloud-azure_4.0.0-beta.2/keyvault/spring-cloud-azure-starter-keyvault-secrets/single-property-source](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-cloud-azure_4.0.0-beta.2/keyvault/spring-cloud-azure-starter-keyvault-secrets/single-property-source)

mvn clean package -DskipTests

java -jar .\target\spring-cloud-azure-starter-keyvault-secrets-sample-single-property-source-1.0.0.jar -Dspring.profiles.active=foo
java -jar .\target\spring-cloud-azure-starter-keyvault-secrets-sample-single-property-source-1.0.0.jar --spring.profiles.active=foo