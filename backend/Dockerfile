FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY ./backend/target/expenseecho-0.0.1-SNAPSHOT.jar app.jar
RUN ls -la
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]