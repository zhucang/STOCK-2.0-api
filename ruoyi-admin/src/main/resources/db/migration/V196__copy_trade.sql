CREATE TABLE IF NOT EXISTS `copy_trade_trader` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '交易员用户ID',
  `trader_title` varchar(128) DEFAULT NULL COMMENT '交易员标题',
  `trader_desc` varchar(500) DEFAULT NULL COMMENT '交易员简介',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态 0启用 1停用',
  `min_follow_amount` decimal(18,6) DEFAULT NULL COMMENT '最小跟单金额',
  `max_follow_amount` decimal(18,6) DEFAULT NULL COMMENT '最大跟单金额',
  `max_follower_count` int DEFAULT NULL COMMENT '最大跟单人数',
  `default_follow_mode` tinyint DEFAULT 1 COMMENT '默认跟单模式 0固定金额 1按比例',
  `default_follow_ratio` decimal(18,6) DEFAULT 1.000000 COMMENT '默认跟单比例',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_copy_trade_trader_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='跟单交易员';

CREATE TABLE IF NOT EXISTS `copy_trade_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `trader_id` bigint NOT NULL COMMENT '交易员主键ID',
  `trader_user_id` bigint NOT NULL COMMENT '交易员用户ID',
  `follower_user_id` bigint NOT NULL COMMENT '跟单用户ID',
  `follow_mode` tinyint NOT NULL DEFAULT 1 COMMENT '跟单模式 0固定金额 1按比例',
  `follow_amount` decimal(18,6) DEFAULT NULL COMMENT '固定跟单金额',
  `follow_ratio` decimal(18,6) DEFAULT 1.000000 COMMENT '跟单比例',
  `max_open_orders` int DEFAULT 10 COMMENT '最大同时持仓数',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态 0跟单中 1已停止',
  `last_follow_time` datetime DEFAULT NULL COMMENT '最近跟单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_copy_trade_relation_unique` (`trader_user_id`,`follower_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='跟单关系';

CREATE TABLE IF NOT EXISTS `copy_trade_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `relation_id` bigint NOT NULL COMMENT '跟单关系ID',
  `trader_user_id` bigint NOT NULL COMMENT '交易员用户ID',
  `product_type` tinyint NOT NULL DEFAULT 2 COMMENT '产品类型 1股票 2加密货币 3期货 4外汇',
  `leader_position_id` bigint NOT NULL COMMENT '主单持仓ID',
  `product_code` varchar(50) DEFAULT NULL COMMENT '主单产品代码快照',
  `order_direction` tinyint DEFAULT NULL COMMENT '主单开仓方向快照 0买涨 1买跌',
  `buy_order_price` decimal(22,6) DEFAULT NULL COMMENT '主单开仓价格快照',
  `follower_user_id` bigint NOT NULL COMMENT '跟单用户ID',
  `follower_position_id` bigint NOT NULL COMMENT '跟单持仓ID',
  `follow_mode` tinyint DEFAULT NULL COMMENT '跟单模式快照 0固定金额 1按比例',
  `follow_amount` decimal(22,6) DEFAULT NULL COMMENT '固定跟单金额快照',
  `follow_ratio` decimal(18,6) DEFAULT NULL COMMENT '跟单比例快照',
  `margin_amount` decimal(22,6) DEFAULT NULL COMMENT '跟单实际使用保证金快照',
  `order_lever` int DEFAULT NULL COMMENT '跟单实际使用杠杆快照',
  `leader_order_code` varchar(200) DEFAULT NULL COMMENT '主单订单号快照',
  `follower_order_code` varchar(200) DEFAULT NULL COMMENT '跟单订单号快照',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态 0持仓中 1已结束',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_copy_trade_order_leader` (`product_type`,`leader_position_id`,`status`),
  KEY `idx_copy_trade_order_relation` (`relation_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='跟单订单映射';

INSERT IGNORE INTO `lang_mgr`
(`lang_key`, `zh`, `en`, `tc`, `de`, `es`, `fr`, `idn`, `jp`, `ko`, `my`, `th`, `vi`, `pt`, `rus`, `blr`, `ida`, `sa`, `ar`, `it`, `remark`)
VALUES
('hint_copyTradeSelectTrader', '请选择要跟随的交易员', 'Please select a trader to follow', '請選擇要跟隨的交易員', 'Bitte wählen Sie einen Trader zum Folgen aus', 'Seleccione un trader para seguir', 'Veuillez sélectionner un trader à suivre', 'Silakan pilih trader untuk diikuti', 'フォローするトレーダーを選択してください', '팔로우할 트레이더를 선택해 주세요', 'Sila pilih pedagang untuk diikuti', 'โปรดเลือกเทรดเดอร์ที่ต้องการติดตาม', 'Vui lòng chọn trader để theo dõi', 'Selecione um trader para seguir', 'Пожалуйста, выберите трейдера для копирования', 'Калі ласка, абярыце трэйдара для капіявання', 'कृपया फॉलो करने के लिए ट्रेडर चुनें', 'يرجى اختيار المتداول الذي تريد متابعته', 'يرجى اختيار المتداول الذي تريد متابعته', 'Seleziona un trader da seguire', '跟单功能');

INSERT IGNORE INTO `lang_mgr`
(`lang_key`, `zh`, `en`, `tc`, `de`, `es`, `fr`, `idn`, `jp`, `ko`, `my`, `th`, `vi`, `pt`, `rus`, `blr`, `ida`, `sa`, `ar`, `it`, `remark`)
VALUES
('hint_copyTradeTraderNotAvailable', '交易员不存在或已停用', 'The trader does not exist or has been disabled', '交易員不存在或已停用', 'Der Trader existiert nicht oder wurde deaktiviert', 'El trader no existe o ha sido deshabilitado', 'Le trader n''existe pas ou a été désactivé', 'Trader tidak ada atau telah dinonaktifkan', 'トレーダーが存在しないか無効になっています', '트레이더가 존재하지 않거나 비활성화되었습니다', 'Pedagang tidak wujud atau telah dinyahdayakan', 'เทรดเดอร์ไม่มีอยู่หรือถูกปิดใช้งานแล้ว', 'Trader không tồn tại hoặc đã bị vô hiệu hóa', 'O trader não existe ou foi desativado', 'Трейдер не существует или отключен', 'Трэйдар не існуе або адключаны', 'ट्रेडर मौजूद नहीं है या अक्षम कर दिया गया है', 'المتداول غير موجود أو تم تعطيله', 'المتداول غير موجود أو تم تعطيله', 'Il trader non esiste o è stato disabilitato', '跟单功能');

INSERT IGNORE INTO `lang_mgr`
(`lang_key`, `zh`, `en`, `tc`, `de`, `es`, `fr`, `idn`, `jp`, `ko`, `my`, `th`, `vi`, `pt`, `rus`, `blr`, `ida`, `sa`, `ar`, `it`, `remark`)
VALUES
('hint_copyTradeCanNotFollowSelf', '不能跟随自己', 'You cannot follow yourself', '不能跟隨自己', 'Sie können sich nicht selbst folgen', 'No puedes seguirte a ti mismo', 'Vous ne pouvez pas vous suivre vous-même', 'Anda tidak dapat mengikuti diri sendiri', '自分自身をフォローすることはできません', '자기 자신을 팔로우할 수 없습니다', 'Anda tidak boleh mengikuti diri sendiri', 'คุณไม่สามารถติดตามตัวเองได้', 'Bạn không thể tự theo dõi chính mình', 'Você não pode seguir a si mesmo', 'Вы не можете копировать сами себя', 'Вы не можаце капіяваць самога сябе', 'आप स्वयं को फॉलो नहीं कर सकते', 'لا يمكنك متابعة نفسك', 'لا يمكنك متابعة نفسك', 'Non puoi seguire te stesso', '跟单功能');

INSERT IGNORE INTO `lang_mgr`
(`lang_key`, `zh`, `en`, `tc`, `de`, `es`, `fr`, `idn`, `jp`, `ko`, `my`, `th`, `vi`, `pt`, `rus`, `blr`, `ida`, `sa`, `ar`, `it`, `remark`)
VALUES
('hint_copyTradeAlreadyFollowing', '您已经在跟随该交易员', 'You are already following this trader', '您已經在跟隨該交易員', 'Sie folgen diesem Trader bereits', 'Ya estás siguiendo a este trader', 'Vous suivez déjà ce trader', 'Anda sudah mengikuti trader ini', 'すでにこのトレーダーをフォローしています', '이미 이 트레이더를 팔로우하고 있습니다', 'Anda sudah mengikuti pedagang ini', 'คุณกำลังติดตามเทรดเดอร์รายนี้อยู่แล้ว', 'Bạn đã theo dõi trader này rồi', 'Você já está seguindo este trader', 'Вы уже копируете этого трейдера', 'Вы ўжо капіруеце гэтага трэйдара', 'आप पहले से ही इस ट्रेडर को फॉलो कर रहे हैं', 'أنت تتابع هذا المتداول بالفعل', 'أنت تتابع هذا المتداول بالفعل', 'Stai già seguendo questo trader', '跟单功能');

INSERT IGNORE INTO `lang_mgr`
(`lang_key`, `zh`, `en`, `tc`, `de`, `es`, `fr`, `idn`, `jp`, `ko`, `my`, `th`, `vi`, `pt`, `rus`, `blr`, `ida`, `sa`, `ar`, `it`, `remark`)
VALUES
('hint_copyTradeFollowerLimitReached', '该交易员跟单名额已满', 'This trader has reached the follower limit', '該交易員跟單名額已滿', 'Dieser Trader hat die maximale Anzahl an Followern erreicht', 'Este trader alcanzó el límite de seguidores', 'Ce trader a atteint la limite de suiveurs', 'Trader ini telah mencapai batas pengikut', 'このトレーダーのフォロワー枠は満員です', '이 트레이더의 팔로워 한도가 가득 찼습니다', 'Pedagang ini telah mencapai had pengikut', 'เทรดเดอร์รายนี้มีผู้ติดตามครบตามจำนวนแล้ว', 'Trader này đã đạt giới hạn người theo dõi', 'Este trader atingiu o limite de seguidores', 'Этот трейдер достиг лимита подписчиков', 'Гэты трэйдар дасягнуў ліміту падпісчыкаў', 'इस ट्रेडर की फॉलोअर सीमा पूरी हो चुकी है', 'لقد وصل هذا المتداول إلى الحد الأقصى من المتابعين', 'لقد وصل هذا المتداول إلى الحد الأقصى من المتابعين', 'Questo trader ha raggiunto il limite di follower', '跟单功能');

INSERT IGNORE INTO `lang_mgr`
(`lang_key`, `zh`, `en`, `tc`, `de`, `es`, `fr`, `idn`, `jp`, `ko`, `my`, `th`, `vi`, `pt`, `rus`, `blr`, `ida`, `sa`, `ar`, `it`, `remark`)
VALUES
('hint_copyTradeModeError', '跟单模式错误', 'Invalid copy trade mode', '跟單模式錯誤', 'Ungültiger Copy-Trading-Modus', 'Modo de copy trading no válido', 'Mode de copy trading invalide', 'Mode copy trade tidak valid', 'コピートレードモードが無効です', '잘못된 카피트레이드 모드입니다', 'Mod copy trade tidak sah', 'โหมดคัดลอกการเทรดไม่ถูกต้อง', 'Chế độ copy trade không hợp lệ', 'Modo de copy trade inválido', 'Неверный режим копитрейдинга', 'Няправільны рэжым капітрэйдынгу', 'अमान्य कॉपी ट्रेड मोड', 'وضع نسخ التداول غير صالح', 'وضع نسخ التداول غير صالح', 'Modalità di copy trade non valida', '跟单功能');

INSERT IGNORE INTO `lang_mgr`
(`lang_key`, `zh`, `en`, `tc`, `de`, `es`, `fr`, `idn`, `jp`, `ko`, `my`, `th`, `vi`, `pt`, `rus`, `blr`, `ida`, `sa`, `ar`, `it`, `remark`)
VALUES
('hint_copyTradeInputFollowAmount', '请输入固定跟单金额', 'Please enter a fixed copy trade amount', '請輸入固定跟單金額', 'Bitte geben Sie einen festen Copy-Trading-Betrag ein', 'Ingrese un monto fijo para el copy trade', 'Veuillez saisir un montant fixe pour le copy trading', 'Silakan masukkan jumlah tetap copy trade', '固定のコピートレード金額を入力してください', '고정 카피 트레이드 금액을 입력해 주세요', 'Sila masukkan jumlah tetap copy trade', 'โปรดกรอกจำนวนเงินคัดลอกการเทรดแบบคงที่', 'Vui lòng nhập số tiền copy trade cố định', 'Digite um valor fixo para copy trade', 'Пожалуйста, введите фиксированную сумму копитрейдинга', 'Калі ласка, увядзіце фіксаваную суму капітрэйдынгу', 'कृपया निश्चित कॉपी ट्रेड राशि दर्ज करें', 'يرجى إدخال مبلغ ثابت لنسخ التداول', 'يرجى إدخال مبلغ ثابت لنسخ التداول', 'Inserisci un importo fisso per il copy trade', '跟单功能');

INSERT IGNORE INTO `lang_mgr`
(`lang_key`, `zh`, `en`, `tc`, `de`, `es`, `fr`, `idn`, `jp`, `ko`, `my`, `th`, `vi`, `pt`, `rus`, `blr`, `ida`, `sa`, `ar`, `it`, `remark`)
VALUES
('hint_copyTradeAmountLtMin', '跟单金额不能低于交易员要求的最小金额', 'The copy trade amount cannot be lower than the trader''s minimum amount', '跟單金額不能低於交易員要求的最小金額', 'Der Copy-Trading-Betrag darf nicht unter dem Mindestbetrag des Traders liegen', 'El monto del copy trade no puede ser menor que el mínimo requerido por el trader', 'Le montant du copy trading ne peut pas être inférieur au minimum requis par le trader', 'Jumlah copy trade tidak boleh lebih kecil dari jumlah minimum trader', 'コピートレード金額はトレーダーが要求する最小金額を下回ることはできません', '카피 트레이드 금액은 트레이더가 요구한 최소 금액보다 작을 수 없습니다', 'Jumlah copy trade tidak boleh kurang daripada jumlah minimum pedagang', 'จำนวนเงินคัดลอกการเทรดต้องไม่น้อยกว่าขั้นต่ำที่เทรดเดอร์กำหนด', 'Số tiền copy trade không được thấp hơn mức tối thiểu trader yêu cầu', 'O valor do copy trade não pode ser inferior ao valor mínimo exigido pelo trader', 'Сумма копитрейдинга не может быть меньше минимальной суммы трейдера', 'Сума капітрэйдынгу не можа быць меншай за мінімальную суму трэйдара', 'कॉपी ट्रेड राशि ट्रेडर द्वारा निर्धारित न्यूनतम राशि से कम नहीं हो सकती', 'لا يمكن أن يكون مبلغ نسخ التداول أقل من الحد الأدنى الذي حدده المتداول', 'لا يمكن أن يكون مبلغ نسخ التداول أقل من الحد الأدنى الذي حدده المتداول', 'L''importo del copy trade non può essere inferiore al minimo richiesto dal trader', '跟单功能');

INSERT IGNORE INTO `lang_mgr`
(`lang_key`, `zh`, `en`, `tc`, `de`, `es`, `fr`, `idn`, `jp`, `ko`, `my`, `th`, `vi`, `pt`, `rus`, `blr`, `ida`, `sa`, `ar`, `it`, `remark`)
VALUES
('hint_copyTradeAmountGtMax', '跟单金额不能高于交易员要求的最大金额', 'The copy trade amount cannot exceed the trader''s maximum amount', '跟單金額不能高於交易員要求的最大金額', 'Der Copy-Trading-Betrag darf den Höchstbetrag des Traders nicht überschreiten', 'El monto del copy trade no puede superar el máximo requerido por el trader', 'Le montant du copy trading ne peut pas dépasser le maximum requis par le trader', 'Jumlah copy trade tidak boleh melebihi jumlah maksimum trader', 'コピートレード金額はトレーダーが要求する最大金額を超えることはできません', '카피 트레이드 금액은 트레이더가 요구한 최대 금액을 초과할 수 없습니다', 'Jumlah copy trade tidak boleh melebihi jumlah maksimum pedagang', 'จำนวนเงินคัดลอกการเทรดต้องไม่เกินจำนวนสูงสุดที่เทรดเดอร์กำหนด', 'Số tiền copy trade không được vượt quá mức tối đa trader yêu cầu', 'O valor do copy trade não pode exceder o valor máximo exigido pelo trader', 'Сумма копитрейдинга не может превышать максимальную сумму трейдера', 'Сума капітрэйдынгу не можа перавышаць максімальную суму трэйдара', 'कॉपी ट्रेड राशि ट्रेडर द्वारा निर्धारित अधिकतम राशि से अधिक नहीं हो सकती', 'لا يمكن أن يتجاوز مبلغ نسخ التداول الحد الأقصى الذي حدده المتداول', 'لا يمكن أن يتجاوز مبلغ نسخ التداول الحد الأقصى الذي حدده المتداول', 'L''importo del copy trade non può superare il massimo richiesto dal trader', '跟单功能');

INSERT IGNORE INTO `lang_mgr`
(`lang_key`, `zh`, `en`, `tc`, `de`, `es`, `fr`, `idn`, `jp`, `ko`, `my`, `th`, `vi`, `pt`, `rus`, `blr`, `ida`, `sa`, `ar`, `it`, `remark`)
VALUES
('hint_copyTradeSelectRelation', '请选择要停止的跟单关系', 'Please select the copy trade relation to stop', '請選擇要停止的跟單關係', 'Bitte wählen Sie die zu stoppende Copy-Trading-Beziehung aus', 'Seleccione la relación de copy trade que desea detener', 'Veuillez sélectionner la relation de copy trading à arrêter', 'Silakan pilih relasi copy trade yang ingin dihentikan', '停止するコピートレード関係を選択してください', '중지할 카피 트레이드 관계를 선택해 주세요', 'Sila pilih hubungan copy trade yang ingin dihentikan', 'โปรดเลือกความสัมพันธ์การคัดลอกการเทรดที่ต้องการหยุด', 'Vui lòng chọn quan hệ copy trade cần dừng', 'Selecione a relação de copy trade que deseja interromper', 'Пожалуйста, выберите связь копитрейдинга для остановки', 'Калі ласка, абярыце сувязь капітрэйдынгу для спынення', 'कृपया रोकने के लिए कॉपी ट्रेड संबंध चुनें', 'يرجى اختيار علاقة نسخ التداول التي تريد إيقافها', 'يرجى اختيار علاقة نسخ التداول التي تريد إيقافها', 'Seleziona la relazione di copy trade da interrompere', '跟单功能');

INSERT IGNORE INTO `lang_mgr`
(`lang_key`, `zh`, `en`, `tc`, `de`, `es`, `fr`, `idn`, `jp`, `ko`, `my`, `th`, `vi`, `pt`, `rus`, `blr`, `ida`, `sa`, `ar`, `it`, `remark`)
VALUES
('hint_copyTradeRelationNotExists', '跟单关系不存在', 'The copy trade relation does not exist', '跟單關係不存在', 'Die Copy-Trading-Beziehung existiert nicht', 'La relación de copy trade no existe', 'La relation de copy trading n''existe pas', 'Relasi copy trade tidak ada', 'コピートレード関係が存在しません', '카피 트레이드 관계가 존재하지 않습니다', 'Hubungan copy trade tidak wujud', 'ไม่มีความสัมพันธ์การคัดลอกการเทรดนี้', 'Quan hệ copy trade không tồn tại', 'A relação de copy trade não existe', 'Связь копитрейдинга не существует', 'Сувязь капітрэйдынгу не існуе', 'कॉपी ट्रेड संबंध मौजूद नहीं है', 'علاقة نسخ التداول غير موجودة', 'علاقة نسخ التداول غير موجودة', 'La relazione di copy trade non esiste', '跟单功能');
