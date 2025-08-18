# Rate Limiter Demo

Here's my version of a Spring Boot application that demonstrates rate limiting using Redis. Users can store and retrieve data through a REST API, but data retrieval is limited to 5 requests per minute to prevent abuse.

This is built with Spring Boot, Redis for rate limiting, and MySQL for data persistence. This project showcases a practical implementation of the sliding window rate limiting pattern.

See `HELP.md` in /ratelimiter/src/main/java/com/example for setup instructions and API documentation.

😃
