# MyHotelProyect

This application was generated using SpringBoot 2.7.3, you can find documentation and help at [https://start.spring.io/](https://start.spring.io/).

## Project Structure

Node is required for generation and recommended for development. `package.json` is always generated for a better development experience with prettier, commit hooks, scripts and so on.

In the project root, JHipster generates configuration files for tools like git, prettier, eslint, husky, and others that are well known and you can find references in the web.

`/src/*` structure follows default Java structure.


- `/src/main/docker` - Docker configurations for the application and services that the application depends on

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. For run this proyect you need to configure  a database for do that ,you need to modify application-dev.yml and configure 
your database, you only need to create a empty database data is supply using liquibase , if you dont have a database in your environment you can use the database provide under docker file
 ```
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:postgresql://localhost:5432/truck
    username: postgres
    password: postgres
    hikari:
      poolName: Hikari
      auto-commit: false
  jpa:
    database-platform: tech.jhipster.domain.util.FixedPostgreSQL10Dialect
  liquibase:
    # Remove 'faker' if you do not want the sample data to be loaded automatically
    contexts: dev, faker
```

After you configure the use of the database you can run the proyect inside e IDE like IntelliJ IDEA o you
can go to the app directory an run this command.

```
./wvnw
```

We offer a postman collection whit  the call of all API rest,the collection is in root directory
```
MyHotelCollection.postma_collection.json
```


## Testing

To launch your application's tests, run:

```
./mvnw verify
```


### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

Note: we have turned off authentication in [src/main/docker/sonar.yml](src/main/docker/sonar.yml) for out of the box experience while trying out SonarQube, for real use cases turn it back on.

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar
```

