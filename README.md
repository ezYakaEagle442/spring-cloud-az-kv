# spring-cloud-az-kv

Read [https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-cloud-azure_4.0.0-beta.2/keyvault/spring-cloud-azure-starter-keyvault-secrets/single-property-source](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-cloud-azure_4.0.0-beta.2/keyvault/spring-cloud-azure-starter-keyvault-secrets/single-property-source)

mvn clean package -DskipTests

java -jar .\target\spring-cloud-azure-starter-keyvault-secrets-sample-single-property-source-1.0.0.jar --spring.profiles.active=az

java -jar .\target\spring-cloud-azure-starter-keyvault-secrets-sample-single-property-source-1.0.0.jar --spring.profiles.active=az --spring.cloud.azure.profile.keyvault.secret.endpoint=https://kv-XXX.vault.azure.net --spring.cloud.azure.profile.tenant-id=XXX --spring.cloud.azure.profile.credential.client-id=XXXX --spring.cloud.azure.profile.credential.client-secret=XXXX"
