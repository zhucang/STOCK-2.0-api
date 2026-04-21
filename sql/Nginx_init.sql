/**
  配置域名
 */
/** 官网*/
set @website = 'website.com';
/** 官网*/
set @website2 = 'website2.com';
/** 手机页*/
set @h5 = 'h5.com';
/** 手机页*/
set @h52 = 'h52.com';
/** 描述*/
set @describe = 'describe.com';
/** 后台1*/
set @admin1 = 'admin1.com';
/** 后台2*/
set @admin2 = 'admin2.com';
/** 线路3*/
set @line3 = 'line3.com';

truncate table nginx_config;
insert into nginx_config(id, server_name, config_type, config_content)
values
    (1,'kmadmin.admin1.com',0,'    server {
        listen       80 ;
        server_name  ${domain};
	    client_max_body_size 30m;

        location   / {
            root   /web-vue/admin/;
            index  index.html index.htm index;
            try_files $uri $uri/ /index.html;
        }
        location /prod-api/ {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201/;
        }
        location /api {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201;
        }
        location /common {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201;
        }
        location /profile {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201;
        }
    }'),
    (2,'kmadmin.admin2.com',0,'    server {
        listen       80 ;
        server_name  ${domain};
	    client_max_body_size 30m;

        location   / {
            root   /web-vue/admin/;
            index  index.html index.htm index;
            try_files $uri $uri/ /index.html;
        }
        location /prod-api/ {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201/;
        }
        location /api {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201;
        }
        location /common {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201;
        }
        location /profile {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201;
        }
    }'),
    (3,'www.admin1.com',4,'    server {
        listen       80 ;
        server_name  ${domain};
        location /prod-api/ {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201/;
        }
        location /api {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201;
        }
        location /common {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201;
        }
        location /profile {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201;
        }
        location /captchaImage {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201;
        }
    }'),
    (4,'www.admin2.com',4,'    server {
        listen       80 ;
        server_name  ${domain};
        location /prod-api/ {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201/;
        }
        location /api {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201;
        }
        location /common {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201;
        }
        location /profile {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201;
        }
        location /captchaImage {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201;
        }
    }'),
    (5,'www.h5.com',1,'    server {
        listen       80 ;
        server_name  ${domain};

        location   / {
             alias   /web-vue/h5/;
             index  index.html index.htm index;
             try_files $uri $uri/ /index.html;
         }
        location   /customerService {
             alias   /web-vue/customerService/;
             index  index.html index.htm index;
             try_files $uri $uri/ /index.html;
         }
    }'),
    (6,'www.describe.com',2,'    server {
        listen       80 ;
        server_name  ${domain};

        location   / {
             alias   /web-vue/h5/;
             index  index.html index.htm index;
             try_files $uri $uri/ /index.html;
         }
        location   /download {
             alias   /web-vue/download/;
             index  index.html index.htm index;
             try_files $uri $uri/ /index.html;
         }
    }'),
    (7,'www.website.com',3,'    server {
        listen       80 ;
        server_name  ${domain};
        location   / {
             alias   /web-vue/website/;
             index  index.html index.htm index;
             try_files $uri $uri/ /index.html;
        }
        location /api {
                proxy_set_header Host $http_host;
                proxy_set_header X-Real-IP $remote_addr;
                  proxy_set_header REMOTE-HOST $remote_addr;
                  proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                 proxy_pass http://127.0.0.1:9201;
        }
        location /common {
                proxy_set_header Host $http_host;
                proxy_set_header X-Real-IP $remote_addr;
                  proxy_set_header REMOTE-HOST $remote_addr;
                  proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                 proxy_pass http://127.0.0.1:9201;
        }
        location /profile {
                proxy_set_header Host $http_host;
                proxy_set_header X-Real-IP $remote_addr;
                proxy_set_header REMOTE-HOST $remote_addr;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_pass http://127.0.0.1:9201;
        }
    }'),
    (8,'www.line3.com',4,'    server {
        listen       80 ;
        server_name  ${domain};
        location /prod-api/ {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201/;
        }
        location /api {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201;
        }
        location /common {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201;
        }
        location /profile {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201;
        }
        location /captchaImage {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:9201;
        }
    }'),
    (9,'www.h52.com',1,'    server {
        listen       80 ;
        server_name  ${domain};

        location   / {
             alias   /web-vue/h5/;
             index  index.html index.htm index;
             try_files $uri $uri/ /index.html;
         }
        location   /customerService {
             alias   /web-vue/customerService/;
             index  index.html index.htm index;
             try_files $uri $uri/ /index.html;
         }
    }'),
    (10,'www.website2.com',3,'    server {
        listen       80 ;
        server_name  ${domain};
        location   / {
             alias   /web-vue/website/;
             index  index.html index.htm index;
             try_files $uri $uri/ /index.html;
        }
        location /api {
                proxy_set_header Host $http_host;
                proxy_set_header X-Real-IP $remote_addr;
                  proxy_set_header REMOTE-HOST $remote_addr;
                  proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                 proxy_pass http://127.0.0.1:9201;
        }
        location /common {
                proxy_set_header Host $http_host;
                proxy_set_header X-Real-IP $remote_addr;
                  proxy_set_header REMOTE-HOST $remote_addr;
                  proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                 proxy_pass http://127.0.0.1:9201;
        }
        location /profile {
                proxy_set_header Host $http_host;
                proxy_set_header X-Real-IP $remote_addr;
                proxy_set_header REMOTE-HOST $remote_addr;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_pass http://127.0.0.1:9201;
        }
    }');
update `stock`.`nginx_config` set server_name = concat('kmadmin.',@admin1) where id = 1;
update `stock`.`nginx_config` set server_name = concat('kmadmin.',@admin2) where id = 2;
update `stock`.`nginx_config` set server_name = concat('www.',@admin1) where id = 3;
update `stock`.`nginx_config` set server_name = concat('www.',@admin2) where id = 4;
update `stock`.`nginx_config` set server_name = concat('www.',@h5) where id = 5;
update `stock`.`nginx_config` set server_name = concat('www.',@describe) where id = 6;
update `stock`.`nginx_config` set server_name = concat('www.',@website) where id = 7;
update `stock`.`nginx_config` set server_name = concat('www.',@line3) where id = 8;
update `stock`.`nginx_config` set server_name = concat('www.',@h52) where id = 9;
update `stock`.`nginx_config` set server_name = concat('www.',@website2) where id = 10;

