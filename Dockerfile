FROM openjdk:13
COPY ./server/build/distributions/server.zip /usr/code_challenge
WORKDIR /usr/src/code_challenge
RUN unzip server.zip
ENTRYPOINT ["./bin/server"]
