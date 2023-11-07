FROM openjdk:19
LABEL authors="Vadim Taratonov"
EXPOSE 8081
ARG JAR_FILE=/target/deal-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} deal

CMD ["java","-jar","deal"]