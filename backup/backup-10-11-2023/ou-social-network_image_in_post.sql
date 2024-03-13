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
-- Table structure for table `image_in_post`
--

DROP TABLE IF EXISTS `image_in_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image_in_post` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `image_url` varchar(300) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `post_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `post_id` (`post_id`),
  CONSTRAINT `image_in_post_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_in_post`
--

LOCK TABLES `image_in_post` WRITE;
/*!40000 ALTER TABLE `image_in_post` DISABLE KEYS */;
INSERT INTO `image_in_post` VALUES (1,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692616639/sfkh79tdl5n0peyrzalb.jpg','2023-08-21 11:17:20.000000','2023-08-21 11:17:20.000000',1),(2,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692623163/yrnkbknc5uefzjrkhoiv.jpg','2023-08-21 13:06:04.000000','2023-08-21 13:06:04.000000',2),(3,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692623163/tcmaysyutm4hikqydt9h.jpg','2023-08-21 13:06:04.000000','2023-08-21 13:06:04.000000',2),(5,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692625594/bhbzswnpexrr1jxhi4vz.jpg','2023-08-21 13:46:35.000000','2023-08-21 13:46:35.000000',4),(6,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692625594/lsiqiitvwqlj8zweqqxt.jpg','2023-08-21 13:46:35.000000','2023-08-21 13:46:35.000000',4),(7,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692625925/ghztgnwjk2bq7yhecxgu.png','2023-08-21 13:52:10.000000','2023-08-21 13:52:10.000000',5),(8,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692625927/vmq8aerwhej0aknlwxgc.png','2023-08-21 13:52:10.000000','2023-08-21 13:52:10.000000',5),(9,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692625929/jqtultbhztzluxvspqpk.png','2023-08-21 13:52:10.000000','2023-08-21 13:52:10.000000',5),(10,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692625916/swuo3mstmkdyrnt8mwbn.png','2023-08-21 13:52:10.000000','2023-08-21 13:52:10.000000',5),(11,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692625919/edpfr8byilzcr1syi3zq.png','2023-08-21 13:52:10.000000','2023-08-21 13:52:10.000000',5),(12,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692625921/szweu4hcggqswlbieuof.png','2023-08-21 13:52:10.000000','2023-08-21 13:52:10.000000',5),(13,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692625923/c9c1vkwrtrpg6n8fosmx.png','2023-08-21 13:52:10.000000','2023-08-21 13:52:10.000000',5),(14,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692625916/jkfsq0fbgpnrurjtn2zv.png','2023-08-21 13:52:10.000000','2023-08-21 13:52:10.000000',5),(15,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692625918/enkva0xidzhsqzxd2zeg.png','2023-08-21 13:52:10.000000','2023-08-21 13:52:10.000000',5),(16,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692625920/u9nyenqk6jobbcrupspy.png','2023-08-21 13:52:10.000000','2023-08-21 13:52:10.000000',5),(17,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692625926/bbpa59ergyu2aqmbt7yz.png','2023-08-21 13:52:10.000000','2023-08-21 13:52:10.000000',5),(18,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692625928/xa6h9lr2ce1pmz4vya2p.png','2023-08-21 13:52:10.000000','2023-08-21 13:52:10.000000',5),(19,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692625922/d4ijnetgprv6axha23gx.png','2023-08-21 13:52:10.000000','2023-08-21 13:52:10.000000',5),(20,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692625924/ocwvx4xxr9cl72nmpskx.png','2023-08-21 13:52:10.000000','2023-08-21 13:52:10.000000',5),(24,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692625988/mxmiaxvl5hdyxx0xtkqk.jpg','2023-08-21 13:53:09.000000','2023-08-21 13:53:09.000000',7),(27,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692626689/qrlpuixqguybzpbrzlcx.jpg','2023-08-21 14:04:51.000000','2023-08-21 14:04:51.000000',10),(29,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692626967/dwesbkdgqhzq5i63onkc.jpg','2023-08-21 14:09:28.000000','2023-08-21 14:09:28.000000',12),(31,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692627028/xjrzbvkqfjwtlckyvdww.jpg','2023-08-21 14:10:28.000000','2023-08-21 14:10:28.000000',14),(32,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692627091/dopdnadapj4zvxsstahp.jpg','2023-08-21 14:11:32.000000','2023-08-21 14:11:32.000000',15),(33,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692627213/m9x08hci250cww9s8bft.jpg','2023-08-21 14:13:34.000000','2023-08-21 14:13:34.000000',16),(35,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692627335/z5laxce76gn5nblxnjjk.jpg','2023-08-21 14:15:36.000000','2023-08-21 14:15:36.000000',18),(36,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692627486/ckj39exa5yu0xdx5ldhf.jpg','2023-08-21 14:18:07.000000','2023-08-21 14:18:07.000000',19),(37,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692627623/ft5rxbmaba8u4uon55vr.jpg','2023-08-21 14:20:24.000000','2023-08-21 14:20:24.000000',20),(38,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1694095673/uv2klz5sgbotsb1sqf0q.jpg','2023-09-07 14:07:54.000000','2023-09-07 14:07:54.000000',6),(39,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1694095711/geuwfaezgtr49lldrkjz.png','2023-09-07 14:08:34.000000','2023-09-07 14:08:34.000000',23),(40,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1694095842/umqnwmvm9eesxtivnmho.jpg','2023-09-07 14:10:43.000000','2023-09-07 14:10:43.000000',8),(41,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1694097209/q6hwr05mufbgfzobkov6.png','2023-09-07 14:33:30.000000','2023-09-07 14:33:30.000000',26),(42,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1694097617/y8hc0lp52i9njvsl1nvk.jpg','2023-09-07 14:40:19.000000','2023-09-07 14:40:19.000000',9),(43,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1694098017/f8rahkjvpftqqyyjeptt.jpg','2023-09-07 14:46:58.000000','2023-09-07 14:46:58.000000',30),(44,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1694098221/rxthdd6sxtlpghhjt2ik.jpg','2023-09-07 14:50:22.000000','2023-09-07 14:50:22.000000',31),(45,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1694098588/vf3ezzp2tyciqcmeqk07.jpg','2023-09-07 14:56:29.000000','2023-09-07 14:56:29.000000',33),(46,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1694098587/s3gjoae6qnyg3ojgdl7y.jpg','2023-09-07 14:56:29.000000','2023-09-07 14:56:29.000000',33),(47,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1694098805/nk2woq2r01tehanmwevr.png','2023-09-07 15:00:06.000000','2023-09-07 15:00:06.000000',34),(48,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1694099133/c3gglprhokgljaz5b3le.jpg','2023-09-07 15:05:34.000000','2023-09-07 15:05:34.000000',35),(50,'https://res.cloudinary.com/dj1stv9ba/image/upload/v1697093240/hxivdepppr3jclgstn1y.png','2023-10-12 06:47:20.778000','2023-10-12 06:47:20.778000',39),(51,'https://res.cloudinary.com/dj1stv9ba/image/upload/v1697093237/zjugkmkgfkasyntkrbsz.png','2023-10-12 06:47:20.778000','2023-10-12 06:47:20.778000',39),(52,'https://res.cloudinary.com/dj1stv9ba/image/upload/v1697093238/rfli9so32hnfaivraxkq.png','2023-10-12 06:47:20.778000','2023-10-12 06:47:20.778000',39),(55,'https://res.cloudinary.com/dj1stv9ba/image/upload/v1697183692/hnv5fqryq1tdimvsebj0.png','2023-10-13 07:54:53.321000','2023-10-13 07:54:53.321000',40),(56,'https://res.cloudinary.com/dj1stv9ba/image/upload/v1697183689/hoqmzk2vrh0m3sxtpmup.png','2023-10-13 07:54:53.321000','2023-10-13 07:54:53.321000',40),(57,'https://res.cloudinary.com/dj1stv9ba/image/upload/v1697183690/fd6dfpo6cgdaqdjqpuml.png','2023-10-13 07:54:53.321000','2023-10-13 07:54:53.321000',40),(58,'https://res.cloudinary.com/dj1stv9ba/image/upload/v1697183684/bvxllvla5nr0gukh9a9s.png','2023-10-13 07:54:53.321000','2023-10-13 07:54:53.321000',40),(59,'https://res.cloudinary.com/dj1stv9ba/image/upload/v1697183682/rpskkkexstmnkpplzoee.png','2023-10-13 07:54:53.321000','2023-10-13 07:54:53.321000',40),(60,'https://res.cloudinary.com/dj1stv9ba/image/upload/v1697183688/ees700lqgpq5vujcq93e.png','2023-10-13 07:54:53.321000','2023-10-13 07:54:53.321000',40),(61,'https://res.cloudinary.com/dj1stv9ba/image/upload/v1697183686/rcmuy4bn5cbpwpqucwbc.png','2023-10-13 07:54:53.321000','2023-10-13 07:54:53.321000',40),(62,'https://res.cloudinary.com/dj1stv9ba/image/upload/v1697863149/sbw0gj6akwkq0cwxelqz.jpg','2023-10-21 04:39:09.503000','2023-10-21 04:39:09.503000',46),(63,'https://res.cloudinary.com/dj1stv9ba/image/upload/v1698209242/zyrcarhqfrriywa5jvwj.png','2023-10-25 04:47:23.094000','2023-10-25 04:47:23.094000',47);
/*!40000 ALTER TABLE `image_in_post` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-10 22:32:13
