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
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `dob` datetime DEFAULT NULL,
  `avatar` varchar(300) DEFAULT NULL,
  `cover_avatar` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`id`) REFERENCES `account` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'TP.HCM','Trường Đại học Mở','1993-07-26 00:00:00','https://ou.edu.vn/wp-content/uploads/2019/01/OpenUniversity.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(2,'Phong','Lại Bình','2002-09-08 00:00:00','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1694094196/jnwlhhfj4vim7gvkel9p.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692616522/omalf8muwthntemxschs.png'),(3,'Linh','Vũ',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(4,'Huyền','Thanh',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(5,'Thanh Hải','Phan',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692624873/cubveg6kfzisoc2rdq7p.jpg','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(6,'Dat','Truong',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(7,'Bùi Thanh','Tâm',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1694101836/azqs7q8ivzfyoxivt1av.jpg','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(8,'Hy','Thanh','2002-12-04 00:00:00','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692626729/rd3tfappks56otsjmbkg.jpg','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692626911/rg6bznnajkosjikxlawv.jpg'),(9,'Nguyễn Hoàng','Hảo',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692627204/owmhav4jsyn4udvrnlry.jpg','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692627239/ll6uvhfo83i4yy36s1mr.jpg'),(10,'Ngaan','Nguyễn',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692682129/fbac3nuwr2yrptdijkhq.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692681333/jgpw5mqtcflcsb5iydc9.png'),(11,'Nhung','Võ','2004-10-26 00:00:00','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1692682828/qdrag8nwjxhuitg1k26s.jpg'),(12,'Hải','Phan Thanh','2002-05-18 00:00:00','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(13,'Mai','Nguyễn Thị Quỳnh ',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(14,'Account','Debug',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(15,'Acc2','Debug',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(17,'Hy','Hy',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(18,'Mai','Nguyễn',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(19,'Anh','Nguyễn Vân',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(20,'Nhựt Nam','Nguyễn Đăng',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(22,'Anh','Nguyễn Vân',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(23,'Hoang Hao','Nguyễn',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(24,'1','Debug ','2023-09-07 00:00:00','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(25,'le','phon',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(26,'Phương','Trịnh',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(29,'Phương','Trịnh ','2002-07-31 00:00:00','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1694104150/xb59wvurxlc6vvqbwct1.jpg','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1694102604/d4i8uqb5nqsuvi0yxrcw.png'),(30,'Tài','Trịnh Trung',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1694103545/w82a3cgkev3xhnwgqwz7.jpg','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(31,'Dat','Lương',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(32,'2','Debug','2023-09-09 00:00:00','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(33,'2','Debug',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(34,'Như Quỳnh','Đào',NULL,'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png'),(35,'Tú','Tăng','2002-03-23 00:00:00','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1694258377/ylrw4txe3sezmjqucwpw.jpg','https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-09-10 17:40:15
