-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema IClinic
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema IClinic
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `IClinic` DEFAULT CHARACTER SET utf8 ;
USE `IClinic` ;

-- -----------------------------------------------------
-- Table `IClinic`.`Patient`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `IClinic`.`Patient` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(125) NULL,
  `image` LONGBLOB NULL,
  `address` VARCHAR(45) NULL,
  `birthdate` DATE NULL,
  `remainingCost` INT NULL,
  `mobile_number` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `name_index` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `IClinic`.`Appointment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `IClinic`.`Appointment` (
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
    REFERENCES `IClinic`.`Patient` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `IClinic`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `IClinic`.`User` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userName` VARCHAR(125) NOT NULL,
  `password` BLOB NOT NULL,
  `salt` BLOB NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `name_index` (`userName` ASC),
  UNIQUE INDEX `userName_UNIQUE` (`userName` ASC))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
