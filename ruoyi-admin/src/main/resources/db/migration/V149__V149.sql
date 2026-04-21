insert into `stock`.`backend_reminder_config` (`id`,`reminder_name`,`reminder_type`,`jump_type`,`jump_url`,`music_source_number`,`search_status`,`status`)
select 7,'理财',1,6,'/finainca/order',1,0,0 from other_value where not exists (select id from backend_reminder_config where id = 7)limit 1;
