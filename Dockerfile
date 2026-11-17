#FROM eclipse-temurin:21-jdk
#
#WORKDIR "/user-service"
#
#COPY ./mvnw ./
#COPY ./.mvn ./.mvn
#COPY pom.xml ./
#
#RUN chmod +x mvnw
#
#CMD ["./mvnw", "spring-boot:run"]