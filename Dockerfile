FROM registry.access.redhat.com/ubi8/openjdk-17:1.17

ENV LANGUAGE='en_US:en'

COPY build/quarkus-app/lib/ /opt/application/lib/
COPY build/quarkus-app/app/ /opt/application/app/
COPY build/quarkus-app/quarkus/ /opt/application/quarkus/
COPY build/quarkus-app/quarkus-run.jar /opt/application/quarkus-run.jar
COPY build/quarkus-app/quarkus-app-dependencies.txt /opt/application/quarkus-app-dependencies.txt

EXPOSE 8080
USER 185

WORKDIR /opt/application

CMD ["java", "-jar", "quarkus-run.jar"]
