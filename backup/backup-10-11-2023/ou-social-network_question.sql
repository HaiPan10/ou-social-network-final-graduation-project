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
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `survey_id` bigint NOT NULL,
  `question_type_id` bigint NOT NULL,
  `question_text` varchar(2000) DEFAULT NULL,
  `is_mandatory` bit(1) DEFAULT NULL,
  `question_order` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `survey_id` (`survey_id`),
  KEY `question_type_id` (`question_type_id`),
  CONSTRAINT `question_ibfk_1` FOREIGN KEY (`survey_id`) REFERENCES `post_survey` (`id`) ON DELETE CASCADE,
  CONSTRAINT `question_ibfk_2` FOREIGN KEY (`question_type_id`) REFERENCES `question_type` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (1,22,1,'Bạn đã dùng được security và jwt ?',_binary '',1),(2,22,1,'Bạn đã validation được chưa ?',_binary '',2),(3,22,1,'ReactJs của bạn tới đâu rồi ?',_binary '',3),(4,22,1,'Bạn đã hoàn thành cả bài được bao nhiêu %',_binary '',4),(5,36,2,'Bạn có bị wibu không ?',_binary '',1),(6,36,2,'Bạn wibu lâu chưa ?',_binary '\0',2),(7,36,3,'Điều gì đã làm bạn wibu sau khi ra trường ?',_binary '\0',3),(8,36,1,'Bạn có dự định cai wibu ?',_binary '\0',4),(9,36,1,'Theo bạn, wibu có đáng bị kì thị',_binary '\0',5),(10,42,1,'Thời gian học của Anh/Chị đến khi tốt nghiệp tại trường?',_binary '\0',1),(11,42,1,'Thời gian tìm được việc làm của Anh/Chị (tính từ thời điểm tốt nghiệp)?',_binary '\0',2),(12,42,2,' Đơn vị công tác (hiện tại) của Anh/Chị',_binary '\0',3),(13,42,2,'Địa chỉ đơn vị công tác (hiện tại) của Anh/Chị',_binary '\0',4),(14,42,2,' Vị trí/chức vụ công việc hiện tại của Anh/Chị',_binary '\0',5),(15,42,1,'Công việc hiện tại của Anh/Chị có đúng ngành đào tạo không?',_binary '\0',6),(16,42,1,'Thu nhập bình quân hàng tháng công việc hiện tại của Anh/Chị? ',_binary '\0',7),(17,42,1,'Anh/Chị hài lòng với công việc/điều kiện làm việc hiện tại?',_binary '\0',8),(18,42,1,' Theo Anh/Chị, những kiến thức và kỹ năng cần thiết cho công việc theo ngành tốt nghiệp?',_binary '\0',9);
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-10 22:31:50
