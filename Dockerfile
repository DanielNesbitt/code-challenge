FROM openjdk:13
COPY ./server/build/distributions/server.zip /usr/code_challenge
COPY ./client/build/distributions/client.zip /usr/code_challenge
WORKDIR /usr/code_challenge
RUN unzip server.zip
RUN unzip client.zip
ENTRYPOINT ["./bin/server"]
