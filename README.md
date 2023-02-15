
RESTful web service delivery application with [Docker](https://www.docker.com/) 

#### Prerequisite 

Installed:   
[Docker](https://www.docker.com/)   
[git](https://www.digitalocean.com/community/tutorials/how-to-contribute-to-open-source-getting-started-with-git)   

Optional:   
[Docker-Compose](https://docs.docker.com/compose/install/)   
[Java 17 ](https://www.oracle.com/technetwork/java/javase/overview/index.html)   
[Maven 3.x](https://maven.apache.org/install.html)

#### Steps

##### Clone source code from git
```
git clone https://gitlab.com/yusupovlab/delivery-app.git
```

##### Build Docker image
```
docker build -t="delivery-app-java" .
```

Maven build will be executed during creation of the docker image.

>Note:if you run this command for first time it will take some time in order to download base image from [DockerHub](https://hub.docker.com/)

##### Run Docker Container
```
docker run -p 8080:8080 -it --rm delivery-app-java
```

##### Test application

```
curl localhost:8080
```


#####  Stop Docker Container:
```
docker stop `docker container ls | grep "delivery-app-java:*" | awk '{ print $1 }'`
```

### Run with docker-compose 

Build and start the container by running 

```
docker-compose up -d 
```

#### Test application with ***curl*** command

```
curl localhost:8080
```

##### Stop Docker Container:
```
docker-compose down
```