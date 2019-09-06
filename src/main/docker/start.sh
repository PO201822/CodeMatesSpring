#! /bin/bash
./delay.sh mypostgres:5432 -t 15
java -Djava.security.egd=file:/dev/./urandom -jar codemates-0.0.1-SNAPSHOT.jar
