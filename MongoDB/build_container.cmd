docker run -d --name mongodb2 -e MONGODB_ROOT_PASSWORD=123123a@ -p27018:27017 -e MONGODB_USERNAME=root -e MONGODB_DATABASE=rootdb -e MONGODB_INITIAL_PRIMARY_PORT_NUMBER=27017 mongo:latest
docker run -d --name mongodb1 -e MONGODB_ROOT_PASSWORD=123123a@ -p27017:27017 -e MONGODB_USERNAME=root -e MONGODB_DATABASE=rootdb -e MONGODB_INITIAL_PRIMARY_PORT_NUMBER=27017 mongo:latest
