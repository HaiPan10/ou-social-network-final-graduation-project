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
-- Table structure for table `post_reaction`
--

DROP TABLE IF EXISTS `post_reaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_reaction` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `post_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `reaction_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `post_id` (`post_id`),
  KEY `user_id` (`user_id`),
  KEY `reaction_id` (`reaction_id`),
  CONSTRAINT `post_reaction_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE,
  CONSTRAINT `post_reaction_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `post_reaction_ibfk_3` FOREIGN KEY (`reaction_id`) REFERENCES `reaction` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_reaction`
--

LOCK TABLES `post_reaction` WRITE;
/*!40000 ALTER TABLE `post_reaction` DISABLE KEYS */;
INSERT INTO `post_reaction` VALUES (1,'2023-08-21 13:06:12.000000',1,5,2),(2,'2023-08-21 13:33:34.000000',2,2,2),(3,'2023-08-21 13:34:02.000000',2,3,2),(4,'2023-08-21 13:34:07.000000',1,3,2),(5,'2023-08-21 13:35:09.000000',2,5,2),(6,'2023-08-21 13:41:48.000000',1,6,2),(7,'2023-08-21 13:41:50.000000',2,6,1),(10,'2023-08-21 13:53:06.000000',6,5,2),(11,'2023-08-21 13:53:32.000000',5,5,1),(12,'2023-08-21 13:54:30.000000',7,5,3),(13,'2023-08-21 13:56:20.000000',8,5,3),(15,'2023-08-21 14:01:29.000000',8,8,3),(16,'2023-08-21 14:02:54.000000',1,8,2),(17,'2023-08-21 14:03:56.000000',9,2,2),(18,'2023-08-21 14:04:39.000000',9,5,1),(19,'2023-08-21 14:04:40.000000',7,4,3),(20,'2023-08-21 14:05:32.000000',10,5,2),(21,'2023-08-21 14:08:17.000000',6,9,2),(23,'2023-08-21 14:11:10.000000',14,5,2),(24,'2023-08-21 14:11:19.000000',12,5,2),(25,'2023-08-21 14:12:02.000000',15,5,2),(26,'2023-08-21 14:12:31.000000',15,2,2),(27,'2023-08-21 14:13:45.000000',16,5,2),(30,'2023-08-21 14:16:03.000000',18,2,3),(31,'2023-08-21 14:16:18.000000',18,5,2),(36,'2023-08-21 14:34:45.000000',20,5,2),(37,'2023-08-21 14:34:47.000000',6,4,2),(38,'2023-08-21 14:35:04.000000',19,5,2),(40,'2023-08-21 17:03:48.000000',20,2,2),(41,'2023-08-21 17:04:56.000000',7,2,3),(42,'2023-08-22 09:15:26.000000',20,3,2),(43,'2023-08-22 09:15:32.000000',19,3,2),(44,'2023-08-22 09:15:40.000000',16,3,2),(45,'2023-08-22 09:15:48.000000',14,3,2),(46,'2023-08-22 09:15:50.000000',12,3,2),(47,'2023-09-07 13:54:54.000000',21,5,1),(48,'2023-09-07 14:08:49.000000',23,5,1),(49,'2023-09-07 14:20:21.000000',21,2,1),(52,'2023-09-07 14:36:53.000000',26,2,2),(53,'2023-09-07 14:41:38.000000',26,5,2),(54,'2023-09-07 14:42:02.000000',28,2,2),(55,'2023-09-07 14:46:59.000000',29,8,1),(56,'2023-09-07 14:47:16.000000',23,8,2),(57,'2023-09-07 14:48:34.000000',30,2,2),(59,'2023-09-07 14:50:30.000000',30,8,2),(60,'2023-09-07 15:00:22.000000',33,5,2),(61,'2023-09-07 15:00:25.000000',31,5,2),(62,'2023-09-07 15:00:29.000000',30,5,2),(63,'2023-09-07 15:00:33.000000',29,5,2),(64,'2023-09-07 15:01:29.000000',34,5,2),(65,'2023-09-07 15:06:10.000000',35,5,2),(66,'2023-09-07 15:07:32.000000',35,8,2),(67,'2023-09-07 15:10:45.000000',36,2,3),(68,'2023-09-07 15:11:00.000000',36,8,3),(69,'2023-09-07 15:11:38.000000',36,5,2),(70,'2023-09-07 15:13:32.000000',28,5,2),(71,'2023-09-07 15:46:29.000000',35,7,1),(72,'2023-09-07 15:52:56.000000',37,5,2),(73,'2023-09-07 15:59:41.000000',15,7,2),(74,'2023-09-08 05:42:01.000000',37,2,2),(77,'2023-09-08 06:15:43.000000',37,31,2),(80,'2023-09-09 03:54:57.000000',37,32,2),(81,'2023-09-09 04:00:00.000000',37,18,2),(82,'2023-09-10 07:19:13.000000',38,2,2),(83,'2023-10-12 06:44:30.465000',38,5,2),(84,'2023-10-12 06:48:01.650000',39,5,2),(85,'2023-10-12 06:48:26.077000',39,2,2),(86,'2023-10-13 13:04:26.000000',40,2,2),(87,'2023-10-13 13:04:11.474000',40,39,2),(89,'2023-10-16 01:30:54.013000',45,2,2),(90,'2023-10-21 10:31:38.466000',46,5,2),(91,'2023-10-25 04:45:15.144000',46,2,2),(92,'2023-10-25 15:17:26.584000',35,2,2),(93,'2023-10-27 14:51:12.936000',47,5,2);
/*!40000 ALTER TABLE `post_reaction` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-10 22:31:47