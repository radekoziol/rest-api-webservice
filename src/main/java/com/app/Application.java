package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
    ./gradlew build
    java -jar build/libs/gs-accessing-data-mysql-0.1.0.jar

    ***
    curl -X POST -d "title=Ex2&content=blablablaa2" localhost:8080/notes?

    curl 'localhost:8080/notes/all'

       curl -X PUT -d "title=Ex2&content=changed!" localhost:8080/notes?


    curl -X DELETE localhost:8080/notes?title=Ex2

    curl -X GET localhost:8080/notes?title=Ex2



    curl 'localhost:8080/demo/all'


 */


@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

