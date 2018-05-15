# rest-api-webservice
Simple RESTful API webservice for managing and storing notes
## Getting Started
### What you need
```
  MySQL version 5.6 or better
  JDK 1.8 or later
  Gradle 4+
  Linux OS probably
```
### Running app
#### Creating database
``` bash
  $ sudo mysql --password
  mysql> create database db_example; 
  mysql> create user 'springuser'@'localhost' identified by 'admin123';
  mysql> grant all on db_example.* to 'springuser'@'localhost';
```
#### Running application
``` bash
  $ ./gradlew build
  $ java -jar build/libs/gs-accessing-data-mysql-0.1.0.jar
```
### CRUD methods
####  (C) Adds new Note with title = "Example" and content="example"
  ``` bash
  $ curl -X POST 'localhost:8080/notes?title=Example&content=example'
  ```
####  (R) Returns note with given title
  ``` bash
  $ curl -X GET 'localhost:8080/notes?title=Example'
  ```
####  (U) Modifies content of note with title = "Example"
  ``` bash
  $ curl -X PUT 'localhost:8080/notes?title=Example&content=changed!'
  ```
#### (D) Deletes note with given title
  ``` bash
  $ curl -X DELETE 'localhost:8080/notes?title=Example'
  ```

### Additional methods
####  Generates x (here 4) notes
  ``` bash
  $ curl 'localhost:8080/notes/generate?number=4'
  ```
####  Returns sorted (sortBy=asc or sortBy=des) notes based on condition (sortHow):
  * sort by title -> sortHow=title
  * sort by content length -> sortHow=contLen
  * sort by modification date -> sortHow=modDate
  * sort by initial commit date -> sortHow=initDate
  ``` bash
  $ curl 'localhost:8080/notes/page?sortBy=asc&sortHow=modDate'
  ```
####  Returns notes that were not modified for x (here 32) days
  ``` bash
  $ curl 'localhost:8080/notes/modMorThan?days=32'
  ```
