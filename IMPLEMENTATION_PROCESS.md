## Rabobank Assignment for Authorizations Area

### Business Requirement - *Interpretation*

  1. Users must be able to create write or read access for payments and savings accounts - this is applicable for grantor
  2. Users need to be able to retrieve a list of accounts they have read or write access for - this is applicable for grantee

### Assumptions - *1st Iteration*

	> The usecase can be implemented for both the types of users, but implementing this for business accounts could 
      make it more complex. Hence the assumption here is, to implement only for private individuals of Rabobank

	> The Power of Attorney could involve various legal process or validating the user on legal terms.  
      The assumption here , the customer is legally complaint with Rabobank and he can 
      give the power of Attorney  to any individual.

	> The grantee, can be anyone who lives in Netherlands or the dependents living outside of Netherlands. 
      Extending to other parties, might invite legalites, which is outside the scope of this application
    
    > Technically I spent some time , in design the database tables and fit that into to a Mongo DB. I got a better 
      understanding, that , relational schema design is not necessary for MONGODB. Hence i started with the 
      pushing the data to the embedded database.

    > Tried connecting with a MONGO DB Docker image. Could not succeed. Left it to the second iteration

### Assumptions - *2nd Iteration*
	> The grantor can grant only one authorisation per account and can override his previous authorizations to limit the scope of this assignment.
  
	> Any single Grantee, can have multiple authorizations, but from a different accounts
    
    > Executing a different authorization for the same account is considered as overridding the previous power of attorney
    
## Swagger Documentation
http://localhost:8080/swagger-ui.html#/power-of-attorney-controller

##Export the Docker Image
    > Docker Installation is a pre-requisite
    > mvn dockerfile:build - Builds the docker image of this project

##Run the application
    > docker -p 8081:8080 nl.rabobank/power-of-attorney

##Postman script
    > The postman script is available at the root directory. Please import the collection into your postman and start
      executing the scripts