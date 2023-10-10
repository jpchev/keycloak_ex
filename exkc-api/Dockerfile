FROM openjdk

RUN mkdir /opt/exkc-api
COPY target/exkc-api.jar /opt/exkc-api/
CMD ["java", "-jar", "/opt/exkc-api/exkc-api.jar"]
