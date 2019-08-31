FROM openjdk:11
ENV API_PORT=8080

COPY target/service-authentication-0.0.1-SNAPSHOT.jar /opt/lib/

ENTRYPOINT ["java"]
CMD ["-jar", "/opt/lib/service-authentication-0.0.1-SNAPSHOT.jar", "--server.port=${API_PORT}"]