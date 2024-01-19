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
-- Table structure for table `answer`
--

DROP TABLE IF EXISTS `answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answer` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `question_id` bigint NOT NULL,
  `response_id` bigint NOT NULL,
  `value` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `question_id` (`question_id`),
  KEY `response_id` (`response_id`),
  CONSTRAINT `answer_ibfk_1` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE,
  CONSTRAINT `answer_ibfk_2` FOREIGN KEY (`response_id`) REFERENCES `response` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer`
--

LOCK TABLES `answer` WRITE;
/*!40000 ALTER TABLE `answer` DISABLE KEYS */;
INSERT INTO `answer` VALUES (1,1,1,NULL),(2,2,1,NULL),(3,3,1,NULL),(4,4,1,NULL),(5,1,2,NULL),(6,2,2,NULL),(7,3,2,NULL),(8,4,2,NULL),(9,1,3,NULL),(10,2,3,NULL),(11,3,3,NULL),(12,4,3,NULL),(13,1,4,NULL),(14,2,4,NULL),(15,3,4,NULL),(16,4,4,NULL),(17,1,5,NULL),(18,2,5,NULL),(19,3,5,NULL),(20,4,5,NULL),(21,1,6,NULL),(22,2,6,NULL),(23,3,6,NULL),(24,4,6,NULL),(25,5,7,'Có nha ?'),(26,6,7,'Mãi là niu bi :3'),(27,7,7,NULL),(28,8,7,NULL),(29,9,7,NULL),(30,1,8,NULL),(31,2,8,NULL),(32,3,8,NULL),(33,4,8,NULL),(34,5,9,'Có'),(35,6,9,'Long time ago'),(36,7,9,NULL),(37,8,9,NULL),(38,9,9,NULL),(39,5,10,''),(40,6,10,''),(41,7,10,NULL),(42,8,10,NULL),(43,9,10,NULL),(44,1,10,NULL),(45,2,10,NULL),(46,3,10,NULL),(47,4,10,NULL),(48,5,11,'chắc là có '),(49,6,11,'chắc là lâu '),(50,7,11,NULL),(51,8,11,NULL),(52,9,11,NULL),(53,5,12,'có'),(54,6,12,'gần đây'),(55,7,12,NULL),(56,8,12,NULL),(57,9,12,NULL),(58,1,12,NULL),(59,2,12,NULL),(60,3,12,NULL),(61,4,12,NULL);
/*!40000 ALTER TABLE `answer` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-09-10 17:40:31
