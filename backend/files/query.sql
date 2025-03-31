CREATE DATABASE  IF NOT EXISTS `gt_ecommerce`;
USE `gt_ecommerce`;

-- D:\Git\gt-ecom-trading-platform-electronic\gt-backend\files\createDateBase.sql
--
-- Table structure for table `tbl_message`
--

DROP TABLE IF EXISTS `tbl_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `body` longtext,
  `created_at` datetime(6) DEFAULT NULL,
  `is_baned` bit(1) NOT NULL DEFAULT b'0',
  `products` longtext,
  `scan_at` datetime(6) DEFAULT NULL,
  `send_at` datetime(6) DEFAULT NULL,
  `shop_id` bigint DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `to_email` varchar(255) DEFAULT NULL,
  `to_name` varchar(255) DEFAULT NULL,
  `type` varchar(50) NOT NULL DEFAULT 'MESSAGE_PRODUCT',
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_message`
--

LOCK TABLES `tbl_message` WRITE;
/*!40000 ALTER TABLE `tbl_message` DISABLE KEYS */;
INSERT INTO `tbl_message` VALUES (1,'<p class=\"mt-6 text-2xl leading-10\">Chào shop Balo Chuối,</p><p class=\"mt-6 text-2xl leading-10\">Praesent dui ex, dapibus eget mauris ut, finibus vestibulum enim. Quisque9 arcu leo, facilisis in fringilla id, luctus in tortor. Nunc vestibulum est quis orci varius viverra. Curabitur dictum volutpat massa vulputate molestie. In at felis ac velit maximus convallis. </p> <p class=\"mt-6 text-2xl leading-10\">Sed elementum turpis eu lorem interdum, sed porttitor eros commodo. Nam eu venenatis tortor, id lacinia diam. Sed aliquam in dui et porta. Sed bibendum orci non tincidunt ultrices. Vivamus fringilla, mi lacinia dapibus condimentum, ipsum urna lacinia lacus, vel tincidunt mi nibh sit amet lorem.</p><p class=\"my-6 text-2xl\">Sincerly,</p>',NULL,_binary '\0','[{\"product_id\" : \"21\",\"slug\" : \"dien-thoai/samsung-galaxy-z-flip3-5g.483d22c888db463cbf1c924e2eebe549\",\"img\" : \"IMAGE_PRODUCT/44uexhsraf1xm5mgenwxvubobjhpbgny.jpg\",\"name\" : \"Samsung Galaxy Z Flip3 5G\",\"isBaned\" : \"0\",\"negTotal\" : \"4\",\"total\" : \"5\",\"percent\" : \"80\",\"count\" : \"1\",},{\"product_id\" : \"11\",\"slug\" : \"phu-kien/pin-sac-du-phong-polymer-10000-mah-type-c-pd-qc30-xmobile-powerslim-pj-jp213.ab853efaf1f546ce86d06d9f96d67e83\",\"img\" : \"IMAGE_PRODUCT/4pq1497ob6x2wbtw80i1rw74ctwsx8wn.jpg\",\"name\" : \"Pin sạc dự phòng Polymer 10.000 mAh Type C PD QC3.0 Xmobile PowerSlim PJ JP213\",\"isBaned\" : \"0\",\"negTotal\" : \"3\",\"total\" : \"3\",\"percent\" : \"100\",\"count\" : \"2\",}]','2023-06-12 21:35:23.000000',NULL,2,'Thông báo về danh sách sản phẩm có nguy cơ bị ban','customer1@gmail.com','Balo Chuối','MESSAGE_PRODUCT',NULL),(2,'<p class=\"mt-6 text-2xl leading-10\">Chào shop Phụ Kiện Điện Thoại 123@,</p><p class=\"mt-6 text-2xl leading-10\">Praesent dui ex, dapibus eget mauris ut, finibus vestibulum enim. Quisque9 arcu leo, facilisis in fringilla id, luctus in tortor. Nunc vestibulum est quis orci varius viverra. Curabitur dictum volutpat massa vulputate molestie. In at felis ac velit maximus convallis. </p> <p class=\"mt-6 text-2xl leading-10\">Sed elementum turpis eu lorem interdum, sed porttitor eros commodo. Nam eu venenatis tortor, id lacinia diam. Sed aliquam in dui et porta. Sed bibendum orci non tincidunt ultrices. Vivamus fringilla, mi lacinia dapibus condimentum, ipsum urna lacinia lacus, vel tincidunt mi nibh sit amet lorem.</p><p class=\"my-6 text-2xl\">Sincerly,</p>',NULL,_binary '\0','[{\"product_id\" : \"54\",\"slug\" : \"tu-lanh/tu-lanh-electrolux-eme3700h-a-dung-tich-337-lit-cong-nghe-inverter-hang-chinh-hang.83612e27ccf84df7916be3984a8865e7\",\"img\" : \"IMAGE_PRODUCT/22211e58f0f561651cf199b280df1390.jpg\",\"name\" : \"Tủ Lạnh Electrolux EME3700H-A - Dung Tích 337 Lít - Công Nghệ Inverter - Hàng Chính Hãng\",\"isBaned\" : \"0\",\"negTotal\" : \"2\",\"total\" : \"2\",\"percent\" : \"100\",\"count\" : \"2\",},{\"product_id\" : \"3\",\"slug\" : \"phu-kien/tai-nghe-bluetooth-airpods-3-apple-mme73-trang.1a705861b747468599491ab6ebe5a575\",\"img\" : \"IMAGE_PRODUCT/4fkue0gwjtkewij5zaxpvbnraerjtb7b.jpg\",\"name\" : \"Tai nghe Bluetooth AirPods 3 Apple MME73 Trắng\",\"isBaned\" : \"0\",\"negTotal\" : \"2\",\"total\" : \"2\",\"percent\" : \"100\",\"count\" : \"2\",}]','2023-06-12 21:35:23.000000',NULL,3,'Thông báo về danh sách sản phẩm có nguy cơ bị ban','customer2@gmail.com','Phụ Kiện Điện Thoại 123@','MESSAGE_PRODUCT',NULL),(3,'<p class=\"mt-6 text-2xl leading-10\">Chào shop MEKAXO - PHỤ KIỆN CÔNG NGHỆ,</p><p class=\"mt-6 text-2xl leading-10\">Praesent dui ex, dapibus eget mauris ut, finibus vestibulum enim. Quisque9 arcu leo, facilisis in fringilla id, luctus in tortor. Nunc vestibulum est quis orci varius viverra. Curabitur dictum volutpat massa vulputate molestie. In at felis ac velit maximus convallis. </p> <p class=\"mt-6 text-2xl leading-10\">Sed elementum turpis eu lorem interdum, sed porttitor eros commodo. Nam eu venenatis tortor, id lacinia diam. Sed aliquam in dui et porta. Sed bibendum orci non tincidunt ultrices. Vivamus fringilla, mi lacinia dapibus condimentum, ipsum urna lacinia lacus, vel tincidunt mi nibh sit amet lorem.</p><p class=\"my-6 text-2xl\">Sincerly,</p>',NULL,_binary '\0','[{\"product_id\" : \"69\",\"slug\" : \"tivi/smart-tivi-samsung-crystal-uhd-4k-50-inch-ua50au7700kxxv-hang-chinh-hang-giao-toan-quoc.42a74bc3a9b44dff9211a86a843a2af6\",\"img\" : \"IMAGE_PRODUCT/a5025f22245a888945436508ce07dca0.png\",\"name\" : \"Smart Tivi Samsung Crystal UHD 4K 50 inch UA50AU7700KXXV - Hàng chính hãng - Giao toàn quốc\",\"isBaned\" : \"0\",\"negTotal\" : \"3\",\"total\" : \"4\",\"percent\" : \"75\",\"count\" : \"1\",},{\"product_id\" : \"57\",\"slug\" : \"tivi/smart-tivi-neo-qled-samsung-4k-55-inch-qa55qn85a.09c731487fd9411da6f6a985e362e991\",\"img\" : \"IMAGE_PRODUCT/606299060c679734e5b429b499dc4dd8.jpg\",\"name\" : \"Smart Tivi Neo QLED Samsung 4K 55 inch QA55QN85A\",\"isBaned\" : \"0\",\"negTotal\" : \"3\",\"total\" : \"4\",\"percent\" : \"75\",\"count\" : \"1\",}]','2023-06-12 21:35:23.000000',NULL,6,'Thông báo về danh sách sản phẩm có nguy cơ bị ban','customer5@gmail.com','MEKAXO - PHỤ KIỆN CÔNG NGHỆ','MESSAGE_PRODUCT',NULL);
/*!40000 ALTER TABLE `tbl_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_product_black_list`
--

DROP TABLE IF EXISTS `tbl_product_black_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_product_black_list` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `count` int NOT NULL DEFAULT '1',
  `img` varchar(255) NOT NULL,
  `is_baned` bit(1) NOT NULL DEFAULT b'0',
  `neg_total` bigint DEFAULT NULL,
  `percent` double DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `product_name` varchar(255) NOT NULL,
  `scan_at` datetime(6) DEFAULT NULL,
  `shop_id` bigint DEFAULT NULL,
  `slug` varchar(300) NOT NULL,
  `status` bit(1) NOT NULL DEFAULT b'1',
  `total` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_product_black_list`
--

LOCK TABLES `tbl_product_black_list` WRITE;
/*!40000 ALTER TABLE `tbl_product_black_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_product_black_list` ENABLE KEYS */;
UNLOCK TABLES;

CREATE OR REPLACE VIEW sentimentAnalysis 
AS
	SELECT p.id product_id, p.shop_id, fb.sentiment, COUNT(fb.sentiment) as count
    FROM tbl_feedback fb RIGHT OUTER JOIN tbl_product p ON fb.product_id = p.id
    GROUP BY p.id, p.shop_id, fb.sentiment;
    
CREATE OR REPLACE VIEW shopStatistic 
AS
	select s.id shop_id, count(os.internal_id) as orderCount, sum(os.total_price_product) earningTotal, 
    ifnull(lm.earningLastMonth, 0) earningLastMonth, 
	ifnull(lw.earningLastWeek, 0) earningLastWeek, ifnull(td.earningToday, 0) earningToday,
	-- target
	ifnull(
	(select avg(lm.total)
	from
	(select sum(total_price_product) total
	from tbl_shop s left join tbl_order_shop os on s.id = os.shop_id
	where CONVERT(os.created_at, DATE) >= CONVERT(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH), DATE)
	group by s.id) lm), 10000000) earningTarget
	from
	tbl_shop s inner join tbl_order_shop os on s.id = os.shop_id
	-- today
	left join (select s.id,  sum(IFNULL(os.total_price_product, 0)) as earningToday
	from tbl_shop s left join tbl_order_shop os on s.id = os.shop_id
	where date(os.created_at) = date(current_date())
	group by s.id) td on s.id = td.id
	-- lastWeek
	left join (select s.id,  sum(total_price_product) as earningLastWeek
	from tbl_shop s left join tbl_order_shop os on s.id = os.shop_id
	where CONVERT(os.created_at, DATE) >= CONVERT(DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), DATE)
	group by s.id) lw on s.id = lw.id
	-- lastMonth
	left join (select s.id,  sum(total_price_product) as earningLastMonth
	from tbl_shop s left join tbl_order_shop os on s.id = os.shop_id
	where CONVERT(os.created_at, DATE) >= CONVERT(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH), DATE)
	group by s.id) lm on s.id = lm.id
	group by s.id;
-- SELECT * FROM shopStatistic;

CREATE OR REPLACE VIEW lastCheckProductBlackList 
AS
	SELECT * FROM tbl_product_black_list gr_pbl
    WHERE scan_at = (SELECT scan_at FROM tbl_product_black_list gr_pbl ORDER BY scan_at DESC LIMIT 0, 1);
    
-- SELECT * FROM  lastCheckProductBlackList;

DROP PROCEDURE IF EXISTS updateBlackListProduct;
DELIMITER //
CREATE PROCEDURE updateBlackListProduct(IN inShopId BIGINT, IN inStartDate DATE, IN inMinAll INT,IN inMinNeg INT, IN startDateNewSession DATE)
BEGIN 
	DECLARE i, j, n, m INT DEFAULT 0;
    DECLARE currentDate DATETIME DEFAULT NOW();
    DECLARE scanAt DATETIME(6);
    DECLARE id, productId, shopId BIGINT DEFAULT NULL;
    DECLARE negTotal, total BIGINT DEFAULT 0;
    DECLARE percent DOUBLE DEFAULT 0;
    DECLARE slug, img VARCHAR(300) DEFAULT "";
    DECLARE productName VARCHAR(255);
    drop temporary table if exists originTemp;
	CREATE TEMPORARY TABLE originTemp
    (SELECT  newp.product_id, newp.shop_id, oldp.id, oldp.scan_at, newp.neg_total, newp.total, newp.percent, newp.slug, newp.img, newp.product_name
	FROM
		(SELECT a.product_id, a.shop_id, a.slug, a.img, a.product_name, count neg_total, allCount total, (count * 100 / allCount) percent
		FROM 
			(SELECT p.id product_id, p.shop_id, COUNT(fb.sentiment) as count, CONCAT(c.slug, '/', p.slug) slug, i.path img, p.name product_name
			FROM tbl_feedback fb RIGHT OUTER JOIN tbl_product p ON fb.product_id = p.id 
				INNER JOIN tbl_category c ON p.category_id = c.id
                INNER JOIN tbl_image i ON p.thumbnail = i.id
			WHERE p.enabled = 1 and fb.sentiment = 'SENTIMENT_NEGATIVE'
			GROUP BY p.id, p.shop_id, fb.sentiment) as a
			INNER JOIN
			(SELECT p.id product_id, p.shop_id, COUNT(fb.id) as allCount
			FROM tbl_feedback fb RIGHT OUTER JOIN tbl_product p ON fb.product_id = p.id
			WHERE p.enabled = 1
			GROUP BY p.id, p.shop_id) b
			ON a.product_id = b.product_id
		WHERE allCount >= inMinAll AND (count * 100 / allCount) >= inMinNeg) as newp
		LEFT OUTER JOIN tbl_product_black_list oldp
		ON newp.product_id = oldp.product_id
	WHERE oldp.status IS NULL or oldp.status = 1 
    AND newp.product_id NOT IN (SELECT DISTINCT product_id FROM tbl_product_black_list gr_pbl WHERE is_baned = 1));
    drop temporary table if exists groupTemp;
	CREATE TEMPORARY TABLE groupTemp AS 
    (SELECT product_id, shop_id, COUNT(*) FROM originTemp GROUP BY product_id, shop_id);
    SELECT count(*) FROM groupTemp INTO n;
    
    WHILE i < n DO
		SELECT groupTemp.product_id, groupTemp.shop_id FROM groupTemp LIMIT i,1 INTO productId, shopId;
        drop temporary table if exists oneTemp;
		CREATE TEMPORARY TABLE oneTemp AS (SELECT * FROM originTemp ot WHERE ot.product_id = productId AND ot.shop_id = shopId);
        SELECT ot.scan_at, ot.neg_total, ot.total, ot.percent, ot.slug, ot.img, ot.product_name FROM oneTemp ot  LIMIT 0,1 INTO scanAt, negTotal, total, percent, slug, img, productName;
		IF scanAt IS NULL THEN
			INSERT INTO tbl_product_black_list(neg_total, percent, product_id, scan_at, shop_id, slug, status, total, count, img, product_name) 
            VALUES (negTotal, percent, productId, currentDate, shopId, slug, DEFAULT, total, DEFAULT, img, productName); 
		ELSE-- IF enabled <=> 0 THEN
			SELECT count(*) FROM oneTemp ot WHERE ot.scan_at < startDateNewSession INTO m;
            SET j = 0;
            WHILE j < m DO
				SELECT ot.id FROM oneTemp ot WHERE ot.scan_at < startDateNewSession LIMIT i,1 INTO id;
                UPDATE tbl_product_black_list SET status = 0 WHERE id = id;
                SET j = j + 1;
            END WHILE;
            SELECT count(*) FROM oneTemp ot WHERE ot.scan_at >= startDateNewSession INTO m;
            IF m < 2 THEN
				INSERT INTO tbl_product_black_list(neg_total, percent, product_id, scan_at, shop_id, slug, status, total, count, img, product_name) 
				VALUES (negTotal, percent, productId, currentDate, shopId, slug, DEFAULT, total, m + 1, img, productName); 
			ELSE 
				INSERT INTO tbl_product_black_list(is_baned, neg_total, percent, product_id, scan_at, shop_id, slug, status, total, count, img, product_name) 
				VALUES (1, negTotal, percent, productId, currentDate, shopId, slug, DEFAULT, total, m + 1, img, productName); 
--                 UPDATE tbl_product SET enabled = 0 WHERE id = productId;
			END IF;
        END IF;
        SET i = i + 1;
    END WHILE;
    drop temporary table if exists originTemp;
    drop temporary table if exists groupTemp;
    drop temporary table if exists oneTemp;
    SELECT * FROM tbl_product_black_list;
    INSERT INTO tbl_message(shop_id, to_name, to_email, is_baned, scan_at, title, body, products)
    (SELECT shop_id, s.name to_name, s.email, is_baned, tbl.scan_at,
		IF(is_baned = '0', 'Notice about the list of potentially banned products', -- 'Thông báo về danh sách sản phẩm có nguy cơ bị ban',
		'Notice of prohibited products list' -- 'Thông báo về danh sách sản phẩm của shop bạn đã bị ban'
        ) title,
		CONCAT('<html>
		<body>
		<p class="mt-6 text-2xl leading-10">Chào shop ', s.name,',</p>', 
		'<p class="mt-6 text-2xl leading-10">We are sending you this letter to inform you that the following list of products has been checked by the system and found that the number of negative reviews is high.</p> 
        <p class="mt-6 text-2xl leading-10">So we ask you to double check and if this happens for the 3rd time, the product will be banned.</p>
        <p>Sincerely,<br>-PNTech Admin</br></p>
		</body>
		</html>') body, 
	CONCAT('[', GROUP_CONCAT('{', CONCAT(
		'"product_id" : "', tbl.product_id, '",', 
        '"slug" : "', tbl.slug, '",', 
        '"img" : "', tbl.img, '",', 
        '"name" : "', tbl.product_name, '",', 
        '"isBaned" : "', export_set(tbl.is_baned,'1','0','',1), '",', 
        '"negTotal" : "', tbl.neg_total, '",', 
        '"total" : "', tbl.total, '",', 
        '"percent" : "', tbl.percent, '",', 
        '"count" : "', tbl.count, '",') 
        , '}'), ']') products
	FROM lastCheckProductBlackList tbl INNER JOIN tbl_shop s ON tbl.shop_id = s.id
	GROUP BY shop_id, tbl.is_baned, tbl.scan_at);
END; //
DELIMITER ;

SET @inShopId = NULL,
	@inStartDate = DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY),
	@inMinAll = 2,
    @inMinNeg = 90,
    @inStartDateNewSession = DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY);
CALL updateBlackListProduct(@inShopId, @inStartDate, @inMinAll, @inMinNeg, @inStartDateNewSession);

DROP PROCEDURE IF EXISTS updateBlackProductStatus;
DELIMITER //
CREATE PROCEDURE updateBlackProductStatus(IN checkDate DATE)
BEGIN 
	UPDATE tbl_product SET enabled = 0 WHERE enabled = 1 AND  id in 
    (select product_id from tbl_product_black_list 
    WHERE DATE(scan_at) = DATE(checkDate) and is_baned = 1);
    select * from tbl_product WHERE enabled = 0; 
END; //
DELIMITER ;

SET @checkDate = CURRENT_DATE();
call updateBlackProductStatus(@checkDate);

DROP PROCEDURE IF EXISTS updateShopPrice;
DELIMITER //
CREATE PROCEDURE updateShopPrice(IN rangeDate INT)
BEGIN 
	SET SQL_SAFE_UPDATES = 0;
	UPDATE tbl_shop SET end_price_at = NULL WHERE CONVERT(end_price_at, DATE) < CONVERT(NOW(), DATE);
	INSERT INTO tbl_message(shop_id, to_name, to_email, is_baned, created_at, scan_at, type, title, body)
    (SELECT s.id, s.name, s.email, false, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'MESSAGE_SHOP_PRICE', 'Notice about the expiration of the shop subscription package', 
    CONCAT('<html>
	<body>
	<p>Dear ',s.name,' shop,</p>
	<p>We are sending you this letter to let you know that you have ', rangeDate, ' days left on your \'', sp.name,'\' subscription which will expire at ', CONVERT(s.end_price_at, DATE), '.
	<br>If you receive this message, please renew or sign up for a new plan before the expiration date. Otherwise, upon expiration, the function of selling new products will be temporarily closed.</p>
	<p>Sincerely,<br>-PNTech Admin</br></p>
	</body>
	</html>')
	FROM tbl_shop s INNER JOIN tbl_shop_price sp ON s.shop_price_id = sp.id
	WHERE CONVERT(s.end_price_at, DATE) = DATE_SUB(CURRENT_DATE(), INTERVAL rangeDate DAY));
    SELECT * FROM tbl_shop WHERE CONVERT(end_price_at, DATE) = DATE_SUB(CURRENT_DATE(), INTERVAL rangeDate DAY);
END; //
DELIMITER ;

-- SET @rangeDate = -29;
-- CALL updateShopPrice(@rangeDate);