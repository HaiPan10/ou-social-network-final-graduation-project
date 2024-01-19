FROM node:alpine AS development
WORKDIR /react-app
COPY ./package.json /react-app
RUN npm install --force
COPY . .
CMD npm start