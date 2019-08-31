FROM openjdk:11

COPY target/service-authentication-0.0.1-SNAPSHOT.jar /opt/lib/

ENTRYPOINT ["java"]
CMD ["-jar", "/opt/lib/service-authentication-0.0.1-SNAPSHOT.jar", "--server.port=8080"]