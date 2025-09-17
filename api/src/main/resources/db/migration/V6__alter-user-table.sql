 ALTER TABLE `user`
  DROP COLUMN `password_salt`,
MODIFY `password` VARCHAR(64) NOT NULL;
