API proxy
================
Introduction
------------
This application creates an API Management Gateway for the Yelp Search API. Besides passing on calls to the Yelp API and
returning results back to the caller, this gateway also implements rate limits and provides analytics. The analytics
service displays a json list with the most popular search term and location and the number of times each was searched for.
The examples below show how to use the gateway.<br />
Example searches:<br />
http://localhost:8080/search<br />
http://localhost:8080/search?term=chinese&location=Santa+Barbara<br />
Analytics:<br />
http://localhost:8080/analytics<br />

Requirements
------------
JDK 1.6 or later<br />
Maven 3.0+

Installation
------------
1) Select 'Clone in Desktop' on the repository page<br />
2) run <code>mvn clean package</code> in the project directory to build the JAR<br />
3) run the jar by typing <code>java -jar target/yelp-api-gateway-0.1.0.jar</code><br />
The service should be running in a few seconds
