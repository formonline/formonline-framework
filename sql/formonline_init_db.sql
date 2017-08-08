-- MySQL dump 10.15  Distrib 10.0.33-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: FOL_EXAMPLE
-- ------------------------------------------------------
-- Server version	10.0.33-MariaDB-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE SCHEMA `FOL_EXAMPLE` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;
USE `FOL_EXAMPLE`;
--
-- Table structure for table `config`
--

DROP TABLE IF EXISTS `config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config` (
  `c_id` int(11) NOT NULL AUTO_INCREMENT,
  `c_param` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `c_val` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `c_type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `c_texte` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`c_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `formulaire`
--

DROP TABLE IF EXISTS `formulaire`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `formulaire` (
  `F_ID` int(11) NOT NULL AUTO_INCREMENT,
  `F_TITRE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `F_INTRODUCTION` longtext COLLATE utf8_unicode_ci,
  `F_CONCLUSION` longtext COLLATE utf8_unicode_ci,
  `F_DATECREATION` date DEFAULT NULL,
  `F_DATEDEB` date DEFAULT NULL,
  `F_DATEFIN` date DEFAULT NULL,
  `F_SHOW_PWD` int(1) NOT NULL DEFAULT '0',
  `P_ID_TITRE` int(11) DEFAULT NULL,
  `t_id_mail_on_lock` int(11) DEFAULT '0',
  `t_id_mail_on_create` int(11) DEFAULT '0',
  `F_MAIL_ADM` varchar(255) COLLATE utf8_unicode_ci DEFAULT 'null',
  `P_ID_MAIL` int(11) DEFAULT '0',
  `f_connected` int(1) NOT NULL DEFAULT '0',
  `f_suppr` int(1) NOT NULL DEFAULT '0',
  `t_id_mail_on_delete` int(11) DEFAULT '0',
  `F_AUTHENT` int(1) DEFAULT '0',
  `P_ID_LOGIN` int(11) DEFAULT '0',
  PRIMARY KEY (`F_ID`),
  UNIQUE KEY `PK_FORM` (`F_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `groupe`
--

DROP TABLE IF EXISTS `groupe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupe` (
  `G_ID` int(11) NOT NULL AUTO_INCREMENT,
  `F_ID` int(11) NOT NULL DEFAULT '0',
  `G_TITRE` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `G_TEXTE` longtext COLLATE utf8_unicode_ci,
  `G_TYPE` char(2) COLLATE utf8_unicode_ci DEFAULT 'PU',
  `G_NUM` int(11) DEFAULT '0',
  PRIMARY KEY (`G_ID`),
  UNIQUE KEY `PK_GROUPE` (`G_ID`),
  KEY `FK_COMPOSE2` (`F_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `proposition`
--

DROP TABLE IF EXISTS `proposition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proposition` (
  `P_ID` int(11) NOT NULL AUTO_INCREMENT,
  `Q_ID` int(11) NOT NULL DEFAULT '0',
  `P_TEXTE` longtext COLLATE utf8_unicode_ci,
  `P_NUM` int(11) DEFAULT '0',
  `P_ALERT` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0',
  `P_STAT` int(1) NOT NULL DEFAULT '0',
  `P_INITLOAD` int(1) DEFAULT '0',
  `P_DATE_MAJ` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`P_ID`),
  UNIQUE KEY `PK_PROPOSITION` (`P_ID`),
  KEY `FK_PROPOSE` (`Q_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question` (
  `Q_ID` int(11) NOT NULL AUTO_INCREMENT,
  `G_ID` int(11) NOT NULL DEFAULT '0',
  `Q_TEXTE` longtext COLLATE utf8_unicode_ci,
  `Q_TYPE` char(2) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Q_NUM` int(11) DEFAULT NULL,
  `Q_MANDATORY` int(1) NOT NULL DEFAULT '0',
  `Q_SEARCH` int(1) NOT NULL DEFAULT '0',
  `Q_SIZE` int(11) NOT NULL DEFAULT '255',
  PRIMARY KEY (`Q_ID`),
  UNIQUE KEY `PK_QUESTION` (`Q_ID`),
  KEY `FK_COMPOSE` (`G_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reponse`
--

DROP TABLE IF EXISTS `reponse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reponse` (
  `S_ID` int(11) NOT NULL DEFAULT '0',
  `P_ID` int(11) NOT NULL DEFAULT '0',
  `r_val` int(1) NOT NULL DEFAULT '0',
  `r_text` longtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`S_ID`,`P_ID`,`r_val`),
  UNIQUE KEY `PK_REPONSE` (`S_ID`,`P_ID`,`r_val`),
  KEY `FK_LIEN_82` (`P_ID`),
  KEY `FK_LIEN_23` (`S_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `submitform`
--

DROP TABLE IF EXISTS `submitform`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `submitform` (
  `S_ID` int(11) NOT NULL AUTO_INCREMENT,
  `F_ID` int(11) NOT NULL DEFAULT '0',
  `S_DATE` date DEFAULT NULL,
  `s_pwd` varchar(20) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `s_lock` int(1) NOT NULL DEFAULT '0',
  `s_id_parent` int(11) DEFAULT NULL,
  `s_date_creat` date NOT NULL DEFAULT '0000-00-00',
  `s_login_maj` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`S_ID`),
  UNIQUE KEY `PK_REPONSE` (`S_ID`),
  KEY `FK_LIEN_282` (`F_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `template`
--

DROP TABLE IF EXISTS `template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `template` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_content` longtext COLLATE utf8_unicode_ci,
  `F_ID` int(11) NOT NULL DEFAULT '1',
  `t_text` varchar(255) COLLATE utf8_unicode_ci DEFAULT 't',
  `t_type` varchar(10) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `t_archive` int(1) NOT NULL DEFAULT '0',
  `T_DATE_MAJ` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'FOL_EXAMPLE'
--

--
-- Dumping routines for database 'FOL_EXAMPLE'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-22 14:16:00
