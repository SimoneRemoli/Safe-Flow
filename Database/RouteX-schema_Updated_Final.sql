-- MySQL dump 10.13  Distrib 8.0.20, for macos10.15 (x86_64)
--
-- Host: localhost    Database: RouteX_Update
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
-- Table structure for table `athens`
--

DROP TABLE IF EXISTS `athens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `athens` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) DEFAULT NULL,
  `disabile` varchar(100) DEFAULT NULL,
  `linea` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=268 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `athens`
--

LOCK TABLES `athens` WRITE;
/*!40000 ALTER TABLE `athens` DISABLE KEYS */;
INSERT INTO `athens` VALUES (1,'Koropi','no','L3'),(2,'Paiania-Kantza','no','L3'),(3,'Pallini','no','L3'),(4,'Doukissis Plakentias','si','L3'),(5,'Halandri','no','L3'),(6,'Aghia Paraskevi','no','L3'),(7,'Nomismatokopio','no','L3'),(8,'Holargos','no','L3'),(9,'Ethniki Amyna','no','L3'),(10,'Katehaki','no','L3'),(11,'Panormou','no','L3'),(12,'Ampelokipi','no','L3'),(13,'Megaro Moussikis','no','L3'),(14,'Evangelismos','no','L3'),(15,'Syntagma','si','L3-L2'),(16,'Panipistimo','no','L2'),(17,'Omonia','si','L2-L1'),(18,'Victoria','si','L1'),(19,'Attiki','si','L1-L2'),(20,'Aghios Nikolaos','no','L1'),(21,'Kato Patissia','no','L1'),(22,'Aghios Eleftherios','no','L1'),(23,'Ano Patissia','no','L1'),(24,'Perissos','no','L1'),(25,'Pefkakia','no','L1'),(26,'Nea Ionia','no','L1'),(27,'Iraklio','no','L1'),(28,'Irini','no','L1'),(29,'Neratziotissa','no','L1'),(30,'Maroussi','no','L1'),(31,'KAT','no','L1'),(32,'Kifissia','si','L1'),(33,'Akropoli','si','L2'),(34,'Syngrou Fix','no','L2'),(35,'Aghios Ioannis','no','L2'),(36,'Dafni','no','L2'),(37,'Aghios Dimitrios','no','L2'),(38,'Illioupoli','no','L2'),(39,'Alimos','no','L2'),(40,'Argyroupoli','no','L2'),(41,'Elliniko','no','L2'),(42,'Monastiraki','si','L3-L1'),(43,'Thissio','no','L1'),(44,'Petralona','no','L1'),(45,'Tavros','no','L1'),(46,'Kallithea','no','L1'),(47,'Moschato','no','L1'),(48,'Faliro','no','L1'),(49,'Piraeus','si','L1-L3'),(50,'Dimotiko Theatro','no','L3'),(51,'Maniatika','no','L3'),(52,'Nikaia','no','L3'),(53,'Korydallos','no','L3'),(54,'Aghia Varvara','no','L3'),(55,'Aghia Marina','no','L3'),(56,'Egaleo','si','L3'),(57,'Eleonas','no','L3'),(58,'Kerameikos','no','L3'),(59,'Metaxourghio','no','L2'),(60,'Larissa Station','no','L2'),(61,'Sepolia','no','L2'),(62,'Aghios Antonios','no','L2'),(63,'Peristeri','si','L2'),(64,'Anthoupoli','si','L2'),(65,'Neos Kosmos','si','L2'),(267,'Airport','si','L3');
/*!40000 ALTER TABLE `athens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `budapest`
--

DROP TABLE IF EXISTS `budapest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `budapest` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) DEFAULT NULL,
  `disabile` varchar(100) DEFAULT NULL,
  `linea` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `budapest`
--

LOCK TABLES `budapest` WRITE;
/*!40000 ALTER TABLE `budapest` DISABLE KEYS */;
INSERT INTO `budapest` VALUES (1,'Pillango utca','no','2R'),(2,'Puskas Ferenc Stadion','no','2R'),(3,'Keleti Palyaudvar','si','2R-4V'),(4,'Blaha Lujza Ter','si','2R'),(5,'Il. Janos Pal Papa Tar','no','4V'),(6,'Rakoczi Ter','no','4V'),(7,'Kalvin Ter','si','4V-3B'),(8,'Fovam Ter','no','4V'),(9,'Szent Gellert Ter - Muegyetem','no','4V'),(10,'Moricz Zsigmond Korter','no','4V'),(11,'Ujbuda-kozport','no','4V'),(12,'Bikas Park','no','4V'),(13,'Kelenfood Vasutallomas','no','4V'),(14,'Kobanya-Kispes','no','3B'),(15,'Hatar Ut','no','3B'),(16,'Pottyos Utca','no','3B'),(17,'Ecseri Ut','no','3B'),(18,'Nepliget','si','3B'),(19,'Nagyvarad Ter','no','3B'),(20,'Semmelweis Klinikak','no','3B'),(21,'Corvin-negyed','no','3B'),(22,'Ferenciek Tere','no','3B'),(23,'Deak Ferenc Ter','si','3B-1G-2R'),(24,'Vorosmarty Ter','no','1G'),(25,'Bajcsy-Zsilinszky ut','no','3B-1G'),(26,'Opera','no','1G'),(27,'Oktogon','no','1G'),(28,'Vorosmarty Utca','no','1G'),(29,'Kodaly Korond','no','1G'),(30,'Bajza Utca','no','1G'),(31,'Hosok Tere','no','1G'),(32,'Szechenyi-furdo','no','1G'),(33,'Mexikoi Ut','no','1G'),(34,'Ujpest-Kozpont','si','3B'),(35,'Ujpest-Varoskapu','no','3B'),(36,'Gyongyosi Utca','no','3B'),(37,'Forgach Utca','no','3B'),(38,'Goncz Arpad Vkp','no','3B'),(39,'Dozsa Gyorgy Ut','no','3B'),(40,'Lehel Ter','no','3B'),(41,'Nyugati Palyaudva','si','3B'),(42,'Arany Janos Utca','no','3B'),(43,'Kossuth Lajos Ter','si','2R'),(44,'Battyhany Ter','si','2R'),(45,'Szell Kalman Ter','no','2R'),(46,'Deli Palyaudvar','si','2R'),(47,'Astoria','no','2R'),(50,'Ors vezer tere','si','2R');
/*!40000 ALTER TABLE `budapest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `london`
--

DROP TABLE IF EXISTS `london`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `london` (
  `id` int NOT NULL,
  `nome` varchar(100) DEFAULT NULL,
  `disabile` varchar(100) DEFAULT NULL,
  `linea` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `london`
--

LOCK TABLES `london` WRITE;
/*!40000 ALTER TABLE `london` DISABLE KEYS */;
INSERT INTO `london` VALUES (0,'Chesham','si','M'),(1,'Amersham','si','M'),(2,'Chalfont&Latimer','si','M'),(3,'Chorleywood','si','M'),(4,'Rickmansworth','no','M'),(5,'Croxley','no','M'),(6,'Watford','no','M'),(7,'Moor Park','no','M'),(8,'Northwood','no','M'),(9,'Northwood Hills','no','M'),(10,'Pinner','si','M'),(11,'North Harrow','no','M'),(12,'Harrow on the Hill','si','M'),(13,'West Harrow','no','M'),(14,'Eastcote','no','M-P'),(15,'Ruislip Manor','no','M-P'),(16,'Ruislip','no','M-P'),(17,'Ickenham','si','M-P'),(18,'Hillingdon','si','M-P'),(19,'Uxbridge','si','M-P'),(20,'Kenton','no','B-O'),(21,'Harrow&Wealdstone','si','B-O'),(22,'Headstone Lane','no','O'),(23,'Hatch End','no','O'),(24,'Carpenders Park','si','O'),(25,'Bushey','no','O'),(26,'Watford High Street','no','O'),(27,'Watford Junction','si','O'),(28,'Preston Road','no','M'),(29,'Wembley Park','si','M-J'),(30,'Finchley Road','no','M-J'),(31,'Baker Street','no','H-C-M'),(32,'Great Portland Street','no','H-C-M'),(33,'Euston Square','no','H-C-M'),(34,'Euston','si','N-LO-V'),(35,'King\'s Cross St Pancras International','si','H-C-M-N-P'),(36,'Farringdon','si','H-C-M-E-TH'),(37,'Barbican','si','H-C-M'),(38,'Moorgate','si','H-C-M-N-CE-E'),(39,'Liverpool Street','si','LO-H-C-M-N-CE-E'),(40,'Bethnal Green','si','LO'),(41,'Cambridge Heath','si','LO'),(42,'London Fields','no','LO'),(43,'Hackney Downs','no','LO'),(44,'Hackney Central','no','LO'),(45,'Homerton','no','LO'),(46,'Hackney Wick','si','LO'),(47,'Rectory Road','si','LO'),(48,'Stoke Newington','si','LO'),(49,'Stamford Hill','si','LO'),(50,'Clapton','si','LO'),(51,'St James Street','si','LO'),(52,'Seven Sisters','si','V-LO'),(53,'Bruce Grove','si','LO'),(54,'White Hart Lane','si','LO'),(55,'Silver Street','si','LO'),(56,'Edmonton Green','si','LO'),(57,'Bush Hill Park','si','LO'),(58,'Enfield Town','si','LO'),(59,'Southbury','si','LO'),(60,'Turkey Street','no','LO'),(61,'Theobalds Grove','no','LO'),(62,'Cheshunt','si','LO'),(63,'Chingford','si','LO'),(64,'Highams Park','si','LO'),(65,'Wood Street','si','LO'),(66,'South Tottenham','si','V-LO'),(67,'Tottenham Hale','si','V'),(68,'Blackhorse Road','si','V-LO'),(69,'Walthamstow Central','si','V-LO'),(70,'Walthamstow Queen\'s Road','si','V-LO'),(71,'Leyton Midland Road','si','LO'),(72,'Leytonstone High Road','si','LO'),(73,'Wanstead Park','',''),(74,'Forest Gate','',''),(75,'Manor Park','',''),(76,'Ilford','',''),(77,'Seven Kings','',''),(78,'Goodmayes','',''),(79,'Chadwell Heath','',''),(80,'Romford','',''),(81,'Gidea Park','',''),(82,'Harold Wood','',''),(83,'Brentwood','',''),(84,'Shenfield','',''),(85,'Emerson Park','',''),(86,'Upminster','',''),(87,'Upminster Bridge','',''),(88,'Hornchurch','',''),(89,'Elm Park','',''),(90,'Dagenham East','',''),(91,'Dagenham Heathway','',''),(92,'Becontree','',''),(93,'Upney','',''),(94,'Barking Riverside','',''),(95,'Barking','',''),(96,'East Ham','',''),(97,'Upton Park','',''),(98,'Plaistow','',''),(99,'Woodgrange Park','',''),(100,'West Ham','',''),(101,'Abbey Road','',''),(102,'Stratford High Street','',''),(103,'Stratford','',''),(104,'Leyton','',''),(105,'Leytonstone','',''),(106,'Snaresbrook','',''),(107,'South Woodford','',''),(108,'Woodford','',''),(109,'Buckhurst Hill','',''),(110,'Loughton','',''),(111,'Debden','',''),(112,'Theydon Bols','',''),(113,'Epping','',''),(114,'Roding Valley','',''),(115,'Chigwell','',''),(116,'Grange Hill','',''),(117,'Hainault','',''),(118,'Fairlop','',''),(119,'Barkingside','',''),(120,'Newbury Park','',''),(121,'Gants Hill','',''),(122,'Redbridge','',''),(123,'Wanstead','',''),(124,'Stratford International','',''),(125,'Dalston Kingsland','',''),(126,'Canonbury','',''),(127,'Highbury/Islington','',''),(128,'Caledonian Road','',''),(129,'Holloway Road','',''),(130,'Arsenal','',''),(131,'Caledonian Road/Barnsbury','',''),(132,'Finsbury Park','',''),(133,'Manor House','',''),(134,'Turnpike Lane','',''),(135,'Wood Green','',''),(136,'Bounds Green','',''),(137,'Arnos Grove','',''),(138,'Southgate','',''),(139,'Oakwood','',''),(140,'Cockfosters','',''),(141,'Harringay Green Lanes','',''),(142,'Crouch Hill','',''),(143,'New Southgate','',''),(144,'Oakleigh Park','',''),(145,'New Barnet','',''),(146,'Towards Welwyn Garden City','',''),(147,'High Barnet','',''),(148,'Totteridge/Whetstone','',''),(149,'Woodside Park','',''),(150,'West Finchley','',''),(151,'Mill Hill East','',''),(152,'Finchley Central','',''),(153,'East Finchley','',''),(154,'Highgate','',''),(155,'Archway','',''),(156,'Upper Holloway','',''),(157,'Tufnell Park','',''),(158,'Kentish Town','',''),(159,'City Thameslink','',''),(160,'St Paul\'s','',''),(161,'Monument','',''),(162,'Cannon Street','',''),(163,'Blackfriars','',''),(164,'London Bridge','',''),(165,'Bermondsey','',''),(166,'Canada Water','',''),(167,'Rotherhithe','',''),(168,'Wapping','',''),(169,'Shadwell','',''),(170,'Shadwell/Limehouse','',''),(171,'Limehouse','',''),(172,'Whitechapel','',''),(173,'Aldgate East','',''),(174,'Tower Hill','',''),(175,'Tower Gateway','',''),(176,'Aldgate','',''),(177,'Bethnal Green','',''),(178,'Mile End','',''),(179,'Bow Road','',''),(180,'Bow Church','',''),(181,'Pudding Mill Lane','',''),(182,'Maryland','',''),(183,'Bromley by Bow','',''),(184,'Devons Road','',''),(185,'Langdon Park','',''),(186,'All Saints','',''),(187,'Poplar','',''),(188,'Blackwall','',''),(189,'East India','',''),(190,'Canning Town','',''),(191,'Star Lane','',''),(192,'Westferry','',''),(193,'Royal Victoria','',''),(194,'Custom House','',''),(195,'Prince Regent','',''),(196,'Royal Albert','',''),(197,'Beckton Park','',''),(198,'Cyprus','',''),(199,'Gallions Reach','',''),(200,'Beckton','',''),(201,'West Silvertown','',''),(202,'Pontoon Dock','',''),(203,'London City Airport','',''),(204,'King George V','',''),(205,'Woolwich','',''),(206,'Abbey Wood','',''),(207,'Plumstead','',''),(208,'Woolwich Arsenal','',''),(209,'Charlton','',''),(210,'Westcombe Park','',''),(211,'Maze Hill','',''),(212,'Cutty Sark','',''),(213,'Island Gardens','',''),(214,'Mudchute','',''),(215,'Crossharbour','',''),(216,'South Quay','',''),(217,'Heron Quays','',''),(218,'Canary Wharf','',''),(219,'Canary Wharf South','',''),(220,'Canary Wharf North','',''),(221,'West India Quay','',''),(222,'Greenwich','',''),(223,'Deptford Bridge','',''),(224,'Elverson Road','',''),(225,'Lewisham','',''),(226,'Crofton Park','',''),(227,'Catford','',''),(228,'Bellingham','',''),(229,'Beckenham Hill','',''),(230,'Shortlands','',''),(231,'Bromley South','',''),(232,'Bickley','',''),(233,'St Mary Cray','',''),(234,'Petts Wood','',''),(235,'Orpington','',''),(236,'Swanley','',''),(237,'Towards Sevenoaks','',''),(238,'Slade Green','',''),(239,'Dartford','',''),(240,'Towards Gravesend','',''),(241,'Beckenham Junction','',''),(242,'Beckenham Road','',''),(243,'Avenue Road','',''),(244,'Birkbeck','',''),(245,'Harrington Road','',''),(246,'Elmers End','',''),(247,'Arena','',''),(248,'Woodside','',''),(249,'Blackhorse Lane','',''),(250,'Addiscombe','',''),(251,'Lloyd Park','',''),(252,'Coombe Lane','',''),(253,'Gravel Hill','',''),(254,'Addington Village','',''),(255,'Fieldway','',''),(256,'King Henry\'s Drive','',''),(257,'New Addington','',''),(258,'Sandilands','',''),(259,'Lebanon Road','',''),(260,'East Croydon','',''),(261,'East Croydon South','',''),(262,'South Croydon','',''),(263,'Purley','',''),(264,'Coulsdon South','',''),(265,'Towards Gatwick Airport','',''),(266,'Norwood Junction','',''),(267,'Anerley','',''),(268,'Penge West','',''),(269,'Crystal Palace','',''),(270,'Sydenham','',''),(271,'Forest Hill','',''),(272,'Honor Oak Park','',''),(273,'Brockley','',''),(274,'New Cross Gate','',''),(275,'New Cross','',''),(276,'Surrey Quays','',''),(277,'Nunhead','',''),(278,'Queens Road Peckham','',''),(279,'Peckham Rye','',''),(280,'Denmark Hill','',''),(281,'Elephant/Castle','',''),(282,'Elephant/Castle Ovest','',''),(283,'Loughborough Junction','',''),(284,'Herne Hill','',''),(285,'Tulse Hill','',''),(286,'Streatham','',''),(287,'Mitcham Eastfields','',''),(288,'Mitcham Junction','',''),(289,'Beddington Lane','',''),(290,'Therapia Lane','',''),(291,'Ampere Way','',''),(292,'Waddon Marsh','',''),(293,'Wandle Park','',''),(294,'Reeves Corner','',''),(295,'Church Street','',''),(296,'Centrale','',''),(297,'West Croydon South','',''),(298,'West Croydon','',''),(299,'Wellesley Road','',''),(300,'George Street','',''),(301,'Hackbridge','',''),(302,'Carshalton','',''),(303,'Sutton','',''),(304,'West Sutton','',''),(305,'Sutton Common','',''),(306,'St Heller','',''),(307,'Morden South','',''),(308,'South Merton','',''),(309,'Wimbledon Chase','',''),(310,'Wimbledon','',''),(311,'Dundonald Road','',''),(312,'Merton Park','',''),(313,'Morden Road','',''),(314,'South Wimbledon','',''),(315,'Phipps Bridge','',''),(316,'Belgrave Walk','',''),(317,'Mitcham','',''),(318,'Colliers Wood','',''),(319,'Tooting Broadway','',''),(320,'Haydons Road','',''),(321,'Tooting Bec','',''),(322,'Balham','',''),(323,'Clapham South','',''),(324,'Clapham Common','',''),(325,'Clapham North','',''),(326,'Clapham High Street','',''),(327,'Stockwell','',''),(328,'Brixton','',''),(329,'Oval','',''),(330,'Kennington','',''),(331,'Waterloo','',''),(332,'Embankment','',''),(333,'Charing Cross','',''),(334,'Piccadilly Circus','',''),(335,'Leicester Square','',''),(336,'Covent Garden','',''),(337,'Holborn','',''),(338,'Chancery Lane','',''),(339,'Russell Square','',''),(340,'Warren Street','',''),(341,'OXFORD Circus','',''),(342,'Tottenham Court Road','',''),(343,'Goodge Street','',''),(344,'Green Park','',''),(345,'Hyde Park Corner','',''),(346,'Knightsbridge','',''),(347,'South Kensington','',''),(348,'Sloane Square','',''),(349,'Victoria','',''),(350,'St James\'s Park','',''),(351,'Westminster','',''),(352,'Bond Street','',''),(353,'Marble Arch','',''),(354,'Lancaster Gate','',''),(355,'Queensway','',''),(356,'Notting Hill Gate','',''),(357,'Bayswater','',''),(358,'PADDINGTON','',''),(359,'Shepherd\'s Bush','',''),(360,'Kensington (Olympia)','',''),(361,'High Street Kensington','',''),(362,'Gloucester Road','',''),(363,'Earl\'s Court','',''),(364,'West Brompton','',''),(365,'Fulham Broadway','',''),(366,'Parsons Green','',''),(367,'Putney Bridge','',''),(368,'Imperial Wharf','',''),(369,'East Putney','',''),(370,'Southfields','',''),(371,'Wimbledon Park','',''),(372,'West Kensington','',''),(373,'Barons Court','',''),(374,'Hammersmith','',''),(375,'Ravenscourt Park','',''),(376,'Stamford Brook','',''),(377,'Goldhawk Road','',''),(378,'Bush Market','',''),(379,'White City','',''),(380,'Wood Lane','',''),(381,'East Acton','',''),(382,'North Acton','',''),(383,'Hanger Lane','',''),(384,'Park Royal','',''),(385,'North Ealing','',''),(386,'Alperton','',''),(387,'Sudbury Town','',''),(388,'Sudbury Hill','',''),(389,'South Harrow','',''),(390,'Perivale','',''),(391,'Greenford','',''),(392,'Northolt','',''),(393,'South Ruislip','',''),(394,'Ruislip Gardens','',''),(395,'West Ruislip','',''),(396,'Acton Main Line','',''),(397,'West Acton','',''),(398,'Acton Central','',''),(399,'South Acton','',''),(400,'Turnham Green','',''),(401,'Chiswick Park','',''),(402,'Gunnersbury','',''),(403,'Acton Town','',''),(404,'Ealing Common','',''),(405,'Ealing Broadway','',''),(406,'West Ealing','',''),(407,'Hanwell','',''),(408,'Southhall','',''),(409,'Hayes/Harlington','',''),(410,'West Drayton','',''),(411,'Iver','',''),(412,'Langley','',''),(413,'Slough','',''),(414,'Burnham','',''),(415,'Taplow','',''),(416,'Maldenhead','',''),(417,'Twyford','',''),(418,'Reading','',''),(419,'Heathrow Terminals 2-3','',''),(420,'Heathrow Terminal 5','',''),(421,'Heathrow Terminal 4','',''),(422,'Richmond','',''),(423,'Kew Gardens','',''),(424,'Hatton Cross','',''),(425,'Hounslow West','',''),(426,'Hounslow Central','',''),(427,'Hounslow East','',''),(428,'Osterley','',''),(429,'Boston Manor','',''),(430,'Northfields','',''),(431,'South Ealing','',''),(432,'Pimlico','',''),(433,'Battersea Power Station','',''),(434,'Nine Elms','',''),(435,'Vauxhall','',''),(436,'Lambeth North','',''),(437,'Southwark','',''),(438,'Borough','',''),(439,'Royal Oak','',''),(440,'Westbourne Park','',''),(441,'Ladbroke Grove','',''),(442,'Latimer Road','',''),(443,'Warwick Avenue','',''),(444,'Malda Vale','',''),(445,'Kilburn Park','',''),(446,'Queen\'s Park','',''),(447,'Kensal Green','',''),(448,'Kilburn High Road','',''),(449,'Edgware Road','',''),(450,'Marylebone','',''),(451,'Edgware Road','',''),(452,'Regent\'s Park','',''),(453,'Kensal Rise','',''),(454,'Brondesbury Parks','',''),(455,'Brondesbury','',''),(456,'Harlesden','',''),(457,'Stonebridge Park','',''),(458,'Wembley Central','',''),(459,'North Wembley','',''),(460,'South Kenton','',''),(461,'Willesden Junction','',''),(462,'Stanmore','',''),(463,'Canons Park','',''),(464,'Queensbury','',''),(465,'Kingsbury','',''),(466,'Neasden','',''),(467,'Dollis Hill','',''),(468,'Willesden Green','',''),(469,'Kilburn','',''),(470,'West Hampstead south','',''),(471,'West Hampstead','',''),(472,'West Hampstead Thameslink','',''),(473,'Finchley Road/Frognal','',''),(474,'South Hampstead','',''),(475,'Swiss Cottage','',''),(476,'St John\'s Wood','',''),(477,'Hampstead Heath','',''),(478,'Belsize Park','',''),(479,'Chalk Farm','',''),(480,'Camden Town','',''),(481,'Mornington Crescent','',''),(482,'Angel','',''),(483,'Old Street','',''),(484,'Dalston Junction','',''),(485,'Haggerston','',''),(486,'Hoxton','',''),(487,'Shoreditch High Street','',''),(488,'Clapham Junction','',''),(489,'Wandsworth Road','',''),(490,'Deptford','',''),(491,'Cricklewood','',''),(492,'Brent Cross West','',''),(493,'Hendon','',''),(494,'Mill Hill Broadway','',''),(495,'Elstree/Borehamwood','',''),(496,'Towards St Albans City and Luton Airport Parkway','',''),(497,'Edgware','',''),(498,'Burnt Oak','',''),(499,'Hendon Central','',''),(500,'Brent Cross','',''),(501,'Golders Green','',''),(502,'Hampstead','',''),(503,'Gospel Oak','',''),(504,'Kentish Town West','',''),(505,'Camden Road','',''),(506,'Tooting','',''),(507,'Stepney Green','',''),(508,'Rayners Lane','',''),(509,'Northwick Park','',''),(510,'Mansion House','',''),(511,'North Greenwich','',''),(512,'Ravensbourne','','');
/*!40000 ALTER TABLE `london` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `milan`
--

DROP TABLE IF EXISTS `milan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `milan` (
  `id` int NOT NULL,
  `nome` varchar(100) DEFAULT NULL,
  `disabile` varchar(100) DEFAULT NULL,
  `linea` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `milan`
--

LOCK TABLES `milan` WRITE;
/*!40000 ALTER TABLE `milan` DISABLE KEYS */;
INSERT INTO `milan` VALUES (0,'Gessate','si','M2'),(1,'C.Na Antonietta','si','M2'),(2,'Gorgonzola','si','M2'),(3,'Villa Pompea','no','M2'),(4,'Bussero','si','M2'),(5,'Cassina De\' Pecchi','si','M2'),(6,'Villa Fiorita','si','M2'),(7,'Cernusco sul Naviglio','si','M2'),(8,'Cascina Burrona','no','M2'),(9,'Vimodrone','si','M2'),(10,'C.Na Gobba','si','M2'),(11,'Cologno Sud','no','M2'),(12,'Cologno Centro','no','M2'),(13,'Cologno Nord','si','M2'),(14,'Crescenzago','si','M2'),(15,'Cimiano','si','M2'),(16,'Udine','si','M2'),(17,'Lambrate FS','si','M2'),(18,'Piola','si','M2'),(19,'Loreto','si','M2-M1'),(20,'Pasteur','si','M1'),(21,'Rovereto','si','M1'),(22,'Turro','si','M1'),(23,'Gorla','si','M1'),(24,'Precotto','si','M1'),(25,'Villa San Giovanni','si','M1'),(26,'Sesto Marelli','si','M1'),(27,'Sesto Rondo','no','M1'),(28,'Sesto 1 Maggio FS','si','M1'),(29,'Caiazzo','no','M2'),(30,'Centrale FS','si','M2-M3'),(31,'Gioia','si','M2'),(32,'Garibaldi FS','si','M2-M5'),(33,'Moscova','no','M2'),(34,'Lanza','no','M2'),(35,'Cadorna FN','si','M2-M1'),(36,'Sant\'Ambrogio','si','M2-M4'),(37,'Sant\'Agostino','si','M2'),(38,'Porta Genova FS','si','M2'),(39,'Romolo','si','M2'),(40,'Famagosta','si','M2'),(41,'Assago Milanofiori Nord','si','M2'),(42,'Piazza Abbiategrasso','si','M2'),(43,'Assago Milanofiori Forum','si','M2'),(44,'Lima','si','M1'),(45,'Porta Venezia','si','M1'),(46,'Palestro','si','M1'),(47,'San Babila','si','M1-M4'),(48,'Duomo','si','M1-M3'),(49,'Cordusio','si','M1'),(50,'Cairoli','si','M1'),(51,'Conciliazione','no','M1'),(52,'Pagano','si','M1'),(53,'Buonarroti','no','M1'),(54,'Amendola','si','M1'),(55,'Lotto','si','M1-M5'),(56,'QT8','no','M1'),(57,'Lampugnano','si','M1'),(58,'Uruguay','no','M1'),(59,'Bonola','si','M1'),(60,'San Leonardo','no','M1'),(61,'Molino Dorino','si','M1'),(62,'Pero','si','M1'),(63,'Rho Fiera','si','M1'),(64,'Wagner','no','M1'),(65,'De Angeli','no','M1'),(66,'Gambara','si','M1'),(67,'Bande Nere','si','M1'),(68,'Primaticcio','no','M1'),(69,'Inganni','si','M1'),(70,'Bisceglie','si','M1'),(71,'Comasina','si','M3'),(72,'Affori FN','si','M3'),(73,'Affori Centro','si','M3'),(74,'Dergano','si','M3'),(75,'Maciachini','si','M3'),(76,'Zara','si','M3-M5'),(77,'Sondrio','si','M3'),(78,'Repubblica','si','M3'),(79,'Turati','si','M3'),(80,'Montenapoleone','si','M3'),(81,'Missori','si','M3-M4'),(82,'Crocetta','si','M3'),(83,'Porta Romana','si','M3'),(84,'Lodi T.I.B.B.','si','M3'),(85,'Brenta','si','M3'),(86,'Corvetto','si','M3'),(87,'Porto di Mare','si','M3'),(88,'Rogoredo FS','si','M3'),(89,'San Donato','si','M3'),(90,'Santa Sofia','si','M4'),(91,'Vetra','si','M4'),(92,'De Amicis','si','M4'),(93,'Coni Zugna','si','M4'),(94,'California','si','M4'),(95,'Washington-Bolivar','si','M4'),(96,'Tolstoj','si','M4'),(97,'Frattini','si','M4'),(98,'Gelsomini','si','M4'),(99,'Segneri','si','M4'),(100,'San Cristoforo','si','M4'),(101,'Bignami Parco Nord','si','M5'),(102,'Ponale','si','M5'),(103,'Bicocca','si','M5'),(104,'Ca\' Granda','si','M5'),(105,'Istria','si','M5'),(106,'Marche','si','M5'),(107,'Isola','si','M5'),(108,'Monumentale','si','M5'),(109,'Cenisio','si','M5'),(110,'Gerusalemme','si','M5'),(111,'Domodossola FN','si','M5'),(112,'Tre Torri','si','M5'),(113,'Portello','si','M5'),(114,'Segesta','si','M5'),(115,'San Siro Ippodromo','si','M5'),(116,'San Siro Stadio','si','M5'),(117,'Tricolore','si','M4'),(118,'Dateo','si','M4'),(119,'Susa','si','M4'),(120,'Argonne','si','M4'),(121,'Forlanini FS','si','M4'),(122,'Repetti','si','M4'),(123,'Linate Aeroporto','si','M4');
/*!40000 ALTER TABLE `milan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `naples`
--

DROP TABLE IF EXISTS `naples`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `naples` (
  `id` int NOT NULL,
  `nome` varchar(100) NOT NULL,
  `disabile` varchar(100) DEFAULT NULL,
  `linea` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `naples`
--

LOCK TABLES `naples` WRITE;
/*!40000 ALTER TABLE `naples` DISABLE KEYS */;
INSERT INTO `naples` VALUES (0,'Pozzuoli Solfatara','si','2'),(1,'Bagnoli-Agnano Terme','si','2'),(2,'Cavalleggeri Aosta','si','2'),(3,'Campi Flegrei','si','2-6'),(4,'Mostra','si','6'),(5,'P.Leopardi','si','2'),(6,'Augusto','si','6'),(7,'Lala','si','6'),(8,'Mergellina','si','6-2'),(9,'Arco Mirelli','si','6'),(10,'San Pasquale','si','6'),(11,'Chiaia','si','6'),(12,'P.Amedeo','si','2'),(13,'Montesanto','si','2'),(14,'Museo-Piazza Cavour','si','2-1'),(15,'Dante','si','1'),(16,'Toledo','si','1'),(17,'Municipio','si','1-6'),(18,'Università','si','1'),(19,'Duomo','si','1'),(20,'Garibaldi','si','2-1'),(21,'Gianturco','no','2'),(22,'S.Giovanni-Barra','si','2'),(23,'Materdei','si','1'),(24,'Salvator Rosa','si','1'),(25,'Quattro Giornate','si','1'),(26,'Vanvitelli','no','1'),(27,'Medaglie D\'Oro','si','1'),(28,'Montedonzelli','no','1'),(29,'Rione Alto','si','1'),(30,'Policlinico','si','1'),(31,'Colli Aminei','no','1'),(32,'Frullone','si','1'),(33,'Chiaiano','si','1'),(34,'Piscinola','si','11-1'),(35,'Mugnano','si','11'),(36,'Giugliano','si','11'),(37,'Aversa Ippodromo','si','11'),(38,'Aversa Centro','si','11');
/*!40000 ALTER TABLE `naples` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pathinfo`
--

DROP TABLE IF EXISTS `pathinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pathinfo` (
  `StartStation` varchar(100) NOT NULL,
  `EndStation` varchar(100) NOT NULL,
  `City` varchar(100) NOT NULL,
  `TipoViaggiatore` varchar(100) NOT NULL,
  `NCambi` int DEFAULT '0',
  `ListaCambi` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '"Cambi non presenti"',
  `StazioneDiInterscambio` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '"Non presente"',
  `NStazioniAttraversate` int DEFAULT '0',
  `TempoDiArrivo` double DEFAULT '0',
  `NStazioniCitta` int DEFAULT '0',
  `PercTerrenoUtilizzato` double NOT NULL,
  `Utente` varchar(16) DEFAULT NULL,
  KEY `fk_utente_codicefiscale` (`Utente`),
  CONSTRAINT `fk_utente_codicefiscale` FOREIGN KEY (`Utente`) REFERENCES `user` (`CODICEFISCALE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pathinfo`
--

LOCK TABLES `pathinfo` WRITE;
/*!40000 ALTER TABLE `pathinfo` DISABLE KEYS */;
INSERT INTO `pathinfo` VALUES ('Ponte Mammolo','Spagna','Rome','Non-disabled Traveler',1,'MB, MA','Termini',13,32.5,76,17.105263157894736,'f'),('Kifissia','Koropi','Athens','Non-disabled Traveler',2,'L1, L2, L3','Omonia, Syntagma',32,80,66,48.484848484848484,'f'),('Jonio','Anagnina','Rome','Non-disabled Traveler',2,'MB1, MB, MA','Bologna, Termini',23,57.5,76,30.263157894736842,'f'),('Cassina de\' Pecchi','Duomo','Milan','Non-disabled Traveler',1,'M2, M1','Loreto',17,42.5,124,13.709677419354838,'f'),('Ponte Mammolo','Rebibbia','Rome','Non-disabled Traveler',0,'Non presenti','Non presenti',2,5,76,2.631578947368421,'f'),('Ponte Mammolo','Rebibbia','Rome','Non-disabled Traveler',0,'Non presenti','Non presenti',2,5,76,2.631578947368421,'f'),('Ponte Mammolo','Pigneto','Rome','Non-disabled Traveler',2,'MB, MA, MC','Termini, San Giovanni',15,37.5,76,19.736842105263158,'f'),('Cassina de\' Pecchi','Duomo','Rome','Non-disabled Traveler',0,'Non presenti','Non presenti',1,2.5,76,1.3157894736842104,'f'),('Cassina de\' Pecchi','Duomo','Rome','Non-disabled Traveler',0,'Non presenti','Non presenti',1,2.5,76,1.3157894736842104,'f'),('Ponte Mammolo','Pigneto','Rome','Non-disabled Traveler',2,'MB, MA, MC','Termini, San Giovanni',15,37.5,76,19.736842105263158,'f'),('Cassina de\' Pecchi','Duomo','Milan','Non-disabled Traveler',1,'M2, M1','Loreto',17,42.5,124,13.709677419354838,'f'),('Ponte Mammolo','Pigneto','Rome','Non-disabled Traveler',2,'MB, MA, MC','Termini, San Giovanni',15,37.5,76,19.736842105263158,'f'),('Ponte Mammolo','Pigneto','Rome','Non-disabled Traveler',2,'MB, MA, MC','Termini, San Giovanni',15,37.5,76,19.736842105263158,'f'),('Ponte Mammolo','Pigneto','Rome','Non-disabled Traveler',2,'MB, MA, MC','Termini, San Giovanni',15,37.5,76,19.736842105263158,'f'),('Ponte Mammolo','Pigneto','Rome','Disabled Traveler',2,'MB, MA, MC','Termini, San Giovanni',15,37.5,76,19.736842105263158,'f'),('Ponte Mammolo','Anagnina','Rome','Non-disabled Traveler',1,'MB, MA','Termini',25,62.5,76,32.89473684210527,'f'),('Ponte Mammolo','Anagnina','Rome','Non-disabled Traveler',1,'MB, MA','Termini',25,62.5,76,32.89473684210527,'f'),('Ponte Mammolo','Anagnina','Rome','Non-disabled Traveler',1,'MB, MA','Termini',25,62.5,76,32.89473684210527,'f');
/*!40000 ALTER TABLE `pathinfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rome`
--

DROP TABLE IF EXISTS `rome`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rome` (
  `id` int NOT NULL,
  `nome` varchar(50) DEFAULT NULL,
  `disabile` varchar(3) DEFAULT NULL,
  `linea` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rome`
--

LOCK TABLES `rome` WRITE;
/*!40000 ALTER TABLE `rome` DISABLE KEYS */;
INSERT INTO `rome` VALUES (0,'Rebibbia','no','MB'),(1,'Ponte Mammolo','si','MB'),(2,'Santa Maria del Soccorso','si','MB'),(3,'Pietralata','si','MB'),(4,'Monti Tiburtini','si','MB'),(5,'Quintiliani','si','MB'),(6,'Tiburtina FS','si','MB'),(7,'Bologna','si','MB-MB1'),(8,'Annibaliano','si','MB1'),(9,'Libia','si','MB1'),(10,'Conca D\'oro','si','MB1'),(11,'Jonio','si','MB1'),(12,'Policlinico','si','MB'),(13,'Castro Pretorio','si','MB'),(14,'Termini','si','MA-MB'),(15,'Cavour','no','MB'),(16,'Colosseo','si','MB'),(17,'Circo Massimo','si','MB'),(18,'Piramide','no','MB'),(19,'Garbatella','no','MB'),(20,'Basilica S. Paolo','si','MB'),(21,'Marconi','si','MB'),(22,'EUR Magliana','no','MB'),(23,'EUR Palasport','si','MB'),(24,'EUR Fermi','no','MB'),(25,'Laurentina','si','MB'),(26,'Anagnina','si','MA'),(27,'Cinecitta','si','MA'),(28,'Subaugusta','si','MA'),(29,'Giulio Agricola','si','MA'),(30,'Lucio Sestio','si','MA'),(31,'Numidio Quadrato','si','MA'),(32,'Porta Furba','no','MA'),(33,'Arco di Travertino','no','MA'),(34,'Colli Albani','si','MA'),(35,'Furio Camillo','si','MA'),(36,'Ponte Lungo','si','MA'),(37,'Re di Roma','si','MA'),(38,'San Giovanni','si','MA-MC'),(39,'Lodi','si','MC'),(40,'Pigneto','si','MC'),(41,'Malatesta','si','MC'),(42,'Teano','si','MC'),(43,'Gardenie','si','MC'),(44,'Mirti','si','MC'),(45,'Parco di Centocelle','si','MC'),(46,'Togliatti','si','MC'),(47,'Grano','si','MC'),(48,'Alessandrino','si','MC'),(49,'Torre Spaccata','si','MC'),(50,'Torre Maura','si','MC'),(51,'Tobagi','si','MC'),(52,'Giardinetti','si','MC'),(53,'Torrenova','si','MC'),(54,'Torre Angela','si','MC'),(55,'Torre Gaia','si','MC'),(56,'Grotte Celoni','si','MC'),(57,'Due Leoni - Fontana Candida','no','MC'),(58,'Borghesiana','no','MC'),(59,'Bolognetta','si','MC'),(60,'Finocchio','si','MC'),(61,'Graniti','si','MC'),(62,'Pantano','si','MC'),(63,'Manzoni','si','MA'),(64,'Vittorio Emanuele','no','MA'),(65,'Repubblica','si','MA'),(66,'Barberini','si','MA'),(67,'Spagna','si','MA'),(68,'Flaminio','si','MA'),(69,'Lepanto','si','MA'),(70,'Ottaviano','si','MA'),(71,'Cipro','si','MA'),(72,'Valle Aurelia','si','MA'),(73,'Baldo degli Ubaldi','si','MA'),(74,'Cornelia','si','MA'),(75,'Battistini','si','MA');
/*!40000 ALTER TABLE `rome` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stockholm`
--

DROP TABLE IF EXISTS `stockholm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stockholm` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) DEFAULT NULL,
  `disabile` varchar(100) DEFAULT NULL,
  `linea` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=205 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stockholm`
--

LOCK TABLES `stockholm` WRITE;
/*!40000 ALTER TABLE `stockholm` DISABLE KEYS */;
INSERT INTO `stockholm` VALUES (1,'Danderyds Sjukhus','no','R'),(2,'Bergshamra','no','R'),(3,'Universitetet','no','R'),(4,'Tekniska Hogskolan','no','R'),(5,'Stadion','no','R'),(6,'Ostermalmstorg','no','R'),(7,'Ropsten','no','R'),(8,'Gardet','no','R'),(9,'Karlaplan','no','R'),(10,'T-Centralen','si','R-Bi-V'),(11,'Gamla Stan','si','R-V'),(12,'Kungstradgarden','no','Bi'),(13,'Slussen','si','R-V'),(14,'Medborgarplatsen','no','V'),(15,'Skanstull','no','V'),(16,'Gullmarsplan','si','V'),(17,'Skarmarbrink','no','V'),(18,'Globen','no','V'),(19,'Blasut','no','V'),(20,'Hammarbyhojden','no','V'),(21,'Bjorkhagen','no','V'),(22,'Karrtorp','no','V'),(23,'Bagarmossen','no','V'),(24,'Skarpnack','no','V'),(25,'Sandsborg','no','V'),(26,'Skogskyrko Garden','no','V'),(27,'Tallkgrogen','no','V'),(28,'Gubbangen','no','V'),(29,'Hokarangen','no','V'),(30,'Farsta','no','V'),(31,'Farsta Strand','no','V'),(32,'Enskede Gard','no','V'),(33,'Sockenplan','no','V'),(34,'Svedmyra','no','V'),(35,'Stureby','no','V'),(36,'Bandhagen','no','V'),(37,'Hogdalen','no','V'),(38,'Ragsved','no','V'),(39,'Hagsatra','no','V'),(40,'Mariatorget','no','R'),(41,'Zinkensdamm','no','R'),(42,'Hornstull','no','R'),(43,'Liljeholmen','no','R'),(44,'Aspudden','no','R'),(45,'Midsommar Kransen','no','R'),(46,'Telefonplan','no','R'),(47,'Hagerstensasen','no','R'),(48,'Vastertorp','no','R'),(49,'Fruangen','no','R'),(50,'Ornsberg','no','R'),(51,'Axels Berg','no','R'),(52,'Malar Hojden','no','R'),(53,'Bredang','no','R'),(54,'Satra','no','R'),(55,'Skarholmen','no','R'),(56,'Varberg','no','R'),(57,'Varby Gard','no','R'),(58,'Masmo','no','R'),(59,'Fittja','no','R'),(60,'Alby','no','R'),(61,'Hallunda','no','R'),(62,'Norsborg','no','R'),(63,'Hotorget','no','V'),(64,'Radmansgatan','no','V'),(65,'Odenplan','si','V'),(66,'Sankt Eriksplan','no','V'),(67,'Fridhemsplan','si','V-Bi'),(68,'Thorildsplan','no','V'),(69,'Rad Huset','no','Bi'),(70,'Kristine Berg','no','V'),(71,'Alvik','no','V'),(72,'Storamossen','no','V'),(73,'Abrahamsberg','no','V'),(74,'Brommaplan','no','V'),(75,'Akeshov','no','V'),(76,'Angbyplan','no','V'),(77,'Islandstorget','no','V'),(78,'Blackeberg','no','V'),(79,'Racksta','no','V'),(80,'Vallingby','si','V'),(81,'Johannelund','no','V'),(82,'Hasselby Gard','no','V'),(83,'Hasselby Strand','no','V'),(84,'Stadshagen','no','Bi'),(85,'Vastra Skogen','no','Bi'),(86,'Solna Centrum','si','Bi'),(87,'Huvudsta','no','Bi'),(88,'Solna Strand','no','Bi'),(89,'Sundbybergs Centrum','si','Bi'),(90,'Duvbo','no','Bi'),(91,'Rissne','no','Bi'),(92,'Rinkeby','no','Bi'),(93,'Tensta','no','Bi'),(94,'Hjulsta','no','Bi'),(95,'Nackrosen','no','Bi'),(96,'Hallonbergen','no','Bi'),(97,'Kymlinge','no','Bi'),(98,'Kista','no','Bi'),(99,'Husby','no','Bi'),(100,'Akalla','si','Bi'),(204,'Morby Centrum','no','R');
/*!40000 ALTER TABLE `stockholm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `CODICEFISCALE` varchar(16) NOT NULL,
  `NOME` varchar(100) NOT NULL,
  `COGNOME` varchar(100) NOT NULL,
  `DATADINASCITA` date NOT NULL,
  `EMAIL` varchar(100) NOT NULL,
  `PASSWORD` varchar(100) NOT NULL,
  `DISABILE` tinyint(1) DEFAULT NULL,
  `PERMESSO` int NOT NULL,
  PRIMARY KEY (`CODICEFISCALE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('f','f','f','2025-07-12','dsadsa@d','f',0,1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'RouteX_Update'
--
/*!50003 DROP PROCEDURE IF EXISTS `getAthens` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE  PROCEDURE `getAthens`(
    IN startstation VARCHAR(100),
    IN endstation VARCHAR(100)
)
BEGIN
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    SET TRANSACTION READ ONLY;

    
    START TRANSACTION;

    
    SELECT r.id 
    FROM routex.athens r 
    WHERE r.nome = startstation;

    
    SELECT r.id 
    FROM routex.athens r 
    WHERE r.nome = endstation;

    
    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getBudapest` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE  PROCEDURE `getBudapest`(
    IN startstation VARCHAR(100),
    IN endstation VARCHAR(100)
)
BEGIN
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    SET TRANSACTION READ ONLY;

    
    START TRANSACTION;

    
    SELECT r.id 
    FROM routex.budapest r 
    WHERE r.nome = startstation;

    
    SELECT r.id 
    FROM routex.budapest r 
    WHERE r.nome = endstation;

    
    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getMilan` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE  PROCEDURE `getMilan`(
    IN startstation VARCHAR(100),
    IN endstation VARCHAR(100)
)
BEGIN
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    SET TRANSACTION READ ONLY;

    
    START TRANSACTION;

    
    SELECT r.id 
    FROM routex.milan r 
    WHERE r.nome = startstation;

    
    SELECT r.id 
    FROM routex.milan r 
    WHERE r.nome = endstation;

    
    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getNaples` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE  PROCEDURE `getNaples`(
    IN startstation VARCHAR(100),
    IN endstation VARCHAR(100)
)
BEGIN
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    SET TRANSACTION READ ONLY;

    
    START TRANSACTION;

    
    SELECT n.id 
    FROM routex.naples n 
    WHERE n.nome = startstation;

    
    SELECT n.id 
    FROM routex.naples n 
    WHERE n.nome = endstation;

    
    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getPath` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE  PROCEDURE `getPath`(
    IN p_Utente VARCHAR(16)
)
BEGIN
    SELECT 
        StartStation,
        EndStation,
        City,
        TipoViaggiatore,
        NCambi,
        ListaCambi,
        StazioneDiInterscambio,
        NStazioniAttraversate,
        TempoDiArrivo,
        NStazioniCitta,
        PercTerrenoUtilizzato,
        Utente
    FROM pathinfo
    WHERE Utente = p_Utente;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getRome` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE  PROCEDURE `getRome`(
    IN startstation VARCHAR(100),
    IN endstation VARCHAR(100)
)
BEGIN
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    SET TRANSACTION READ ONLY;

    
    START TRANSACTION;

    
    SELECT r.id 
    FROM routex.rome r 
    WHERE r.nome = startstation;

    
    SELECT r.id 
    FROM routex.rome r 
    WHERE r.nome = endstation;

    
    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getStockholm` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE  PROCEDURE `getStockholm`(
    IN startstation VARCHAR(100),
    IN endstation VARCHAR(100)
)
BEGIN
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    SET TRANSACTION READ ONLY;

    
    START TRANSACTION;

    
    SELECT r.id 
    FROM routex.stockholm r 
    WHERE r.nome = startstation;

    
    SELECT r.id 
    FROM routex.stockholm r 
    WHERE r.nome = endstation;

    
    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `login_user` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE  PROCEDURE `login_user`(
    IN p_email VARCHAR(100),
    IN p_password VARCHAR(100)
)
BEGIN
    SELECT 
        u.permesso        AS p_ruolo,
        u.codicefiscale   AS p_codiceFiscale,
        u.nome            AS p_nome,
        u.cognome         AS p_cognome,
        u.datadinascita   AS p_dataDiNascita,
        u.disabile        AS p_disabile
    FROM 
        user u
    WHERE 
        u.email = p_email AND u.password = p_password
    LIMIT 1;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `register_user` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE  PROCEDURE `register_user`(
    IN p_nome VARCHAR(100),
    IN p_cognome VARCHAR(100),
    IN p_codice_fiscale VARCHAR(16),
    IN p_password VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_data_nascita DATE,
    IN p_disabile INT,
    IN p_permesso INT
)
BEGIN
    INSERT INTO routex.user (
        nome,
        cognome,
        codicefiscale,
        password,
        email,
        datadinascita,
        disabile,
        permesso
    ) VALUES (
        p_nome,
        p_cognome,
        p_codice_fiscale,
        p_password,
        p_email,
        p_data_nascita,
        p_disabile,
        p_permesso
    );
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `RestituisciStazioni` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE  PROCEDURE `RestituisciStazioni`(
    IN startstation VARCHAR(100),
    IN endstation VARCHAR(100),
    IN city VARCHAR(100)
)
BEGIN
    
    IF city = 'Rome' THEN
        CALL getRome(startstation, endstation);
    END IF;

    IF city = 'Naples' THEN
        CALL getNaples(startstation, endstation);
    END IF;

    IF city = 'Milan' THEN
        CALL getMilan(startstation, endstation);
    END IF;

    IF city = 'Athens' THEN
        CALL getAthens(startstation, endstation);
    END IF;

    IF city = 'Budapest' THEN
        CALL getBudapest(startstation, endstation);
    END IF;

    IF city = 'London' THEN
        CALL getLondon(startstation, endstation);
    END IF;

    IF city = 'Stockholm' THEN
        CALL getStockholm(startstation, endstation);
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `saveRoute` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE  PROCEDURE `saveRoute`(
    IN p_StartStation VARCHAR(100),
    IN p_EndStation VARCHAR(100),
    IN p_City VARCHAR(100),
    IN p_TipoViaggiatore VARCHAR(100),
    IN p_NCambi INT,
    IN p_ListaCambi VARCHAR(200),
    IN p_StazioneDiInterscambio VARCHAR(100),
    IN p_NStazioniAttraversate int,
    IN p_TempoDiArrivo DOUBLE,
    IN p_NStazioniCitta INT,
    IN p_PercTerrenoUtilizzato DOUBLE,
    IN p_Utente VARCHAR(16)
)
BEGIN
    INSERT INTO pathinfo (
        StartStation,
        EndStation,
        City,
        TipoViaggiatore,
        NCambi,
        ListaCambi,
        StazioneDiInterscambio,
        NStazioniAttraversate,
        TempoDiArrivo,
        NStazioniCitta,
        PercTerrenoUtilizzato,
        Utente
    ) VALUES (
        p_StartStation,
        p_EndStation,
        p_City,
        p_TipoViaggiatore,
        p_NCambi,
        p_ListaCambi,
        p_StazioneDiInterscambio,
        p_NStazioniAttraversate,
        p_TempoDiArrivo,
        p_NStazioniCitta,
        p_PercTerrenoUtilizzato,
        p_Utente
    );
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VerificaStazioni` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE  PROCEDURE `VerificaStazioni`(
    IN p_city VARCHAR(100),
    IN p_startstation VARCHAR(100),
    IN p_endstation VARCHAR(100)
)
BEGIN
    DECLARE temp_start INT DEFAULT 0;
    DECLARE temp_end INT DEFAULT 0;

    IF p_city = 'Rome' THEN
        SELECT COUNT(*) INTO temp_start FROM Rome WHERE nome = p_startstation;
        SELECT COUNT(*) INTO temp_end FROM Rome WHERE nome = p_endstation;
    ELSEIF p_city = 'Naples' THEN
        SELECT COUNT(*) INTO temp_start FROM Naples WHERE nome = p_startstation;
        SELECT COUNT(*) INTO temp_end FROM Naples WHERE nome = p_endstation;
    ELSEIF p_city = 'Milan' THEN
        SELECT COUNT(*) INTO temp_start FROM Milan WHERE nome = p_startstation;
        SELECT COUNT(*) INTO temp_end FROM Milan WHERE nome = p_endstation;
    ELSEIF p_city = 'Athens' THEN
        SELECT COUNT(*) INTO temp_start FROM Athens WHERE nome = p_startstation;
        SELECT COUNT(*) INTO temp_end FROM Athens WHERE nome = p_endstation;
    ELSEIF p_city = 'Budapest' THEN
        SELECT COUNT(*) INTO temp_start FROM Budapest WHERE nome = p_startstation;
        SELECT COUNT(*) INTO temp_end FROM Budapest WHERE nome = p_endstation;
    ELSEIF p_city = 'Stockholm' THEN
        SELECT COUNT(*) INTO temp_start FROM Stockholm WHERE nome = p_startstation;
        SELECT COUNT(*) INTO temp_end FROM Stockholm WHERE nome = p_endstation;
    END IF;

    SELECT 
        temp_start > 0 AS p_startExists,
        temp_end > 0 AS p_endExists;
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

-- Dump completed on 2025-07-27 22:15:12
