# Bank Account Service 

## Requirement 

Users should be able to open a bank account, and each bank account begins with an initial deposit. For simplicity, there is no need to consider closing a bank account.
Moreover, each bank account utilizes a credit line granted by the bank to overdraw an account. Assume that a given credit line does not change over time.
Each payment might be a credit or debit payment, consisting of a value date and booking amount.
Credit and debit payments affect the bank account by either increasing or decreasing the account balance. There is no need to consider overdrafts at this point, i.e., if payment is released, the account balance gets debited or credited accordingly.

Finally, let's define some typical CRUD (Create, Read, Update, and Delete) operations on our domain model:

It should be possible to open a bank account with an initial deposit and credit line.
It should be possible to retrieve the current account balance of a given bank account.
It should be possible to test if a pending debit payment would exceed the overdraft limit of that bank account.
It should be possible to get a list of all transactions booked of a given bank account since a given calendar date.
It should be possible to receive a list of all bank accounts in the red, i.e., whose account balance is lower than zero.

## Prerequisites


- [`Java 17`](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [`Docker`](https://www.docker.com/)
- [`Docker-Compose`](https://docs.docker.com/compose/install/)
- [`Maven`](https://maven.apache.org/download.cgi)

## Architecture

Event Sourcing and CQRS is used in application 

![2](https://github.com/baranipek/account-service/assets/8599551/c80d25b4-378e-4c61-87b8-bf7efe26651c)
![1](https://github.com/baranipek/account-service/assets/8599551/de443fed-4aec-497d-9702-27bad8c1aac8)


## Start Environment

Clone or download the app  

- Open a terminal and inside  root folder run

  ```
   docker-compose up 
  ```

- Wait a bit until `mysql` , `mongi` , `kafka` container is Up (healthy). You can check their status running
  ```
  docker-compose ps
  ```
  You should see running containers 
 ```
16ce4d32c4b0   mongo:latest             "docker-entrypoint.s…"   16 hours ago   Up 16 hours   0.0.0.0:27017->27017/tcp                             bank-service-master-mongo-1
b0ffa116408b   mysql:latest             "docker-entrypoint.s…"   16 hours ago   Up 16 hours   0.0.0.0:3306->3306/tcp, 33060/tcp                    bank-service-master-mysql-1
2c6fe471dfd1   wurstmeister/kafka       "start-kafka.sh"         16 hours ago   Up 16 hours   0.0.0.0:9092->9092/tcp                               bank-service-master-kafka-1
ea862b36d99d   wurstmeister/zookeeper   "/bin/sh -c '/usr/sb…"   16 hours ago   Up 16 hours   22/tcp, 2888/tcp, 3888/tcp, 0.0.0.0:2181->2181/tcp   bank-service-master-zookeeper-1

  ```

## Applications

- ### cqrs.core

This app uses internal library, so we need to install it first. Library is used as dependency in other projects

- Open a terminal and navigate `cqrs.core`  folder and run

  ```
   mvn clean install
  ```

- ### account.cmd 
This app manages account commands , lets install and run the app

 ```
 mvn clean install
 ```
 ```
 mvn spring-boot:run
 ```
You can open account with initial balance and credit line
 ```
curl --location --request POST 'http://localhost:5001/api/v1/account' \
--header 'Content-Type: application/json' \
--data-raw '{
"accountHolder" : "testUser",
"openingBalance" : 10,
"openCreditLine" : 10
}'
 ```
Response 
 ```
{
"message": "Bank Account created successfully",
"id": "5d338310-ba24-421c-afab-a171a271c724"
}
```
## URLs

| Application | URL                                   | Payload |
|-------------| ------------------------------------- |---------|
| account.cmd |http://localhost:5001/api/v1/account| {"accountHolder" : "testUser","openingBalance" : 10,"openCreditLine" : 10}  |


- ### account.query
This app manages account queries , lets install and run the app

 ```
 mvn clean install
 ```
 ```
 mvn spring-boot:run
 ```
Query bank account with account id 
 ```
curl --location --request GET 'http://localhost:5002/api/v1/account/5d338310-ba24-421c-afab-a171a271c724' \
--header 'Content-Type: application/json' \
--data-raw ''
 ```

Retrieve negative balance accounts 
 ```
curl --location --request GET 'http://localhost:5002/api/v1/account/balance/LESS_THAN/0' \
--header 'Content-Type: application/json' \
--data-raw ''
 ```

## URLs

| Application   | URL                               | Payload |
|---------------| --------------------------------- |---------|
| account.query |/{id}|   |
| account.query |/balance/{equalityType}/{balance}|   |


- ### payment.cmd
This app manages payment commands , lets install and run the app

 ```
 mvn clean install
 ```
 ```
 mvn spring-boot:run
 ```
Debit payment , balances goes down.Just note when the debit payment goes pending state , and account balance does not decrease
when available balance (balance+credit line) is negative
 ```
curl --location --request POST 'http://localhost:5004/api/v1/payment/debit' \
--header 'Content-Type: application/json' \
--data-raw '{
"accountId" : "95d75439-788f-4338-9a6b-1d7abe25a863",
"amount" : 4

}'
 ```
Credit payment , balances goes up
 ```
curl --location --request POST 'http://localhost:5004/api/v1/payment/credit' \
--header 'Content-Type: application/json' \
--data-raw '{
"accountId" : "95d75439-788f-4338-9a6b-1d7abe25a863",
"amount" : 4

}'
 ```

| Application   | URL     | Payload |
|---------------|---------|---------|
| payment.cmd   | /debit  | {"accountId" : "95d75439-788f-4338-9a6b-1d7abe25a863","amount" : 4}        |
| payment.cmd   | /credit | {"accountId" : "95d75439-788f-4338-9a6b-1d7abe25a863","amount" : 4}        |

 
- ### payment.query
This app manages payment queries , lets install and run the app

 ```
 mvn clean install
 ```
 ```
 mvn spring-boot:run
 ```

Get payments by account id and since the date give
 ```
curl --location --request GET 'http://localhost:5006/api/v1/payment/account/95d75439-788f-4338-9a6b-1d7abe25a863?fromDate=2023-07-19' \
--header 'Content-Type: application/json' \
--data-raw ''
 ```

Pending Statements 
 ```
curl --location --request GET 'http://localhost:5006/api/v1/payment/PENDING' \
--header 'Content-Type: application/json' \
--data-raw ''
 ```


## Business Flow 

First open bank account with init balance and credit line 

 ```
curl --location --request POST 'http://localhost:5001/api/v1/account' \
--header 'Content-Type: application/json' \
--data-raw '{
"accountHolder" : "testUserBaran",
"openingBalance" : 10,
"openCreditLine" : 10
}'
 ```

 ```
{
"message": "Bank Account created successfully",
"id": "279b984f-74c6-40c2-a2de-cf773c917105"
}
 ```

Query Account now 
 ```
curl --location --request GET 'http://localhost:5002/api/v1/account/279b984f-74c6-40c2-a2de-cf773c917105' \
--header 'Content-Type: application/json' \
--data-raw ''
 ```

 ```
{
    "message": "Successfully returned bank accounts.",
    "accounts": [
        {
            "id": "279b984f-74c6-40c2-a2de-cf773c917105",
            "accountHolder": "testUserBaran",
            "creationDate": "2023-07-19T10:55:42.085+00:00",
            "balance": 10.00,
            "creditLines": 10.00,
            "payments": []
        }
    ]
}
 ```

Credit Payment  and check the balance again 
 ```
curl --location --request POST 'http://localhost:5004/api/v1/payment/credit' \
--header 'Content-Type: application/json' \
--data-raw '{
"accountId" : "279b984f-74c6-40c2-a2de-cf773c917105",
"amount" : 5

}'
 ``` 

 ```
 {
    "message": "Successfully returned bank accounts.",
    "accounts": [
        {
            "id": "279b984f-74c6-40c2-a2de-cf773c917105",
            "accountHolder": "testUserBaran",
            "creationDate": "2023-07-19T10:55:42.085+00:00",
            "balance": 15.00,
            "creditLines": 10.00,
            "payments": [
                {
                    "id": "a5986482-2cb3-44ee-a1f0-75a55dc3e2c7",
                    "valueDate": "2023-07-19T10:58:55.835+00:00",
                    "amount": 5.00,
                    "paymentType": "CREDIT",
                    "processType": "SUCCESSFUL"
                }
            ]
        }
    ]
}
 ``` 

Lets do  few debit payment 
 ```
curl --location --request POST 'http://localhost:5004/api/v1/payment/debit' \
--header 'Content-Type: application/json' \
--data-raw '{
"accountId" : "279b984f-74c6-40c2-a2de-cf773c917105",
"amount" : 15

}'
 ``` 

 ```
curl --location --request POST 'http://localhost:5004/api/v1/payment/debit' \
--header 'Content-Type: application/json' \
--data-raw '{
"accountId" : "279b984f-74c6-40c2-a2de-cf773c917105",
"amount" : 10

}'
 ``` 

Query again as you see balance is now -10 

 ```
 {
    "message": "Successfully returned bank accounts.",
    "accounts": [
        {
            "id": "279b984f-74c6-40c2-a2de-cf773c917105",
            "accountHolder": "testUserBaran",
            "creationDate": "2023-07-19T10:55:42.085+00:00",
            "balance": -10.00,
            "creditLines": 10.00,
            "payments": [
                {
                    "id": "181a1e01-8285-4bf5-af02-844f5db2319e",
                    "valueDate": "2023-07-19T11:18:19.342+00:00",
                    "amount": 10.00,
                    "paymentType": "DEBIT",
                    "processType": "SUCCESSFUL"
                },
                {
                    "id": "6e20bc67-4da7-418a-a803-3c9a251b7f4f",
                    "valueDate": "2023-07-19T11:01:14.934+00:00",
                    "amount": 15.00,
                    "paymentType": "DEBIT",
                    "processType": "SUCCESSFUL"
                },
                {
                    "id": "a5986482-2cb3-44ee-a1f0-75a55dc3e2c7",
                    "valueDate": "2023-07-19T10:58:55.835+00:00",
                    "amount": 5.00,
                    "paymentType": "CREDIT",
                    "processType": "SUCCESSFUL"
                }
            ]
        }
    ]
}
``` 

if you have another debit payment more than total balance (balance + credit line)
 ```
curl --location --request POST 'http://localhost:5004/api/v1/payment/debit' \
--header 'Content-Type: application/json' \
--data-raw '{
"accountId" : "279b984f-74c6-40c2-a2de-cf773c917105",
"amount" : 50

}'
``` 
balance stays same and 50 debit payment is in pending state 
 ```
{
    "message": "Successfully returned bank accounts.",
    "accounts": [
        {
            "id": "279b984f-74c6-40c2-a2de-cf773c917105",
            "accountHolder": "testUserBaran",
            "creationDate": "2023-07-19T10:55:42.085+00:00",
            "balance": -10.00,
            "creditLines": 10.00,
            "payments": [
                {
                    "id": "181a1e01-8285-4bf5-af02-844f5db2319e",
                    "valueDate": "2023-07-19T11:18:19.342+00:00",
                    "amount": 10.00,
                    "paymentType": "DEBIT",
                    "processType": "SUCCESSFUL"
                },
                {
                    "id": "6caa6dee-6cf2-4462-b619-8f2ee0a35df3",
                    "valueDate": "2023-07-19T11:21:13.741+00:00",
                    "amount": 50.00,
                    "paymentType": "DEBIT",
                    "processType": "PENDING"
                },
                {
                    "id": "6e20bc67-4da7-418a-a803-3c9a251b7f4f",
                    "valueDate": "2023-07-19T11:01:14.934+00:00",
                    "amount": 15.00,
                    "paymentType": "DEBIT",
                    "processType": "SUCCESSFUL"
                },
                {
                    "id": "a5986482-2cb3-44ee-a1f0-75a55dc3e2c7",
                    "valueDate": "2023-07-19T10:58:55.835+00:00",
                    "amount": 5.00,
                    "paymentType": "CREDIT",
                    "processType": "SUCCESSFUL"
                }
            ]
        }
    ]
}
``` 

Lets query pending payments  
 ```
curl --location --request GET 'http://localhost:5006/api/v1/payment/PENDING' \
--header 'Content-Type: application/json' \
--data-raw ''
``` 
Let see the red accounts there 
 ```
curl --location --request GET 'http://localhost:5002/api/v1/account/balance/LESS_THAN/0' \
--header 'Content-Type: application/json' \
--data-raw ''
``` 

You will see expected results here , our system is eventually consistent 
