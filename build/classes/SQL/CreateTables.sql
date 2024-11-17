DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `students`;
DROP TABLE IF EXISTS `departements`;
DROP TABLE IF EXISTS `communes`;


CREATE TABLE IF NOT EXISTS `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(70) NOT NULL,
  `password` varchar(190) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
)

create table `students`(
    id int AUTO_INCREMENT PRIMARY KEY,
    matricule int unique not null,
    nom varchar(20) not null,
    prenom varchar(80) not null,
    date_naissance datetime not null,
    email varchar(50),
    telephone int not null
);

create table departements(
    id int PRIMARY key AUTO_INCREMENT,
    nom varchar(50) unique not null
);

create table communes(
    id int AUTO_INCREMENT PRIMARY key,
    nom varchar(50) unique not null,
    departement_id int,
    FOREIGN key (departement_id) REFERENCES departements(id)
);