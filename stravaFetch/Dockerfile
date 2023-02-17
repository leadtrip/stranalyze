FROM eclipse-temurin:17
RUN mkdir /opt/app
COPY ./build/libs/stravaFetch-0.1-all.jar /home/app/application.jar

ENTRYPOINT ["java", "-jar", "/home/app/application.jar"]