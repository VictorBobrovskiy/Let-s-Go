FROM amazoncorretto:11
ENV TZ=America/Argentina/Buenos_Aires
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]