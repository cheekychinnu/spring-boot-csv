# What
Sample application to
- upload CSV file and map the entries to Model objects
- write POJOs to Response stream directly to be downloaded as CSV file
- Download a CSV file in classpath

# Tech Stack
- Spring Boot
- Swagger
- Gradle
- com.fasterxml.jackson.dataformat:jackson-dataformat-csv for CSV Mapper

# Build
./gradlew build
/.gradlew :bootRun

Go to http://localhost:8080/swagger-ui.html to access the Swagger API