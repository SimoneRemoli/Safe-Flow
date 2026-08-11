-- MySQL dump 10.13  Distrib 8.0.20, for macos10.15 (x86_64)
--
-- Host: localhost    Database: SafeFlow_Update
-- ------------------------------------------------------
-- Server version	8.0.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `SafeFlow_Update`
--

-- ===============================
-- Safe Flow - CLEAN DATABASE SCHEMA
-- Operational data is intentionally not populated.
-- Load SafeFlow_bootstrap_accounts.sql only when an initial admin login is needed.
-- ===============================

-- DROP + CREATE DATABASE
DROP DATABASE IF EXISTS SafeFlow_Update;
CREATE DATABASE SafeFlow_Update
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE SafeFlow_Update;

--
-- Table structure for table `Citta`
--

DROP TABLE IF EXISTS `Citta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Citta` (
  `id_Citta` int NOT NULL AUTO_INCREMENT,
  `nome_Citta` varchar(100) NOT NULL,
  PRIMARY KEY (`id_Citta`),
  UNIQUE KEY `nome_Citta` (`nome_Citta`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Citta`
--

LOCK TABLES `Citta` WRITE;
/*!40000 ALTER TABLE `Citta` DISABLE KEYS */;
INSERT INTO `Citta` VALUES (1,'Rome'),(3,'Naples'),(5,'Athens'),(6,'Budapest');
/*!40000 ALTER TABLE `Citta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comunicazioni`
--

DROP TABLE IF EXISTS `comunicazioni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comunicazioni` (
  `testo` varchar(500) NOT NULL,
  `data` timestamp NOT NULL,
  `risolto` tinyint(1) DEFAULT NULL,
  `approvato` tinyint(1) NOT NULL DEFAULT '1',
  `letto` tinyint(1) NOT NULL DEFAULT '1',
  `status` varchar(20) NOT NULL DEFAULT 'APPROVED',
  `sender_role` varchar(20) NOT NULL DEFAULT 'ADMIN',
  `sender_cf` varchar(16) DEFAULT NULL,
  `recipient_cf` varchar(16) DEFAULT NULL,
  `city` varchar(100) DEFAULT 'Rome',
  `pickpocket_alert` tinyint(1) NOT NULL DEFAULT '0',
  `fight_alert` tinyint(1) NOT NULL DEFAULT '0',
  `crowd_alert` tinyint(1) NOT NULL DEFAULT '0',
  `general_alert` tinyint(1) NOT NULL DEFAULT '0',
  `station_name` varchar(120) DEFAULT NULL,
  `suspect_clothing` varchar(180) DEFAULT NULL,
  PRIMARY KEY (`testo`,`data`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Clean table for `comunicazioni`
--

LOCK TABLES `comunicazioni` WRITE;
/*!40000 ALTER TABLE `comunicazioni` DISABLE KEYS */;
/*!40000 ALTER TABLE `comunicazioni` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `listaregistrati`
--

DROP TABLE IF EXISTS `listaregistrati`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `listaregistrati` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `codicefiscale` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `codice_fiscale` (`codicefiscale`),
  UNIQUE KEY `uq_email_cf` (`email`,`codicefiscale`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `listaregistrati`
--

LOCK TABLES `listaregistrati` WRITE;
/*!40000 ALTER TABLE `listaregistrati` DISABLE KEYS */;
/*!40000 ALTER TABLE `listaregistrati` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Permessi`
--

DROP TABLE IF EXISTS `Permessi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Permessi` (
  `id` int NOT NULL AUTO_INCREMENT,
  `Nome` varchar(100) NOT NULL,
  `Cognome` varchar(100) DEFAULT NULL,
  `Email` varchar(100) NOT NULL,
  `Password` varchar(100) DEFAULT NULL,
  `Ruolo` varchar(100) DEFAULT NULL,
  `Codice_Fiscale` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Clean table for `Permessi`
--

LOCK TABLES `Permessi` WRITE;
/*!40000 ALTER TABLE `Permessi` DISABLE KEYS */;
/*!40000 ALTER TABLE `Permessi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `User` (
  `CODICEFISCALE` varchar(16) NOT NULL,
  `NOME` varchar(100) NOT NULL,
  `COGNOME` varchar(100) NOT NULL,
  `DATADINASCITA` date NOT NULL,
  `EMAIL` varchar(100) NOT NULL,
  `PASSWORD` varchar(100) NOT NULL,
  `DISABILE` tinyint(1) DEFAULT NULL,
  `RUOLO` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`CODICEFISCALE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Clean table for `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for Safe Flow social data
--

DROP TABLE IF EXISTS `sf_user_profiles`;
CREATE TABLE `sf_user_profiles` (
  `codice_fiscale` varchar(16) NOT NULL,
  `bio` varchar(500) NOT NULL DEFAULT '',
  `avatar_content_type` varchar(80) DEFAULT NULL,
  `avatar_data` longblob,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`codice_fiscale`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `sf_report_images`;
CREATE TABLE `sf_report_images` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `notification_key` varchar(512) NOT NULL,
  `file_name` varchar(32) NOT NULL,
  `content_type` varchar(80) NOT NULL,
  `image_data` longblob NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_report_image` (`notification_key`,`file_name`),
  KEY `idx_report_images_notification` (`notification_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `sf_notification_likes`;
CREATE TABLE `sf_notification_likes` (
  `notification_key` varchar(512) NOT NULL,
  `traveler_cf` varchar(16) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`notification_key`,`traveler_cf`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `sf_notification_comments`;
CREATE TABLE `sf_notification_comments` (
  `id` varchar(64) NOT NULL,
  `notification_key` varchar(512) NOT NULL,
  `author_cf` varchar(16) NOT NULL,
  `parent_comment_id` varchar(64) DEFAULT NULL,
  `reply_to_cf` varchar(16) DEFAULT NULL,
  `text` varchar(600) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_comments_notification` (`notification_key`,`created_at`),
  KEY `idx_comments_parent` (`parent_comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `sf_comment_likes`;
CREATE TABLE `sf_comment_likes` (
  `comment_id` varchar(64) NOT NULL,
  `traveler_cf` varchar(16) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`comment_id`,`traveler_cf`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `sf_like_notification_markers`;
CREATE TABLE `sf_like_notification_markers` (
  `marker_type` varchar(32) NOT NULL,
  `item_key` varchar(512) NOT NULL,
  `actor_cf` varchar(16) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`marker_type`,`item_key`,`actor_cf`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `sf_internal_notification_targets`;
CREATE TABLE `sf_internal_notification_targets` (
  `internal_notification_key` varchar(512) NOT NULL,
  `report_notification_key` varchar(512) NOT NULL,
  `comment_id` varchar(64) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`internal_notification_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `sf_notification_dismissals`;
CREATE TABLE `sf_notification_dismissals` (
  `notification_type` varchar(24) NOT NULL,
  `traveler_cf` varchar(16) NOT NULL,
  `notification_key` varchar(512) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`notification_type`,`traveler_cf`,`notification_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping events for database 'SafeFlow_Update'
--

--
-- Dumping routines for database 'SafeFlow_Update'
--
/*!50003 DROP PROCEDURE IF EXISTS `getAllCity` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getAllCity`()
BEGIN
    SELECT nome_Citta
    FROM Citta
    ORDER BY nome_Citta ASC;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getMessages` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getMessages`()
BEGIN
    SELECT
        testo,
        data,
        risolto,
        approvato,
        letto,
        status,
        sender_role,
        sender_cf,
        recipient_cf,
        city,
        pickpocket_alert,
        fight_alert,
        crowd_alert,
        general_alert,
        station_name,
        suspect_clothing
    FROM comunicazioni
    ORDER BY data DESC;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `login_User` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `login_User`(
    IN p_email VARCHAR(100),
    IN p_password VARCHAR(100)
)
BEGIN
    
    SELECT
        u.codicefiscale   AS p_codiceFiscale,
        u.nome            AS p_nome,
        u.cognome         AS p_cognome,
        u.datadinascita   AS p_dataDiNascita,
        u.disabile        AS p_disabile,
        1                 AS buffer,
        u.ruolo			  AS ruolo
    FROM User u
    WHERE u.email = p_email AND u.password = p_password

    UNION

    
    SELECT
        p.Codice_Fiscale  AS p_codiceFiscale,
        p.nome            AS p_nome,
        p.cognome         AS p_cognome,
        NULL              AS p_dataDiNascita,
        NULL              AS p_disabile,
        2                 AS buffer,
        p.Ruolo			  AS ruolo
    FROM Permessi p
    WHERE p.email = p_email AND p.password = p_password

    LIMIT 1;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ListAdmins` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `ListAdmins`()
BEGIN
    SELECT
        Nome AS nome,
        Cognome AS cognome,
        Email AS email,
        Codice_Fiscale AS codice_fiscale
    FROM SafeFlow_Update.Permessi
    WHERE Ruolo = '2'
    ORDER BY Nome, Cognome, Email;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `CreateAdmin` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `CreateAdmin`(
    IN p_nome VARCHAR(100),
    IN p_cognome VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_password VARCHAR(100),
    IN p_codice_fiscale VARCHAR(100)
)
BEGIN
    INSERT INTO SafeFlow_Update.Permessi (
        Nome,
        Cognome,
        Email,
        Password,
        Ruolo,
        Codice_Fiscale
    ) VALUES (
        p_nome,
        p_cognome,
        p_email,
        p_password,
        '2',
        p_codice_fiscale
    );
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `DeleteAdminByCodiceFiscale` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `DeleteAdminByCodiceFiscale`(
    IN p_codice_fiscale VARCHAR(100)
)
BEGIN
    DELETE FROM SafeFlow_Update.Permessi
    WHERE Codice_Fiscale = p_codice_fiscale
      AND Ruolo = '2';

    SELECT ROW_COUNT() AS deleted_rows;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ListTravelers` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `ListTravelers`()
BEGIN
    SELECT
        NOME AS nome,
        COGNOME AS cognome,
        EMAIL AS email,
        CODICEFISCALE AS codice_fiscale
    FROM SafeFlow_Update.User
    WHERE RUOLO = '3'
    ORDER BY NOME, COGNOME, EMAIL;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `register` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `register`(
    IN p_codice_fiscale VARCHAR(16),
    IN p_email VARCHAR(100)
)
BEGIN
    INSERT INTO SafeFlow_Update.listaregistrati (
        codicefiscale,
        email
    ) VALUES (
        p_codice_fiscale,
        p_email
    );
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `register_User` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `register_User`(
    IN p_nome VARCHAR(100),
    IN p_cognome VARCHAR(100),
    IN p_codice_fiscale VARCHAR(16),
    IN p_password VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_data_nascita DATE,
    IN p_disabile INT,
    IN p_ruolo INT
)
BEGIN
    INSERT INTO SafeFlow_Update.User (
        nome,
        cognome,
        codicefiscale,
        password,
        email,
        datadinascita,
        disabile,
        ruolo
    ) VALUES (
        p_nome,
        p_cognome,
        p_codice_fiscale,
        p_password,
        p_email,
        p_data_nascita,
        p_disabile,
        p_ruolo
    );
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `DeleteTravelerByCodiceFiscale` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `DeleteTravelerByCodiceFiscale`(
    IN p_codice_fiscale VARCHAR(16)
)
BEGIN
    DELETE FROM SafeFlow_Update.listaregistrati
    WHERE codicefiscale = p_codice_fiscale;

    DELETE FROM SafeFlow_Update.User
    WHERE CODICEFISCALE = p_codice_fiscale
      AND RUOLO = '3';

    SELECT ROW_COUNT() AS deleted_rows;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `spCommunication` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spCommunication`(
    IN p_message VARCHAR(500),
    IN p_data TIMESTAMP,
    IN p_risolto BOOLEAN,
    IN p_approvato BOOLEAN,
    IN p_letto BOOLEAN,
    IN p_status VARCHAR(20),
    IN p_senderRole VARCHAR(20),
    IN p_senderCf VARCHAR(16),
    IN p_recipientCf VARCHAR(16),
    IN p_city VARCHAR(100),
    IN p_pickpocketAlert BOOLEAN,
    IN p_fightAlert BOOLEAN,
    IN p_crowdAlert BOOLEAN,
    IN p_generalAlert BOOLEAN,
    IN p_stationName VARCHAR(120),
    IN p_suspectClothing VARCHAR(180)
)
BEGIN
    INSERT INTO SafeFlow_Update.comunicazioni (
        testo,
        data,
        risolto,
        approvato,
        letto,
        status,
        sender_role,
        sender_cf,
        recipient_cf,
        city,
        pickpocket_alert,
        fight_alert,
        crowd_alert,
        general_alert,
        station_name,
        suspect_clothing
    ) VALUES (
        p_message,
        p_data,
        p_risolto,
        p_approvato,
        p_letto,
        p_status,
        p_senderRole,
        p_senderCf,
        p_recipientCf,
        p_city,
        p_pickpocketAlert,
        p_fightAlert,
        p_crowdAlert,
        p_generalAlert,
        p_stationName,
        p_suspectClothing
    );
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ApproveTravelerCommunication` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `ApproveTravelerCommunication`(
    IN p_message VARCHAR(500),
    IN p_data TIMESTAMP
)
BEGIN
    UPDATE SafeFlow_Update.comunicazioni
    SET approvato = 1,
        status = 'APPROVED'
    WHERE testo = p_message
      AND data = p_data
      AND status = 'PENDING';

    SELECT ROW_COUNT() AS updated_rows;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `RejectTravelerCommunication` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `RejectTravelerCommunication`(
    IN p_message VARCHAR(500),
    IN p_data TIMESTAMP
)
BEGIN
    UPDATE SafeFlow_Update.comunicazioni
    SET approvato = 0,
        status = 'REJECTED'
    WHERE testo = p_message
      AND data = p_data
      AND status = 'PENDING';

    SELECT ROW_COUNT() AS updated_rows;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `MarkCommunicationAsRead` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `MarkCommunicationAsRead`(
    IN p_message VARCHAR(500),
    IN p_data TIMESTAMP
)
BEGIN
    UPDATE SafeFlow_Update.comunicazioni
    SET letto = 1
    WHERE testo = p_message
      AND data = p_data;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-10 18:04:41
