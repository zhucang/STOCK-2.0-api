SET @INDEX_NAME := 'channel_name';
SET @TABLE_NAME := 'recharge_channel_config';
SET @DB_NAME := 'stock';

SELECT COUNT(*) INTO @index_exists
FROM INFORMATION_SCHEMA.STATISTICS
WHERE TABLE_SCHEMA = @DB_NAME
  AND TABLE_NAME = @TABLE_NAME
  AND INDEX_NAME = @INDEX_NAME;

SET @sql := IF(@index_exists > 0,
               CONCAT('ALTER TABLE `', @DB_NAME, '`.`', @TABLE_NAME, '` DROP INDEX `', @INDEX_NAME, '`;'),
               'SELECT "Index does not exist";');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;