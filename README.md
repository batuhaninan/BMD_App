# Requirements
* IDE (IntelliJ) - Optional
* Java 16
* OpenJDK 17

# Info

#### This app uses Spring Boot with Maven
#### For database it uses SQLite. It stores database locally in sqlitesample.db file

#### That means you can import the project into IntelliJ and run app / test with minimal configuration

#### We didn't implement the scheduling messages for later time feature (we tried to implement it but we couldn't finish it)

#### Before you can do anything you first need to signup and that creates jwt (token)
#### Without that token you can't access any other endpoint 
#### After 15 mins your token will expire and you need to login again
#### Also you need to put token to every request header's Authorization field
#### Date formats should be like 2021-10-16T12:08:56.235-0700
#### When you send a message it might take up to 3 minutes to get a respond because of retrying, it's not a bug

# Postman Collections for API (endpoints)

* [Auth Collection](Collections/Auth.postman_collection.json)
* [Client Collection](Collections/Client.postman_collection.json)
* [Message Collection](Collections/Message.postman_collection.json)
* [Query Collection](Collections/Query.postman_collection.json)




# Authors
### M. Batuhan INAN - Atilla R. Basaran