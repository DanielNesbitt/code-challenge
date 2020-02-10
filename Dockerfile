FROM openjdk:13
WORKDIR /usr/code_challenge
COPY ./server/build/distributions/server.zip .
COPY ./client/build/distributions/client.zip .
RUN yum install -y unzip
RUN unzip server.zip
RUN unzip client.zip
ENTRYPOINT ["./server/bin/server"]
