FROM openjdk:13
WORKDIR /usr/code_challenge
RUN yum install -y unzip
RUN ./gradlew distZip
COPY ./server/build/distributions/server.zip .
COPY ./client/build/distributions/client.zip .
RUN unzip server.zip
RUN unzip client.zip
ENTRYPOINT ["./server/bin/server"]
