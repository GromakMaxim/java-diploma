FROM adoptopenjdk:11-jre-hotspot

ADD target/diploma1-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]