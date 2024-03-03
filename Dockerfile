#FROM eclipse-temurin:17-jdk-alpine as build
##WORKDIR /workspace/app
##ARG JAR_FILE=target/*.jar
##COPY ./target/prod-0.0.1-SNAPSHOT.jar app.jar
#ADD /target/prod-0.0.1-SNAPSHOT.jar app.jar
#ENV SERVER_PORT=8080
#CMD ["sh", "-c", "exec java -Dserver.port=$SERVER_PORT -Dserver.address=0.0.0.0 -cp app:app/lib/* com.bulibuli.prod.ProdApplication"]
#ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]