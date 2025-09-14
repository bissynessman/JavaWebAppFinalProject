   DROP TABLE IF EXISTS `tokens`
CASCADE;

CREATE TABLE `tokens` (
	         `id` VARCHAR(36)  NOT NULL,
	      `token` VARCHAR(512) NOT NULL,
	   `username` VARCHAR(32)  NOT NULL,
	`valid_until` DATE         NOT NULL,
	  PRIMARY KEY (`id`)
);
