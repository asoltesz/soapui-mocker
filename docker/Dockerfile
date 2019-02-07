FROM openjdk:8-jre

MAINTAINER András Soltész <soltesz.andras@gmail.com>

RUN mkdir /soapui-mocker && \
    cd /soapui-mocker && \
    mkdir config && \
    mkdir project-sets && \
    cd project-sets && \
    mkdir set-1 && \
    mkdir set-2 && \
    mkdir set-3 && \
    mkdir set-4 && \
    mkdir set-5 && \
    mkdir set-6 && \
    mkdir set-7 && \
    mkdir set-8 && \
    mkdir set-9 && \
    mkdir set-10

WORKDIR /soapui-mocker

# Add the service itself
ARG JAR_FILE
ADD target/${JAR_FILE} application.jar

ENTRYPOINT ["/usr/bin/java", "-jar", "/soapui-mocker/application.jar"]
