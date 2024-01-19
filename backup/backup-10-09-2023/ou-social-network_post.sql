-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 34.128.124.93    Database: ou-social-network
-- ------------------------------------------------------
-- Server version	8.1.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `is_active_comment` tinyint(1) DEFAULT '1',
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `post_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (1,'Hello xin chào mọi người, mạng xã hội cựu sinh viên v2 đã có thêm newfeed, reactions, đổi mật khẩu. Enjoy testing','2023-08-21 11:17:03','2023-08-21 11:17:20',1,2),(2,'Mình nhận cài win 11, Ubuntu dạo nhé\r\nGiá 100k 1 lần uy tín đảm bảo thành công','2023-08-21 13:06:01','2023-08-21 13:06:01',1,5),(4,'Khởi nghiệp','2023-08-21 13:46:33','2023-08-21 13:46:33',1,2),(5,'Góc bán sách kiếm tiền cho Hải mua laptop ','2023-08-21 13:51:55','2023-08-21 13:51:55',1,2),(6,'Lời thú tội của bạn Phong','2023-08-21 13:52:01','2023-09-07 14:07:54',1,5),(7,'Flex intern cực mạnh','2023-08-21 13:53:07','2023-08-21 13:53:07',1,2),(8,'Đi intern chưa chắc đã có đc cái này :)','2023-08-21 13:55:29','2023-09-07 14:10:43',1,5),(9,'Có huy chương chưa chắc có cái này','2023-08-21 14:03:36','2023-09-07 14:40:19',1,2),(10,'Chưa cho đăng video nữa ?','2023-08-21 14:04:48','2023-08-21 14:04:48',1,8),(12,'Ko cho đổi mật khẩu luôn ','2023-08-21 14:09:26','2023-08-21 14:09:26',1,8),(14,'Mỗi lần quên mật khẩu bắt tạo acc mới ?','2023-08-21 14:10:27','2023-08-21 14:10:27',1,8),(15,'Pùn vãi ò ??','2023-08-21 14:11:30','2023-08-21 14:11:30',1,8),(16,'Sao để ảnh cái chuông mà m bình luận ko hiện thông báo zẫy ? \r\nTrang trí thoi hả ?\r\n@PhongLại ','2023-08-21 14:13:33','2023-08-21 14:13:33',1,8),(18,'Đột nhập OU','2023-08-21 14:15:33','2023-08-21 14:15:33',1,4),(19,'Ít ra ấn vô logo OU cũng phải cho t về trang newsfeed chớ :v','2023-08-21 14:18:05','2023-08-21 14:18:05',1,8),(20,'Web lậu này kỳ thị ng dùng điện thoại nha bà con ?\r\nFeedback 5 câu 3 câu nó rep bắt mở lap r ?','2023-08-21 14:20:22','2023-08-21 14:20:22',1,8),(21,'Mạng xã hội cựu sinh viên v3 đã có thêm các tính năng mới:\r\n- Nhắn tin\r\n- Bài đăng thư mời sự kiện\r\n- Bài đăng khảo sát\r\nLưu ý: để sử dụng chức năng nhắn tin, các tài khoản testers ở version trước BẮT BUỘC phải đổi mật khẩu (có thể nhập lại mật khẩu cũ)','2023-09-07 13:48:48','2023-09-07 13:48:48',1,1),(22,'Bài đăng này khảo sát tình hình môn java hiện tại của các bạn','2023-09-07 13:55:00','2023-09-07 13:55:00',1,1),(23,'Mọi người nhớ logout trước khi đổi mật khẩu vì trên trình duyệt còn lưu dữ liệu cũ từ bản test v2 trước để tránh lỗi !!!','2023-09-07 14:08:27','2023-09-07 14:08:27',1,2),(24,'Sự kiện này bạn @Phan Thanh Hải muốn tri ân mọi người vì đã đồng hành test app cho chúng mình, nhà đầu tư @Phan Thanh Hải sẽ đãi mỗi người 1 cây kem 130k ở Vòng tròn đỏ ngày thi mobile 11/9','2023-09-07 14:11:20','2023-09-07 14:11:20',0,1),(25,'THE OPEN DAYS đang cần ca sĩ và diễn viên hài, mời các em sinh viên về hỗ trợ trường','2023-09-07 14:12:27','2023-09-07 14:12:27',0,1),(26,'Hello mọi người, mình là K23 ngành Khoa học máy tính. Hy vọng được học bổng 4.0 như anh Phong và anh Hải ạ!','2023-09-07 14:33:29','2023-09-07 14:33:29',1,22),(27,'Đổi ảnh đại diện không được nha :)))','2023-09-07 14:35:30','2023-09-07 14:35:30',1,20),(28,'Trên đây có a nào 6 múi để em lấy ck hong chứ em mệt mõi quá :))','2023-09-07 14:36:18','2023-09-07 14:36:18',1,20),(29,'Tiền test của Phong và Hải là 300k','2023-09-07 14:45:41','2023-09-07 14:45:41',1,20),(30,'Hé lu mấy ní ','2023-09-07 14:46:57','2023-09-07 14:46:57',1,8),(31,'Ủa cho đăng vid chưa ','2023-09-07 14:50:20','2023-09-07 14:50:20',1,8),(33,'Đây là kỳ thị mn ạ ?\r\nMobile - user cũng là con người mờ ?','2023-09-07 14:56:27','2023-09-07 14:56:27',1,8),(34,'???','2023-09-07 15:00:05','2023-09-07 15:00:05',1,8),(35,'Mọi người nhớ làm bài đăng khảo sát trong Trang quản trị viên để test thống kê nhe','2023-09-07 15:05:32','2023-09-07 15:05:32',1,2),(36,'Bài đăng này nhằm khảo sát các bạn cựu sinh viên bị wibu','2023-09-07 15:10:08','2023-09-07 15:10:08',1,1),(37,'Làm sao để 8 điểm thi học kỳ Java =(((\r\n','2023-09-07 15:49:49','2023-09-07 15:49:49',1,7),(38,'Cho sửa cái tên với ','2023-09-09 11:20:28','2023-09-09 11:20:28',1,35);
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-09-10 17:40:25
