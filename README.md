# uid2-attestation-aws

UID2 attestation implementation for AWS Nitro Enclave.

- nsm-java contains a JNI wrapper of Nitro Secure Module (NSM) library.
- attestation-aws implements [UID2 attestation API](https://github.com/IABTechLab/uid2-attestation-api)
- example contains an example Java program to consume the API

## How to Install

For maven projects:
```
<!-- https://mvnrepository.com/artifact/com.uid2/attestation-aws -->
<dependency>
    <groupId>com.uid2</groupId>
    <artifactId>attestation-aws</artifactId>
    <version>1.1.0</version>
</dependency>
```
