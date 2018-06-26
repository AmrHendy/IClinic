-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema IClient
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema IClient
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `IClient` DEFAULT CHARACTER SET utf8 ;
USE `IClient` ;

-- -----------------------------------------------------
-- Table `IClient`.`Patient`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `IClient`.`Patient` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(125) NULL,
  `image` LONGBLOB NULL,
  `address` VARCHAR(45) NULL,
  `birthdate` DATE NULL,
  `remainingCost` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `name_index` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `IClient`.`Appointment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `IClient`.`Appointment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `patientId` INT NOT NULL,
  `date` DATETIME NOT NULL,
  `paidCost` INT NULL DEFAULT 0,
  `finished` TINYINT NULL DEFAULT 0,
  `image` LONGBLOB NULL,
  `comment` VARCHAR(125) NULL,
  PRIMARY KEY (`id`),
  INDEX `patient_index` (`patientId` ASC),
  INDEX `date_index` (`date` ASC),
  INDEX `finished_index` (`finished` ASC),
  CONSTRAINT `fk_appointment_patient_id`
    FOREIGN KEY (`patientId`)
    REFERENCES `IClient`.`Patient` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `IClient`.`PatientMobile`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `IClient`.`PatientMobile` (
  `id` INT NOT NULL,
  `mobile_number` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`, `mobile_number`),
  CONSTRAINT `fk_patient_mobile_id`
    FOREIGN KEY (`id`)
    REFERENCES `IClient`.`Patient` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `IClient`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `IClient`.`User` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userName` VARCHAR(125) NOT NULL,
  `password` BLOB NOT NULL,
  `salt` BLOB NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `name_index` (`userName` ASC))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
