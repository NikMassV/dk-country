Network:
- docker network create country_net

Mongo:
- docker pull mongo:latest
- docker run -d -p 27017:27017 --name mongodb-dk-country -d --network country_net mongo

PostgresSQl:
- docker pull postgres:latest
- docker run -d -p 5432:5432 --name postgres-dk-country -e POSTGRES_HOST_AUTH_METHOD=trust -d --network country_net postgres

Project:
- docker build . --tag=dk-country:latest

- docker run -p 9200:9200 -e postgres.host='postgres-dk-country' -e postgres.port='5432' -e postgres.password='postgres' -e mongo.host='mongodb-dk-country' -e mongo.port='27017' -e mongo.database='mongodb-dk-country' --network country_net dk-country:latest
