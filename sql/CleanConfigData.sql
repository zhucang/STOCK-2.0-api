/**
  初始化系统配置数据
 */
TRUNCATE TABLE bonus_config;
TRUNCATE TABLE client_version;
TRUNCATE TABLE cryptocurrency_everyday_record;
TRUNCATE TABLE fast_order_control_config;
-- TRUNCATE TABLE fast_trade_order_options;
TRUNCATE TABLE forex_everyday_record;
TRUNCATE TABLE futures_everyday_record;
TRUNCATE TABLE gen_table;
TRUNCATE TABLE gen_table_column;
TRUNCATE TABLE ip_black_list;
TRUNCATE TABLE new_product_apply_purchase;
TRUNCATE TABLE product_quote_control;
TRUNCATE TABLE stock_everyday_record;
update site_info set ios_download_url='https://www.stock.com/download/stock.mobileconfig',android_download_url ='https://www.stock.com/#/?install=1',android_apk_download_url='',app_url='https://www.stock.com',website_url='https://www.website.com';
update web_menu_config set jump_url='https://www.stock.com/fast/stock' where id = 5;
update web_menu_config set jump_url='https://www.stock.com/h5' where id = 14;
update web_menu_config set jump_url='https://www.stock.com' where id = 18;
update web_menu_config set jump_url='https://www.stock.com' where id = 27;