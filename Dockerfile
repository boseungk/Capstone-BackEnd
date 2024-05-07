FROM openjdk:17
ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} /aistyling.jar
ENTRYPOINT ["java","-jar","/aistyling.jar"]