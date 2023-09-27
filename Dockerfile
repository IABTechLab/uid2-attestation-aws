FROM rust:1.72 as jnsm_builder
COPY ./jnsm /
WORKDIR /jnsm
RUN cargo build --lib --release

FROM maven:3.8.6-jdk-8 AS java_builder
WORKDIR /
COPY attestation-aws /attestation-aws
COPY example /example
COPY build.sh /
RUN chmod +x /build.sh
RUN /build.sh

ARG LIB_VERSION=1.0.0-SNAPSHOT

WORKDIR /attestation-aws
RUN mvn package
RUN mvn deploy:deploy-file -Durl=file:///example/repo -Dfile="/attestation-aws/target/attestation-aws-${LIB_VERSION}.jar" -DgroupId='com.uid2' -DartifactId='attestation-aws' -Dpackaging=jar -Dversion=${LIB_VERSION}
WORKDIR /example
RUN mvn clean compile assembly:single


FROM ubuntu:22.04
ARG JAR_VERSION=1.0.0-SNAPSHOT
ENV JAR_VERSION=${JAR_VERSION}
ARG LIB_VERSION=1.0.0-SNAPSHOT

ENV LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/app/lib
WORKDIR /
RUN mkdir app
COPY --from=java_builder /example/repo /app/repo
COPY --from=java_builder /example/target/* /app/
COPY --from=jnsm_builder /jnsm/target/release/libjnsm.so /app/lib/
# COPY ./jnsm/target/release/libjnsm.so /app/lib/

COPY entrypoint.sh /app/
RUN chmod +x /app/entrypoint.sh
CMD ["/app/entrypoint.sh"]
