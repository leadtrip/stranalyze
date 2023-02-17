FROM eclipse-temurin:17
WORKDIR /home/app
COPY ./build/libs/stravaView-0.1-all.jar /home/app/application.jar

ENTRYPOINT ["java", "-jar", "/home/app/application.jar"]