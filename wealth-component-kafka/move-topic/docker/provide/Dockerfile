FROM eclipse-temurin:8-jre-focal

LABEL COPYRIGHT="huenhui"
MAINTAINER "huenhui" <huenhui@datapipeline.com>

ARG VERSION
ENV PROD_VERSION $VERSION

ENV HOME /opt
RUN chmod 777 $HOME
WORKDIR $HOME

COPY move-topic-1.0.0-jar-with-dependencies.jar .
COPY startMoveTopic.sh .
COPY conf ./conf
COPY log ./log
COPY README.md .

# 为了让容器不退出，执行一个一直休眠的程序
CMD ["nohup","java","-cp", "move-topic-1.0.0-jar-with-dependencies.jar","move.topic.api.DockerMain"]
