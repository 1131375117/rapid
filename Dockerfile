FROM openjdk:8-jre-alpine
ENV TZ=Asia/Shanghai
ENTRYPOINT ["java", "-jar", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005", "/opt/server.jar","-Dspring.profiles.active=dev"]
ARG JAR_FILE
ADD target/${JAR_FILE} /opt/server.jar