FROM mysql:8.0

COPY ./init.sql /docker-entrypoint-initdb.d/

ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_DATABASE=banco_bps
ENV MYSQL_USER=admin
ENV MYSQL_PASSWORD=root

EXPOSE 3306