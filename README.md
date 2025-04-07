Group members: Sourav Modak, Bhavdeep Singh, Abdul Mubeen and Hitesh Jha

Download Wildfly from 

https://www.wildfly.org/downloads/

extract and go to the bin/standalone

do a 

```
chmod +x ./standalone.sh
./standalone.sh
```

Then download the project and do a
```
mvn clean install wildfly:deploy
```
copy the war file from the target of the projects to standalone/deployments

The application is running on 
http://127.0.0.1:8080/ordermanagementsystem
