FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
# Use Docker-specific environment config (backend reachable via service name)
RUN cp src/environments/environment.docker.ts src/environments/environment.ts
# Build production bundle
RUN npm run build -- --configuration production

FROM nginx:1.27-alpine
WORKDIR /usr/share/nginx/html
RUN rm -rf ./*
COPY --from=build /app/dist/admin-dashboard/browser ./
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
# Default nginx image serves from /usr/share/nginx/html
