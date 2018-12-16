The following Technologies were used in the implementation of this assignment:

1- Eclipse
2- Maven
3- Spring Boot
4- Hibernate
5- Junit
6- MySql
7- Git
8- AWS EC2 Server
9- Postman tool


Please follow the following steps to test the source code:

1- Install MySql Database (you don't need to do this if you already have MySql on the Server)
2- Create a New Schema on MySql for convenience you can call it "offermanager"
3- Create a new user on MySql and grant the user full privileges on the schema created (for convenience you can name the user "manageruser")
4- Create a password for the user if you wish, (for convenience you can set it to "managerpass")
5- specify the port of which you would like to run the service on. for this I have configured it to be 8088 which can be changed.

6- Open a terminal and navigate to the working directory of the project at ~/worldpay_workspace/offer-manager (master)
7- Clone the git repository from https://github.com/jaberelfasi/My-Spring-Boot-Demo-For-The-World.git
8- import the project in eclipse from ~/worldpay_workspace/offer-manager (master)
9- if you chose to use different configuration values please update the application.properties file located at  ~/worldpay_workspace/offer-manager/src/main/resources (master)
10- If you chose to change the port number, please update the port value in line 29 in ~/worldpay_workspace/offer-manager/src/test/java/com/manager/offermanager/OfferManagerApplicationTests.java
11- Update Maven repository for the project
12- insure MySql is running
13- Run the project as a Spring Boot App
14- navigate in the working directory of the git repository at  ~/worldpay_workspace/offer-manager (master)
15- Run the following command "mvn clean package" to build the source code and run the Junit tests 


To Deploy the rest api:
1- copy the generated jar in the target folder in your desired server
2- Insure to have MySql available for the data persistence (you can use another db, but I have written a method in the Utilities that connects to MySql to make an update).
3- navigate to the location of the jar on the server and run the the following command: "java -jar location_to_jar/generateJarFromTheBuild.jar"

To consume the Rest Api please use the following patterns:

1- To add new offer (POST):
localhost:{port.number}/api/offer
sample post body messaage (json string)
{
        "offername": "buy 1 get 1 free",
        "offercode": "b1g2",
        "offerperiod": 1,
        "periodunit": "Month",
        "canceled": true
}


2- To retrieve all offers (GET):
localhost:{port.number}/api/offer

3- To retrieve an array of offers with the same names (GET):
localhost:{port.number}/api/offer/{common.offer.name}

4- To retrieve a single offer (GET):
localhost:{port.number}/api/singleoffer/1

5- To cancel an offer (PUT):
localhost:{port.number}/api/offer/{offer.id}
{
    "offername": "buy 1 get 1 free",
    "offercode": "b1g2",
    "offerperiod":"0.2",
    "periodunit":"Day",
    "canceled":true
}

6- To check if an offer is valid, expired or cancelled (GET):
localhost:{port.number}/api/checkoffer/{offer.id}


Finally the this api has been build and deployed on an AWS EC2 server at:

ec2-3-16-25-5.us-east-2.compute.amazonaws.com:8088/api/offer







