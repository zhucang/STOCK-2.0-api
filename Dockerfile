# 使用官方 OpenJDK 21 作为基础镜像
FROM openjdk:8

# 将应用程序的 JAR 文件复制到容器中：这里的test。jar要改为相对路径例如：target/test.jar
COPY app.jar /springboot/app.jar

# 启动 Spring Boot 应用
