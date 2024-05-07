FROM openjdk:17
ARG DB_USERNAME
ARG DB_PASSWORD
ARG DB_URL
ENV DB_USERNAME=${DB_USERNAME} \
    DB_PASSWORD=${DB_PASSWORD} \
    DB_URL=${DB_URL}
COPY ./build/libs/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]