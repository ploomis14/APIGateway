Yelp API Gateway
================
Introduction
------------
This application creates an API Management Gateway for the Yelp Search API. Besides passing on calls to the Yelp API and
returning results back to the caller, this gateway also implements rate limits and provides analytics. The analytics
service displays a json list with the most popular search term and location and the number of times each was searched for.
The examples below show how to use the gateway.
Example searches:
http://localhost:8080/search
http://localhost:8080/search?term=chinese&location=Santa+Barbara
Analytics:
http://localhost:8080/analytics

Requirements
------------
JDK 1.6 or later
Maven 3.0+

Installation
------------
1) Select 'Clone in Desktop' on the repository page
2) run <code>mvn clean package</code> in the project directory to build the JAR
3) run the jar by typing <code>java -jar target/yelp-api-gateway-0.1.0.jar</code>
The service should be running in a few seconds