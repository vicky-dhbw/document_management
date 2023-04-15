FROM node:latest AS build

WORKDIR /app/frontend

COPY . .
RUN rm -rf node_modules && npm install && npm install -g @angular/cli@latest && ng build

FROM nginx:latest

WORKDIR /usr/share/nginx/html

RUN rm -rf ./*

COPY --from=build /app/frontend/dist/document-management-app .

ENTRYPOINT [ "nginx" , "-g", "daemon off;"]


