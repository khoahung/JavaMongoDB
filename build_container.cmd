docker stop mongodb1 mongodb2 mongodb3 mariadb1 mariadb2
docker rm mongodb1 mongodb2 mongodb3 mariadb1 mariadb2
docker run -d --name mongodb1 -e MONGO_INITDB_ROOT_PASSWORD=123123Abc -p27016:27017 -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_DATABASE=rootdb -e MONGODB_INITIAL_PRIMARY_PORT_NUMBER=27017 mongo:latest
docker run -d --name mongodb2 -e MONGO_INITDB_ROOT_PASSWORD=123123Abc -p27017:27017 -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_DATABASE=rootdb -e MONGODB_INITIAL_PRIMARY_PORT_NUMBER=27017 mongo:latest
docker run -d --name mongodb3 -e MONGO_INITDB_ROOT_PASSWORD=123123Abc -p27018:27017 -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_DATABASE=rootdb -e MONGODB_INITIAL_PRIMARY_PORT_NUMBER=27017 mongo:latest
docker run -d -p 3306:3306  --name mariadb1 -eMARIADB_ROOT_PASSWORD=root mariadb/server:10.3
docker run -d -p 3307:3306  --name mariadb2 -eMARIADB_ROOT_PASSWORD=root mariadb/server:10.3