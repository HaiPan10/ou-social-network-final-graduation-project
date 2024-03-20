-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 34.101.213.59    Database: ou-social-network
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
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `post_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `level` int DEFAULT NULL,
  `parent_comment_id` bigint DEFAULT NULL,
  `replied_user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `post_id` (`post_id`),
  KEY `user_id` (`user_id`),
  KEY `FKhvh0e2ybgg16bpu229a5teje7` (`parent_comment_id`),
  KEY `FKnuraq8ssi7sdhw5p5r2hj4elu` (`replied_user_id`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE,
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKhvh0e2ybgg16bpu229a5teje7` FOREIGN KEY (`parent_comment_id`) REFERENCES `comment` (`id`),
  CONSTRAINT `FKnuraq8ssi7sdhw5p5r2hj4elu` FOREIGN KEY (`replied_user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,'Kiếm tiền mua lap mới hả','2023-08-21 13:33:50.000000','2023-08-21 13:33:50.000000',2,2,1,NULL,NULL),(2,'Kiếm tiền mua laptop mới chớ gì nx','2023-08-21 13:34:59.000000','2023-08-21 13:34:59.000000',2,5,1,NULL,NULL),(3,'Wao','2023-08-21 13:42:06.000000','2023-08-21 13:42:06.000000',2,6,1,NULL,NULL),(4,'Đau khổ chưa','2023-08-21 13:48:26.000000','2023-08-21 13:48:26.000000',1,5,1,NULL,NULL),(5,'dmm xóa hình kia đi đmmm','2023-08-21 13:52:30.000000','2023-08-21 13:52:30.000000',6,2,1,NULL,NULL),(6,'Đéo :)','2023-08-21 13:53:15.000000','2023-08-21 13:53:15.000000',6,5,1,NULL,NULL),(7,'đcm','2023-08-21 14:00:56.000000','2023-08-21 14:00:56.000000',8,2,1,NULL,NULL),(8,'do lỡ dại thôi','2023-08-21 14:01:02.000000','2023-08-21 14:01:02.000000',8,2,1,NULL,NULL),(9,':))))))))','2023-08-21 14:01:54.000000','2023-08-21 14:01:54.000000',8,5,1,NULL,NULL),(10,'out nhây','2023-08-21 14:03:55.000000','2023-08-21 14:03:55.000000',9,2,1,NULL,NULL),(11,'ok :)','2023-08-21 14:04:43.000000','2023-08-21 14:04:43.000000',9,5,1,NULL,NULL),(12,'Hẹn đồ án khóa luận =)))','2023-08-21 14:05:14.000000','2023-08-21 14:05:14.000000',10,2,1,NULL,NULL),(15,'đẹp nhờ','2023-08-21 14:08:28.000000','2023-08-21 14:08:28.000000',6,9,1,NULL,NULL),(16,'trời ơi đừng comment nó bump lên','2023-08-21 14:09:13.000000','2023-08-21 14:09:13.000000',6,2,1,NULL,NULL),(17,'biết z tắt cái vụ comment nó bump lên','2023-08-21 14:09:29.000000','2023-08-21 14:09:29.000000',6,2,1,NULL,NULL),(18,'haha ','2023-08-21 14:09:38.000000','2023-08-21 14:09:38.000000',6,5,1,NULL,NULL),(20,'dô bằng laptop điii t quên nhét cái nút cho điện thoại :)))','2023-08-21 14:10:03.000000','2023-08-21 14:10:03.000000',12,2,1,NULL,NULL),(22,'hong có do t xóa acc cũ m đó ','2023-08-21 14:10:55.000000','2023-08-21 14:10:55.000000',14,2,1,NULL,NULL),(23,'Lên đầu nàoooo','2023-08-21 14:12:19.000000','2023-08-21 14:12:19.000000',6,5,1,NULL,NULL),(25,'để cho đẹp thiệt, thông báo hẹn đồ án khóa luận ','2023-08-21 14:14:25.000000','2023-08-21 14:14:25.000000',16,2,1,NULL,NULL),(26,'hê hê','2023-08-21 14:14:29.000000','2023-08-21 14:14:29.000000',16,2,1,NULL,NULL),(27,'Lên đầu nèooooo','2023-08-21 14:15:16.000000','2023-08-21 14:15:16.000000',6,5,1,NULL,NULL),(28,'Tha t','2023-08-21 14:15:26.000000','2023-08-21 14:15:26.000000',6,2,1,NULL,NULL),(29,'Con mèo cute thé','2023-08-21 14:15:54.000000','2023-08-21 14:15:54.000000',18,2,1,NULL,NULL),(30,'ờ hén bên mobile t hỏng có cho nút dìa newfeed =))','2023-08-21 14:18:34.000000','2023-08-21 14:18:34.000000',19,2,1,NULL,NULL),(31,'m mở laptop lên nèo','2023-08-21 14:18:40.000000','2023-08-21 14:18:40.000000',19,2,1,NULL,NULL),(32,'t quên thoi nào hẹn đồ án làm hẳn cái app cho sài','2023-08-21 14:20:51.000000','2023-08-21 14:20:51.000000',20,2,1,NULL,NULL),(33,'Ngon :))))','2023-08-21 14:22:57.000000','2023-08-21 14:22:57.000000',20,8,1,NULL,NULL),(34,'Bắt về nuôi đi','2023-08-21 14:23:31.000000','2023-08-21 14:23:31.000000',18,4,1,NULL,NULL),(35,'Bắt người đăng bài','2023-08-21 14:23:50.000000','2023-08-21 14:23:50.000000',18,2,1,NULL,NULL),(36,'Một vài bài đăng bị mất hình ảnh do @Phan Thanh Hải test chức năng xóa bài đăng không phải tôi','2023-09-07 13:49:20.000000','2023-09-07 13:49:20.000000',21,2,1,NULL,NULL),(37,'Bài đăng bị mất hình ảnh có thể up lại hình ảnh ở dấu ba chấm góc phải phía trên và vào chỉnh sửa','2023-09-07 13:49:55.000000','2023-09-07 13:49:55.000000',21,2,1,NULL,NULL),(38,'Dùng điện thoại thì không nhắn tin được vì responsive mệt @Thanh Hy','2023-09-07 13:50:32.000000','2023-09-07 13:50:32.000000',21,2,1,NULL,NULL),(39,'Chắc là người ta tin là t xóa?','2023-09-07 13:54:49.000000','2023-09-07 13:54:49.000000',21,5,1,NULL,NULL),(40,'Ok bạn','2023-09-07 14:11:03.000000','2023-09-07 14:11:03.000000',23,5,1,NULL,NULL),(41,'bump','2023-09-07 14:17:15.000000','2023-09-07 14:17:15.000000',23,2,1,NULL,NULL),(43,'à acc mới thì khỏi đổi pass ai mới tạo acc thì khỏi :v','2023-09-07 14:20:09.000000','2023-09-07 14:20:09.000000',21,2,1,NULL,NULL),(44,'Tan nát :)))','2023-09-07 14:31:54.000000','2023-09-07 14:31:54.000000',22,20,1,NULL,NULL),(45,'Dùng laptop đi ;-; sao đt bug z te','2023-09-07 14:36:51.000000','2023-09-07 14:36:51.000000',27,2,1,NULL,NULL),(46,'Ê có jz chứ t không thấy gì hết :))','2023-09-07 14:37:17.000000','2023-09-07 14:37:17.000000',9,20,1,NULL,NULL),(47,'Hải nó xóa bài đăng của t','2023-09-07 14:39:28.000000','2023-09-07 14:39:28.000000',9,2,1,NULL,NULL),(48,'=))) clm','2023-09-07 14:42:08.000000','2023-09-07 14:42:08.000000',28,2,1,NULL,NULL),(49,'bumppp','2023-09-07 14:42:50.000000','2023-09-07 14:42:50.000000',21,2,1,NULL,NULL),(50,'bumpp','2023-09-07 14:42:56.000000','2023-09-07 14:42:56.000000',23,2,1,NULL,NULL),(51,'@Phan Thanh Hải ghi tên Nam vô danh sách nhận kem ngày 11/9','2023-09-07 14:46:56.000000','2023-09-07 14:46:56.000000',29,2,1,NULL,NULL),(52,'test chat đe','2023-09-07 14:47:33.000000','2023-09-07 14:47:33.000000',30,2,1,NULL,NULL),(53,'Chưa mài, hẹn đồ án khóa luận','2023-09-07 14:50:46.000000','2023-09-07 14:50:46.000000',31,2,1,NULL,NULL),(54,'Bắt chước facebook mài facebook dô đt bắt vào messenger','2023-09-07 15:00:18.000000','2023-09-07 15:00:18.000000',33,2,1,NULL,NULL),(55,'thoi nàoo','2023-09-07 15:01:10.000000','2023-09-07 15:01:10.000000',34,2,1,NULL,NULL),(56,'Làm kìa m @Lại Bình Phong','2023-09-07 15:01:40.000000','2023-09-07 15:01:40.000000',34,5,1,NULL,NULL),(57,'Ôi trường tâm lý quá','2023-09-07 15:10:53.000000','2023-09-07 15:10:53.000000',36,2,1,NULL,NULL),(58,'không phải t đăng','2023-09-07 15:11:20.000000','2023-09-07 15:11:20.000000',36,2,1,NULL,NULL),(59,':)','2023-09-07 15:12:34.000000','2023-09-07 15:12:34.000000',36,5,1,NULL,NULL),(60,':0','2023-09-07 15:12:35.000000','2023-09-07 15:12:35.000000',36,5,1,NULL,NULL),(61,'Submit xog bài đăng sẽ biến mất hẹn đồ án khóa luận hiện những gì bạn đã chọn','2023-09-07 15:16:13.000000','2023-09-07 15:16:13.000000',36,2,1,NULL,NULL),(62,'avatar wibu tui set cho đâu','2023-09-07 15:52:07.000000','2023-09-07 15:52:07.000000',37,2,1,NULL,NULL),(63,'hông','2023-09-07 15:55:18.000000','2023-09-07 15:55:18.000000',35,29,1,NULL,NULL),(64,'Ơ kè','2023-09-07 15:57:52.000000','2023-09-07 15:57:52.000000',35,2,1,NULL,NULL),(65,'bạn hãy ôn bài thật kĩ vào là đc :v','2023-09-07 16:21:30.000000','2023-09-07 16:21:30.000000',37,30,1,NULL,NULL),(66,'Sửa không được bạn ey','2023-10-12 06:44:06.067000','2023-10-12 06:44:06.067000',38,2,1,NULL,NULL),(67,'đúng đúng','2023-10-12 06:44:17.967000','2023-10-12 06:44:17.967000',38,5,2,66,2),(69,'mé haha =))','2023-10-12 06:46:54.690000','2023-10-12 06:46:54.690000',39,5,1,NULL,NULL),(70,'Đau khổ vãi','2023-10-12 06:47:22.374000','2023-10-12 06:47:22.374000',39,2,2,69,5),(71,'bug Phong ơi','2023-10-12 06:47:33.580000','2023-10-12 06:47:33.581000',39,5,2,69,2),(72,'bug j é','2023-10-12 06:47:38.174000','2023-10-12 06:47:38.174000',39,2,2,69,5),(73,'Con bug do m code thiếu đó','2023-10-12 06:49:47.592000','2023-10-12 06:49:47.592000',39,5,2,69,2),(74,'\"Thả sad\"','2023-10-12 06:50:43.473000','2023-10-12 06:50:43.473000',39,2,2,69,5),(75,'hé lu','2023-10-13 05:25:00.304000','2023-10-13 05:25:00.304000',40,37,1,NULL,NULL),(76,'hey','2023-10-13 05:27:35.886000','2023-10-13 05:27:35.886000',40,2,2,75,37),(77,'ayzo','2023-10-13 05:27:55.086000','2023-10-13 05:27:55.086000',40,2,2,75,37),(78,'bump bump bump','2023-10-13 05:35:13.797000','2023-10-13 05:35:13.797000',40,37,2,75,2),(79,'thấy k','2023-10-13 05:35:51.328000','2023-10-13 05:35:51.328000',40,2,2,75,37),(80,'Dữ chèn','2023-10-13 05:43:55.261000','2023-10-13 05:43:55.261000',40,36,1,NULL,NULL),(81,'kaka','2023-10-13 05:44:02.384000','2023-10-13 05:44:02.384000',40,2,2,80,36),(82,'thấy t rep ko','2023-10-13 05:44:04.331000','2023-10-13 05:44:04.331000',40,2,2,80,36),(83,'Có thấy rep mà ko có thông báo','2023-10-13 05:44:48.345000','2023-10-13 05:44:48.345000',40,36,2,80,2),(84,'m dùng điện thoại làm gì có cái chuông','2023-10-13 05:45:07.489000','2023-10-13 05:45:07.489000',40,2,2,80,36),(91,'thấy r nè','2023-10-13 06:14:43.951000','2023-10-13 06:14:43.951000',40,37,2,75,2),(96,'Tự bình luận','2023-10-16 01:31:00.384000','2023-10-16 01:31:00.384000',45,39,1,NULL,NULL),(97,'Bình luận','2023-10-16 01:31:04.931000','2023-10-16 01:31:04.931000',45,2,1,NULL,NULL),(98,'PHản hồi','2023-10-16 01:31:43.342000','2023-10-16 01:31:43.342000',45,2,2,97,2),(99,'reply level 2','2023-10-16 01:32:07.715000','2023-10-16 01:32:07.715000',45,39,2,97,2),(100,'=)) ủa alo haha','2023-10-21 10:32:00.026000','2023-10-21 10:32:00.026000',46,5,1,NULL,NULL),(102,'Chuyển nhà qua AWS đê','2023-10-27 14:51:21.695000','2023-10-27 14:51:21.695000',47,5,1,NULL,NULL);
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

-- Dump completed on 2023-11-10 22:32:19