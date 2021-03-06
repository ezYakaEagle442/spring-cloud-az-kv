# spring-cloud-az-kv

Read :

- [https://microsoft.github.io/spring-cloud-azure/4.0.0/reference/html/index.html#basic-usage-3](https://microsoft.github.io/spring-cloud-azure/4.0.0/reference/html/index.html#basic-usage-3)
- [https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-cloud-azure_4.0.0/keyvault/spring-cloud-azure-starter-keyvault-secrets/property-source](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-cloud-azure_4.0.0/keyvault/spring-cloud-azure-starter-keyvault-secrets/property-source)



## Special Characters in Property Name
Key Vault secret name only support characters in [0-9a-zA-Z-]. Refs: Vault-name and Object-name. 
[If your property name contains other characters, you can use these workarounds](https://microsoft.github.io/spring-cloud-azure/4.0.0/reference/html/index.html#advanced-usage):

Use - instead of . in secret name. . isn’t supported in secret name. If your application have property name which contains ., like spring.datasource.url, just replace . to - when save secret in Azure Key Vault. For example: Save spring-datasource-url in Azure Key Vault. In your application, you can still use spring.datasource.url to retrieve property value.

This method can not satisfy requirement like spring.datasource-url. When you save spring-datasource-url in Key Vault, only spring.datasource.url and spring-datasource-url is supported to retrieve property value, spring.datasource-url isn’t supported. To handle this case, please refer to the following option: Use property placeholders.

```sh
mvn clean package -DskipTests


create the file .env:
AZURE_CLIENT_ID=XXX
AZURE_CLIENT_SECRETXXX
ENDPOINT=https://kv-xxx.vault.azure.net
AZURE_TENANT_ID=xxx

export $(cat .env | xargs)

create setenv.bat
SET AZURE_CLIENT_IDxxx
SET AZURE_CLIENT_SECRET=xxx
SET ENDPOINT=https://kv-xxx.vault.azure.net
SET AZURE_TENANT_ID=xxxx


java -jar .\target\spring-cloud-azure-starter-keyvault-secrets-sample-single-property-source-1.0.0.jar --spring.profiles.active=azure
java -jar .\target\spring-cloud-azure-starter-keyvault-secrets-sample-single-property-source-1.0.0.jar --spring.profiles.active=mysql
java -jar .\target\spring-cloud-azure-starter-keyvault-secrets-sample-single-property-source-1.0.0.jar --spring.profiles.active=default

java -jar .\target\spring-cloud-azure-starter-keyvault-secrets-sample-single-property-source-1.0.0.jar --spring.profiles.active=azure \
--spring.cloud.azure.keyvault.secret.property-sources[].endpoint="https://kv-XXX.vault.azure.net"  \
--spring.cloud.azure.keyvault.secret.property-sources[].profile.tenant-id=XXXX \
--spring.cloud.azure.keyvault.secret.property-sources[].credential.client-id="XXXX" \
--spring.cloud.azure.keyvault.secret.property-sources[].credential.client-secret="XXXX"

```