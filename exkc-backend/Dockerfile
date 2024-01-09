FROM docker.io/openjdk

RUN mkdir /opt/exkc-backend
COPY target/exkc-backend.jar /opt/exkc-backend/
CMD ["java", "-jar", "/opt/exkc-backend/exkc-backend.jar"]
