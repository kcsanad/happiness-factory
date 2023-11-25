FROM eclipse-temurin:17-jdk-alpine

ARG USERNAME=appuser
ARG GRPNAME=appgrp
ARG USER_UID=1101
ARG USER_GID=$USER_UID

ARG JAVA_OPTS
ENV JAVA_OPTS="-XX:+UseG1GC $JAVA_OPTS"

USER root

RUN apk update && apk --no-cache add shadow

RUN mkdir /dps && \
    mkdir /dps/config && \
    addgroup -g $USER_GID $GRPNAME && \
    useradd -u $USER_UID -g $USER_GID $USERNAME && \
    chown -R $USER_UID:$USER_GID /dps

COPY docker/entrypoint.sh /

RUN chown -R $USER_UID:$USER_GID /entrypoint.sh && \
    chmod 740 /entrypoint.sh

COPY target/happiness-factory.jar /dps/app.jar

RUN chown -R $USER_UID:$USER_GID /dps && \
    chmod 740 /dps/app.jar

USER appuser

WORKDIR /dps
ENTRYPOINT ["/entrypoint.sh"]
CMD ["run"]