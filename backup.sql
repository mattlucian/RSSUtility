-- phpMyAdmin SQL Dump
-- version 3.4.11.1
-- http://www.phpmyadmin.net
--
-- Host: mysql3000.mochahost.com
-- Generation Time: Jul 28, 2015 at 03:38 PM
-- Server version: 5.5.42
-- PHP Version: 5.4.31

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `alpin3_rssutility`
--

-- --------------------------------------------------------

--
-- Table structure for table `rssfeed`
--

CREATE TABLE IF NOT EXISTS `rssfeed` (
  `id` mediumint(9) NOT NULL AUTO_INCREMENT,
  `name` char(255) NOT NULL,
  `url` char(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=25 ;

--
-- Dumping data for table `rssfeed`
--

INSERT INTO `rssfeed` (`id`, `name`, `url`) VALUES
(2, 'NY Times Americas', 'http://rss.nytimes.com/services/xml/rss/nyt/Americas.xml'),
(3, 'Digital Reader', 'http://feeds.feedburner.com/labnol?format=xml'),
(9, 'Yahoo Yech News', 'http://news.yahoo.com/rss/tech'),
(15, 'NYTimes Main Feed', 'http://feeds.nytimes.com/nyt/rss/HomePage'),
(24, 'CNN World', 'http://rss.cnn.com/rss/cnn_world.rss');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
