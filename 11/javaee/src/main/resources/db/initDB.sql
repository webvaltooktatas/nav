CREATE DATABASE IF NOT EXISTS `Bank`;
USE Bank;
CREATE TABLE IF NOT EXISTS `Felhasznalo` (`nev` varchar(150) NOT NULL, PRIMARY KEY (`nev`)) ENGINE=MyISAM DEFAULT CHARSET=utf8;
CREATE TABLE IF NOT EXISTS `Egyenleg` (`nev` varchar(150) NOT NULL, `egyenleg` int(50) NOT NULL, PRIMARY KEY (`nev`)) ENGINE=MyISAM DEFAULT CHARSET=utf8;