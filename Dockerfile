FROM registry.opensource.zalan.do/stups/openjdk:8-26
COPY ./target/pazuzu-ui.jar /
COPY scm-source.json /
EXPOSE 8080
CMD ["java", "-jar", "pazuzu-ui.jar"]
