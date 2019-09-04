FROM openjdk:8-jre
WORKDIR /app
VOLUME ["/app"]
COPY maven/codemates-0.0.1-SNAPSHOT.jar codemates-0.0.1-SNAPSHOT.jar
COPY maven/start.sh start.sh
COPY maven/delay.sh delay.sh
RUN sh -c 'touch codemates-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["./start.sh"]
