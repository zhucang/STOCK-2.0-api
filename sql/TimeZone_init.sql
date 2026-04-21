/**
  初始化时区
 */

-- 美东时区 America/Toronto
-- 北京时区 Asia/Shanghai

set @timeZone = 'America/Toronto';
update `stock`.`other_value` set other_value =  @timeZone where other_key = 'jackson_time_zone';