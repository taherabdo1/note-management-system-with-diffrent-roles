SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `notesDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `notesDB` ;

-- -----------------------------------------------------
-- Table `notesDB`.`user`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `notesDB`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `email` VARCHAR(100) NOT NULL ,
  `password` VARCHAR(100) NOT NULL ,
  `first_name` VARCHAR(50) NULL ,
  `last_name` VARCHAR(50) NULL ,
  `role` VARCHAR(10) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `notesDB`.`note`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `notesDB`.`note` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `description` VARCHAR(500) NOT NULL ,
  `start_date` DATE NOT NULL ,
  `period` FLOAT NOT NULL ,
  `preferred_working_hour_per_day` FLOAT NOT NULL ,
  `user_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_note_user_idx` (`user_id` ASC) ,
  CONSTRAINT `fk_note_user`
    FOREIGN KEY (`user_id` )
    REFERENCES `notesDB`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `notesDB` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
