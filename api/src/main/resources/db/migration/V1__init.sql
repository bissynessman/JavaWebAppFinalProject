   DROP TABLE IF EXISTS `grade`
CASCADE;
   DROP TABLE IF EXISTS `course`
CASCADE;
   DROP TABLE IF EXISTS `professor`
CASCADE;
   DROP TABLE IF EXISTS `student`
CASCADE;
   DROP TABLE IF EXISTS `user`
CASCADE;

CREATE TABLE `course` (
		   `id` VARCHAR(36) NOT NULL,
		 `name` VARCHAR(50) NOT NULL,
		 `ects` INTEGER     NOT NULL,
	`professor` VARCHAR(36),
	PRIMARY KEY (`id`)
);
CREATE TABLE `grade` (
		   `id` VARCHAR(36) NOT NULL,
	   `course` VARCHAR(36),
	  `student` VARCHAR(36),
		`grade` INTEGER     NOT NULL,
	PRIMARY KEY (`id`)
);
CREATE TABLE `professor` (
			`id` VARCHAR(36)           NOT NULL,
	`first_name` VARCHAR(50)           NOT NULL,
	 `last_name` VARCHAR(50)           NOT NULL,
	`authorized` BOOLEAN DEFAULT FALSE NOT NULL,
	 PRIMARY KEY (`id`)
);
CREATE TABLE `student` (
	        `id` VARCHAR(36) NOT NULL,
	     `jmbag` VARCHAR(11) NOT NULL,
	`first_name` VARCHAR(50) NOT NULL,
	 `last_name` VARCHAR(50) NOT NULL,
		 `major` VARCHAR(32) NOT NULL,
	 PRIMARY KEY (`id`)
);
CREATE TABLE `user` (
	       `id` VARCHAR(36)  NOT NULL,
	     `role` VARCHAR(32)  NOT NULL,
	`user_uuid` VARCHAR(36),
	    `email` VARCHAR(40)  NOT NULL,
	 `username` VARCHAR(32)  NOT NULL,
	 `password` VARCHAR(255) NOT NULL,
	PRIMARY KEY (`id`)
);

      ALTER TABLE `course`
        ADD CONSTRAINT `courseProfessorConstraint`
FOREIGN KEY (`professor`)
 REFERENCES `professor`(`id`)
  ON DELETE CASCADE;

      ALTER TABLE `grade`
        ADD CONSTRAINT `gradeCourseConstraint`
FOREIGN KEY (`course`)
 REFERENCES `course`(`id`)
  ON DELETE CASCADE,
        ADD CONSTRAINT `gradeStudentConstraint`
FOREIGN KEY (`student`)
 REFERENCES `student`(`id`)
  ON DELETE CASCADE;