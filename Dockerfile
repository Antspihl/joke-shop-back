FROM eclipse-temurin:17-jre-alpine
ADD build/libs/veebiprojekt-test-0.0.1-SNAPSHOT.jar /app/app.jar
CMD java -jar -Dspring.config.location=classpath:/application.properties,file:/app/application.properties /app/app.jar

