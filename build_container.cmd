docker stop mongodb1 mongodb2 mariadb
docker rm mongodb1 mongodb2 mariadb
docker run -d --name mongodb2 -e MONGO_INITDB_ROOT_PASSWORD=123123Abc -p27018:27017 -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_DATABASE=rootdb -e MONGODB_INITIAL_PRIMARY_PORT_NUMBER=27017 mongo:latest
docker run -d --name mongodb1 -e MONGO_INITDB_ROOT_PASSWORD=123123Abc -p27017:27017 -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_DATABASE=rootdb -e MONGODB_INITIAL_PRIMARY_PORT_NUMBER=27017 mongo:latest
docker run -d -p 3306:3306  --name mariadb -eMARIADB_ROOT_PASSWORD=root mariadb/server:10.3