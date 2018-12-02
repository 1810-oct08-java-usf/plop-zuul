FROM openjdk:8-jdk-alpine
ADD . /zuul-service
WORKDIR /zuul-service
EXPOSE 8762
CMD ["java", "-jar", "target/rpm-zuul-server-0.0.1-SNAPSHOT.jar"]
