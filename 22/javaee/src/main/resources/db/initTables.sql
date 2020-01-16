CREATE TABLE IF NOT EXISTS `Felhasznalo` (`id` int(10) NOT NULL AUTO_INCREMENT, `nev` varchar(150) NOT NULL, PRIMARY KEY (`id`), UNIQUE (`nev`)) ENGINE=MyISAM DEFAULT CHARSET=utf8;
CREATE TABLE IF NOT EXISTS `Egyenleg` (`id` int(10) NOT NULL AUTO_INCREMENT, `felhasznaloId` int(10) NOT NULL, `egyenleg` int(10) NOT NULL, PRIMARY KEY (`id`), FOREIGN KEY (`felhasznaloId`) REFERENCES Felhasznalo(`id`)) ENGINE=MyISAM DEFAULT CHARSET=utf8;
