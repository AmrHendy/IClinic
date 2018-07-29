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
CREATE SCHEMA IF NOT EXISTS `IClinic` DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci ;
USE `IClinic` ;

-- -----------------------------------------------------
-- Table `IClinic`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `IClinic`.`User` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userName` VARCHAR(125) NOT NULL,
  `password` BLOB NOT NULL,
  `salt` BLOB NOT NULL,
  `clinic` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `name_index` (`userName` ASC),
  UNIQUE INDEX `userName_UNIQUE` (`userName` ASC),
  UNIQUE INDEX `clinic_UNIQUE` (`clinic` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `IClinic`.`Patient`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `IClinic`.`Patient` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(125) NOT NULL,
  `address` VARCHAR(45) NULL,
  `birthdate` DATE NULL,
  `remainingCost` INT NULL DEFAULT 0,
  `mobile_number` VARCHAR(45) NULL,
  `file_number` VARCHAR(45) NOT NULL,
  `clinic_number` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `name_index` (`name` ASC),
  UNIQUE INDEX `file_id_UNIQUE` (`file_number` ASC),
  INDEX `clinic_fk_idx` (`clinic_number` ASC),
  CONSTRAINT `clinic_fk`
    FOREIGN KEY (`clinic_number`)
    REFERENCES `IClinic`.`User` (`clinic`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `IClinic`.`Appointment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `IClinic`.`Appointment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `patientId` INT NOT NULL,
  `date` DATETIME NOT NULL,
  `paidCost` INT NOT NULL DEFAULT 0,
  `finished` TINYINT NULL DEFAULT 0,
  `image` LONGBLOB NULL,
  `comment` VARCHAR(125) NULL,
  `confirmed_paid` TINYINT NULL DEFAULT 0,
  `clinic_number` INT NOT NULL,
  INDEX `patient_index` (`patientId` ASC),
  INDEX `date_index` (`date` ASC),
  INDEX `finished_index` (`finished` ASC),
  INDEX `fk_appointment_clinic_number_idx` (`clinic_number` ASC),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  PRIMARY KEY (`date`, `clinic_number`),
  CONSTRAINT `fk_appointment_patient_id`
    FOREIGN KEY (`patientId`)
    REFERENCES `IClinic`.`Patient` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_appointment_clinic_number`
    FOREIGN KEY (`clinic_number`)
    REFERENCES `IClinic`.`User` (`clinic`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
