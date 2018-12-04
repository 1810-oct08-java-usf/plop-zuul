FROM java:8
COPY target/rpm-zuul-server-0.0.1-SNAPSHOT.jar /tmp/rpm-zuul-server-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "/tmp/rpm-zuul-server-0.0.1-SNAPSHOT.jar","--server.servlet.context-path=/rpm-zuul","&"]
