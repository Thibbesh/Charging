Everon Back-End coding assignment
Charging is spring-boot application. Maven build tool which I used to build the project goals.
use maven command to build and deploy to any containers. Ex: Docker or Kubernetes

Tech Stack:
Java 8
Spring Boot 2.1.9.RELEASE
spring mock 2.0.8
junit-jupiter 5.3.2
Lombok 1.18.10
maven 3.3.1
mockito 2.23.4 

How to run it
Download this Charging project 
run this below maven command to build

$ mvn clean install

after that just run ChargingApplication.java. main class of Springboot Application.  

The application will start on port 8080.

Example: 
http://localhost:8080/chargingSessions

Charging Session API
The following endpoints have been implemented:
(Use Postman for testing)

POST /chargingSession – submit a new charging session for the station. 
End point name:
http://localhost:8080/chargingSessions
Request Body is:
{ ABC-123 }

GET /chargingSessions  - getAllChargingSession with below response:
End point name:
http://localhost:8080/chargingSessions

[
{
"id": "564769b8-0dd0-41ad-970a-c937a2f43856",
"stationId": "ABC-789",
"startedAt": "2020-03-03T15:57:59.594",
"stoppedAt": "2020-03-03T15:58:33.838",
"status": "FINISHED"
},
{
"id": "c7c7c85c-f6ff-4327-9918-23e0f9a4d7b1",
"stationId": "ABC-123",
"startedAt": "2020-03-03T15:57:50.262",
"status": "IN_PROGRESS"
},
{
"id": "313fce22-77b0-4b0b-b4be-5d0924b289bd",
"stationId": "ABC-456",
"startedAt": "2020-03-03T15:57:54.695",
"status": "IN_PROGRESS"
}
]

PUT /chargingSession/<session_id> – stops charging session.
 End point name:
 http://localhost:8080/chargingSessions/564769b8-0dd0-41ad-970a-c937a2f43856
 
 Response Body is :
 {
     "id": "564769b8-0dd0-41ad-970a-c937a2f43856",
     "stationId": "ABC-789",
     "startedAt": "2020-03-03T15:57:59.594",
     "stoppedAt": "2020-03-03T15:58:33.838",
     "status": "FINISHED"
 }


GET /chargingSession - retrieves a summary of submitted charging sessions including:
End point name:
http://localhost:8080/chargingSessions/summary

Response body is:
{
"totalCount": 4,
"startedCount": 3,
"stoppedCount": 1
}

totalCount – total number of charging sessions for the last minute
startedCount – total number of started charging sessions for the last minute
stoppedCount – total number of stopped charging sessions for the last minute