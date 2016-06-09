FROM registry.opensource.zalan.do/stups/openjdk:8-26
RUN mkdir /usr/pazuzu-ui
COPY ./target/pazuzu-ui.jar /usr/pazuzu-ui
WORKDIR /usr/pazuzu-ui
EXPOSE 8080
CMD ["java", "-jar", "pazuzu-ui.jar"]