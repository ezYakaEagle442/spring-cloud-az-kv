# Simple test for spring-cloud-azure-starter-keyvault-secrets

Read :

- [https://microsoft.github.io/spring-cloud-azure/4.4.0/reference/html/index.html#basic-usage-3](https://microsoft.github.io/spring-cloud-azure/4.4.0/reference/html/index.html#basic-usage-3)
- [https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-cloud-azure_4.0.0/keyvault/spring-cloud-azure-starter-keyvault-secrets/property-source](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-cloud-azure_4.0.0/keyvault/spring-cloud-azure-starter-keyvault-secrets/property-source)



## Special Characters in Property Name
Key Vault secret name only support characters in [0-9a-zA-Z-]. Refs=Vault-name and Object-name. 
[If your property name contains other characters, you can use these workarounds](https://microsoft.github.io/spring-cloud-azure/4.4.0/reference/html/index.html#advanced-usage):

Use - instead of . in secret name. . isn’t supported in secret name. If your application have property name which contains ., like spring.datasource.url, just replace . to - when save secret in Azure Key Vault. For example=Save spring-datasource-url in Azure Key Vault. In your application, you can still use spring.datasource.url to retrieve property value.

This method can not satisfy requirement like spring.datasource-url. When you save spring-datasource-url in Key Vault, only spring.datasource.url and spring-datasource-url is supported to retrieve property value, spring.datasource-url isn’t supported. To handle this case, please refer to the following option=Use property placeholders.

```sh
mvn clean package -DskipTests

```

create the file .env:
```sh
AZURE_CLIENT_ID=XXX
AZURE_CLIENT_SECRET=XXX
SPRING_CLOUD_AZURE_KEY_VAULT_ENDPOINT=https://kv-xxx.vault.azure.net
SPRING_CLOUD_AZURE_TENANT_ID=xxx

export $(cat .env | xargs)
```

create setenv.bat

```sh
SET AZURE_CLIENT_ID=xxx
SET AZURE_CLIENT_SECRET=xxx
SET SPRING_CLOUD_AZURE_KEY_VAULT_ENDPOINT=https://kv-xxx.vault.azure.net
SET SPRING_CLOUD_AZURE_TENANT_ID=xxxx
```


```sh
java -jar .\target\spring-cloud-azure-starter-keyvault-secrets-sample-single-property-source-1.0.0.jar --spring.profiles.active=azure
java -jar .\target\spring-cloud-azure-starter-keyvault-secrets-sample-single-property-source-1.0.0.jar --spring.profiles.active=mysql
java -jar .\target\spring-cloud-azure-starter-keyvault-secrets-sample-single-property-source-1.0.0.jar --spring.profiles.active=default

java -jar .\target\spring-cloud-azure-starter-keyvault-secrets-sample-single-property-source-1.0.0.jar --spring.profiles.active=azure \
--spring.cloud.azure.keyvault.secret.property-sources[].endpoint="https://kv-XXX.vault.azure.net"  \
--spring.cloud.azure.keyvault.secret.property-sources[].profile.tenant-id=XXXX \
--spring.cloud.azure.keyvault.secret.property-sources[].credential.client-id="XXXX" \
--spring.cloud.azure.keyvault.secret.property-sources[].credential.client-secret="XXXX"

```

# Test using Managed Identity





## Set ENV

```sh

subName="set here the name of your subscription"
subName=$(az account list --query "[?name=='${subName}'].{name:name}" --output tsv)
echo "subscription Name :" $subName

SUBSCRIPTION_ID=$(az account list --query "[?name=='${subName}'].{id:id}" --output tsv)
SUBSCRIPTION_ID=$(az account show --query id -o tsv)
TENANT_ID=$(az account show --query tenantId -o tsv)

AKS_CLUSTER_NAME=aks-petcliaks
TARGET_NAMESPACE=petclinic

ACR_NAME=acrpetcliaks
REGISTRY_URL=acrpetcliaca.azurecr.io  # set this to the URL of your registry
REPOSITORY=petclinic   

SPRING_CLOUD_AZURE_KEY_VAULT_ENDPOINT=https://kv-petcliaks33.vault.azure.net/
KV_NAME=kv-petcliaks33 # The name of the KV, must be UNIQUE. A vault name must be between 3-24 alphanumeric characters
WI_VERSION=0.15.0 
TEST_SVC_APP_APP_ID_NAME=id-aks-test-svc-dev-westeurope-101 
SECRET_PROVIDER_CUSTOMERS=azure-kv-wi-test

LOCATION=westeurope
RG_KV=rg-iac-kv33 # RG where to deploy KV
RG_APP=rg-iac-aks-petclinic-mic-srv # RG where to deploy the other Azure services=AKS, ACR, MySQL, etc.
```

## Docker Build

```sh
#docker build --build-arg --no-cache -t "test-v0.1" -f "./Dockerfile" .
tag_id=$(docker build --build-arg --no-cache -t "test-v0.0.1" . 2>/dev/null | awk '/Successfully built/{print $NF}')
#docker tag "test-v0.1"  $REGISTRY_URL/petclinic/test-service

```

## Push to ACR
```sh
set -euo pipefail
access_token=$(az account get-access-token --query accessToken -o tsv)
refresh_token=$(curl https://$REGISTRY_URL /oauth2/exchange -v -d "grant_type=access_token&service=$REGISTRY_URL &access_token=$access_token" | jq -r .refresh_token)
docker login $REGISTRY_URL  -u 00000000-0000-0000-0000-000000000000 --password-stdin <<< "$refresh_token"

docker push $REGISTRY_URL/petclinic/test-service
docker pull $REGISTRY_URL/petclinic/test-service
```

## Create Identities


```sh
az identity create --name $TEST_SVC_APP_APP_ID_NAME -g $RG_APP --location $LOCATION
testServiceClientId=$(az identity show --name $TEST_SVC_APP_APP_ID_NAME -g $RG_APP --query clientId -o tsv)
testServicePrincipalId=$(az identity show --name $TEST_SVC_APP_APP_ID_NAME -g $RG_APP --query principalId -o tsv)
echo "testServiceClientId=$testServiceClientId"

# built-in roles
OWNER="/subscriptions/$SUBSCRIPTION_ID/providers/Microsoft.Authorization/roleDefinitions/8e3af657-a8ff-443c-a75c-2fe8c4bcb635"
CONTRIBUTOR="/subscriptions/$SUBSCRIPTION_ID/providers/Microsoft.Authorization/roleDefinitions/b24988ac-6180-42a0-ab88-20f7382dd24c"
READER="/subscriptions/$SUBSCRIPTION_ID/providers/Microsoft.Authorization/roleDefinitions/acdd72a7-3385-48ef-bd42-f606fba81ae7"
NETWORK_CONTRIBUTOR="/subscriptions/$SUBSCRIPTION_ID/providers/Microsoft.Authorization/roleDefinitions/4d97b98b-1d4f-4787-a291-c67834d212e7"
ACR_PULL="/subscriptions/$SUBSCRIPTION_ID/providers/Microsoft.Authorization/roleDefinitions/7f951dda-4ed3-4680-a7ca-43fe172d538d"
KEY_VAULT_ADMINISTRATOR="/subscriptions/$SUBSCRIPTION_ID/providers/Microsoft.Authorization/roleDefinitions/00482a5a-887f-4fb3-b363-3b7fe8e74483"
KEY_VAULT_READER="/subscriptions/$SUBSCRIPTION_ID/providers/Microsoft.Authorization/roleDefinitions/21090545-7ca7-4776-b22c-e363652d74d2"
KEY_VAULT_SECRET_USER="/subscriptions/$SUBSCRIPTION_ID/providers/Microsoft.Authorization/roleDefinitions/4633458b-17de-408a-b874-0445c86b69e6"

az role assignment create --assignee $testServiceClientId --scope /subscriptions/${SUBSCRIPTION_ID}/resourceGroups/${RG_KV} --role $KEY_VAULT_SECRET_USER


```

## Kube-login

```sh
az aks get-credentials --name ${AKS_CLUSTER_NAME} -g ${RG_APP}
#managed_rg=$(az aks show --resource-group $RG_APP --name ${{ env.AKS_CLUSTER_NAME }} --query nodeResourceGroup -o tsv)
#echo "CLUSTER_RESOURCE_GROUP:" $managed_rg

```


## AAD WI Pre-req

```sh
export CONTAINER_REGISTRY=${{ env.AZURE_CONTAINER_REGISTRY }}
export REPO=${{ env.REPOSITORY }} 
export IMAGE_TAG=$tag_id

# AAD WI
export AAD_WI_VERSION=${WI_VERSION}
export KEYVAULT_NAME=${KV_NAME}
export IDENTITY_TENANT=${{ env.SPRING_CLOUD_AZURE_TENANT_ID }}
export USER_ASSIGNED_CLIENT_ID=$testServiceClientId
export SERVICE_ACCOUNT_NAME="sa-aad-wi-test"

export AKS_OIDC_ISSUER="$(az aks show -n ${AKS_CLUSTER_NAME} -g $RG_APP --query "oidcIssuerProfile.issuerUrl" -o tsv)"
export PETCLINIC_NS=$TARGET_NAMESPACE
export AUDIENCES="api://AzureADTokenExchange"

echo ""
echo "AKS_OIDC_ISSUER:" $AKS_OIDC_ISSUER
echo ""


ls -al ./k8s
envsubst < ./k8s/sa.yaml > ./k8s/deploy/sa-test.yaml
echo "Cheking folder " ./k8s/deploy
ls -al ./k8s/deploy
cat ./k8s/deploy/sa-test.yaml

kubectl apply -f ./k8s/deploy/sa-test.yaml -n $TARGET_NAMESPACE

az identity federated-credential create --name customersServiceFedIdentity --identity-name "${TEST_SVC_APP_APP_ID_NAME}" --issuer "${AKS_OIDC_ISSUER}" --subject system:serviceaccount:"$TARGET_NAMESPACE":"${SERVICE_ACCOUNT_NAME}" -g "$RG_APP"

```


## Create ConfigMap

```sh
mkdir ./k8s/deploy
echo "Cheking folder " .
ls -al ./k8s


cat <<EOF >> ./k8s/deploy/cm-test.yaml
apiVersion=v1
kind=ConfigMap
metadata:
    name=springcloudazure
data:
    SPRING_CLOUD_AZURE_TENANT_ID=${SPRING_CLOUD_AZURE_TENANT_ID}
    SPRING_CLOUD_AZURE_KEY_VAULT_ENDPOINT="${SPRING_CLOUD_AZURE_KEY_VAULT_ENDPOINT}"
    TEST_SVC_APP_IDENTITY_CLIENT_ID=$testServiceClientId
EOF

cat ./k8s/deploy/cm-test.yaml
kubectl apply -f ./k8s/deploy/cm-test.yaml -n ${TARGET_NAMESPACE}
```



## Deploy to AKS 

```sh
export SECRET_PROVIDER_CLASS_NAME=${SECRET_PROVIDER_TEST}
echo "SECRET_PROVIDER_CLASS_NAME " $SECRET_PROVIDER_CLASS_NAME

export USER_ASSIGNED_CLIENT_ID=$customersServiceClientId
envsubst < ./k8s/secret-provider-class.yaml > ./k8s/deploy/secret-provider-class.yaml 

echo "Cheking folder " .
ls -al ./k8s/deploy
cat ./k8s/deploy/secret-provider-class.yaml 
kubectl apply -f ./k8s/deploy/secret-provider-class.yaml -n ${TARGET_NAMESPACE}

envsubst < ./k8s/test-deployment.yaml > ./k8s/deploy/test-deployment.yaml
envsubst < ./k8s/svc-cluster-ip.yaml > ./k8s/deploy/svc-cluster-ip.yaml 

kubectl apply -f test-deployment.yaml -n ${TARGET_NAMESPACE}
kubectl get SecretProviderClass -n ${TARGET_NAMESPACE}
```

## Test from the Pod

```sh

for pod in $(kubectl get po -n ${TARGET_NAMESPACE} -l app=test-service -o custom-columns=:metadata.name)
do
    kubectl logs $pod -n ${TARGET_NAMESPACE} | grep -i "Error"
    kubectl exec -it $pod -c test-service -n ${TARGET_NAMESPACE}  -- ls -al /mnt/secrets-store
    kubectl exec -it $pod -c test-service -n ${TARGET_NAMESPACE}  -- cat -al /mnt/secrets-store/dbpassword
done

```