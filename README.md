## Micronaut 4.8.0 Documentation

- [User Guide](https://docs.micronaut.io/4.8.0/guide/index.html)
- [API Reference](https://docs.micronaut.io/4.8.0/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/4.8.0/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

## Deployment with GraalVM

If you want to deploy to AWS Lambda as a GraalVM native image, run:

```bash
./mvnw package -Dpackaging=docker-native -Dmicronaut.runtime=lambda -Pgraalvm
```

This will build the GraalVM native image inside a docker container and generate the `function.zip` ready for the deployment.

Build with Docker:

```bash
docker build -t micronaut-aws-lambda .
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v $(pwd):/app -w /app micronaut-lambda-builder
```

## Terraform
0. **Create backend S3 bucket**
   ```bash
   cd terraform/remote-state
   terraform init && terraform apply
   cd ..
   ```
1. **Check AWS region**:
    ```bash
    aws configure get region
    ```
2. **Set AWS region**:
    ```bash
    export AWS_REGION=us-east-1
    ```
3. **cd to terraform directory**:
    ```bash
    cd terraform
    ```
4. **Initialize Terraform**:
    ```bash
    terraform init
    ```
5. **Apply Terraform configuration**:
    ```bash
    terraform apply -var-file=terraform_A75.tfvars # specific .tfvars file
    ```
6. **List all deployed resources**:
    ```bash
    terraform state list
    ```
7. **Destroy all resources**:
    ```bash
    terraform destroy -var-file=terraform_A75.tfvars
    ```
8. **Update only Lambda jar**:
    ```bash
    aws lambda update-function-code --function-name entsoe-scraper --zip-file fileb://../target/function.zip
    ```
9. **If required, deleting ENI or you may need to wait**:
   ```bash
   aws ec2 delete-network-interface --network-interface-id eni-1234567890abcdef0
   ```

## Handler

Handler: org.example.FunctionRequestHandler

[AWS Lambda Handler](https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html)

- [Micronaut Maven Plugin documentation](https://micronaut-projects.github.io/micronaut-maven-plugin/latest/)
## Feature maven-enforcer-plugin documentation

- [https://maven.apache.org/enforcer/maven-enforcer-plugin/](https://maven.apache.org/enforcer/maven-enforcer-plugin/)


## Feature aws-lambda-custom-runtime documentation

- [Micronaut Custom AWS Lambda runtime documentation](https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambdaCustomRuntimes)

- [https://docs.aws.amazon.com/lambda/latest/dg/runtimes-custom.html](https://docs.aws.amazon.com/lambda/latest/dg/runtimes-custom.html)


## Feature serialization-jackson documentation

- [Micronaut Serialization Jackson Core documentation](https://micronaut-projects.github.io/micronaut-serialization/latest/guide/)


## Feature aws-lambda documentation

- [Micronaut AWS Lambda Function documentation](https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambda)


## Feature aws-lambda-events-serde documentation

- [Micronaut AWS Lambda Events Serde documentation](https://micronaut-projects.github.io/micronaut-aws/snapshot/guide/#eventsLambdaSerde)

- [https://github.com/aws/aws-lambda-java-libs/tree/main/aws-lambda-java-events](https://github.com/aws/aws-lambda-java-libs/tree/main/aws-lambda-java-events)


## Feature http-client-jdk documentation

- [Micronaut HTTP Client Jdk documentation](https://docs.micronaut.io/latest/guide/index.html#jdkHttpClient)

- [https://openjdk.org/groups/net/httpclient/intro.html](https://openjdk.org/groups/net/httpclient/intro.html)


