   DROP TABLE IF EXISTS `report`
CASCADE;

CREATE TABLE `report` (
	        `id` VARCHAR(36)  NOT NULL,
	      `data` BLOB         NOT NULL,
	 `signature` BLOB         NOT NULL,
	 `file_name` VARCHAR(255) NOT NULL,
    `student_id` VARCHAR(36),
	 PRIMARY KEY (`id`)
);

      ALTER TABLE `report`
        ADD CONSTRAINT `reportStudentConstraint`
FOREIGN KEY (`student_id`)
 REFERENCES `student`(`id`)
  ON DELETE CASCADE;