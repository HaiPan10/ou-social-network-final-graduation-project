# ou_social_network_graduation_project

Hệ thống mạng xã hội cựu sinh viên trường đại học Mở thành phố Hồ Chí Minh với mục tiêu gắn kết các sinh viên sau khi ra trường với nhà trường và tạo điều kiện thuận lợi cho nhà trường khi cần giao tiếp và liên lạc với cựu sinh viên.

## Hướng dẫn chạy local phía server
### Môi trường cần được chuẩn bị sẵn
- JAVA Development Kit 17.
- Biến môi trường JAVA_HOME.
- Docker cho Desktop.
- Tạo sẵn các database: ou-social-network-accountdb, ou-social-network-commentdb, ou-social-network-postdb.
### Thiết lập kết nối với MySQL
- Mở file config.json
- Thêm 1 thông tin user theo mẫu
```
"<Tên tùy ý>": {
        "username": "root",
        "password": "<mật khẩu mysql trên máy>"
}
```
- Gõ .\Set-Password.ps1 sau đó nhập tên mới được thêm vào.
### Khởi chạy các service
- Có 8 services nên cần mở 8 terminal tương ứng với từng services
- Lần lượt thực hiện các commands sau:
```
cd social_network/account_service
mvn spring-boot:run
```
```
cd social_network/admin_service
mvn spring-boot:run
```
```
cd social_network/api-gateway
mvn spring-boot:run
```
```
cd social_network/comment_service
mvn spring-boot:run
```
```
cd social_network/eureka-server
mvn spring-boot:run
```
```
cd social_network/mail_service
mvn spring-boot:run
```
```
cd social_network/post_service
mvn spring-boot:run
```
```
cd social_network/realtime_service
mvn spring-boot:run
```
### Khởi chạy kafka và redis
Mở 1 terminal khác, gõ docker compose up -d

> [!IMPORTANT]  
> Sau khi xong các bước trên, ở các service sẽ không còn hiện lỗi hay exception nào

### Back up dữ liệu
Bước này cần thiết để tạo các trường dữ liệu cần thiết, đồng thời phục hồi dữ liệu của người dùng. Mở MySQL, lần lượt chạy các file sql sau:
- social_network\account_service\backup.sql
- social_network\comment_service\backup.sql
- social_network\post_service\backup.sql

## Hướng dẫn chạy local phía client
### Môi trường cần được chuẩn bị sẵn
- Môi trường ReactJS

### Cài đặt các dependencies của project
Chạy câu lệnh
```
npm install --legacy-peer-deps
```

### Khởi chạy client
Tạo 1 terminal mới thực thi lệnh sau:
```
cd client
npm start
```
