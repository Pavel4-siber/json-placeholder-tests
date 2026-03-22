FROM jenkins/jenkins:lts
USER root
RUN apt install -y maven
USER jenkins