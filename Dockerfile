FROM eclipse-temurin:17-jdk

COPY "./target/sigprod-1.jar" "app.jar"

EXPOSE "8206"

ENTRYPOINT ["java", "-jar", "app.jar"]