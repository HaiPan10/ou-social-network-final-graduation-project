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
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `post_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `post_id` (`post_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE,
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,'Kiếm tiền mua lap mới hả','2023-08-21 13:33:50','2023-08-21 13:33:50',2,2),(2,'Kiếm tiền mua laptop mới chớ gì nx','2023-08-21 13:34:59','2023-08-21 13:34:59',2,5),(3,'Wao','2023-08-21 13:42:06','2023-08-21 13:42:06',2,6),(4,'Đau khổ chưa','2023-08-21 13:48:26','2023-08-21 13:48:26',1,5),(5,'dmm xóa hình kia đi đmmm','2023-08-21 13:52:30','2023-08-21 13:52:30',6,2),(6,'Đéo :)','2023-08-21 13:53:15','2023-08-21 13:53:15',6,5),(7,'đcm','2023-08-21 14:00:56','2023-08-21 14:00:56',8,2),(8,'do lỡ dại thôi','2023-08-21 14:01:02','2023-08-21 14:01:02',8,2),(9,':))))))))','2023-08-21 14:01:54','2023-08-21 14:01:54',8,5),(10,'out nhây','2023-08-21 14:03:55','2023-08-21 14:03:55',9,2),(11,'ok :)','2023-08-21 14:04:43','2023-08-21 14:04:43',9,5),(12,'Hẹn đồ án khóa luận =)))','2023-08-21 14:05:14','2023-08-21 14:05:14',10,2),(15,'đẹp nhờ','2023-08-21 14:08:28','2023-08-21 14:08:28',6,9),(16,'trời ơi đừng comment nó bump lên','2023-08-21 14:09:13','2023-08-21 14:09:13',6,2),(17,'biết z tắt cái vụ comment nó bump lên','2023-08-21 14:09:29','2023-08-21 14:09:29',6,2),(18,'haha ','2023-08-21 14:09:38','2023-08-21 14:09:38',6,5),(20,'dô bằng laptop điii t quên nhét cái nút cho điện thoại :)))','2023-08-21 14:10:03','2023-08-21 14:10:03',12,2),(22,'hong có do t xóa acc cũ m đó ','2023-08-21 14:10:55','2023-08-21 14:10:55',14,2),(23,'Lên đầu nàoooo','2023-08-21 14:12:19','2023-08-21 14:12:19',6,5),(25,'để cho đẹp thiệt, thông báo hẹn đồ án khóa luận ','2023-08-21 14:14:25','2023-08-21 14:14:25',16,2),(26,'hê hê','2023-08-21 14:14:29','2023-08-21 14:14:29',16,2),(27,'Lên đầu nèooooo','2023-08-21 14:15:16','2023-08-21 14:15:16',6,5),(28,'Tha t','2023-08-21 14:15:26','2023-08-21 14:15:26',6,2),(29,'Con mèo cute thé','2023-08-21 14:15:54','2023-08-21 14:15:54',18,2),(30,'ờ hén bên mobile t hỏng có cho nút dìa newfeed =))','2023-08-21 14:18:34','2023-08-21 14:18:34',19,2),(31,'m mở laptop lên nèo','2023-08-21 14:18:40','2023-08-21 14:18:40',19,2),(32,'t quên thoi nào hẹn đồ án làm hẳn cái app cho sài','2023-08-21 14:20:51','2023-08-21 14:20:51',20,2),(33,'Ngon :))))','2023-08-21 14:22:57','2023-08-21 14:22:57',20,8),(34,'Bắt về nuôi đi','2023-08-21 14:23:31','2023-08-21 14:23:31',18,4),(35,'Bắt người đăng bài','2023-08-21 14:23:50','2023-08-21 14:23:50',18,2),(36,'Một vài bài đăng bị mất hình ảnh do @Phan Thanh Hải test chức năng xóa bài đăng không phải tôi','2023-09-07 13:49:20','2023-09-07 13:49:20',21,2),(37,'Bài đăng bị mất hình ảnh có thể up lại hình ảnh ở dấu ba chấm góc phải phía trên và vào chỉnh sửa','2023-09-07 13:49:55','2023-09-07 13:49:55',21,2),(38,'Dùng điện thoại thì không nhắn tin được vì responsive mệt @Thanh Hy','2023-09-07 13:50:32','2023-09-07 13:50:32',21,2),(39,'Chắc là người ta tin là t xóa?','2023-09-07 13:54:49','2023-09-07 13:54:49',21,5),(40,'Ok bạn','2023-09-07 14:11:03','2023-09-07 14:11:03',23,5),(41,'bump','2023-09-07 14:17:15','2023-09-07 14:17:15',23,2),(43,'à acc mới thì khỏi đổi pass ai mới tạo acc thì khỏi :v','2023-09-07 14:20:09','2023-09-07 14:20:09',21,2),(44,'Tan nát :)))','2023-09-07 14:31:54','2023-09-07 14:31:54',22,20),(45,'Dùng laptop đi ;-; sao đt bug z te','2023-09-07 14:36:51','2023-09-07 14:36:51',27,2),(46,'Ê có jz chứ t không thấy gì hết :))','2023-09-07 14:37:17','2023-09-07 14:37:17',9,20),(47,'Hải nó xóa bài đăng của t','2023-09-07 14:39:28','2023-09-07 14:39:28',9,2),(48,'=))) clm','2023-09-07 14:42:08','2023-09-07 14:42:08',28,2),(49,'bumppp','2023-09-07 14:42:50','2023-09-07 14:42:50',21,2),(50,'bumpp','2023-09-07 14:42:56','2023-09-07 14:42:56',23,2),(51,'@Phan Thanh Hải ghi tên Nam vô danh sách nhận kem ngày 11/9','2023-09-07 14:46:56','2023-09-07 14:46:56',29,2),(52,'test chat đe','2023-09-07 14:47:33','2023-09-07 14:47:33',30,2),(53,'Chưa mài, hẹn đồ án khóa luận','2023-09-07 14:50:46','2023-09-07 14:50:46',31,2),(54,'Bắt chước facebook mài facebook dô đt bắt vào messenger','2023-09-07 15:00:18','2023-09-07 15:00:18',33,2),(55,'thoi nàoo','2023-09-07 15:01:10','2023-09-07 15:01:10',34,2),(56,'Làm kìa m @Lại Bình Phong','2023-09-07 15:01:40','2023-09-07 15:01:40',34,5),(57,'Ôi trường tâm lý quá','2023-09-07 15:10:53','2023-09-07 15:10:53',36,2),(58,'không phải t đăng','2023-09-07 15:11:20','2023-09-07 15:11:20',36,2),(59,':)','2023-09-07 15:12:34','2023-09-07 15:12:34',36,5),(60,':0','2023-09-07 15:12:35','2023-09-07 15:12:35',36,5),(61,'Submit xog bài đăng sẽ biến mất hẹn đồ án khóa luận hiện những gì bạn đã chọn','2023-09-07 15:16:13','2023-09-07 15:16:13',36,2),(62,'avatar wibu tui set cho đâu','2023-09-07 15:52:07','2023-09-07 15:52:07',37,2),(63,'hông','2023-09-07 15:55:18','2023-09-07 15:55:18',35,29),(64,'Ơ kè','2023-09-07 15:57:52','2023-09-07 15:57:52',35,2),(65,'bạn hãy ôn bài thật kĩ vào là đc :v','2023-09-07 16:21:30','2023-09-07 16:21:30',37,30);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-09-10 17:40:06
