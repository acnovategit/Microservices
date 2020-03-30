FROM openjdk:8
ADD target/DocWebApp-0.0.1-SNAPSHOT.jar  DocWebApp-0.0.1-SNAPSHOT.jar
EXPOSE 8088
ENTRYPOINT ["java","-jar","DocWebApp-0.0.1-SNAPSHOT.jar"]