   DROP TABLE IF EXISTS `assignment`
CASCADE;

CREATE TABLE `assignment` (
                   `id` VARCHAR(36)  NOT NULL,
    `parent_assignment` VARCHAR(36),
		        `title` VARCHAR(255),
		         `task` TEXT,
              `content` TEXT,
		        `grade` INTEGER      CHECK (`grade` BETWEEN 0 AND 100),
			   `course` VARCHAR(36),
              `student` VARCHAR(36),
            PRIMARY KEY (`id`),

     CONSTRAINT `fk_assignment_parent`
	FOREIGN KEY (`parent_assignment`)
     REFERENCES `assignment`(`id`)
      ON DELETE CASCADE
);

	  ALTER TABLE `assignment`
	    ADD CONSTRAINT `assignmentCourseConstraint`
FOREIGN KEY (`course`)
 REFERENCES `course`(`id`)
  ON DELETE CASCADE,
		ADD CONSTRAINT `assignmentStudentConstraint`
FOREIGN KEY (`student`)
 REFERENCES `student`(`id`)
  ON DELETE CASCADE;
