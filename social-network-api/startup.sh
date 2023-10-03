#!/bin/bash
export DB_URL=jdbc:mysql://yourhost.mysql.tools/yourhost_facebook
export DB_USERNAME=yourhost_facebook
export DB_PASSWORD=v6ay23%%XZ
export EMAIL_HOST=mail.adm.tools
export EMAIL_PORT=465
export EMAIL=facebook@prison.kiev.ua
export EMAIL_PASSWORD=604df7f3-a832-4eec-b729-020f9074effB
export SERVER_PORT=9000
export SPRING_PROFILES_ACTIVE=prod
export FRONTEND_URL=*

cd /home/ec2-user
#aws s3 cp s3://fb-bucket-elena/facebook-0.0.1-SNAPSHOT.jar .
java -jar facebook-0.0.1-SNAPSHOT.jar
