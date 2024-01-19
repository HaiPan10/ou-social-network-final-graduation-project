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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(45) NOT NULL,
  `password` varchar(300) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `verification_code` varchar(64) DEFAULT NULL,
  `status` enum('LOCKED','ACTIVE','AUTHENTICATION_PENDING','EMAIL_VERIFICATION_PENDING','REJECT','PASSWORD_CHANGE_REQUIRED') DEFAULT 'ACTIVE',
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `account_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'admin456@gmail.com','$2a$10$3YohAzphxM8cU1uvEkikleeAf4xPlPZrQ0eqx5iPgc0bcUT48j/SC','2023-09-07 12:02:01',NULL,'ACTIVE',3),(2,'phongvulai96@gmail.com','$2a$10$A5j1szYgmro27Q8GDp90ceXfKEuQ/Wi2ROc.jx2wtuwLVegUksjrG','2023-08-21 11:13:11','Mru7lHpLiPx4NFdFOz5T1mM2KvIjX8LNDP86ooyhePLznp4AazgTHyDNg7FnVES5','ACTIVE',1),(3,'2051012054linh@ou.edu.vn','$2a$10$QCZtc8nCH76CIxnEw48cLugNNsechvpZvu1pjplzhMOsvriruHOIS','2023-08-21 12:19:09','1ScrnZ5Qv0Sw66FGRBp5SmFoZNvjO7x5P0xOuXDpuPMJnjBRvLoXTXAmJRLfxPrD','ACTIVE',1),(4,'thanhhuyen1952002@gmail.com','$2a$10$ozvYVGApzQVgZbC9S9uD5.M0bU1aB5v9A79j4eiCyXR7QRifgNMfC','2023-08-21 12:20:39','IXYPWuMzJiKYS4NbeTAFAJOKjW6Lpl73MAfC2h3zhht2f1w9W151B3MNxPdq0LgJ','PASSWORD_CHANGE_REQUIRED',1),(5,'phanthanhhai3317@gmail.com','$2a$10$IsyOwpkTKt8wmdzrl6jvx.ksLvvzfd1yAmQUnRMM4n7MfyLiHOIqy','2023-08-21 13:03:09','hwExrThXHUUxXa4Ph9aQyMe6hQLD8hEa2TRL66hZ9qCelHuAIcsefz4tBvpWv3cT','ACTIVE',1),(6,'truongthanhdat2002@gmail.com','$2a$10$pf2I/6.pK8YYJ4UZ0LRuoeWXMRObf1ssGKQ5ukEvegOuHZQSTsOey','2023-08-21 13:39:28','akibHDG8P18J3unpyWI055ZELoRtCKuvy0FJD3zbBiaxrRsjB8m7xc2siL0BnkId','PASSWORD_CHANGE_REQUIRED',1),(7,'tambui@gmail.com','$2a$10$psf.TkokI4frE1IEVY9gNe6G8uYCulJKNfFDr6/w6dRxavC0uyWHa','2023-08-21 13:44:15',NULL,'LOCKED',2),(8,'qingxuan131415@gmail.com','$2a$10$WwEYXKMbKxlEoCpjCtIdOOkOOo85w/NH0Hzqh1YruvrK9hdfcORLO','2023-08-21 13:57:42','5YOaiNlVGiuD5ZTFMR0sDX5s7igN6qUIex95etEFFtkKsoty7OqC2PfmMut4ob90','LOCKED',1),(9,'AncuaHao@gmail.com','$2a$10$CDYy8bUyzSeE/IaNKedrB.vRM8KR50A1l.SAdnHnpkE34uhfIZ6Va','2023-08-21 14:06:43',NULL,'PASSWORD_CHANGE_REQUIRED',2),(10,'2051052087ngan@ou.edu.vn','$2a$10$5cZR/luJ3oKsKjmbBRBn8umPis3cH7SES.nApLVvv7537ZUJJvCMq','2023-08-22 05:09:29','CwZjEfXSpHGwRxQSectfG2F5FGFlGInAA8ikGIt0FU52i2rFZqY45e9EKjURY3ar','ACTIVE',1),(11,'vonhung5852@gmail.com','$2a$10$DOctWpvIQFLyQOqhp6ahnu4UlvBll37kS6csvZLRm2FysQs1bRioO','2023-08-22 05:25:44','DXKmBzKXxhkBZXXtlFs44oElUzQzGkvFYI4gJQYCK1gKmAODbun2LxcqHLmGvoQT','PASSWORD_CHANGE_REQUIRED',1),(12,'hai2000@gmail.com','$2a$10$NFcdBlXE/USg97/tkwKfDuq6tihp398ac4Ed4d3TarWb.drkYX8p6','2023-08-22 05:34:49',NULL,'LOCKED',2),(13,'panwink160617@gmail.com','$2a$10$.TsS4ztJYfgDVKQts78E0.07I1Xkx66WtPKVdGoCKNsUB4Tfay4xa','2023-08-22 13:30:32','3fV75nDfqID1uC6W4wULzOViBD5yeDGeJhl6yVnxzoBMF1Ea5arNkzyxAg8D36Rh','PASSWORD_CHANGE_REQUIRED',1),(14,'ousocialnetwork@gmail.com','$2a$10$89bXeEnuFKbSWgICqFBpLuft0F.qmolB0WtJcsJn62p3evAXKGiGW','2023-08-22 14:01:36','ZzsdhGxYc0d0zIhmTby5hO8TwYKze5QimaDy0xZWKITsfDki8TeXsZrT35ZHVygc','LOCKED',1),(15,'phongvulai99@gmail.com','$2a$10$z0rXuvgyQpRjGNVDXyfrgOmOiO5J39pvNNqjKsg4qfcupmunVhNjK','2023-08-22 14:04:14','79EAKrLZ3iq1zIxEZzTqwqnbvX1woO2gQjUuDxbKI2qPvHDnZC9T0XCl2Xi83SRw','LOCKED',1),(17,'thithanhhuyennguyen002@gmail.com','$2a$10$um/P7rGUETlaEEkDzVs78eszLNeXp32lZUS1DgmV/bF4LoCXtNwoq','2023-08-22 15:31:47','2rFXKRSI6FMBt7MEIci5kvCk1CCuANAM6f8yxfxhjYPRMM3o1ZVhOQjWK4oDYkXm','PASSWORD_CHANGE_REQUIRED',1),(18,'thanhhai18052002@gmail.com','$2a$10$8q/4tMZZiEiKSDuxSdEGK.iVfcCiI22ljO239lHwYL4nEa7BzQiNm','2023-08-22 15:51:07','LT5fWLblaeQrRPJhgwEO1NiyHbRoj8JOxBBD68WLiClqDFnZofZfaAQwUg8GoU0e','ACTIVE',1),(19,'anhnguyen@gmail.com','$2a$10$EnFx7QEkYFqxa87B7oRJyupldNmtYjdYuabcieXfTuS0ZmTHBgKPe','2023-09-07 14:26:29','uob26AGRk7CTfdrJ2biVel9ZeuR0iPkcHFGYto5aUdhOUM2Tu6tZxdObOZKSxA7v','LOCKED',1),(20,'nhutnam249.gg@gmail.com','$2a$10$4GTKJQ/IE62yTBAfYDEzsup3yyW/1RECmz5tlz54WSGLA.ZKnfha.','2023-09-07 14:28:40','V5ebD6E81OErMI0XrPPTfqEXWfk0l4W06NNnCDcCueus1YOWNLvuhOHiRqjT2Uqu','ACTIVE',1),(22,'2051012004anh@ou.edu.vn','$2a$10$6uNzVAsoHo5aive3EYx75uaKJBi4smpetRQTsVAPXlK0a4KwFsjqO','2023-09-07 14:30:24','y1MJQnYyNnEmlgebgR4Z258Eocy6uYgGgxKnRhWj9sV1MxHVxLSRlfWhQKRbFllx','ACTIVE',1),(23,'hao8a1vn@gmail.com','$2a$10$4alv3//aN4O3XJwoZothQOEzzxi5iQTGsseRZcMT.5VI6QKwP2rMS','2023-09-07 14:35:14','fjfLdVhZxKVX2I8UqFgFwfkvINa117CYJTYAAwpoNMImf7OE6MMY21ERmzYRE8vd','ACTIVE',1),(24,'hai2001@gmail.com','$2a$10$2ZYMho9diV3JYt1xLbDTJ.ttjdu4KxyAc8tCOjfe2Po8b78WmvvQu','2023-09-07 15:00:58',NULL,'LOCKED',2),(25,'phle8966@gmail.com','$2a$10$cS6zaWWeKKrAbrfXILec5OF00/KZ59sw9abH5sTuWJZ9D3MuvYnYO','2023-09-07 15:19:25','aMY5AMmJjofJmdrrkRWQtUxwjkQ6SdrGJLQJNqsBkHOTkuw6PWQKGyM5o7dVFP5q','ACTIVE',1),(26,'trucp498@gmail.com','$2a$10$vc6eyDaflIwDdVElPR8IQeyyjceaNDx3HzjPUq/61PDea4TCJb5/K','2023-09-07 15:43:04','rDG4hPoVBTtb08uR1UVcoy6RvyxKdo0kW3x4m3AqzS9tuy1Xyx26HrRcYEX1A6OK','LOCKED',1),(29,'trucp489@gmail.com','$2a$10$S9NLrat3q1Y4MFkCGVzx3.MkjxalQVfA1KiymsF25Zr6t/Rfvdq..','2023-09-07 15:50:33','e1pTGXwR1Hplfj2QkCHlceqUFtV7pxc8kzaqcuHCnXmofkPTwKMXxpAVxRwd5v4A','ACTIVE',1),(30,'taicuaphuong@gmail.com','$2a$10$uLcX7xd2SS4NA8u4l.8Uz.gASFCPHQnJS/IX7ubvR8LNpw49Cn2Cy','2023-09-07 16:15:09',NULL,'ACTIVE',2),(31,'datluongtan1410@gmail.com','$2a$10$Ee8UV4ub11bnqYds.wSEKu/0vMiP5mhHvOO0oj4vohNx0DyHIAcVW','2023-09-08 05:23:30','B8pRV1i1tUTMZSFBFHOOZsSppMbStqh3Zzx39SJsw4gPVsGjetWMMEVF5ghMnOhN','ACTIVE',1),(32,'hai2002@gmail.com','$2a$10$NpHBTpcYO2mgyk60EGJI3uUK4CJlYQkHq1ar.rhkwJ8fCqlaFDIDS','2023-09-09 03:54:30',NULL,'ACTIVE',2),(33,'phongvulai69@gmail.com','$2a$10$T6o3YOb8p6zTwM4VzURBleB707VHtoBA6aguqXlme8KUW4R/j3n5G','2023-09-09 04:04:43','5mH4JD4EbsRbKe3pEPnLAmSjqePbaqcnqjcMb2CEF5pkBwnGAbEoxPv2pEakRWfx','LOCKED',1),(34,'2054052051quynh@ou.edu.vn','$2a$10$yx3RBxarazqV4JSU6FgqXelx4B7zDN43hbSf8PEgppIFHG2OeeLb6','2023-09-09 06:35:07','VGBPF0FZhIMU3inzLKZyhggQLLoRh3jy3zD1o3zIg2js73sx1u9SuAXr89LAXdnr','ACTIVE',1),(35,'2051010352tu@ou.edu.vn','$2a$10$0A5w8g/1B6OYlsOw5/KZSue/X/A0KqBrK4azrZDwWPwdukzXUNUo.','2023-09-09 11:04:28','xrZHGiH2j7gwYRVduMBcJsejXevAZjazFaSNGTIOfkcOP9yfkNsyXq0VHxWApyX6','ACTIVE',1);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-09-10 17:40:02
