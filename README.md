# HotelManager

## Description
In this project you can register/log in, once you are registered, if you are an admin, you can add a room, edit, and delete, and all of the users can also, for the photos I'm using Firebase bucket to store the photos, MySQL for the database.
If your role is the user, you can book a room for specific dates, select details there, view all the rooms available, cancel the order, and more.

## Prerequisites
- Docker
- Docker Compose
- Java Development Kit (JDK)
- Maven

## Installation

### 1. Clone the Repository
`
git clone https://github.com/AlejandroRomanIbanez/HotelManager.git
cd your-repo
`

2. Build the JAR File

Ensure you have the JDK and Maven installed on your system. Then, build the JAR file using the following command:

`mvn clean package`

This command will generate a JAR file in the target directory.

3. Create Environment Files

Create the required environment files. You need to set up these files with the necessary configurations.
.env
secret.properties(There are examples of what you need to add here and in .env)
src/main/resources/firebase_credentials(You need to create a project there and create a storage, then take the SDK to use it and paste the JSON here)


4. Run the Application with Docker Compose

Use Docker Compose to build and run the application:

`docker compose up --build`


5. Access the Application

Once the containers are up and running, you can access the application at http://localhost:5454.

Test it in Postman to see if all is working, additionally, you can install this repo for having the frontend --> [React-Hotel](https://github.com/AlejandroRomanIbanez/Hotel-React) 

```


Additionally, you can see the demo deployed on this website --> [RestHotel](https://rest-hotel-one.vercel.app/)
