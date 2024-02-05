FROM tomcat:10.1.15-jdk11

COPY /target/root.war /usr/local/tomcat/webapps

