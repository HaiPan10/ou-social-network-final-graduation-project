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
-- Table structure for table `answer_option`
--

DROP TABLE IF EXISTS `answer_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answer_option` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `answer_id` bigint NOT NULL,
  `question_option_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `answer_id` (`answer_id`),
  KEY `question_option_id` (`question_option_id`),
  CONSTRAINT `answer_option_ibfk_1` FOREIGN KEY (`answer_id`) REFERENCES `answer` (`id`) ON DELETE CASCADE,
  CONSTRAINT `answer_option_ibfk_2` FOREIGN KEY (`question_option_id`) REFERENCES `question_option` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer_option`
--

LOCK TABLES `answer_option` WRITE;
/*!40000 ALTER TABLE `answer_option` DISABLE KEYS */;
INSERT INTO `answer_option` VALUES (1,1,3),(2,2,6),(3,3,9),(4,4,13),(5,5,3),(6,6,6),(7,7,9),(8,8,12),(9,9,1),(10,10,4),(11,11,7),(12,12,10),(13,13,2),(14,14,5),(15,15,7),(16,16,10),(17,17,1),(18,18,5),(19,19,7),(20,20,10),(21,21,3),(22,22,6),(23,23,9),(24,24,13),(25,27,14),(26,28,18),(27,29,25),(28,30,2),(29,31,5),(30,32,9),(31,33,13),(32,36,14),(33,36,15),(34,36,16),(35,37,18),(36,38,25),(37,44,1),(38,45,4),(39,46,7),(40,47,10),(41,50,15),(42,51,18),(43,52,25),(44,55,15),(45,56,18),(46,57,24),(47,62,27),(48,63,31),(49,67,36),(50,68,38),(51,69,41),(52,70,45),(53,71,26),(54,72,30),(55,76,35),(56,77,39),(57,78,41),(58,79,45),(59,80,27),(60,81,30),(61,85,35),(62,86,37),(63,87,43),(64,88,46),(65,89,27),(66,90,31),(67,94,35),(68,95,39),(69,96,42),(70,97,45),(71,98,29),(72,99,32),(73,103,35),(74,104,40),(75,105,41),(76,106,45);
/*!40000 ALTER TABLE `answer_option` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-10 22:32:10
