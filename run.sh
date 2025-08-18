#!/bin/bash

# Load environment variables from .env file
if [ -f .env ]; then
    export $(cat .env | xargs)
fi

# Run the Spring Boot application
./mvnw spring-boot:run
