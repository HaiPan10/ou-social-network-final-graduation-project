# ou_social_network_graduation_project

Hệ thống mạng xã hội cựu sinh viên trường đại học Mở thành phố Hồ Chí Minh với mục tiêu gắn kết các sinh viên sau khi ra trường với nhà trường và tạo điều kiện thuận lợi cho nhà trường khi cần giao tiếp và liên lạc với cựu sinh viên.

## Hướng dẫn chạy local
### Chuẩn bị môi trường
- Cài đặt JAVA Development Kit 17 từ trang: https://www.oracle.com/java/technologies/downloads/#java17 <br>
- Cài đặt biến JAVA_HOME bằng cách vào tìm kiếm trên Windows gõ Edit Environment Variables for your account
- Trong mục User variables chọn New 
- Nhập JAVA_HOME trong variable name và đường dẫn tới JDK trong variable values

## Hướng dẫn triển khai

### Một số lưu ý khi triển khai 
#### Phía server
Trong application.yml, comment dòng code
```
driver-class-name: com.mysql.cj.jdbc.Driver
url: jdbc:mysql://localhost:3306/ou-social-network
username: root
# password: mai2604
password: IamPhong89
```
Sau đó uncomment dòng code
```
# url: jdbc:mysql://34.118.232.140:3306/${DB_NAME}
# username: ${DB_USERNAME}
# password: ${DB_PASSWORD}
```
Trong configs.properties, comment dòng code
```
SERVER_HOSTNAME=http://localhost:8080
CLIENT_HOSTNAME=http://localhost:3000
```
Sau đó uncomment dòng code
```
SERVER_HOSTNAME=http://34.101.48.117:80
CLIENT_HOSTNAME=http://ousocialnetwork.id.vn
```

#### Phía client
Trong package.json, sửa đoạn code
```
"scripts": {
    "start": "react-scripts start",
    "build": "react-scripts build",
    "test": "react-scripts test",
    "eject": "react-scripts eject"
},
```
thành
```
"scripts": {
    "start": "export PORT=80 && react-scripts start",
    "build": "react-scripts build",
    "test": "react-scripts test",
    "eject": "react-scripts eject"
},
```
Trong configs/Api.jsx, comment toàn bộ đoạn code
```
baseURL: 'http://127.0.0.1:8080/api'
export const socketUrl = 'http://127.0.0.1:8080/api/ws'
```
Và uncomment toàn bộ đoạn code
```
baseURL: "http://34.101.48.117:80/api"
baseURL:"'http://34.101.48.117:80/api/ws"
```

### Một số câu lệnh build Docker trước khi triển khai
#### Khi build server
```
docker build -t ou-social-network:latest . 
docker tag ou-social-network:latest phonglai0809/ou-social-network:latest 
docker push phonglai0809/ou-social-network:latest
```
#### Khi build client
```
docker build -t react-app:latest . 
docker tag react-app:latest phonglai0809/react-app:latest 
docker push phonglai0809/react-app:latest
```

#### Note mới
mvn clean compile jib:build
update timestamp in userDoc