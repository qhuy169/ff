package gt.electronic.ecommerce.utils;

import gt.electronic.ecommerce.dto.request.AuthRegisterDTO;
import gt.electronic.ecommerce.dto.request.OrderCreationDTO;
import gt.electronic.ecommerce.dto.request.OrderDetailCreationDTO;
import gt.electronic.ecommerce.dto.response.OrderResponseDTO;
import gt.electronic.ecommerce.entities.*;
import gt.electronic.ecommerce.models.clazzs.FullAddress;
import gt.electronic.ecommerce.models.enums.*;
import gt.electronic.ecommerce.repositories.*;
import gt.electronic.ecommerce.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author quang huy
 * @created 08/10/2025 - 9:46 PM
 */
@Component
public class InitData {
        private final Logger LOGGER = LoggerFactory.getLogger(InitData.class);
        private final Random generator = new Random();

        @Autowired
        private AddressService addressService;
        @Autowired
        private BrandRepository brandRepo;
        @Autowired
        private CategoryRepository categoryRepo;
        @Autowired
        private CommentRepository commentRepo;
        private DiscountRepository discountRepo;

        @Autowired
        public void DiscountRepository(DiscountRepository discountRepo) {
                this.discountRepo = discountRepo;
        }

        @Autowired
        private FeedbackRepository feedbackRepo;
        @Autowired
        private ImageRepository imageRepo;
        @Autowired
        private LocationService locationService;
        @Autowired
        private OrderRepository orderRepo;
        @Autowired
        private OrderService orderService;
        @Autowired
        private ProductRepository productRepo;
        @Autowired
        private ProductService productService;
        @Autowired
        private SaleRepository saleRepo;
        @Autowired
        private ShopRepository shopRepo;
        @Autowired
        private ShopPriceRepository shopPriceRepo;
        @Autowired
        private UserRepository userRepo;
        @Autowired
        private UserService userService;

        public void Init() {
                this.LOGGER.info("Start init data to database");
                // if (true) {
                // ShopPrice[] shopPrices = new ShopPrice[500];
                // createShopPrice(shopPrices);
                // return;
                // }
                Pageable pageable = PageRequest.of(0, 1000);
                Date currentDate = new Date();
                long hourTime = 1000 * 3600;
                long dayTime = hourTime * 24;
                long monthTime = dayTime * 30;

                LocationVariable lv = new LocationVariable();
                createLocation(lv);

                UserVariable uv = new UserVariable();
                createUser(uv, lv);

                BrandVariable bv = new BrandVariable();
                createBrand(bv);

                CategoryVariable cv = new CategoryVariable();
                createCategory(cv, bv);

                FullAddress[] fullAddresses = new FullAddress[500];
                int maxIndexFullAddress = createFullAddress(fullAddresses);

                ShopPrice[] shopPrices = new ShopPrice[500];
                int maxIndexShopPrice = createShopPrice(shopPrices);

                Shop[] shops = new Shop[500];
                int maxIndexShop = createShop(shops, uv.customers, uv.maxIndexCustomer, fullAddresses,
                                maxIndexFullAddress);

                Product[] products = new Product[500];
                int maxIndexProduct = createProduct(products, lv, cv, bv, shops, maxIndexShop);

                Sale[] sales = new Sale[100];
                int maxIndexSale = createSale(sales, products, maxIndexProduct, uv.customers, uv.maxIndexCustomer,
                                currentDate, dayTime,
                                monthTime,
                                pageable, cv, bv);

                Discount[] discounts = new Discount[500];
                int maxIndexDiscounts = createDiscount(discounts, shops, maxIndexShop);

                // Comment[] mainComments = new Comment[500];
                // int maxIndexMainComment =
                // createMainComments(mainComments, products, maxIndexProduct, uv.customers,
                // uv.maxIndexCustomer);
                //
                // Comment[] childComments = new Comment[500];
                // int maxIndexChildComment =
                // createChildComments(childComments, mainComments, maxIndexMainComment,
                // uv.customers,
                // uv.maxIndexCustomer);

                Feedback[] mainFeedbacks = new Feedback[500];
                int maxIndexMainFeedback = createMainFeedbacks(mainFeedbacks, products, maxIndexProduct, uv.customers,
                                uv.maxIndexCustomer);

                // Comment[] childFeedbacks = new Comment[500];
                // int maxChildFeedback =
                // createChildFeedbacks(childFeedbacks, mainFeedbacks, maxIndexMainFeedback,
                // uv.customers,
                // uv.maxIndexCustomer);

                createOrder(products, maxIndexProduct, uv.customers, uv.maxIndexCustomer, currentDate, dayTime,
                                monthTime);

                this.LOGGER.info("Init data to database is done!");
        }

        private int createShopPrice(ShopPrice[] shopPrices) {
                int i = 0;
                shopPrices[i] = new ShopPrice("Gói tháng thường",
                                new BigDecimal("1000000"),
                                50,
                                1,
                                EDateType.MONTH,
                                EShopPriceType.NORMAL,
                                "");
                shopPrices[i] = shopPriceRepo.save(shopPrices[i]);
                i++;
                shopPrices[i] = new ShopPrice("Gói tháng vip",
                                new BigDecimal("3000000"),
                                500,
                                1,
                                EDateType.MONTH,
                                EShopPriceType.VIP,
                                "");
                shopPrices[i] = shopPriceRepo.save(shopPrices[i]);
                i++;
                shopPrices[i] = new ShopPrice("Gói năm thường",
                                new BigDecimal("10000000"),
                                70,
                                1,
                                EDateType.YEAR,
                                EShopPriceType.NORMAL,
                                "");
                shopPrices[i] = shopPriceRepo.save(shopPrices[i]);
                i++;
                shopPrices[i] = new ShopPrice("Gói năm vip",
                                new BigDecimal("30000000"),
                                700,
                                1,
                                EDateType.YEAR,
                                EShopPriceType.VIP,
                                "");
                shopPrices[i] = shopPriceRepo.save(shopPrices[i]);
                i++;
                return i;
        }

        private int createDiscount(Discount[] discounts, Shop[] shops, int maxIndexShop) {
                int countDiscount = 50;
                List<BigDecimal> minSpends = Arrays.asList(new BigDecimal(0000),
                                new BigDecimal(99000),
                                new BigDecimal(150000),
                                new BigDecimal(199000),
                                new BigDecimal(250000),
                                new BigDecimal(299000),
                                new BigDecimal(399000),
                                new BigDecimal(499000),
                                new BigDecimal(600000),
                                new BigDecimal(2000000));
                List<BigDecimal> cappedAts = Arrays.asList(null,
                                new BigDecimal(20000),
                                new BigDecimal(30000),
                                new BigDecimal(69000),
                                new BigDecimal(99000),
                                new BigDecimal(111000),
                                new BigDecimal(150000),
                                new BigDecimal(5000000));
                List<Double> percents = Arrays.asList(0.11d, 0.15d, 0.25d, 0.30d, 0.33d, 0.39d, 0.49d, 0.50d, 0.59d,
                                1.00d);
                List<BigDecimal> prices = Arrays.asList(new BigDecimal(15000),
                                new BigDecimal(20000),
                                new BigDecimal(30000),
                                new BigDecimal(40000),
                                new BigDecimal(99000),
                                new BigDecimal(220000));
                for (int i = 0; i < countDiscount; i++) {
                        BigDecimal minSpend = null;
                        Double percent = null;
                        BigDecimal cappedAt = null;
                        BigDecimal price = null;
                        EDiscountType type = EDiscountType.DISCOUNT_SHOP_PERCENT;
                        // percent
                        if (this.generateInt(2) == 0) {
                                percent = percents.get(this.generateInt(percents.size()));
                                cappedAt = cappedAts.get(this.generateInt(cappedAts.size()));
                        } // price
                        else {
                                price = prices.get(this.generateInt(prices.size()));
                                type = EDiscountType.DISCOUNT_SHOP_PRICE;
                        }
                        minSpend = minSpends.get(this.generateInt(minSpends.size()));
                        Shop shop = shops[this.generateInt(maxIndexShop)];
                        discounts[i] = new Discount(99, percent, cappedAt, price, minSpend, shop, type);
                        this.discountRepo.save(discounts[i]);
                }
                return countDiscount;
        }

        private void createOrder(
                        Product[] products,
                        int maxIndexProduct,
                        User[] customers,
                        int maxIndexCustomer,
                        Date currentDate,
                        long dayTime,
                        long monthTime) {
                OrderCreationDTO[] orders = new OrderCreationDTO[500];
                List<OrderDetailCreationDTO> orderItems;
                int countOrder = 50;
                int maxProductOrder = 5;
                int maxQuantityProduct = 3;
                for (int i = 0; i < countOrder; i++) {
                        int rand = generator.nextInt(maxProductOrder) + 1;
                        Map<Product, Long> productMap = new HashMap<>();
                        for (int j = 0; j < rand; j++) {
                                productMap.putIfAbsent(products[generator.nextInt(maxIndexProduct + 1)],
                                                (long) (generator.nextInt(maxQuantityProduct) + 1));
                        }
                        User user = customers[generator.nextInt(maxIndexCustomer + 1)];

                        orders[i] = new OrderCreationDTO(user, productMap);
                        OrderResponseDTO response = this.orderService.createOrder(
                                        user.getPhone() != null ? user.getPhone() : user.getEmail(),
                                        orders[i]);
                        Order orderFound = this.orderRepo.findById(response.getId()).orElse(null);
                        if (orderFound != null) {
                                long time = currentDate.getTime() - dayTime * maxProductOrder + dayTime * rand;
                                orderFound.setCreatedAt(new Date(time));
                                orderFound.setUpdatedAt(new Date(time));
                                for (OrderShop orderShop : orderFound.getOrderShops()) {
                                        orderShop.setCreatedAt(new Date(time));
                                        orderShop.setUpdatedAt(new Date(time));
                                }
                                this.orderRepo.save(orderFound);
                        }
                }
        }

        private int createShop(
                        Shop[] shops, User[] customers, int maxIndexCustomer, FullAddress[] fullAddresses,
                        int maxIndexFullAdress) {
                int i = 0;
                customers[i].setRole(ERole.ROLE_SELLER);
                customers[i] = this.userRepo.save(customers[i]);
                shops[i] = new Shop("SAMISO OFFICIAL", customers[i],
                                fullAddresses[generator.nextInt(maxIndexFullAdress + 1)],
                                "a0c248661f5eb79154aaf6a68694412f_tn.jpg", "cc071e3f5902c71e6319ad723047450e_tn.jpg");
                shops[i] = this.shopRepo.save(shops[i]);

                i++;
                customers[i].setRole(ERole.ROLE_SELLER);
                customers[i] = this.userRepo.save(customers[i]);
                shops[i] = new Shop("Balo Chuối", customers[i],
                                fullAddresses[generator.nextInt(maxIndexFullAdress + 1)],
                                "9dc2fb6e38bf9c5b71dff224206b3b9c_tn.png", "8a7f0ccf07ede9528687465d28122b62_tn.jpg");
                shops[i] = this.shopRepo.save(shops[i]);

                i++;
                customers[i].setRole(ERole.ROLE_SELLER);
                customers[i] = this.userRepo.save(customers[i]);
                shops[i] = new Shop("Phụ Kiện Điện Thoại 123@", customers[i],
                                fullAddresses[generator.nextInt(maxIndexFullAdress + 1)],
                                "d3343e34b9e18b3114a7ea6f1c134691_tn.jpg", "83d79e88e5cf7c8d9f87b458eb26a148_tn.jpg");
                shops[i] = this.shopRepo.save(shops[i]);

                i++;
                customers[i].setRole(ERole.ROLE_SELLER);
                customers[i] = this.userRepo.save(customers[i]);
                shops[i] = new Shop("Zulia's Shop", customers[i],
                                fullAddresses[generator.nextInt(maxIndexFullAdress + 1)],
                                "d0c9753c42f92c611f603d9e157df22c_tn.png", "2d9a458d3a8a57e1aa5a6e669048b36d_tn.jpg");
                shops[i] = this.shopRepo.save(shops[i]);

                i++;
                customers[i].setRole(ERole.ROLE_SELLER);
                customers[i] = this.userRepo.save(customers[i]);
                shops[i] = new Shop("BINTECH 27", customers[i],
                                fullAddresses[generator.nextInt(maxIndexFullAdress + 1)],
                                "6e80d189ff9edc2a580217b427d209b0_tn.jpg", "3a0555857f16be8625e35c80ffe61638_tn.jpg");
                shops[i] = this.shopRepo.save(shops[i]);

                i++;
                customers[i].setRole(ERole.ROLE_SELLER);
                customers[i] = this.userRepo.save(customers[i]);
                shops[i] = new Shop("MEKAXO - PHỤ KIỆN CÔNG NGHỆ", customers[i],
                                fullAddresses[generator.nextInt(maxIndexFullAdress + 1)],
                                "fc050d5aec10b15c10031fca06007b3c_tn.jpg", "2de3af7035ab40e931ca231e291853dd_tn.jpg");
                shops[i] = this.shopRepo.save(shops[i]);

                i++;
                customers[i].setRole(ERole.ROLE_SELLER);
                customers[i] = this.userRepo.save(customers[i]);
                shops[i] = new Shop("SenBenBao.vn", customers[i],
                                fullAddresses[generator.nextInt(maxIndexFullAdress + 1)],
                                "a2c4553100a451d866c9eb44928ede68_tn.jpg", "519193eb165324541f467893fdc68765_tn.jpg");
                shops[i] = this.shopRepo.save(shops[i]);

                return i;
        }

        private int createSale(
                        Sale[] sales, Product[] products, int maxIndexProduct, User[] customers, int maxIndexCustomer,
                        Date currentDate, long dayTime, long monthTime, Pageable pageable, CategoryVariable cv,
                        BrandVariable bv) {
                int i = 0;
                Set<Product> productSet1 = new HashSet<>(this.productRepo.findAll());

                sales[i] = new Sale("Sinh nhật", "", 0.2, productSet1,
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                currentDate,
                                new Date(currentDate.getTime() + 5 * dayTime));
                sales[i] = this.saleRepo.save(sales[i]);

                Set<Product> productSet2 = this.productRepo.findAllByCategory(cv.categoryPhone, pageable).stream()
                                .collect(Collectors.toSet());
                productSet2.addAll(
                                this.productRepo.findAllByCategory(cv.categoryLaptop, pageable).stream()
                                                .collect(Collectors.toSet()));
                productSet2.addAll(
                                this.productRepo.findAllByCategory(cv.categoryAccessory, pageable).stream()
                                                .collect(Collectors.toSet()));
                i++;
                sales[i] = new Sale("Giảm sốc", "", 0.35, productSet2,
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                currentDate,
                                new Date(currentDate.getTime() + 10 * dayTime));
                sales[i] = this.saleRepo.save(sales[i]);

                Set<Product> productSet3 = this.productRepo.findAllByCategory(cv.categoryWatch, pageable).stream()
                                .collect(Collectors.toSet());
                productSet3.addAll(
                                this.productRepo.findAllByCategory(cv.categorySmartWatch, pageable).stream()
                                                .collect(Collectors.toSet()));

                i++;
                sales[i] = new Sale("Giá sốc cuối tuần", "", 0.23, productSet3,
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                currentDate, new Date(currentDate.getTime() + 30 * 24 * 3600));
                sales[i] = this.saleRepo.save(sales[i]);

                Set<Product> productSet4 = this.productRepo.findAllByBrand(bv.brandSamsung, pageable).stream()
                                .collect(Collectors.toSet());
                i++;
                sales[i] = new Sale("Gói Samsung care+", "", 0.08, productSet4,
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                currentDate, new Date(currentDate.getTime() + monthTime));
                sales[i] = this.saleRepo.save(sales[i]);

                Set<Product> productSet5 = this.productRepo.findAllByCategory(cv.categoryLaptop, pageable).stream()
                                .collect(Collectors.toSet());
                i++;
                sales[i] = new Sale("Intel gen 12", "", 0.15, productSet5,
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                currentDate,
                                new Date(currentDate.getTime() + monthTime));
                sales[i] = this.saleRepo.save(sales[i]);

                return i;
        }

        private int createProduct(
                        Product[] products, LocationVariable lv, CategoryVariable cv, BrandVariable bv, Shop[] shop,
                        int maxIndexShop) {
                Map<String, String> descriptions;
                int i = 0;

                products[i] = new Product("Samsung Galaxy M93", new BigDecimal(12490000), 1000L, cv.categoryPhone,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)], lv.locationHaNoi,
                                Image.createImageProduct("wz5ybvug2v1fice49vtk8ihvxm5fml7n.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("IPhone 13 Pro Max 128GB", new BigDecimal(29190000), 1000L, cv.categoryPhone,
                                bv.brandIphone,
                                shop[generator.nextInt(maxIndexShop + 1)], lv.locationAnGiang,
                                Image.createImageProduct("5uh3kxfylucxbl4zcl2hjxga78q7w3sd.png"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Tai nghe Bluetooth AirPods 3 Apple MME73 Trắng",
                                new BigDecimal(4790000),
                                1000L,
                                cv.categoryAccessory,
                                bv.brandApple,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("4fkue0gwjtkewij5zaxpvbnraerjtb7b.jpg"),
                                "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Apple Watch S6 40mm viền nhôm dây silicone", new BigDecimal(8991000), 1000L,
                                cv.categoryWatch,
                                bv.brandApple, shop[generator.nextInt(maxIndexShop + 1)], lv.locationHCM,
                                Image.createImageProduct("2s54kr7xfx0u0uafwscuk88cjnsihmn6.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Samsung Galaxy Watch 4 40mm Vàng Hồng", new BigDecimal(5490000), 1000L,
                                cv.categoryPhone,
                                bv.brandSamsung, shop[generator.nextInt(maxIndexShop + 1)], lv.locationHCM,
                                Image.createImageProduct("rtds2by05fjl87iv6i5wukw0hzin35kv.jpeg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product(
                                "Pin sạc dự phòng Polymer 10.000 mAh Type C PD QC3.0 Xmobile PowerSlim PJ JP21",
                                new BigDecimal(490000),
                                1000L,
                                cv.categoryAccessory,
                                bv.brandXMobile,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHaNoi,
                                Image.createImageProduct("4pq1497ob6x2wbtw80i1rw74ctwsx8wn.jpg"),
                                "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Ốp lưng MagSafe iPhone 13 Pro Max Nhựa dẻo Apple MM2U3",
                                new BigDecimal(4790000),
                                1000L,
                                cv.categoryAccessory,
                                bv.brandIphone,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationCanTho,
                                Image.createImageProduct("m2a3b6jeioshmsbjjjioljojxvh0z02q.jpg"),
                                "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("AVA+ DS005-PP", new BigDecimal(4790000), 1000L, cv.categoryBackupCharger,
                                bv.brandOther,
                                shop[generator.nextInt(maxIndexShop + 1)], lv.locationHCM,
                                Image.createImageProduct("ava-plus-ds005-pp-thumb-600x600.jpeg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("eValu PA F1 Air", new BigDecimal(4790000), 1000L, cv.categoryBackupCharger,
                                bv.brandOther,
                                shop[generator.nextInt(maxIndexShop + 1)], lv.locationHaNoi,
                                Image.createImageProduct("aw2kh2whgu1ehaxjeznpi8t7gua9xr6g.jpeg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Hydrus PA CK01", new BigDecimal(4790000), 1000L, cv.categoryBackupCharger,
                                bv.brandOther,
                                shop[generator.nextInt(maxIndexShop + 1)], lv.locationHaNoi,
                                Image.createImageProduct("mz18q98nht286ps2ei230h6f0pic6ip0.jpeg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product(
                                "Pin sạc dự phòng Polymer 10.000 mAh Type C PD QC3.0 Xmobile PowerSlim PJ JP213",
                                new BigDecimal(4790000),
                                1000L,
                                cv.categoryAccessory,
                                bv.brandXMobile,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationCanTho,
                                Image.createImageProduct("4pq1497ob6x2wbtw80i1rw74ctwsx8wn.jpg"),
                                "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Acer TravelMate B3 TMB311 31 P49D N5030 (NX.VNFSV.005)",
                                new BigDecimal(420000),
                                1000L,
                                cv.categoryLaptop,
                                bv.brandAcer,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("68fx28jk93n2opzg162ezj62gnr7a265.jpg"),
                                "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("MSI Modern 14 B11MOU i3 1115G4 (1027VN)", new BigDecimal(14490000), 1000L,
                                cv.categoryLaptop,
                                bv.brandMSI, shop[generator.nextInt(maxIndexShop + 1)], lv.locationHaNoi,
                                Image.createImageProduct("68fx28jk93n2opzg162ezj62gnr7a265.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Apple Watch Series 7 GPS 41mm", new BigDecimal(8590000), 1000L,
                                cv.categoryWatch,
                                bv.brandApple,
                                shop[generator.nextInt(maxIndexShop + 1)], lv.locationHCM,
                                Image.createImageProduct("cj3hfpo01q0ajrmfvhqcuqwdqke53ygw.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Loa Bluetooth Mozard X21", new BigDecimal(7590000), 1000L,
                                cv.categoryLoudSpeaker,
                                bv.brandMozard,
                                shop[generator.nextInt(maxIndexShop + 1)], lv.locationHCM,
                                Image.createImageProduct("yq4ldqh8a0e0lciop8zg4rrd8vzvkx75.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Pin sạc dự phòng Polymer 10.000mAh AVA+ JP208",
                                new BigDecimal(7890000),
                                1000L,
                                cv.categoryAccessory,
                                bv.brandOther,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationCanTho,
                                Image.createImageProduct("rm8y5o5qnb2focjot6iuosx8hgyzx8eu.jpeg"),
                                "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Webcam 480P A4Tech PK-710G Đen", new BigDecimal(3490000), 1000L,
                                cv.categoryCamera,
                                bv.brandOther, shop[generator.nextInt(maxIndexShop + 1)], lv.locationHCM,
                                Image.createImageProduct("6ed2u95i3ybyoimjwnyfv8902x96bo5g.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Tai nghe Bluetooth True Wireless Galaxy Buds 2 R177N",
                                new BigDecimal(3490000),
                                1000L,
                                cv.categoryAccessory,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHaNoi,
                                Image.createImageProduct("7z3ls9yben0mevtlpsbxj6lsrre8nlj2.jpg"),
                                "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Bút cảm ứng S Pen Pro EJ-P5450 Đen", new BigDecimal(2490000), 1000L,
                                cv.categoryBumper,
                                bv.brandOther, shop[generator.nextInt(maxIndexShop + 1)], lv.locationHCM,
                                Image.createImageProduct("fV0F68z9vGdRV6fppN1tJHd3cBMihtlp.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Tai nghe Bluetooth True Wireless Galaxy Buds Live R180 Gold",
                                new BigDecimal(3490000),
                                1000L,
                                cv.categoryAccessory, bv.brandSamsung, shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("rVk8YHqvMgWmztAcCEDJg6vgUycg3Jji.jpeg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Samsung Galaxy Z Flip3 5G", new BigDecimal(13490000), 1000L,
                                cv.categoryPhone,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)], lv.locationHaNoi,
                                Image.createImageProduct("44uexhsraf1xm5mgenwxvubobjhpbgny.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Samsung Galaxy Z Flip3 5G Flex Your Way", new BigDecimal(13490000), 1000L,
                                cv.categoryPhone,
                                bv.brandSamsung, shop[generator.nextInt(maxIndexShop + 1)], lv.locationHCM,
                                Image.createImageProduct("44uexhsraf1xm5mgenwxvubobjhpbgny.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Samsung Galaxy A32", new BigDecimal(14490000), 1000L, cv.categoryPhone,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)], lv.locationHaNoi,
                                Image.createImageProduct("44uexhsraf1xm5mgenwxvubobjhpbgny.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Samsung Galaxy S22+ 5G", new BigDecimal(19490000), 1000L, cv.categoryPhone,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)], lv.locationCanTho,
                                Image.createImageProduct("44uexhsraf1xm5mgenwxvubobjhpbgny.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Samsung Galaxy A13", new BigDecimal(13490000), 1000L, cv.categoryPhone,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)], lv.locationHCM,
                                Image.createImageProduct("44uexhsraf1xm5mgenwxvubobjhpbgny.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Samsung Galaxy S21 FE 5G", new BigDecimal(23490000), 1000L, cv.categoryPhone,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)], lv.locationHaNoi,
                                Image.createImageProduct("44uexhsraf1xm5mgenwxvubobjhpbgny.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Samsung Galaxy S22 5G", new BigDecimal(23490000), 1000L, cv.categoryPhone,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)], lv.locationHCM,
                                Image.createImageProduct("44uexhsraf1xm5mgenwxvubobjhpbgny.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Samsung Galaxy M53", new BigDecimal(13490000), 1000L, cv.categoryPhone,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)], lv.locationCanTho,
                                Image.createImageProduct("44uexhsraf1xm5mgenwxvubobjhpbgny.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Acer TravelMate B3 TMB 31 P49D N5030 (NX.VNFSV.005)",
                                new BigDecimal(23490000),
                                1000L,
                                cv.categoryLaptop,
                                bv.brandAcer,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationCanTho,
                                Image.createImageProduct("68fx28jk93n2opzg162ezj62gnr7a265.jpg"),
                                "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Acer TravelMate B3 31 P49D N5030 (NX.VNFSV.005)",
                                new BigDecimal(3490000),
                                1000L,
                                cv.categoryLaptop,
                                bv.brandAcer,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHaNoi,
                                Image.createImageProduct("68fx28jk93n2opzg162ezj62gnr7a265.jpg"),
                                "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Samsung Galaxy Tab S8", new BigDecimal(13490000), 1000L, cv.categoryPhone,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)], lv.locationHCM,
                                Image.createImageProduct("44uexhsraf1xm5mgenwxvubobjhpbgny.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Alcatel 3T8", new BigDecimal(3490000), 1000L, cv.categoryChargingCable,
                                bv.brandOther,
                                shop[generator.nextInt(maxIndexShop + 1)], lv.locationHaNoi,
                                Image.createImageProduct("iojb0099pi5z2r28o21n2v0y30q5dyde.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("IPad Pro M1 11 Inch 5G", new BigDecimal(13490000), 1000L, cv.categoryTablet,
                                bv.brandApple,
                                shop[generator.nextInt(maxIndexShop + 1)], lv.locationHCM,
                                Image.createImageProduct("surasj24jb7ut26fn6i2o248gtt33q0c.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("IPad Pro M1 11 Inch 8G", new BigDecimal(13490000), 1000L, cv.categoryTablet,
                                bv.brandApple,
                                shop[generator.nextInt(maxIndexShop + 1)], lv.locationHCM,
                                Image.createImageProduct("surasj24jb7ut26fn6i2o248gtt33q0c.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Macbook 245 G8 R5 5500U (61C65PA)", new BigDecimal(33490000), 1000L,
                                cv.categoryLaptop,
                                bv.brandMacbook, shop[generator.nextInt(maxIndexShop + 1)], lv.locationHaNoi,
                                Image.createImageProduct("68fx28jk93n2opzg162ezj62gnr7a265.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Đồng hồ CITIZEN 30 mm Nam NH8391-51X", new BigDecimal(13490000), 1000L,
                                cv.categoryWatch,
                                bv.brandCitizen, shop[generator.nextInt(maxIndexShop + 1)], lv.locationDaNang,
                                Image.createImageProduct("3deffrpscectxlmmxdq53exi26tddsf1.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Đồng hồ CITIZEN 32 mm Nam NH8391-51E", new BigDecimal(13490000), 1000L,
                                cv.categoryWatch,
                                bv.brandCitizen, shop[generator.nextInt(maxIndexShop + 1)], lv.locationAnGiang,
                                Image.createImageProduct("FVcCBoamZx8LO6OZLDHy8bJBAZNsyQ4P.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Đồng hồ CITIZEN 30 mm Nam NJ0084-59A", new BigDecimal(18490000), 1000L,
                                cv.categoryWatch,
                                bv.brandCitizen, shop[generator.nextInt(maxIndexShop + 1)], lv.locationHCM,
                                Image.createImageProduct("ms6mehgsbm389151mm9fbabkrnrcsxao.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Đồng hồ ESPRIT 32 mm Nữ ES1L336L0035", new BigDecimal(16490000), 1000L,
                                cv.categoryWatch,
                                bv.brandEsprit, shop[generator.nextInt(maxIndexShop + 1)], lv.locationHCM,
                                Image.createImageProduct("esprit-es1l336l0035-nu-thumb-600x600.jpg"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;

                products[i] = new Product("Đồng hồ DKNY 30 mm Nữ NY2915", new BigDecimal(14490000), 1000L,
                                cv.categoryWatch,
                                bv.brandDKNY,
                                shop[generator.nextInt(maxIndexShop + 1)], lv.locationHCM,
                                Image.createImageProduct("hdy9vzrefhc48na2w9ys7hfxkd85ryr7.png"), "");
                products[i] = this.productRepo.save(products[i]);

                i++;
                products[i] = new Product(
                                "Laptop Dell Inspiron 15 3511 P112F001BBL (Core i5-1135G7/ 4GB DDR4/ 512GB SSD NVMe PCIe/ 15.6 FHD/ Win10 + OfficeH&S 2019) - Hàng Chính Hãng",
                                new BigDecimal(164900000),
                                1000L,
                                cv.categoryLaptop,
                                bv.brandDell,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("a5c1704a6be312795c6a47b0b347687d.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><h3 class=\"\">THÔNG SỐ KỸ THUẬT</h3>\n"
                                                +
                                                "<div class=\"content\">\n" +
                                                "<p><strong>Bộ vi xử lý:</strong><span>Intel Core i5-1135G7 (8MB Cache, 2.4GHz, Turbo Boost 4.2GHz)</span></p>\n"
                                                +
                                                "<p><strong>Bộ nhớ:</strong>4GB (1x4GB) DDR4 2666MHz</p>\n" +
                                                "<p><strong>Ổ cứng lưu trữ:</strong>512GB M.2 PCIe NVMe SSD</p>\n" +
                                                "<p><strong>Đồ hoạ:</strong>Intel(R) Iris Xe Graphics</p>\n" +
                                                "<p><strong>Màn hình:</strong>15.6-inch FHD (1920 x 1080) Anti-glare LED Backlight Non-Touch Narrow Border WVA Display</p>\n"
                                                +
                                                "<p><strong>Bàn phím:</strong>Non Led backlit</p>\n" +
                                                "<p><strong>Cổng kết nối:</strong>2x USB 3.2 Gen 1 ports<br>1x USB 2.0 port<br>1x HDMI 1.4 port<br>1x Audio jack</p>\n"
                                                +
                                                "<p><strong>Ổ quang:</strong>Không</p>\n" +
                                                "<p><strong>Kết nối không dây:</strong>Intel(R) Wi-Fi 6 2x2 (Gig+) and Bluetooth 5.1</p>\n"
                                                +
                                                "<p><strong>Hệ điều hành:</strong>Windows 10 Home Single Language&nbsp;</p>\n"
                                                +
                                                "<p><strong>Cảm ứng:</strong>Không</p>\n" +
                                                "<p><strong>Màu sắc:</strong>Black</p>\n" +
                                                "<p><strong>Pin:</strong>3 Cell 41WHr</p>\n" +
                                                "</div><p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(
                                new HashSet<>(this.imageRepo
                                                .saveAllAndFlush(Image.createImageProductGallery(new String[] {
                                                                "a885170e788a6f5e3dd8d0711ed3c6e2.jpg",
                                                                "64ade1e894f13838fdb0831b9c611ceb.jpg",
                                                                "65a6731d4a032c318cc13fefbd84e403.jpg",
                                                                "25e506c702bcd114807d47654eb3ef24.jpg", }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Bluetooth", "BT 5.0" },
                                { "Thương hiệu", "Dell" },
                                { "Xuất xứ thương hiệu", "Mỹ" },
                                { "Card đồ họa", "Intel(R) Iris Xe Graphics" },
                                { "CPU", "Intel Core i5-1135G7 (8MB Cache, 2.4GHz, Turbo Boost 4.2GHz)" },
                                { "Dung lượng ổ cứng", "512GB M.2 PCIe NVMe SSD" },
                                { "Model", "P112F001BBL" },
                                { "Loại pin", "3 Cell 41WHr" },
                                { "Loại RAM", "4GB (1x4GB) DDR4 2666MHz" },
                                { "Xuất xứ", "Trung Quốc" },
                                { "Hệ điều hành", "Win10 Home SL" },
                                { "Wifi", "WLAN 802.11ac" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Laptop MSI Modern 14 B11MOU i3 1115G4/8GB/256GB/14\"F/Win11/(1027VN)/Xám - Hàng chính hãng",
                                new BigDecimal(11900000),
                                1000L,
                                cv.categoryLaptop,
                                bv.brandMSI,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("a20ac84526ab0eb71b0ed8772f67dab5.png"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><h3>MSI Modern 14 B11MOU i3 (1027VN) sở hữu một thiết kế mỏng nhẹ, sang trọng với vỏ bọc bền bỉ, hiệu năng ổn định từ bộ vi xử lý&nbsp;Intel thế hệ 11, đa nhiệm tốt, mang đến một lựa chọn chất lượng tốt cho nhu cầu tìm kiếm&nbsp;laptop học tập - văn phòng.</h3>\n"
                                                +
                                                "<h3>Thiết kế mỏng nhẹ, bền bỉ</h3>\n" +
                                                "<p>Máy được bao bọc bởi lớp vỏ kim loại cứng cáp, sang trọng cùng gam màu xám hiện đại tạo nên sự tinh tế của máy. Linh hoạt hơn khi laptop chỉ mang trọng lượng&nbsp;<strong>1.3 kg</strong>&nbsp;và độ dày&nbsp;<strong>18.1 mm</strong>&nbsp;đủ mỏng gọn để tiện lợi mang theo laptop sử dụng mọi lúc, mọi nơi bạn cần.</p>\n"
                                                +
                                                "<p>MSI Modern 14 được ứng dụng tiêu chuẩn&nbsp;độ bền chuẩn quân đội MIL STD 810G, mang đến sức mạnh bảo vệ ấn tượng cho laptop trước các tình huống va đập, rung sốc, bụi bẩn, trong quá trình sử dụng, nâng cao tuổi thọ của máy.</p>\n"
                                                +
                                                "<p>Phục vụ nhu cầu học tập, làm việc cơ bản,&nbsp;laptop MSI Modern&nbsp;sử dụng bàn phím có hành trình phím tốt, nhấn êm và nhạy, đồng thời tích hợp&nbsp;<strong>đèn nền&nbsp;</strong>để&nbsp;bạn có thể soạn thảo một cách chuẩn xác, tiện lợi dù trong điều kiện thiếu sáng.</p>\n"
                                                +
                                                "<p>Được trang bị đầy đủ các cổng kết nối cần thiết, bao gồm USB 3.2 x 2, HDMI, jack tai nghe 3.5 mm, USB Type-C, khe đọc thẻ Micro SD và chuẩn không dây&nbsp;<strong>Bluetooth 5.1, Wi-Fi 6&nbsp;(802.11ax)\u200B&nbsp;</strong>chất lượng cao, đủ đáp ứng mọi nhu cầu kết nối thông thường sử dụng cho học tập, làm việc hay giải trí trên&nbsp;laptop.</p>\n"
                                                +
                                                "<p>Cùng với&nbsp;<strong>HD Webcam</strong>, việc học online hay tham dự các buổi họp trực tuyến qua ứng dụng Zoom, Microsoft Teams hay Google Meet,… sẽ thêm tiện lợi, hiệu quả.</p>\n"
                                                +
                                                "<h3>Hiệu năng ổn định, đủ dùng</h3>\n" +
                                                "<p>Sở hữu dòng chip<strong>&nbsp;Intel Core i3 Tiger Lake 1115G4</strong>&nbsp;với tốc độ xử lý cơ bản đạt&nbsp;<strong>3.00 GHz</strong>&nbsp;và Turbo Boost lên đến&nbsp;<strong>4.1 GHz</strong>,&nbsp;laptop MSI&nbsp;chạy đa nhiệm mạnh mẽ, mượt mà trên các ứng dụng văn phòng quen thuộc như Word, Excel, PowerPoint, hay duyệt web, tra cứu thông tin cũng diễn ra trơn tru hơn.</p>\n"
                                                +
                                                "<p>Sự hỗ trợ của card tích hợp&nbsp;Intel UHD Graphics&nbsp;giúp cải thiện chất lượng đồ họa trên máy, màn hình trải nghiệm cùng thao tác ứng dụng mướt hơn, hỗ trợ bạn thiết kế, chỉnh sửa hình ảnh đơn giản trên các phần mềm AI, Canva, một cách hiệu quả.</p>\n"
                                                +
                                                "<p>RAM 8 GB<strong>&nbsp;</strong>chuẩn<strong>&nbsp;DDR4 2 khe</strong>&nbsp;(1 khe&nbsp;<strong>8 GB</strong>&nbsp;+ 1 khe rời) với tốc độ Bus RAM&nbsp;<strong>3200 MHz</strong>&nbsp;đẩy mạnh khả năng đa nhiệm, giữ hiệu suất máy ổn định khi hoạt động hàng giờ liền với nhiều ứng dụng được mở đồng thời, không lo giật lag, đơ màn hình khi thao tác. Đặc biệt hỗ trợ nâng cấp RAM đến&nbsp;<strong>64 GB</strong>&nbsp;để laptop cải thiện ấn tượng sức mạnh của nó.</p>\n"
                                                +
                                                "<p>Ổ cứng&nbsp;SSD&nbsp;256 GB<strong>&nbsp;NVMe PCle</strong>&nbsp;(có thể tháo rời lắp thanh khác tối đa&nbsp;<strong>2 TB</strong>) thoải mái cho nhu cầu lưu trữ các dữ liệu cần thiết trên máy, rút ngắn thời gian khởi động máy, truy xuất dữ liệu nhanh chóng và ổn định, đảm bảo tốc độ và hiệu quả công việc.</p>\n"
                                                +
                                                "<h3>Trải nghiệm khung hình sắc nét chi tiết, âm thanh sống động lan tỏa</h3>\n"
                                                +
                                                "<p>Laptop MSI Modern có kích thước&nbsp;màn hình 14 inch, độ phân giải đạt chuẩn&nbsp;<strong>Full HD&nbsp;(1920 x 1080)</strong><strong>&nbsp;</strong>hiển thị khung hình với các chi tiết rõ nét. Kết hợp cùng&nbsp;tấm nền IPS&nbsp;không chỉ tạo góc nhìn rộng lên đến<strong>&nbsp;178 độ</strong>&nbsp;mà còn nâng cao chất lượng tương phản của màu sắc và hình ảnh, trải nghiệm thêm đã mắt.</p>\n"
                                                +
                                                "<p>Độ sáng màn hình&nbsp;<strong>250 nits</strong>&nbsp;và công nghệ chống chói&nbsp;Anti Glare&nbsp;cho chất lượng hiển thị màn hình ổn định cả khi dùng dưới trời nắng, không lóa mờ hay bóng gương, bảo vệ thị giác. Màn hình mỏng hơn, toả nhiệt ít hơn, tiết kiệm điện năng nhờ công nghệ&nbsp;<strong>LED Backlit</strong>.</p>\n"
                                                +
                                                "<p>Kết hợp cùng chuẩn âm thanh chất lượng cao với hiệu ứng âm vòm sống động, lọc nhiều, khử ồn tốt từ công nghệ&nbsp;Hi-Res Audio&nbsp;và&nbsp;Nahimic Audio&nbsp;mang đến những khung giờ giải trí âm nhạc, phim ảnh, thể thao,… đầy thỏa mãn trên chiếc laptop cá nhân của bạn.</p>\n"
                                                +
                                                "<p>Tìm kiếm một chiếc laptop bền bỉ, hiệu năng ổn định, giá tốt để sử dụng cho nhu cầu học tập và làm việc cơ bản thì&nbsp;MSI Modern 14 B11MOU i3 (1027VN)&nbsp;sẽ là ứng cử viên sáng giá cho bạn lựa chọn.</p><p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(
                                new HashSet<>(this.imageRepo
                                                .saveAllAndFlush(Image.createImageProductGallery(new String[] {
                                                                "a20ac84526ab0eb71b0ed8772f67dab5.png",
                                                                "033137ef5c98f5be9c9e529602e3a562.jpg",
                                                                "d6126d7647193b1c021ef3b36d682256.jpg",
                                                                "b8577379dcd4d0534fc0a8c0dc508700.jpg",
                                                                "f88549392f7363680b08131f45c5f467.jpg",
                                                                "739d10ed9d95d6e5a30c4a8720353447.jpg", }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Thương hiệu", "MSI" }, { "Xuất xứ thương hiệu", "Đài Loan" },
                                { "Xuất xứ", "Trung Quốc" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Máy Tính Xách Tay HUAWEI MateBook 14 (8GB/512GB) | Intel Core Thế Hệ Thứ 11 | Màn Hình HUAWEI 3:2 Fullview 2k | Nút Nguồn Vân Tay | Hàng Chính Hãng",
                                new BigDecimal(24990000),
                                1000L,
                                cv.categoryLaptop,
                                bv.brandHuawei,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("61de5262edc2c0c23b693a3142f28545.png"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><p><strong>Matebook D15 11th</strong></p>\n"
                                                +
                                                "<p><label class=\"_1A0RCW\">Hạn bảo hành - </label>24 tháng</p>\n" +
                                                "<p>*Hình ảnh sản phẩm và nội dung hiển thị trên các trang nêu trên chỉ nhằm mục đích tham khảo. Tính năng và thông số kỹ thuật của sản phẩm thực tế(bao gồm nhưng không giới hạn ở kiểu dáng, màu sắc và kích thước), cũng như nội dung hiển thị thực tế(bao gồm nhưng không giới hạn ở hình nền, giao diện người dùng và biểu tượng)có thể thay đổi.</p>\n"
                                                +
                                                "<p>**Tất cả dữ liệu trong các trang nêu trên là giá trị lý thuyết do các phòng nghiên cứu nội bộ của HUAWEI thu thập qua các thử nghiệm được thực hiện trong điều kiện cụ thể. Để biết thêm thông tin, hãy tham khảo chi tiết sản phẩm nêu trên. Dữ liệu thực tế có thể thay đổi do khác biệt về từng sản phẩm, phiên bản phần mềm, tình trạng ứng dụng và yếu tố môi trường. Mọi dữ liệu đều dựa trên sử dụng thực tế.</p>\n"
                                                +
                                                "<p>***Do các thay đổi trong thời gian thực liên quan đến các lô sản phẩm, yếu tố sản xuất và cung ứng nên để cung cấp thông tin, thông số kỹ thuật và tính năng sản phẩm chính xác, HUAWEI có thể điều chỉnh mô tả văn bản và hình ảnh trong các trang thông tin nêu trên theo thời gian thực sao cho tương ứng với hiệu suất sản phẩm, thông số kỹ thuật, chỉ số và cấu phần của sản phẩm thực tế. Thông tin sản phẩm có thể được thay đổi và điều chỉnh như trên mà không thông báo.</p><p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(
                                new HashSet<>(this.imageRepo
                                                .saveAllAndFlush(Image.createImageProductGallery(new String[] {
                                                                "caae25ae9ebf0e354ca7bc9638e04eff.jpg",
                                                                "e34088e8c7066e039faf7a9218c8eff9.jpg",
                                                                "2fdf65ccfa9fe39aeab799dffa063b23.png",
                                                                "f66c0eff33287acb58fd6b2c95de0b94.png",
                                                                "61de5262edc2c0c23b693a3142f28545.png", }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Bluetooth", "Bluetooth 5.1" },
                                { "Thương hiệu", "Huawei" },
                                { "Xuất xứ thương hiệu", "China" },
                                { "Model", "HUAWEI MateBook D 15 11th" },
                                { "Xuất xứ", "china" },
                                { "Trọng lượng sản phẩm", "Khoảng 1,56 kg" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Laptop Dell Inspiron 14 5406 TYCJN1 (Core i7-1165G7/ 8GB DDR4 3200MHz/ 512GB M.2 PCIe NVMe/ MX330 2GB GDDR5/ 14 FHD IPS/ Win10) - Hàng Chính Hãng",
                                new BigDecimal(26590000),
                                1000L,
                                cv.categoryLaptop,
                                bv.brandDell,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("cde8081e0b3dad57dff98792f8f07009.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\"><h3>Cảm nhận sức mạnh</h3>\n" +
                                                "<p>Bộ xử lý Intel Core I7 Thế hệ thứ 11 mới nhất mang lại khả năng phản hồi đáng kinh ngạc và đa nhiệm mượt mà, liền mạch. Dễ dàng chuyển đổi qua lại giữa các ứng dụng đang mở với bộ nhớ DDR4 lên đến 8GB Buss 3200.</p>\n"
                                                +
                                                "<h3>Hiệu suất hoàn hảo</h3>\n" +
                                                "<p>Tận hưởng ổ SSD PCIe lên tới 512GB để có hiệu suất nhanh và ổn định khi di chuyển. Ngoài ra, bạn có khả năng mở rộng lên hai SSD NVMe (M.2) và người dùng nâng cao có thể thiết lập hai SSD trong một mảng RAID 0 hoặc RAID 1, có thể định cấu hình trong BIOS.</p>\n"
                                                +
                                                "<h3>Kết nối đa năng</h3>\n" +
                                                "<p>Cổng USB Type C đa chức năng mang lại khả năng tương thích ngoại vi mở rộng, đồng thời hỗ trợ cổng hiển thị và phân phối điện để bạn có thể có nhiều không gian màn hình hơn khi muốn.</p>\n"
                                                +
                                                "<h3>Tận hưởng khung cảnh</h3>\n" +
                                                "<p>Thoải mái với màn hình rộng rãi của máy tính xách tay 14 inch và xem mọi cảnh theo phong cách với màn hình góc nhìn rộng FHD tuyệt đẹp. Đường viền tràn viền hẹp đẹp mắt và tỷ lệ màn hình trên thân máy mở rộng sẽ tập trung sự chú ý của bạn vào nơi đó thuộc về trên màn hình của bạn.</p><p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(
                                new HashSet<>(this.imageRepo
                                                .saveAllAndFlush(Image.createImageProductGallery(new String[] {
                                                                "1011e3ab318ab8f3d14967e7b2ae2eb3.jpg",
                                                                "f05f54b5eeef810f376a0e0d456fab9e.jpg",
                                                                "e5ab1bb39222b193dcce0809e8dc8796.jpg",
                                                                "d74d8e6b6adb2b1fdda35165e0f8f43b.jpg",
                                                                "3c5c2964b7e5b44a773e8484ca7fe2bb.jpg", }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Bluetooth", "v5.1" },
                                { "Thương hiệu", "Dell" },
                                { "Xuất xứ thương hiệu", "Mỹ" },
                                { "Card đồ họa", "NVIDIA GeForce MX330 2GB GDDR5 + Intel Iris Xe Graphics" },
                                {
                                                "Kết nối",
                                                "2 x USB 3.2 Gen 1 ports Type A; 1 x USB 3.2 Gen2 Type-C with DisplayPort; 1 x HDMI 1.4b port; 1 x SD-card slot; 1 x headset (headphone and microphone combo) port" },
                                { "CPU", "Intel Core i7-1165G7 Processor" },
                                { "Dung lượng ổ cứng", "512GB SSD M.2 PCIe NVMe" },
                                { "Xuất xứ", "Trung Quốc" },
                                { "RAM", "8GB DDR4 3200MHz (1 x 8GB)" },
                                { "Độ phân giải", "Full HD (1920 x 1080)" },
                                { "Wifi", "Intel Wi-fi 6 2x2 (Gig+)" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Máy tính xách tay Asus BR1100FK cảm ứng Chip Intel N6000 | RAM 8GB | 128GB-EMMC | 11.6 Inch IPS | Wifi6 | 3C42WHR | win bản quyền - Hàng Chính Hãng",
                                new BigDecimal(14650000),
                                1000L,
                                cv.categoryLaptop,
                                bv.brandAsus,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("30175cfef628b991ebf045b856eb7975.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><h3>Laptop Asus BR1100FKA&nbsp;hướng tới phân khúc&nbsp;laptop học tập - văn phòng, với kích thước nhỏ nhắn và thiết kế tiện ích tựa như chiếc máy tính bảng, để phục vụ hiệu quả cho những buổi học trực tuyến, cùng cấu hình ổn để đáp ứng nhu cầu giải trí cơ bản.</h3>\n"
                                                +
                                                "<h3>Nhỏ gọn, bền bỉ với tiêu chuẩn độ bền quân đội Mỹ</h3>\n" +
                                                "<p>Asus BR1100FKA được thiết kế rất nhỏ gọn, kích thước không nhỉnh hơn quá nhiều so với máy tính bảng, với trọng lượng máy&nbsp;<strong>1.4 kg</strong>, độ dày&nbsp;<strong>20 mm</strong>&nbsp;thoải mái để mang theo sử dụng ở mọi nơi.</p>\n"
                                                +
                                                "<p></p>\n" +
                                                "<p>Được nhắm tới phục vụ nhu cầu học tập, đặc biệt dễ dùng cho trẻ nhỏ, laptop Asus trang bị&nbsp;<strong>bản lề 360 độ</strong>, gập mở linh hoạt như 1 máy tính bảng thực thụ, giúp bạn sử dụng được ở nhiều tư thế sao cho thoải mái nhất.</p>\n"
                                                +
                                                "<p></p>\n" +
                                                "<p>Đặc biệt hơn với&nbsp;<strong>màn hình cảm ứng đa điểm</strong>&nbsp;cho các thao tác tương tác màn hình thêm đơn giản và nhanh chóng trong các buổi học, bạn chỉ cần chạm vào để thực hiện thao tác mà không cần sử dụng chuột hay bàn phím.</p>\n"
                                                +
                                                "<p>Laptop&nbsp;có vỏ ngoài bằng nhựa, cấu trúc chắc chắn và cực kỳ bền tốt, chất lượng đạt<strong>&nbsp;chuẩn quân đội Mỹ MIL-STD-810H</strong>, bảo vệ máy trước các sự cố va đập nhẹ, rung sốc hay sự thay đổi nhiệt độ đột ngột, sương muối, bụi bẩn, nhờ đó mà bạn thoải mái sử dụng laptop mà không ái ngại các tổn hại do ngoại lực thông thường.</p>\n"
                                                +
                                                "<p>Hệ bàn phím của máy với phím nhấn êm và nhạy, khoảng cách phím tốt cho thao tác soạn thảo nhanh và chuẩn xác, cấu trúc bàn phím đơn giản, dễ dùng cho trẻ nhỏ.</p>\n"
                                                +
                                                "<p>Máy hỗ trợ 2 chuẩn kết nối không dây chất lượng cao&nbsp;<strong>Bluetooth 5.2</strong>&nbsp;và&nbsp;<strong>Wi-Fi 6&nbsp;(802.11ax)</strong>&nbsp;mang đến kết nối ổn định và liền mạch cho các buổi học trực tuyến. Đồng thời, các cổng ngoại vi USB 3.2, HDMI, jack tai nghe 3.5 mm, LAN (RJ45), USB 2.0, USB Type-C đáp ứng tốt các nhu cầu kết nối phục vụ học tập, giải trí và làm việc khi cần.</p>\n"
                                                +
                                                "<p></p>\n"
                                                + "<h3>Màn hình cảm ứng rõ nét, âm thanh to rõ, trong trẻo</h3>\n" +
                                                "<p>Chiếc&nbsp;laptop Asus&nbsp;này có kích thước&nbsp;<strong>màn hình 11.6 inch</strong>, độ phân giải đạt chuẩn&nbsp;<strong>HD (1366 x 768)&nbsp;</strong>với độ sáng màn hình&nbsp;<strong>250 nits</strong>&nbsp;đủ đáp ứng tiêu chuẩn màn hình rõ nét, hình ảnh ổn định, màu sắc tương phản tốt, dễ nhìn, không gây khó chịu cho mắt khi dùng liên tục nhiều giờ.</p>\n"
                                                +
                                                "<p>Bên cạnh đó,&nbsp;<strong>HD Webcam</strong>&nbsp;với khả năng ghi hình rõ nét hỗ trợ tích cực khi cần giao tiếp video, học trực tuyến tiện lợi.&nbsp;<strong>Công tắc khóa camera</strong>&nbsp;bảo vệ sự riêng tư của bạn trong những trường hợp cần thiết.</p>\n"
                                                +
                                                "<p>Cùng với đó là công nghệ&nbsp;SonicMaster audio&nbsp;mang đến chất lượng âm thanh to rõ, khuếch đại tốt, các dải âm rành mạch, đảm bảo cả tiêu chí nghe nhìn khi sử dụng laptop cho học tập, làm việc hay giải trí.</p>\n"
                                                +
                                                "<h3>Hiệu năng ổn định, đủ dùng</h3>\n" +
                                                "<p>Asus BR1100FKA được trang bị bộ xử lý&nbsp;<strong>Intel Pentium N6000</strong>&nbsp;có tốc độ CPU trung bình&nbsp;<strong>1.1 GHz</strong>, tối đa đến&nbsp;<strong>3.3 GHz</strong>&nbsp;khi ép xung, đáp ứng đa tác vụ các ứng dụng văn phòng, học tập trên Word, nhập liệu, tính toán qua Excel, soạn bản thuyết trình với PowerPoint, Kết hợp card tích hợp&nbsp;<strong>Intel UHD Graphics&nbsp;</strong>nâng cao khả năng đồ hoạ, mang đến sự chuyển động giữa các khung hình mượt mà hơn khi xem phim, duyệt web,</p>\n"
                                                +
                                                "<p>Laptop RAM 8GB&nbsp;chuẩn<strong>&nbsp;DDR4 (On board)</strong>, tốc độ Bus RAM&nbsp;<strong>2933 MHz</strong>&nbsp;hỗ trợ đa nhiệm thêm ổn định và mượt mà, giúp máy chạy đồng thời nhiều ứng dụng mà không bị lag hay đơ màn hình.</p>\n"
                                                +
                                                "<p><strong>Ổ cứng 128 GB eMMC</strong>&nbsp;đủ dung lượng cho nhu cầu lưu trữ cá nhân trên laptop, hỗ trợ khởi động và truy xuất dữ liệu nhanh chóng, ổn định hơn, di chuyển nhanh nhạy giữa các ứng dụng được mở, ổn định hiệu suất sử dụng.</p>\n"
                                                +
                                                "<p><strong>ASUS BacGuard Kháng khuẩn toàn diện</strong> Bề mặt của máy tính xách tay chứa hàng ngàn vi khuẩn tiềm ẩn nguy hại. Để giữ cho ASUS BR1100F luôn sạch sẽ, bàn phím, chuột cảm ứng và phần kê tay được phủ lớp kháng khuẩn ASUS BacGuard6,7. Trang bị này đã được kiểm định theo tiêu chuẩn ISO 22196 được quốc tế công nhận là ức chế sự phát triển của vi khuẩn hơn 99% trong thời gian 24 giờ. ASUS BacGuard cũng có khả năng chịu được các sản phẩm vệ sinh chứa cồn, cho phép bạn vệ sinh bề mặt máy tính và gần như không lo bị vi khuẩn gây hại tấn công.</p>\n"
                                                +
                                                "<p>Nếu bạn đang tìm kiếm một chiếc laptop nhỏ gọn, tiện lợi để phục vụ chủ đích cho nhu cầu học tập cơ bản, lựa chọn laptop Asus BR1100FKA hẳn sẽ rất tương thích, mang lại những trải nghiệm thích thú và hài lòng.</p>\n"
                                                +
                                                "<p>&nbsp;</p><p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(
                                new HashSet<>(this.imageRepo
                                                .saveAllAndFlush(Image.createImageProductGallery(new String[] {
                                                                "b406b7391b90c8455a30193acaac74b5.png",
                                                                "e22ffa3b7ba4a02312a2e6fbd0276344.png",
                                                                "0ea584fd8db44147da78494ab5f4a8ea.png",
                                                                "ffcc41f4adf0562200f7cb568f14b32d.png",
                                                                "b1b5efb933a42df7657a352b0850e3ca.png",
                                                                "1c9bca5d2a252ee4b4223ddd58cbdd3f.jpg",
                                                                "84db9a4f26c7829f9396bdd73a476646.jpg",
                                                                "6a0f4c8d5556c3f86fecbf2f19db8fd3.jpg",
                                                                "a442a1a7d6d65c874add81595147facd.jpg",
                                                                "b76b743ca15aee6fe0b47e3482745a5c.jpg", }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Bluetooth", "5" },
                                { "Bộ nhớ đồ họa", "UHD" },
                                { "Thương hiệu", "Asus" },
                                { "Xuất xứ thương hiệu", "Đài Loan" },
                                { "Camera", "dual Camera" },
                                { "Card đồ họa", "Onboard" },
                                { "Chip set", "N6000" },
                                { "CPU", "Intel" },
                                { "Loại/ Công nghệ màn hình", "IPS" },
                                { "Giao tiếp mạng", "Có dây + không dây" },
                                { "Model", "BR1100FAK" },
                                { "Kết nối không dây khác", "Wifi + B;uetooth" },
                                { "Loại ổ đĩa", "eMMC" },
                                { "Loại pin", "nguyên khối" },
                                { "Loại RAM", "DDR4" },
                                { "Cổng internet (LAN)", "RJ45" },
                                { "Xuất xứ", "Trung Quốc" },
                                { "Trọng lượng sản phẩm", "2 kg" },
                                { "RAM", "8 GB" },
                                { "Hệ điều hành", "Win bản quyền" },
                                { "Thiết kế card", "Onboard" },
                                { "Wifi", "6" } }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Laptop Acer Aspire 3 A314 35 P3G9 N6000/4GB/256GB/Win11 - Hàng Chính Hãng",
                                new BigDecimal(9780000),
                                1000L,
                                cv.categoryLaptop,
                                bv.brandAcer,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("455cd70b893a01f4fa432dc2db2ec80c.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\">\n" +
                                                "<p>Laptop Acer Aspire 3 A314 35 P3G9 (NX.A7SSV.007) là chiếc máy phù hợp với học sinh, sinh viên với thiết kế mỏng nhẹ và hiệu năng ổn định của chip Intel và card tích hợp trên máy đáp ứng tốt các nhu cầu học tập - văn phòng, tác vụ cơ bản hằng ngày.<br>• Laptop Acer được trang bị CPU Intel Pentium N6000 cho hiệu năng tốt cùng card màn hình tích hợp Intel UHD Graphics giúp chiếc máy vận hành tiết kiệm điện năng nhưng vẫn đảm bảo ổn định các ứng dụng như Word, Excel, PowerPoint,</p>\n"
                                                +
                                                "<p>• RAM 4 GB cho khả năng đa nhiệm các tác vụ tốt cùng SSD 256 GB tăng tốc độ truy xuất dữ liệu, khởi động ứng dụng và máy tính nhanh chóng.</p>\n"
                                                +
                                                "<p>• Diện mạo laptop được hoàn thiện bằng lớp vỏ nhựa, tạo nên tổng thể hài hoà giúp chiếc máy chỉ vỏn vẹn nặng 1.45 kg và dày 19.9 mm, khá nhỏ gọn và tiện lợi để mang theo bên người.</p>\n"
                                                +
                                                "<p>• Laptop Acer Aspire có màn hình 14 inch, độ phân giải HD (1366 x 768) cho chất lượng hiển thị rõ ràng đi kèm công nghệ TFT, LED Blacklit, Acer ComfyView giảm tình trạng chói, loá, ánh sáng xanh, bảo vệ thị giác người dùng.</p>\n"
                                                +
                                                "<p>• Đa dạng cổng kết nối tiện lợi cho người dùng để kết nối với thiết bị ngoại vi như 2 x USB 3.2, HDMI, Jack tai nghe 3.5 mm, LAN (RJ45), USB 2.0.</p>\n"
                                                +
                                                "<p>• Công nghệ âm thanh Stereo speakers giúp tái tạo âm thanh tốt, mang lại trải nghiệm nghe nhạc hay hơn với âm thanh di chuyển giữa 2 loa.</p>\n"
                                                +
                                                "<p>&nbsp;</p>\n" +
                                                "<p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "b20dcefad6725c14a5bdc816b884fe9e.jpg",
                                                                "38e79fafb720127172c434c66a40d89b.jpg",
                                                                "975c6b771410fbf3e63bd164b34b0ec2.jpg",
                                                                "bc042bb67122cceea04ddd63c2ba4a02.jpg",
                                                                "8bb5271ea4ac58c3ed856844da8ac277.jpg",
                                                                "4dcab27138d4fd61bc2c6293bc2d8fbb.jpg",
                                                                "7fd1a39e2a24f4cc5910ae7fac05a498.jpg",
                                                                "35f72bfad50149fb49e51ac73eca419c.jpg",
                                                                "c732ba31a087d76d1ef8a0e677623c1c.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Thương hiệu", "Acer" }, { "Xuất xứ thương hiệu", "Trung Quốc" },
                                { "Xuất xứ", "Trung Quốc" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Laptop Lenovo ThinkBook 13s G3 ACN (20YA0039VN) | AMD Ryzen 7 5800U | 8GB | 512GB SSD | 13.3 inch IPS | Win 11 | Hàng chính hãng",
                                new BigDecimal(15930000),
                                1000L,
                                cv.categoryLaptop,
                                bv.brandLenovo,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("1714e39c1f5fa691d50cc9411f765c9f.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\">\n" +
                                                "<p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(
                                this.imageRepo.saveAllAndFlush(
                                                Image.createImageProductGallery(
                                                                new String[] {
                                                                                "55b5afd6bf6e5f3edc342122da8c5391.png",
                                                                                "0159021820bd33df4d9f88585f5e3d6e.png",
                                                                                "d1e8a72bfe7eae357e600510bb39f1d2.png",
                                                                                "4e256962969365a329e2256afac59b04.png",
                                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Thương hiệu", "Lenovo" },
                                { "Xuất xứ thương hiệu", "Trung Quốc" },
                                { "Xuất xứ", "Trung Quốc" },
                                { "CPU", "AMD Ryzen 7 5800U Processor (1.90 GHz, 16MB Cache, Up to 4.40 GHz, 8 Cores 16 Threads)" },
                                { "Memory", "8GB LPDDR4x Bus 4266MHz Memory Onboard" },
                                { "Hard Disk", "512GB PCIe NVMe M.2 SSD" },
                                { "VGA", "AMD Radeon Graphics Vega" },
                                {
                                                "Display",
                                                "13.3 inch WUXGA (1920 x 1200) Low Power IPS 300 Nits Anti Glare 100% sRGB Color Gamut Dolby Vision" },
                                {
                                                "Other",
                                                "Speakers Dolby Audio Harman Branded, Webcam 720p, Backlight Keyboard, USB 3.2 Gen 1 Type A, USB 3.2 Gen 1 Type A (Always On), USB 3.2 Type C (Support Data Transfer, Power Delivery 3.0 and DisplayPort 1.4), HDMI 2.0b, Headphone / Microphone Combo Jack, Firmware TPM 2.0, Finger Print Reader on Smart Power Button" },
                                { "Wireless", "Intel Wi Fi 6 (802.11ax) + Bluetooth 5.1" },
                                { "Battery", "4 Cells 56 Whrs Battery" },
                                { "Weight", "1,30 Kg" },
                                { "SoftWare", "Windows 11 Home SL 64bit" },
                                { "Màu sắc", "Mineral Grey" },
                                { "Bảo hành", "24 tháng" },
                                { "VAT", "Đã bao gồm VAT" }
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Tủ lạnh Samsung Inverter 208 lít RT19M300BGS/SV",
                                new BigDecimal(7089500),
                                1000L,
                                cv.categoryRefrigerator,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("248fc945469dac7ad47b94fc36319cc0.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><h3>Thiết kế hiện đại, sang trọng</h3>\n"
                                                +
                                                "<p><strong> Tủ Lạnh Inverter Samsung RT19M300BGS/SV (208L) </strong> có thiết kế tinh tế, sáng tạo, tạo cho không gian nhà bạn thêm hiện đại. Tủ lạnh có dung tích 208 lít, phù hợp với gia đình có từ 3-5 thành viên.</p>\n"
                                                +
                                                "<h3>Mát lạnh đồng đều khắp các ngăn tủ</h3>\n" +
                                                "<p>Công nghệ All-around Cooling giúp duy trì nhiệt độ và độ ẩm luôn ổn định. Không khí lạnh lan tỏa đều khắp tủ, giúp thực phẩm tươi ngon trong thời gian lâu hơn.</p>\n"
                                                +
                                                "<h3>Khay kính cường lực và đèn Led tiết kiệm</h3>\n" +
                                                "<p>Tủ lạnh được trang bị hệ thống khay đựng làm bằng kính cường lực chắc chắn có thể chịu được khối lượng lên đến 150kg. Dễ dàng chứa nhiều loại trái cây yêu thích và bình chứa nặng mà không sợ ảnh hưởng đến độ bền của kệ.</p>\n"
                                                +
                                                "<p>Hệ thống đèn LED mới được thiết kế nhỏ và tiết kiệm năng lượng hơn các đèn chiếu sáng thông thường. Nhờ đó, không chỉ không gian tủ lạnh được mở rộng, ánh sáng dịu mắt lan tỏa đến từng góc tủ, mà bạn còn giảm được lượng điện tiêu thụ.<br><br></p>\n"
                                                +
                                                "<h3>Miếng đệm cửa kháng khuẩn, chống nấm mốc</h3>\n" +
                                                "<p>Vi khuẩn gây hại có thể tích tụ nhiều ở viền xung quanh cửa tủ lạnh. Nhờ miếng đệm làm từ các chất liệu và thiết kế kháng khuẩn ngăn chặn nấm mốc, vi khuẩn xâm nhập từ bên ngoài. Thực phẩm bên trong tủ lạnh sẽ luôn được bảo vệ an toàn và đảm bảo vệ sinh.</p>\n"
                                                +
                                                "<h3>Công nghệ làm lạnh chống đóng tuyết</h3>\n" +
                                                "<p>Tủ lạnh RT19M300BGS/SV được trang bị công nghệ chống đóng tuyết tăng cường luân chuyển không khí, nhanh chóng đạt đến nhiệt độ ổn định và cân bằng đến mọi ngóc ngách bên trong tủ lạnh. Thức ăn tươi ngon, không bị đông đá và giữ nguyên giá trị dinh dưỡng. Bạn sẽ không cần phải mất quá nhiều thời gian cho việc rã đông thực phẩm.</p>\n"
                                                +
                                                "<h3>Ngăn chứa đựng rau quả</h3>\n" +
                                                "<p>Ngăn chứa độc đáo của tủ lạnh Samsung lưu trữ nhiều loại trái cây và rau quả khác nhau. Ngăn chứa cực lớn giúp bạn dễ dàng tìm kiếm thực phẩm yêu thích mà không tốn nhiều không gian trong phòng bếp của bạn.</p><p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "8d8292a9a047c654f659325d2de542d7.jpg",
                                                                "2.u5395.d20170824.t162040.468900.jpg",
                                                                "9b74354498138aecf37f12a415ed6f01.jpg",
                                                                "3.u5395.d20170824.t162040.497745.jpg",
                                                                "38776c85ca524bbde2ab240afe10e2ce.jpg",
                                                                "0bb1f8b8ce8682c2f3ce99d1fa5a0977.jpg",
                                                                "87bbe310027b4d3a89fecda6b35aa839.jpg",
                                                                "bff39e49c2b3620289977ec240cc6906.jpg",
                                                                "5.u5395.d20170824.t162040.553764.jpg",
                                                                "6ffd280521b4409adf72503cf3399bab.jpg",
                                                                "eead932240e8be215f342c23af265fd7.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Thương hiệu", "Samsung" },
                                { "Chất liệu khay ngăn", "Kính chịu lực" },
                                { "Công nghệ bảo quản thực phẩm", "Ngăn rau củ lớn giữ ẩm Big Box" },
                                { "Công nghệ kháng khuẩn, khử mùi", "Bộ lọc than hoạt tính Deodorizer" },
                                { "Công nghệ làm lạnh", "Làm lạnh đa chiều" },
                                { "Công suất", "~ 0.9 Kw/ngày" },
                                { "Điện áp", "220V/50Hz" },
                                { "Kích thước", "(D x R x C) 55.5 x 63.7 x 144.5 cm" },
                                { "Dung tích ngăn đá", "63L" },
                                { "Dung tích ngăn lạnh", "155L" },
                                { "Dung tích tổng", "216L" },
                                { "Số cửa", "2" },
                                { "Công nghệ Inverter", "Có" },
                                { "Model", "RT19M300BGS/SV" },
                                { "Kiểu tủ lạnh", "Ngăn đá phía trên" },
                                { "Xuất xứ", "Việt Nam" },
                                { "Tiện ích", "Làm lạnh đa chiều, Bộ lọc than hoạt tính Deodorizer" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Tủ Lạnh Panasonic 167L Inverter NR-BA189PAVN - Kháng khuẩn AG Clean - Hàng chính hãng",
                                new BigDecimal(6390000),
                                1000L,
                                cv.categoryRefrigerator,
                                bv.brandPanasonic,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("372f24eae8ed87a1f5c0ad4e9adc141a.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><p>Thời gian giao hàng ở khu vực miền Trung (Huế, Đà Nẵng, Quảng Nam, Quảng Ngãi, Bình Định, Phú Yên) có thể từ 10-15 ngày, để biết thêm thông vui lòng liên hệ chăm sóc khách hàng qua Chatbox để được tư vấn</p>\n"
                                                +
                                                "<p>---------------------------------------------------------------------------------------------------------------------</p>\n"
                                                +
                                                "<p><span><strong>THÔNG TIN LẮP ĐẶT:</strong><br>- Tủ lạnh không cần lắp đặt. Khách hàng khi nhận tủ chỉ cần để tủ lạnh trong 4 tiếng, sau đó cắm điện là dùng bình thường<br>- Phí vận chuyển đến tầng trệt: Miễn phí (Nếu khách hàng cần bê lên tầng lầu và không có thang máy thì sẽ có phát sinh thêm phí bê lên tầng lâu, chi tiết vui lòng liên hệ qua phần Chatbox<br>- Quy định đổi trả: <br>+ Để đảm bảo được hỗ trợ đổi/ trả sản phẩm tốt và nhanh gọn nhất trong trường hợp hàng giao đến không còn nguyên vẹn/ thiếu phụ kiện/ nhận sai hàng, khách hàng cần QUAY VIDEO QUÁ TRÌNH MỞ HÀNG<br>+ Với các sản phẩm đã được cắm điện sử dụng và/ hoặc lắp đặt, gặp lỗi kĩ thuật, sản phẩm sẽ được hưởng chế độ bảo hành theo đúng chính sách của nhà sản xuất</span></p>\n"
                                                +
                                                "<p><span><strong>THÔNG TIN LẮP ĐẶT:</strong><br>- Tủ lạnh không cần lắp đặt. Khách hàng khi nhận tủ chỉ cần để tủ lạnh trong 4 tiếng, sau đó cắm điện là dùng bình thường<br>- Phí vận chuyển đến tầng trệt: Miễn phí (Nếu khách hàng cần bê lên tầng lầu và không có thang máy thì sẽ có phát sinh thêm phí bê lên tầng lâu, chi tiết vui lòng liên hệ qua phần Chatbox<br>- Quy định đổi trả: <br>+ Để đảm bảo được hỗ trợ đổi/ trả sản phẩm tốt và nhanh gọn nhất trong trường hợp hàng giao đến không còn nguyên vẹn/ thiếu phụ kiện/ nhận sai hàng, khách hàng cần QUAY VIDEO QUÁ TRÌNH MỞ HÀNG<br>+ Với các sản phẩm đã được cắm điện sử dụng và/ hoặc lắp đặt, gặp lỗi kĩ thuật, sản phẩm sẽ được hưởng chế độ bảo hành theo đúng chính sách của nhà sản xuất</span></p>\n"
                                                +
                                                "<p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "4e0daecbbab897a245acc88854fa4ee3.jpg",
                                                                "16ee90d0f40f27caec5b04cae6d94d6f.png",
                                                                "c454430c6ef519c33b68fcf60163dcc7.png",
                                                                "9059d6c5db863d18152637cd5027305c.jpg",
                                                                "698dd1fb28f75304a154f37db723d5c0.jpg",
                                                                "aa47b594dad4176999636be476d1b0c9.jpg",
                                                                "a5d1d1c6df0116f2f4e25bd08dba7cf3.jpg",
                                                                "3139fe37d7f8731d1fb2d259545ddf21.jpg",
                                                                "b752cb8e5f17d49fd586714eb3656df4.jpg",
                                                                "121e072e9888bfce9a84777359164c15.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Thương hiệu", "Panasonic" },
                                { "Xuất xứ thương hiệu", "Nhật Bản" },
                                { "Chất liệu khay ngăn", "Kính chịu lực" },
                                { "Công suất", "~ 0.95 kW/ngày" },
                                { "Dung tích ngăn đá", "53 lít" },
                                { "Dung tích ngăn lạnh", "114 lít" },
                                { "Dung tích tổng", "188 lít" },
                                { "Công nghệ Inverter", "Có" },
                                { "Model", "NR-BA189PAVN" },
                                { "Kiểu tủ lạnh", "Ngăn đá phía trên" },
                                { "Xuất xứ", "Việt Nam" },
                                {
                                                "Tiện ích",
                                                "Công nghệ kháng khuẩn Ag Clean với tinh thể bạc Ag+, Công nghệ làm lạnh:Panorama, Chế độ tiết kiệm điện:Econavi" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Tủ lạnh Sharp Inverter 150 lít SJ-X176E-SL",
                                new BigDecimal(4380000),
                                1000L,
                                cv.categoryRefrigerator,
                                bv.brandSharp,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("582fb440afb7f0fbe5bd471a7791254d.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\">MIỄN PHÍ VẬN CHUYỂN VÀ CÔNG LẮP ĐẶT - VẬT TƯ PHÁT SINH TÍNH THEO THỰC TẾ\n"
                                                +
                                                "(MÁY GIẶT, MÁY RỬA CHÉN .V.V CHỈ HỖ TRỢ GẮN VÀO ĐƯỜNG ỐNG CÓ SẴN , KHÔNG HỖ TRỢ KHOAN ĐỤC)<p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "f2d1db6f341a0c54e00145d905b3119a.jpg",
                                                                "f209b55ffaf1106072727aeaaac53093.jpg",
                                                                "35a63faa6c0b164ffdecbf630a25d8fb.jpg",
                                                                "586909fd99d32006bb8120d045e1e839.jpg",
                                                                "50aa3b3c66bd28efc3c15ae7f42e7cce.jpg",
                                                                "dda3f5de8b6a56e48941a114988ace8f.jpg",
                                                                "6ad7f462cb9e9f106168398fefd767e4.jpg",
                                                                "7f033e5c04d9f2dfbd8568ed422e791c.jpg",
                                                                "00f819f32a52a235a10ba6eb98a45e98.jpg",
                                                                "b1af232608c3343ffb8129750171cf46.jpg",
                                                                "51d12efb38ccb050d150555702fc8c93.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Thương hiệu", "Sharp" },
                                { "Chất liệu", "Thép không gỉ, kính chịu lực" },
                                { "Chất liệu khay ngăn", "Khay kính chịu lực" },
                                { "Điện áp", "220V - 240V" },
                                { "Kích thước", "128.1 x 58.7 x 53.3 cm" },
                                { "Dung tích ngăn đá", "35 lít" },
                                { "Dung tích ngăn lạnh", "115 lít" },
                                { "Số cửa", "2" },
                                { "Công nghệ Inverter", "Có" },
                                { "Model", "SJ-X176E-SL" },
                                { "Kiểu tủ lạnh", "Ngăn đá phía trên" },
                                { "Xuất xứ", "Thái Lan" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Tủ lạnh mini Aqua 50 lít AQR-D59FA-BS",
                                new BigDecimal(2590000),
                                1000L,
                                cv.categoryRefrigerator,
                                bv.brandAqua,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("3bac43e10759b7db021360947fb724e6.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\"><h3 dir=\"ltr\">Thiết kế nhỏ gọn, gàm màu đen sang trọng</h3>\n"
                                                +
                                                "<p><strong>Tủ Lạnh Mini Aqua AQR-D59FA-BS </strong>thiết kế 1 cửa mở tiện lợi cho việc lấy đồ, kiểu dáng nhỏ gọn, đơn giản nhưng không kém phần sang trọng với gam màu đen thanh lịch, phù hợp lắp đặt ở mọi không gian, đặc biệt là những nơi chật hẹp.</p>\n"
                                                +
                                                "<h3>Dung tích sử dụng 50 lít</h3>\n" +
                                                "<p>Tủ lạnh có dung tích sử dụng 50 lít. Phù hợp với nhu cầu sử dụng ít hoặc những người sống độc thân. Với dung tích này, bạn cũng có thể dùng để đựng các sản phẩm mỹ phẩm cần bảo quản nhiệt độ mát.</p>\n"
                                                +
                                                "<h3>Công nghệ làm lạnh trực tiếp</h3>\n" +
                                                "<p>Aqua AQR-D59FA-BS được ứng dụng công nghệ làm lạnh trực tiếp, đây là loại công nghệ làm lạnh cơ bản nhất, làm lạnh bên trong tủ lạnh mà không cần hoặc rất ít cần đến quạt làm mát. Việc làm lạnh được hoạt động trên nguyên tắc làm lạnh đối lưu và tạo ra hơi lạnh khác nhau ở những nơi khác nhau trong tủ lạnh.</p>\n"
                                                +
                                                "<h3>Đèn LED chiếu sáng, tiết kiệm điện</h3>\n" +
                                                "<p>Bên cạnh đó, tủ lạnh Aqua AQR-D59FA-BS còn sử dụng đèn chiếu sáng là đèn LED, tiết kiệm điện hơn rất nhiều so với đèn dây tóc thông thường. Hơn nữa, loại đèn này còn có độ bền cao, trong quá trình sử dụng không tỏa nhiều nhiệt nên không gây ảnh hưởng đến nhiệt độ bên trong tủ.&nbsp;</p><p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "5d8768573b374839aee9954d3beb719c.jpg",
                                                                "55b1cb05095f2591cb8673794cf1ca6f.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Thương hiệu", "Aqua" },
                                { "Xuất xứ thương hiệu", "Trung Quốc" },
                                { "Chất liệu", "Cửa tủ: Mặt thép" },
                                { "Kích thước", "47.6 x 49.4 x 79.5 (cm)" },
                                { "Dung tích tổng", "50L" },
                                { "Công nghệ Inverter", "Không" },
                                { "Model", "AQR-D59FA-BS" },
                                { "Kiểu tủ lạnh", "Mini" },
                                { "Xuất xứ", "Việt Nam" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Tủ lạnh Panasonic cao cấp 3 cánh NR-CW530XMMV 495L - Lấy nước ngoài - Làm đá tự động - Hàng chính hãng",
                                new BigDecimal(45990000),
                                1000L,
                                cv.categoryRefrigerator,
                                bv.brandPanasonic,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("ce40abc9191a3a319ffd5dcc1f9774f0.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><p>Thời gian giao hàng ở khu vực miền Trung (Huế, Đà Nẵng, Quảng Nam, Quảng Ngãi, Bình Định, Phú Yên) có thể từ 10-15 ngày, để biết thêm thông vui lòng liên hệ chăm sóc khách hàng qua Chatbox để được tư vấn</p>\n"
                                                +
                                                "<p>&nbsp;</p>\n" +
                                                "<p>Tủ lạnh cao cấp 3 cánh NR-CW530XMMV 495L được ra mắt với những công nghệ cấp đông độc đáo, giúp giữ trọn những dưỡng chất và hương vị nguyên bản của thực phẩm lên tới 7 ngày. PRIME+ Edition mang đến trải nghiệm đẳng cấp, đón đầu xu hướng thiết kế hiện đại và công nghệ đỉnh cao, giúp bảo quản thực phẩm trong điều kiện lý tưởng nhất. Thiết kế đơn giản nhưng không kém phần sang trọng mang lại sự cân bằng hoàn hảo giữa trải nghiệm tiệc ích đẳng cấp và thẩm mỹ tinh tế thời thượng, khơi nguồn cảm hứng sống khỏe mỗi ngày trong không gian yêu thích của riêng bạn.</p>\n"
                                                +
                                                "<p>&nbsp;</p>\n" +
                                                "<p><strong>THÔNG TIN SẢN PHẨM</strong></p>\n" +
                                                "<p><strong>Đặc điểm nổi bật</strong></p>\n" +
                                                "<p>Prime Freeze thế hệ mới mang tới nhiều bí quyết tiện lợi độc đáo, giúp bạn dễ dàng chế biến những thực đơn tươi ngon &amp; lành mạnh, khơi nguồn cảm hứng sống khỏe mỗi ngày.</p>\n"
                                                +
                                                "<p>Prime Fresh cấp đông mềm thịt, cá hoàn hảo chuẩn -3 ° C, giúp giữ trọn chất dinh dưỡng và hương vị tươi ngon tới 7 ngày. Thật tuyệt vời khi chỉ cần đi chợ 1 lần mà vẫn đầy ắp đồ tươi cả tuần, mỗi lần chế biến lại cực kỳ tiện lợi vì không mất thời gian đợi rã đông.</p>\n"
                                                +
                                                "<p>Ngăn Fresh Safe với Bộ Lọc Kiểm Soát Độ Ẩm cung cấp khả năng kiểm soát độ ẩm tối ưu trong ngăn rau chuyên biệt, là môi trường lý tưởng để bảo quản trái cây và rau củ tươi xanh lâu hơn.</p>\n"
                                                +
                                                "<p>Công nghệ nanoe X giải phóng các hạt nước có kích thước nano bay khắp các ngóc ngách tủ lạnh. Công nghệ lọc không khí này giúp đảm bảo không khí và các bề mặt bên trong tủ lạnh nhà bạn luôn an toàn và sạch sẽ.</p>\n"
                                                +
                                                "<p>nanoe X đã được chứng minh giúp vô hiệu hóa tới 99,99% * vi khuẩn, giảm dư lượng thuốc trừ sâu, và khử mùi mạnh mẽ.</p>\n"
                                                +
                                                "<p>&nbsp;</p>\n" +
                                                "<p><strong>Tiện ích &amp; Tiết kiệm điện</strong></p>\n" +
                                                "<p>1. Tự động tối ưu để tiết kiệm điện với Cảm biến thông minh</p>\n" +
                                                "<p>2. Lấy nước ngoài kháng khuẩn, khử mùi tiện lợi</p>\n" +
                                                "<p>3. Khay kính trượt có thể điều chỉnh linh hoạt</p>\n" +
                                                "<p>4. Thiết kế máy nén trên nóc tủ tối ưu không gian sử dụng bên trong</p>\n"
                                                +
                                                "<p>5. Hệ thống làm đá tự động</p>\n" +
                                                "<p>&nbsp;</p>\n" +
                                                "<p><strong>Thông số kỹ thuật</strong></p>\n" +
                                                "<p>Dung tích định mức</p>\n" +
                                                "<p>- Tổng cộng: 495 L</p>\n" +
                                                "<p>- Ngăn lạnh: 309 L</p>\n" +
                                                "<p>- Ngăn đông: 186 L (bao gồm Prime Fresh 61L)</p>\n" +
                                                "<p>Kích thước sản phẩm: 750 x 750 x 1730 mm</p>\n" +
                                                "<p>Loại xả băng: Điều khiển PCB điện</p>\n" +
                                                "<p>Mức tiêu thụ năng lượng: 418 kWh/năm</p>\n" +
                                                "<p>Mức năng lượng: 5 Sao</p>\n" +
                                                "<p>Trọng lượng tịnh: 90 kg</p>\n" +
                                                "<p>Tổng trọng lượng: 98 kg</p>\n" +
                                                "<p>Điện áp: 220 V</p>\n" +
                                                "<p>Héc: 50 Hz</p>\n" +
                                                "<p>Số cửa: 3</p>\n" +
                                                "<p>Màu cửa: Mặt Gương Đen</p>\n" +
                                                "<p>Vật liệu cửa: Gương kính</p>\n" +
                                                "<p>Bảng điều khiển: Bên ngoài cánh cửa ngăn mát</p>\n" +
                                                "<p>&nbsp;</p>\n" +
                                                "<p><strong>THÔNG TIN THƯƠNG HIỆU</strong></p>\n" +
                                                "<p>Panasonic thành lập năm 1918 bởi Konosuke Matsushita với khẩu hiệu \"A better life, a better world\" cam kết mang đến cho khách hàng một cuộc sống hiện đại và tốt đẹp hơn. Trong suốt quá trình phát triển, Panasonic từng bước khẳng định thương hiệu qua các sản phẩm được ưa chuộng trên khắp thế giới: lò vi sóng, tivi, máy giặt, tủ lạnh, điều hoà, máy lọc không khí, thiết bị hỗ trợ sức khoẻ và làm đẹp, điện thoại bàn...</p>\n"
                                                +
                                                "<p><strong>THÔNG TIN LẮP ĐẶT:</strong><br>- Tủ lạnh không cần lắp đặt. Khách hàng khi nhận tủ chỉ cần để tủ lạnh trong 4 tiếng, sau đó cắm điện là dùng bình thường<br>- Phí vận chuyển đến tầng trệt: Miễn phí (Nếu khách hàng cần bê lên tầng lầu và không có thang máy thì sẽ có phát sinh thêm phí bê lên tầng lâu, chi tiết vui lòng liên hệ qua phần Chatbox<br>- Quy định đổi trả: <br>+ Để đảm bảo được hỗ trợ đổi/ trả sản phẩm tốt và nhanh gọn nhất trong trường hợp hàng giao đến không còn nguyên vẹn/ thiếu phụ kiện/ nhận sai hàng, khách hàng cần QUAY VIDEO QUÁ TRÌNH MỞ HÀNG<br>+ Với các sản phẩm đã được cắm điện sử dụng và/ hoặc lắp đặt, gặp lỗi kĩ thuật, sản phẩm sẽ được hưởng chế độ bảo hành theo đúng chính sách của nhà sản xuất</p><p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "664d88f891b935f79bc5c144cdb3cfd6.png",
                                                                "44dbd823d5e7340297c6832d65880a8e.png",
                                                                "3d3958e343629c9d515bb4cedea61252.png",
                                                                "b2c2505f7aa020a1f48c1fae40217bfc.png",
                                                                "16b65a987d766bdd9df992784c104c64.png",
                                                                "823f5c778db1bbf15f26c50d2e82c108.png",
                                                                "bdfd9d42d6f73e4995ace176d1c5f4e0.png",
                                                                "212d50392663974006cd12e428193e1c.png",
                                                                "582b4d4a9ce44fdf68078e49a754c987.png",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Thương hiệu", "Panasonic" },
                                { "Công suất", "418 kWh/năm" },
                                { "Điện áp", "220 V" },
                                { "Dung tích ngăn đá", "186 L" },
                                { "Dung tích ngăn lạnh", "309 L" },
                                { "Dung tích tổng", "495 L" },
                                { "Model", "CW530XMM" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Tủ Lạnh 2 Cánh Panasonic 420 lít NR-BX471WGKV ngăn đá dưới - Ngăn đông mềm siêu tốc - Hàng chính hãng",
                                new BigDecimal(24190000),
                                1000L,
                                cv.categoryRefrigerator,
                                bv.brandPanasonic,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("7e1b8c8b985385ecea1d1610a4e630c4.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><p>Thời gian giao hàng ở khu vực miền Trung (Huế, Đà Nẵng, Quảng Nam, Quảng Ngãi, Bình Định, Phú Yên) có thể từ 10-15 ngày, để biết thêm thông vui lòng liên hệ chăm sóc khách hàng qua Chatbox để được tư vấn</p>\n"
                                                +
                                                "<p>---------------------------------------------------------------------------------------------------------------------</p>\n"
                                                +
                                                "<p><strong>THÔNG TIN LẮP ĐẶT:</strong><br>- Tủ lạnh không cần lắp đặt. Khách hàng khi nhận tủ chỉ cần để tủ lạnh trong 4 tiếng, sau đó cắm điện là dùng bình thường<br>- Phí vận chuyển đến tầng trệt: Miễn phí (Nếu khách hàng cần bê lên tầng lầu và không có thang máy thì sẽ có phát sinh thêm phí bê lên tầng lâu, chi tiết vui lòng liên hệ qua phần Chatbox<br>- Quy định đổi trả: <br>+ Để đảm bảo được hỗ trợ đổi/ trả sản phẩm tốt và nhanh gọn nhất trong trường hợp hàng giao đến không còn nguyên vẹn/ thiếu phụ kiện/ nhận sai hàng, khách hàng cần QUAY VIDEO QUÁ TRÌNH MỞ HÀNG<br>+ Với các sản phẩm đã được cắm điện sử dụng và/ hoặc lắp đặt, gặp lỗi kĩ thuật, sản phẩm sẽ được hưởng chế độ bảo hành theo đúng chính sách của nhà sản xuất</p>\n"
                                                +
                                                "<p><span style=\"font-weight:400\">#Panasonic #PanasonicVietnam #TuLanhPanasonic #Tủlạnhdiệtkhuẩn #PrimeFresh #BlueAg #giutrondinhduong #ngonkhoesuothe</span></p><p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "5d40fec35c880416722a1c8e81e2af7f.jpg",
                                                                "2dd199a4cad56f9decf2850ff6a6b9da.png",
                                                                "d73fed2c29c0ca4c9b00bb5e7300e3c4.png",
                                                                "79f39f39ca1668d3b8a9f2f28589f011.jpg",
                                                                "7af80f6d12cf4798387074dc9816bc86.jpg",
                                                                "05a8d182e3a57e580325eede0699e441.jpg",
                                                                "f90c13e9ded69e52125262ce44515ee8.jpg",
                                                                "9f7771ce39fc649251d2da74884ca79c.jpg",
                                                                "6830dba1b45564867223c66fda4d4168.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Tiện ích", "làm đá tự động" },
                                { "Thương hiệu", "Panasonic" },
                                { "Xuất xứ thương hiệu", "Nhật Bản" },
                                { "Chất liệu khay ngăn", "nhôm" },
                                { "Dung tích ngăn đá", "112" },
                                { "Dung tích ngăn lạnh", "308" },
                                { "Dung tích tổng", "420" },
                                { "Model", "NR-BX471WGKV" },
                                { "Xuất xứ", "Việt Nam" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Tủ Lạnh Electrolux EME3700H-A - Dung Tích 337 Lít - Công Nghệ Inverter - Hàng Chính Hãng",
                                new BigDecimal(12890000),
                                1000L,
                                cv.categoryRefrigerator,
                                bv.brandElectrolux,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("22211e58f0f561651cf199b280df1390.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><p>Tủ lạnh Electrolux Inverter 340 lít EME3700H-A sở hữu kiểu dáng thanh lịch sang trọng góp phần tô điểm căn bếp của gia đình. Nhờ trang bị công nghệ tiết kiệm điện Inverter, Công nghệ TasteGuard diệt khuẩn khử mùi kết hợp cùng những ngăn kệ đa năng linh hoạt, chiếc tủ lạnh hứa hẹn sẽ khiến bạn hài lòng với những tiện ích mà nó mang lại. Với dung tích 340 lít, chiếc&nbsp;tủ lạnh Electrolux&nbsp;này là một lựa chọn phù hợp cho những gia đình có từ 3 – 4 thành viên.&nbsp;</p>\n"
                                                +
                                                "<h3>ĐẶC ĐIỂM NỔI BẬT CỦA SẢN PHẨM:</h3>\n" +
                                                "<p><strong>- Tủ lạnh Electrolux Inverter tiết kiệm điện năng hiệu quả</strong></p>\n"
                                                +
                                                "<p>Tủ lạnh Electrolux Inverter 340 lít EME3700H-A trang bị công nghệ Inverter hiện đại giúp tiết kiệm điện năng hiệu quả. Sở hữu chiếc&nbsp;tủ lạnh Inverter&nbsp;này các bà nội trợ sẽ giảm bớt đi nỗi lo về hóa đơn tiền điện mỗi tháng. Không chỉ vậy, công nghệ Inverter còn góp phần giúp tủ lạnh vận hành ổn định êm ái và bền bỉ hơn.&nbsp;</p>\n"
                                                +
                                                "<p><strong>- Hệ thống làm lạnh EvenTemp&nbsp;</strong></p>\n" +
                                                "<p>Tủ lạnh&nbsp;trang bị hệ thống làm lạnh EvenTemp&nbsp;giữ cho thực phẩm tươi ngon lâu hơn nhờ giảm sự dao động về nhiệt độ.&nbsp;Cụ thể, công nghệ này sẽ&nbsp;làm lạnh từng ngăn riêng biệt, nhiệt độ sẽ được duy trì ổn định trong toàn bộ khoang tủ, lưu giữ hương vị và cấu trúc thực phẩm lâu hơn.</p>\n"
                                                +
                                                "<p>&nbsp;</p>\n" +
                                                "<p><strong>- Tủ lạnh loại bỏ vi khuẩn và mùi khó chịu</strong></p>\n" +
                                                "<p>Tủ lạnh Electrolux Inverter 340 lít EME3700H-A trang bị hệ thống khử mùi và diệt khuẩn hiện đại TasteGuard sử dụng bộ lọc than hoạt tính giúp loại bỏ&nbsp;&nbsp;93,5%&nbsp;vi khuẩn và những mùi khó chịu, trả lại bầu không khí trong lành mát lạnh cho tủ lạnh.&nbsp;</p>\n"
                                                +
                                                "<p>&nbsp;</p>\n" +
                                                "<p><strong>- Ngăn mát rau quả Tastelock</strong></p>\n" +
                                                "<p>Sở hữu chiếc&nbsp;tủ lạnh Electrolux&nbsp;này, bạn sẽ có được không gian lưu trữ rau xanh hiện đại. Đó chính là ngăn rau TasteLock, cho phép người dùng&nbsp;tuỳ chỉnh độ ẩm trong ngăn chứa với gờ cao su khép kín&nbsp;giữ cho rau củ và trái cây luôn tươi ngon và bảo quản được lâu hơn.</p>\n"
                                                +
                                                "<p>&nbsp;</p>\n" +
                                                "<p><strong>- Ngăn kệ linh hoạt FlexStor</strong></p>\n" +
                                                "<p>Tủ lạnh Electrolux Inverter tích hợp hệ thống ngăn kệ FlexStor giúp bạn có thể linh hoạt di chuyển các khay kệ để phù hợp với mọi nhu cầu lưu trữ, từ những hộp sữa thông thường đến những chai lọ nhỏ hay loại có hình dạng, kích thước khác nhau.</p>\n"
                                                +
                                                "<p>&nbsp;</p>\n" +
                                                "<p><strong>- Ngăn lưu trữ linh hoạt TasteSealPlus&nbsp;</strong></p>\n"
                                                +
                                                "<p>Ngoài những tiện ích trên, tủ lạnh Electrolux còn trang bị&nbsp;ngăn lưu trữ linh hoạt TasteSealPlus&nbsp;cho phép người dùng&nbsp;dễ dàng điều chỉnh nhiệt độ theo nhu cầu của, thoải mái lưu trữ mọi thứ, từ rau tươi, đến thịt và cá mềm, hoặc đồ uống lạnh và kem. Ngoài ra, người dùng sẽ có đượckhông gian thích hợp để giữ cho thực phẩm của mình luôn tươi ngon và trọn vị bằng cách tinh chỉnh cài đặt nhiệt độ thích hợp từ -12°C đến 3°C.</p>\n"
                                                +
                                                "<p>&nbsp;</p>\n" +
                                                "<h3>THÔNG SỐ KỸ THUẬT:</h3>\n" +
                                                "<p>- Model: EME3700H-A</p>\n" +
                                                "<p>- Màu sắc: BẠC</p>\n" +
                                                "<p>- Nhà sản xuất: Electrolux</p>\n" +
                                                "<p>- Xuất xứ: Thái Lan</p>\n" +
                                                "<p>- Khối lượng sản phẩm (kg): 77.6 kg</p>\n" +
                                                "<p>- Kích thước sản phẩm: 1855 x 598 x 650 mm</p>\n" +
                                                "<p>&nbsp;</p>\n" +
                                                "<h3>CHÍNH SÁCH BẢO HÀNH</h3>\n" +
                                                "<p>Tủ Lạnh Electrolux Inverter EME3700H-A được bảo hành chính hãng 24 tháng.</p>\n"
                                                +
                                                "<p>&nbsp;</p>\n" +
                                                "<p>&nbsp;</p>\n" +
                                                "<p>#tulanh #tulanh3ngan #tulanh337l #tulanhelectrolux #tulanhinverter #tulanhgiare #tulanhchinhhang #tulanhgiadinh #electrolux #bepxanh #EME3700HA</p><p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "68c8d23922e3192f734723e1f9e5086e.jpg",
                                                                "7f0ff2a1b50c3dad71abedae5e040b41.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Thương hiệu", "Electrolux" },
                                { "Xuất xứ thương hiệu", "Thái Lan" },
                                { "Chất liệu", "Chất liệu khay: Khay kính cường lực" },
                                { "Chất liệu khay ngăn", "Khay kính cường lực" },
                                { "Công nghệ kháng khuẩn, khử mùi", "FreshTaste" },
                                { "Điện áp", "220V" },
                                { "Kích thước", "1855 x 598 x 650 mm" },
                                { "Dung tích tổng", "337 Lít" },
                                { "Số cửa", "3" },
                                { "Công nghệ Inverter", "Có" },
                                { "Model", "EME3700H-A" },
                                { "Kiểu tủ lạnh", "Ngăn đá phía trên" },
                                { "Xuất xứ", "Thái Lan" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Tủ lạnh Funiki Hòa Phát FR 135CD 130 lít - Hàng Chính Hãng",
                                new BigDecimal(3179000),
                                1000L,
                                cv.categoryRefrigerator,
                                bv.brandHoaPhat,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("be0180d7926b194f8944b00be92747a1.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><h2>Tủ lạnh Funiki FR-135CD&nbsp;thiết kế dạng 2 cánh làm lạnh đa chiều, tăng hiệu quả lưu trữ thực phẩm</h2>\n"
                                                +
                                                "<p>Tủ lạnh 2 cánh&nbsp;<strong>Funiki FR-135CD</strong>&nbsp;vận hành êm ái và có khả năng tiết kiệm điện tối đa. Cơ chế vận hành đa chiều giúp tỏa lạnh và phân bổ rộng, đảm bảo thực phẩm được làm lạnh đồng đều và đảm bảo không bị héo vì quá lạnh. Ngăn đá có 2 ngăn giúp bạn đựng làm đá và trữ đông thực phẩm hiệu quả. Khay nhựa có thể tháo rời và chịu lực tốt.</p>\n"
                                                +
                                                "<p>Tủ lạnh Funiki&nbsp;thiết kế mở dễ dàng và đóng chặt nhờ đường nút gioăng cao su, đảm bảo hơi lạnh không bị thoát ra ngoài khi đóng cửa. Chân đế bằng nhựa cách ẩm nhằm duy trì độ bền cho khung máy, cũng như an toàn cho người sử dụng.</p>\n"
                                                +
                                                "<p>Tủ lạnh&nbsp;sở hữu nhiều ngăn chứa với các khay nhựa chịu lực cực tốt, có khả năng thay đổi linh hoạt giữa các khay này để phù hợp mục đích sử dụng:</p>\n"
                                                +
                                                "<ul>\n" +
                                                "<li class=\"_ilcss4_\">Ngăn bảo quản thực phẩm tươi sống</li>\n" +
                                                "<li class=\"_ilcss4_\">Ngăn đựng có nắp đậy</li>\n" +
                                                "<li>Khay đá thông minh</li>\n" +
                                                "<li>Ngăn đựng rau quả giữ ẩm cao</li>\n" +
                                                "</ul>\n" +
                                                "<p><strong>Funiki FR-135CD</strong>&nbsp;sở hữu dung tích 135 lít, giá rẻ nên đây cũng được xếp vào loại&nbsp;tủ lạnh mini&nbsp;dành cho các phòng có diện tích nhỏ hay gia đình có 2 - 3 thành viên.</p><p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Thương hiệu", "Hòa Phát" }, { "Xuất xứ thương hiệu", "Việt Nam" },
                                { "Xuất xứ", "Việt Nam" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Smart Tivi NanoCell LG 4K 43 inch 43NANO77TPA",
                                new BigDecimal(9470000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandLG,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("6674e170a7d751bb6ec645707b1fadf7.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><h3>8 triệu điểm ảnh cùng thể hiện trong 1 bức tranh</h3>\n"
                                                +
                                                "<p>Màu sắc thuần khiết được thể hiện tuyệt đẹp trên màn hình 4K Đích thực với khoảng 8 triệu điểm ảnh, TV 4K đích thực truyền tải hình ảnh sắc nét hơn và chi tiết hơn rõ rệt so với TV UHD thông thường. Với TV NanoCell, độ phân giải 4K đích thực được bổ sung công nghệ NanoCell để mang lại trải nghiệm 4K vượt qua mọi tiêu chuẩn quốc tế.</p>\n"
                                                +
                                                "<h3>Mọi màu sắc hiện lên trong veo</h3>\n" +
                                                "<p>TV LG NanoCell sử dụng các hạt nano, công nghệ Nano đặc biệt của riêng chúng tôi, để lọc và tinh chỉnh màu sắc, loại bỏ tín hiệu gây nhiễu khỏi các bước sóng RGB. Nghĩa là, chỉ những màu sắc tinh khiết, chính xác mới được hiển thị trên màn hình. Nhờ đó hình ảnh tạo ra sẽ tươi đẹp hơn, chân thực hơn, giúp nội dung của bạn trở nên sống động.</p>\n"
                                                +
                                                "<h3>Kinh ngạc từ mọi góc nhìn</h3>\n" +
                                                "<p>Màu sắc tuyệt đối của TV NanoCell đảm bảo rằng màu sắc được hiển thị chính xác cho hình ảnh trung thực ngay cả khi nhìn từ góc rộng.</p>\n"
                                                +
                                                "<h3>Chân đỡ nghệ thuật tinh tế Gallery Stand - Thiết kế tinh tế, thêm gu thẩm mỹ</h3>\n"
                                                +
                                                "<p>Tác phẩm nghệ thuật đẹp như tranh vẽ không chỉ dành để treo trên các bức tường nữa. Chân đỡ Gallery cho phép bạn tự do đặt TV và biến ngôi nhà của bạn thành một phòng trưng bày hiện đại.</p>\n"
                                                +
                                                "<h3>Chất lượng ngang tầm với màn chiếu lớn</h3>\n" +
                                                "<p>Pure Colors và một loạt các công nghệ màn hình mới nhất mang cả rạp phim về nhà bạn với LG NanoCell TV. Với công nghệ HDR nâng cao của chúng tôi, các công nghệ được nâng cấp của Dolby, và một chế độ điện ảnh mới vừa được hãng công bố sẽ mang đến trải nghiệm điện ảnh thực sự.</p>\n"
                                                +
                                                "<h3>Nơi hội tụ những gì bạn yêu thích</h3>\n" +
                                                "<p>Truy cập Ứng dụng Apple TV, Disney+ và Netflix. Chọn từ những bộ phim mới nhất, chương trình TV, phim tài liệu và thể thao trực tiếp, và tìm tất cả nội dung ở cùng một nơi.</p>\n"
                                                +
                                                "<h3>Filmmaker Mode - Hiện thực hóa góc nhìn của đạo diễn</h3>\n" +
                                                "<p>Filmmaker Mode tắt tính năng làm mịn chuyển động trong khi vẫn giữ nguyên tỷ lệ khung hình, màu sắc và tốc độ khung hình ban đầu. Khả năng này mang đến một cách chính xác tầm nhìn ban đầu của đạo diễn, vì vậy bạn trải nghiệm bộ phim đúng như dự định của đạo diễn.</p>\n"
                                                +
                                                "<h3>HDR 10 Pro - Tận hưởng trọn vẹn các nội dung giải trí</h3>\n" +
                                                "<p>Công nghệ dải màu động của riêng LG, HDR 10 Pro, điều chỉnh độ sáng để tăng cường màu sắc, thể hiện mọi chi tiết nhỏ nhất, và mang lại độ chi tiết rõ như thật cho mọi hình ảnh - công nghệ này cũng tăng cường nội dung HDR thông thường. Giờ đây tất cả các bộ phim và chương trình yêu thích của bạn sẽ sống động và sôi động hơn từ đầu đến cuối.</p>\n"
                                                +
                                                "<h3>Nano Gaming - Trang hoàng thế giới Game với TV</h3>\n" +
                                                "<p>Từ những hang động tối tăm nhất đến những thế giới mới tươi sáng nhất, LG NanoCell TV trang hoàng trò chơi của bạn với màu sắc sống động. Công nghệ chơi game qua điện toán đám mây (phụ thuộc vào quốc gia, khu vực) và tự động điều chỉnh hình ảnh chất lượng cao mang đến trải nghiệm chơi game thực sự thú vị.</p>\n"
                                                +
                                                "<h3>Trình tối ưu hóa trò chơi - Mức độ kiểm soát chưa từng thấy</h3>\n"
                                                +
                                                "<p>Tất cả các game của bạn đều được lên cấp. Trình tối ưu hóa trò chơi tự động điều chỉnh thiết lập hình ảnh, ttối ưu hóa đồ họa và hiển thị, mang lại trải nghiệm chơi game tốt hơn bất kể bạn đang chơi loại game nào.</p>\n"
                                                +
                                                "<h3>Đối tác với Xbox - Một sự kết hợp bất khả chiến bại</h3>\n" +
                                                "<p>Mở ra thế giới game tự do của bạn. LG là đối tác của Xbox nhằm đảm bảo bạn luôn sẵn sàng chơi nhữngi game thế hệ mới. Tận hưởng những gì tinh túy nhất của Xbox nhờ chất lượng hình ảnh tuyệt đẹp và thời gian phản hồi cực nhanh.</p>\n"
                                                +
                                                "<h3>Nano Sport - Thưởng thức những trận đấu đẳng cấp ở chất lượng cao</h3>\n"
                                                +
                                                "<p>LG NanoCell TV mang đến trải nghiệm hồi hộp khi xem các trận đấu. Âm thanh Vòm Bluetooth mang toàn bộ bầu không khí ở sân vận động về nhà bạn, còn Thông báo Thể thao luôn cập nhật kịp thời cho bạn mọi tin tức mới nhất về đội bạn yêu thích.</p>\n"
                                                +
                                                "<h3>Không bao giờ bỏ lỡ trận đấu của đội yêu thích</h3>\n" +
                                                "<p>Thông báo thể thao thông báo cho bạn trước, trong và sau các trận đấu. Bạn sẽ không bao giờ phải lo lắng về việc bỏ lỡ các trận đấu lớn từ các đội yêu thích của mình, ngay cả khi bạn đang xem nội dung khác.</p>\n"
                                                +
                                                "<h3>Âm thanh vòm Bluetooth sẵn sàng - Âm thanh hoành tráng như tại sân vận động</h3>\n"
                                                +
                                                "<p>Dễ dàng kết nối loa Bluetooth để có trải nghiệm âm thanh vòm không dây thực sự. Tất cả các trải nghiệm âm thanh đều trở nên phong phú hơn và thực tế hơn, mang lại bầu không tại sân vận động về phòng khách nhà bạn.</p>\n"
                                                +
                                                "<h3>Bộ xử lý lõi tứ 4K - Nâng cấp chất lượng các nội dung bạn xem</h3>\n"
                                                +
                                                "<p>Bộ xử lý giúp loại bỏ các tín hiệu nhiễu và tái tạo lại hình ảnh với màu sắc sống động cùng độ tương phản cao hơn. Các hình ảnh với độ phân giải thấp sẽ được nâng cấp và tái tạo lại để đạt chất lượng tiệm cận 4K nhất có thể.</p>\n"
                                                +
                                                "<h3>AI ThinQ - Định nghĩa tính năng thông minh của bạn là gì? Đây là câu trả lời</h3>\n"
                                                +
                                                "<p>LG ThinQ ở đây để tối ưu hóa trải nghiệm TV của bạn. Chọn trợ lý giọng nói yêu thích của bạn, điều khiển TV bằng giọng nói, màn hình chính với giao diện hoàn toàn mới, tất cả nhằm mang đến sự thuận tiện khi điều khiển TV, mọi thứ trở nên đơn giản, dễ dàng hơn.</p>\n"
                                                +
                                                "<h3>Màn hình chính mới - Giao diện thiết kế hoàn toàn mới</h3>\n" +
                                                "<p>Màn hình chính được thiết kế mới hiển thị các đề xuất nội dung được cá nhân hóa, cho phép bạn truy cập dễ dàng hơn vào các mục yêu thích và cho phép bạn điều khiển tất cả các thiết bị kết nối ở một nơi.</p>\n"
                                                +
                                                "<h3>Điều khiển từ xa Magic Remote mới - Giống như một cây đũa thần</h3>\n"
                                                +
                                                "<p>Magic Remote được thiết kế lại có một thiết kế tiện lợi dễ cầm, và hệ thống trỏ và cuộn của thiết bị cho phép tìm kiếm nhanh hơn. AI tích hợp mang đến khả năng truy cập dễ dàng vào các dịch vụ, trong khi phím nóng cho các nhà cung cấp nội dung lớn mang đến cho bạn các phím tắt cho tất cả các mục yêu thích của mình. Bên cạnh tất cả những tính năng đó, bạn có Magic Tap, một thủ thuật mới thông minh kết nối điện thoại của bạn với TV.</p>\n"
                                                +
                                                "<h3>Điều khiển bằng giọng nói - Trung tâm kiểm soát thiết bị thông minh của bạn</h3>\n"
                                                +
                                                "<p>LG ThinQ cho phép ra lệnh đơn giản và điều khiển hệ sinh thái Home IoT của bạn với khả năng nhận dạng giọng nói tự nhiên*. Bạn có thể điều khiển TV LG NanoCell bằng giọng nói và truy cập giải trí nhanh hơn.</p><p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "82a382a8bbf89f90a3aba5f8ab12dcb1.jpg",
                                                                "d1fa70bca2401eb239d2e19cc2ea4eac.jpg",
                                                                "96767c1cd518c9e9795803e84dfd1615.jpg",
                                                                "6b8f9822fcc80850ad95168d07082310.jpg",
                                                                "639e704cc9609c94ed00c1eb122b2af8.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Tổng công suất loa", "20W" },
                                { "Số lượng loa", "2" },
                                {
                                                "Công nghệ âm thanh",
                                                "AI Sound / Pro, AI Acoustic Tuning, Clear Voice III, Sound Alive, Sound Mode Sync" },
                                { "Thương hiệu", "LG" },
                                { "Xuất xứ thương hiệu", "Hàn Quốc" },
                                { "Điều khiển tivi bằng điện thoại", "Có" },
                                { "Hệ điều hành, giao diện", "webOS" },
                                {
                                                "Công nghệ xử lý hình ảnh",
                                                "4K Upscaling, HDR10 Pro, FILMMAKER MODE, 4K Upscaler, HGIG Mode, Active HDR, HLG," },
                                { "Model", "43NANO77TPA" },
                                { "Kết nối không dây", "Screen share, Airplay2" },
                                { "Cổng HDMI", "3 Cổng" },
                                { "Cổng internet (LAN)", "Có" },
                                { "Wifi", "Có (802.11ac)" },
                                { "Xuất xứ", "Indonesia" },
                                { "Năm ra mắt", "2021" },
                                { "Remote thông minh", "Có" },
                                { "Độ phân giải", "3840 x 2160 pixels" },
                                { "Kích thước màn hình", "43 inch" },
                                { "Kích thước không chân/treo tường", "967 x 564 x 57.7 mm" },
                                { "Kích thước có chân/đặt bàn", "967 x 622 x 216 mm" },
                                { "Loại Tivi", "Smart Tivi NanoCell 4K" },
                                { "USB", "2 Cổng" },
                                { "Khối lượng không chân", "9.2kg" },
                                { "Khối lượng có chân", "9.3kg" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Smart Tivi Neo QLED Samsung 4K 55 inch QA55QN85A",
                                new BigDecimal(19659900),
                                1000L,
                                cv.categoryTivi,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("606299060c679734e5b429b499dc4dd8.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><h3>Thiết kế NeoSlim siêu mỏng hiện đại</h3>\n"
                                                +
                                                "<div><strong>Smart Tivi QLED Neo Samsung 4K 55 inch QA55QN85A</strong> có thiết kế siêu mỏng, thanh lịch như một kiệt tác sẽ làm sáng bừng mọi không gian nhà của bạn, tăng thêm niềm tự hào cho chủ nhân.</div>\n"
                                                +
                                                "<div>&nbsp;</div>\n" +
                                                "<div>&nbsp;</div>\n" +
                                                "<h3>Công nghệ Motion Xcelerator Turbo+</h3>\n" +
                                                "<div>Công nghệ Motion Xcelerator Turbo+ sẽ giúp bạn tận hưởng từng khung hình game tuyệt đẹp và chiến thắng dễ dàng mọi thử thách trong các màn thi đấu nhờ hình ảnh mượt mà, chuyển cảnh không rung lắc.</div>\n"
                                                +
                                                "<div>&nbsp;</div>\n" +
                                                "<h3>Công nghệ ma trận lượng tử Quantum Matrix</h3>\n" +
                                                "<div>Trải nghiệm Neo QLED - màn hình đến từ tương lai và đột phá công nghệ Ma Trận Lượng Tử Quantum Matrix, cho phép điều khiển chính xác tới từng lượng tử của đèn nền Mini LED. Với độ sáng được tinh chỉnh tối ưu, TV truyền tải trọn vẹn từng chi tiết rõ nét cùng sắc đen sâu thẳm và sắc trắng thuần khiết.</div>\n"
                                                +
                                                "<div>&nbsp;</div>\n" +
                                                "<h3>Bộ xử lý lượng tử Neo 4K</h3>\n" +
                                                "<div>Mang đến bạn trải nghiệm xem tối ưu chưa từng có. Bộ xử lý mạnh mẽ với công nghệ học sâu từ Samsung tinh chỉnh từng khung hình dựa trên điều kiện xem, truyền tải hoàn hảo từng phút giây giải trí linh hoạt. Tính năng nâng cấp hình ảnh bằng trí tuệ nhân tạo (AI) đảm bảo bạn luôn thưởng thức được chất lượng hình ảnh chuẩn 4K khác biệt.</div>\n"
                                                +
                                                "<div>&nbsp;</div>\n" +
                                                "<h3>Công nghệ lượng tử HDR 24x</h3>\n" +
                                                "<div>Tái hiện chuẩn xác từng sắc thái, những chi tiết ẩn chân thực với dải màu rực rỡ và độ tương phản ấn tượng. Thưởng thức trọn mọi chi tiết hoàn mỹ với công nghệ HDR10+ tuỳ chỉnh màu sắc và độ tương phản trong từng cảnh phim.</div>\n"
                                                +
                                                "<div>&nbsp;</div>\n" +
                                                "<h3>Công nghệ chấm lượng tử hiển thị 100% dải màu</h3>\n" +
                                                "<div>Công nghệ Chấm Lượng Tử hiển thị 100% dải màu với độ chân thực sống động và rõ nét, cho bạn thưởng thức từng khung hình mang màu sắc cuộc sống, tuyệt đẹp ở mọi mức độ sáng.</div>\n"
                                                +
                                                "<div>&nbsp;</div>\n" +
                                                "<h3>Khả năng chống chói vượt trội</h3>\n" +
                                                "<div>Công nghệ Chống Chói ấn tượng từ Samsung loại bỏ tối đa ánh sáng bên ngoài gây xao nhãng như ánh nắng hay ánh đèn, để bạn đắm chìm vào các nội dung giải trí, bất kể ngày hay đêm.</div>\n"
                                                +
                                                "<div>&nbsp;</div>\n" +
                                                "<h3>Công nghệ Ultra Viewing Angle cho góc nhìn rộng từ mọi hướng</h3>\n"
                                                +
                                                "<div>Công nghệ Ultra Viewing Angle giảm thiểu tối đa sự rò rỉ ánh sáng bằng cách tập trung và phân bổ đều nguồn sáng khắp màn hình TV. Đảm bảo trải nghiệm xem tối ưu ở mọi góc nhìn khác nhau trong căn phòng. Dù bạn xem phim một mình hay với nhiều bạn bè, TV Samung vẫn đem đến cho bạn hình ảnh đẹp nhất ở bất kỳ góc độ nào.</div>\n"
                                                +
                                                "<div>&nbsp;</div>\n" +
                                                "<h3>Công nghệ Adaptive Picture</h3>\n" +
                                                "<div>Sẵn sàng đắm chìm trong trải nghiệm xem đỉnh cao, đồng nhất ở bất kỳ thời điểm nào trong ngày với khả năng tự động điều chỉnh độ sáng và độ tương phản phù hợp với nội dung hiển thị và môi trường thực xung quanh.</div>\n"
                                                +
                                                "<div>&nbsp;</div>\n" +
                                                "<h3>Công nghệ Object Tracking Sound+ (OTS+)</h3>\n" +
                                                "<div>Với các loa tích hợp sẵn bên trong TV tạo hiệu ứng 3D, âm thanh sẽ di chuyển theo chuyển động của từng phân cảnh khiến bạn cảm thấy như thể đang xảy ra xung quanh mình. Dù xem ca nhạc hay điện ảnh hay TV, tính năng OTS+ sẽ cho bạn trải nghiệm âm thanh chân thực nhất.</div>\n"
                                                +
                                                "<div>&nbsp;</div>\n" +
                                                "<h3>Công nghệ Q-Symphony</h3>\n" +
                                                "<div>Không cần lo lắng bộ loa của bạn không đủ công suất và số lượng kênh vòm khi xem những thước phim đẳng cấp tại nhà. Công nghệ Q-Symphony cho phép tối ưu các loa của TV trở thành những kênh âm thanh bổ sung cho dàn loa thanh của bạn, giúp bạn trải nghiệm nghe và xem thật mãn nhãn.</div>\n"
                                                +
                                                "<div>&nbsp;</div>\n" +
                                                "<h3>Công nghệ SpaceFit Sound</h3>\n" +
                                                "<div>Bạn không cần lo lắng cách đặt TV hay thiết kế nhà sẽ ảnh hưởng đến trải nghiệm âm thanh trên TV. Công nghệ SpaceFit Sound sẽ đem đến âm thanh nguyên bản trung thực nhất ở bất kỳ không gian nào.</div>\n"
                                                +
                                                "<div>&nbsp;</div>\n" +
                                                "<h3>Nghe rõ từng câu thoại nhờ công nghệ Active Voice Amplifier (AVA)</h3>\n"
                                                +
                                                "<div>Công nghệ AVA giúp khuếch đại âm thanh thoại, giúp bạn luôn hiểu rõ nội dung phim và các tình tiết quan trọng dù không gian xung quanh bạn có nhiều tiếng ồn làm nhiễu, ảnh hưởng trải nghiệm xem.</div>\n"
                                                +
                                                "<div>&nbsp;</div>\n" +
                                                "<h3>Tính năng Super Ultrawide GameView &amp; Game Bar</h3>\n" +
                                                "<div>Các game thủ chuyên nghiệp sẽ dễ dàng chiến thắng trong các pha gay cấn của mọi loại game nhờ góc nhìn rộng hơn với tùy chọn khung hình 21:9 và 32:9 trên TV Samsung. Ngoài ra Menu Game bar thiết kế chuyên cho game thủ sẽ giúp thao tác thiết lập mọi thông số tối ưu cho game như tỉ lệ màn hình, kiểm tra độ trễ đầu vào, FPS, HDR, thiết lập của tai nghe không dây và hơn thế nữa, tất cả chỉ trong nháy mắt.</div>\n"
                                                +
                                                "<div>&nbsp;</div>\n" +
                                                "<h3>Công nghệ FreeSync Premium Pro chơi game mượt mà</h3>\n" +
                                                "<div>Công nghệ cốt lõi AMD FreeSync Premium Pro được chứng nhận giúp giảm thiểu đáng kể tình trạng giựt hình và xé hình. Chơi game mượt mà với hiệu năng cực đỉnh, kết hợp với hình ảnh HDR chân thực cùng độ trễ thấp cho bạn dễ dàng chiến thắng trong từng màn game đỉnh cao.</div>\n"
                                                +
                                                "<div>&nbsp;</div>\n" +
                                                "<h3>Tính năng Multi View</h3>\n" +
                                                "<div>Không cần phải lo lắng bản thân sẽ lỡ mất những thông tin quan trọng trên điện thoại khi đang xem TV với tính năng Multi View. Bạn sẽ thỏa sức theo dõi video hướng dẫn game trong lúc vẫn nắm được tỷ số trận đấu yêu thích chỉ trong 1 màn hình TV kết nối với điện thoại dễ dàng.</div>\n"
                                                +
                                                "<div>&nbsp;</div>\n" +
                                                "<h3>Tính năng Tap View - Kết nối chỉ với 1 chạm</h3>\n" +
                                                "<div>Không cần phải mất thời gian kết nối TV và điện thoại để xem các nội dung yêu thích khi giờ đây bạn chỉ cần chạm nhẹ vào cạnh TV để gửi tín hiệu mọi nội dung và hình ảnh từ điện thoại. Xem phim, hình ảnh hay nghe nhạc từ điện thoại sang TV cực nhanh chỉ với một chạm.</div>\n"
                                                +
                                                "<div>&nbsp;</div>\n" +
                                                "<div>&nbsp;</div>\n" +
                                                "<p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "289049823176a02f953ce3697b67340f.jpg",
                                                                "d3fb5b6d7fcd0969a56202f7da87c11b.jpg",
                                                                "bb4fb990b3f17564a7b3d2f2f339aa08.jpg",
                                                                "a018718feeda32602cca8f39a1a78b13.jpg",
                                                                "7abafabc1c991ccf13328c61c9448828.jpg",
                                                                "c9e798bc020f26ac14186f78a24f42f0.jpg",
                                                                "b2cef4a6bcec817591cc90adcaf67242.jpg",
                                                                "f337bd7c0d69ef439c48280350b9178a.jpg",
                                                                "183868b425fbc904fa8548a71ad158b8.jpg",
                                                                "6665c613d5a27a664b074d8b3c6b6644.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Tổng công suất loa", "60W" },
                                { "Số lượng loa", "2" },
                                { "Công nghệ âm thanh",
                                                "Dolby Digital Plus, Dolby 5.1 Decoder, Object Tracking Sound, Q-Symphony" },
                                { "Thương hiệu", "Samsung" },
                                { "Xuất xứ thương hiệu", "Hàn Quốc" },
                                { "Composite video", "1" },
                                { "Tivi kỹ thuật số (DVB-T2)", "Có" },
                                { "Tần số quét", "120Hz" },
                                { "Hệ điều hành, giao diện", "Tizen" },
                                {
                                                "Công nghệ xử lý hình ảnh",
                                                "Neo Quantum Processor 4K, Quantum HDR 24x, Certified(HDR10+), Quantum Matrix Technology, 100% Color Volume, Wide Viewing Angle, Supreme UHD Dimming" },
                                { "Model", "QA55QN85A" },
                                { "Cổng HDMI", "4" },
                                { "Cổng internet (LAN)", "Có" },
                                { "Wifi", "Có" },
                                { "Xuất xứ", "Việt Nam" },
                                { "Độ phân giải", "UHD 4K (3840 x 2160)" },
                                { "Kích thước màn hình", "55 inch" },
                                { "Kích thước không chân/treo tường", "1227.4 x 706.2 x 26.9 mm" },
                                { "Kích thước có chân/đặt bàn", "1227.4 x 767.8 x 257.5 mm" },
                                { "Loại Tivi", "Smart Tivi QLED" },
                                { "USB", "2" },
                                { "Khối lượng không chân", "17.7kg" },
                                { "Khối lượng có chân", "20.8 kg" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Smart Tivi Casper HD 32 inch 32HX6200",
                                new BigDecimal(3800000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandCasper,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("2ad10132655e7d039fe0c5d7a5795295.png"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><h3>Thiết kế hiện đại, chắc chắn</h3>\n"
                                                +
                                                "<p><strong>Smart Tivi Casper HD 32 inch 32HX6200</strong>&nbsp;sở hữu độ phân giải HD đem lại hình ảnh có độ nét cao tối ưu với màn hình kích thước 32 inch, chân đế thiết kế hình chữ V vững chãi ở mọi mặt phẳng. Tivi thanh mảnh, nhỏ gọn phù hợp với đa số không gian chật hẹp ở đô thị Viêt Nam hiện nay. Màn hình tràn viền mang lại trải nghiệm hình ảnh hoàn hảo.</p>\n"
                                                +
                                                "<h3>Tấm nền công nghệ IPS</h3>\n" +
                                                "<p>Hiệu quả trình chiếu tối ưu nhờ tấm nền công nghệ IPS: Màn hình Tivi Casper được tối ưu góc nhìn rộng tới 178 độ. Các chuyển động được truyền tải mượt mà, không bị giật hình, đặc biệt, mức độ bền bỉ tốt, tuổi thọ cao.</p>\n"
                                                +
                                                "<h3>Kho ứng dụng đa dạng với hệ điều hành Linux</h3>\n" +
                                                "<p>Hệ điều hành Linux với giao diện đơn giản, dễ sử dụng và được cài đặt sẵn 36 ứng dụng bản quyền phổ biến cả trong nước và quốc tế như Netflix, Prime Video, FPT Play, Nhaccuatui… giúp nâng tầm trải nghiệm giải trí mà vẫn đảm bảo an toàn và bảo mật cho người sử dụng.</p>\n"
                                                +
                                                "<h3>Công nghệ âm thanh Dolby Audio chân thực</h3>\n" +
                                                "<p>Thưởng thức trọn vẹn âm thanh với công nghệ Dolby Audio: Được cấp phép bởi Dolby Laboratories – tập đoàn Mỹ với công nghệ mã hóa và lọc âm nổi danh toàn cầu – Casper Tivi với chuẩn âm thanh vòm Dolby Audio mang lại trải nghiệm như ở rạp hát trong chính ngôi nhà của bạn.</p>\n"
                                                +
                                                "<h3>Bộ xử lý thông minh chip 4 nhân</h3>\n" +
                                                "<p>Bộ xử lý thông minh của Tivi Casper sử dụng chip xử lý 4 nhân với vai trò bộ não liên kết hoạt động của mọi con chip bên trong bộ mạch chủ. Chassis phiên bản 64 Bit giúp thực hiện các tác vụ đa nhiệm cùng lúc, đảm bảo tốc độ hoạt động mượt mà và mạnh mẽ của toàn bộ hệ thống.</p>\n"
                                                +
                                                "<h3>Đa dạng cổng kết nối</h3>\n" +
                                                "<p>Để kết nối với các thiết bị ngoại vi tốt hơn, cổng kết nối trên chiếc tivi cũng đa dạng hơn giúp dễ dàng sử dụng tivi với những mục đích khác nhau. Hãy biến chúng trở thành phương tiện giải trí thông minh phục vụ nhu cầu tốt nhất của bạn nhé.</p>\n"
                                                +
                                                "<h3>Tận hưởng điện ảnh trọn vẹn trên Smart tivi</h3>\n" +
                                                "<p>Với sự phát triển của công nghệ, ngày nay ngoài máy tính và điện thoại, người dùng còn có thể xem phim online trên tivi để nâng cao trải nghiệm giải trí. Bạn có thể xem những ứng dụng xem phim online phổ biến và được yêu thích trên Smart tivi như Netflix, iflix, HBO, FPT Play, Fim+.</p>\n"
                                                +
                                                "<p>&nbsp;</p><p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "5a1f8cede955503e1a6ddd4d1bac2f42.jpg",
                                                                "77c32a066fb50070654c7c3e6dab9ecb.jpg",
                                                                "d39b54740a0589e6b252d0eea15935e9.jpg",
                                                                "deeac59b53ceb42ddbebcf3418c3b72a.jpg",
                                                                "28039d09a59888d4295c4bc488659b4d.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Công nghệ âm thanh", "Dolby Audio" },
                                { "Thương hiệu", "Casper" },
                                { "Xuất xứ thương hiệu", "Thái Lan" },
                                { "Composite video", "Có" },
                                { "Tivi kỹ thuật số (DVB-T2)", "Có" },
                                { "Hệ điều hành, giao diện", "Linux OS" },
                                { "Công nghệ xử lý hình ảnh", "Công nghê IPS" },
                                { "Model", "32HX6200" },
                                { "Cổng HDMI", "2 cổng" },
                                { "Cổng internet (LAN)", "Có" },
                                { "Wifi", "Có" },
                                { "Xuất xứ", "Thái Lan" },
                                { "Độ phân giải", "HD (1366 x 768)" },
                                { "Kích thước màn hình", "32 inch" },
                                { "Loại Tivi", "Smart Tivi" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Smart TV TCL Android 8.0 32 inch HD wifi - 32L61 - HDR, Micro Dimming, Dolby, Chromecast, T-cast, AI+IN",
                                new BigDecimal(5990000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandTCL,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("6c8575ed19910f07b358d1addc1efa3d.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\">" +
                                                "<p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "eb71edb203bac0e4013be4111d82e4f4.jpg",
                                                                "75e9b18e2388ce3323b24a6e007456d0.png",
                                                                "2ea465aa59404303bb0d5640d45a1506.png",
                                                                "e3194a8585d2e848bd42226f0cecd1ff.png",
                                                                "a96094597f145247377266b9efd2f301.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Tổng công suất loa", "16" },
                                { "Công nghệ âm thanh", "Dolby MS12" },
                                { "Thương hiệu", "TCL" },
                                { "Xuất xứ thương hiệu", "Trung Quốc" },
                                { "Điều khiển tivi bằng điện thoại",
                                                "màn hình Chromecast, Chiếu màn hình Screen Mirroring" },
                                { "Tần số quét", "60 Hz" },
                                { "Hệ điều hành, giao diện", "Android 8.0" },
                                { "Công nghệ xử lý hình ảnh", "Micro Dimming, HDR" },
                                { "Model", "32L61" },
                                { "Kết nối bàn phím, chuột", "Có" },
                                { "Kết nối không dây", "Wifi" },
                                { "Cổng HDMI", "2" },
                                { "Wifi", "Wifi" },
                                { "Xuất xứ", "Việt Nam" },
                                { "Năm ra mắt", "2019" },
                                { "Hình ảnh", "Full HD" },
                                { "Kích thước không chân/treo tường",
                                                "Không chân: Ngang 90.1 cm - Cao 51.63 cm - Dày 7.5 cm" },
                                { "Loại Tivi", "Smart TV, AI" },
                                {
                                                "Tương tác thông minh",
                                                "Chiếu màn hình Chromecast, Chiếu màn hình Screen Mirroring, Chiếu màn hình AirPlay qua ứng dụng AirScreen" },
                                { "USB", "1" },
                                { "Xem 3D", "Không" },
                                { "Khối lượng không chân", "6.31 kg" },
                                { "Khối lượng có chân", "6.37 kg" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Smart Tivi Casper Full HD 43 inch 43FX6200",
                                new BigDecimal(9990000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandCasper,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("26961fda2b84fbb02b2a88abdd3e655c.png"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><h3>Thiết kế hiện đại, chắc chắn</h3>\n"
                                                +
                                                "<p><strong>Smart Tivi Casper Full HD 43 inch 43FX6200</strong> sở hữu độ phân giải HD đem lại hình ảnh có độ nét cao tối ưu với màn hình kích thước 32 inch, chân đế thiết kế hình chữ V vững chãi ở mọi mặt phẳng. Tivi thanh mảnh, nhỏ gọn phù hợp với đa số không gian chật hẹp ở đô thị Viêt Nam hiện nay. Màn hình tràn viền mang lại trải nghiệm hình ảnh hoàn hảo.</p>\n"
                                                +
                                                "<h3>Tấm nền công nghệ IPS</h3>\n" +
                                                "<p>Hiệu quả trình chiếu tối ưu nhờ tấm nền công nghệ IPS: Màn hình Tivi Casper được tối ưu góc nhìn rộng tới 178 độ. Các chuyển động được truyền tải mượt mà, không bị giật hình, đặc biệt, mức độ bền bỉ tốt, tuổi thọ cao.</p>\n"
                                                +
                                                "<h3>Kho ứng dụng đa dạng với hệ điều hành Linux</h3>\n" +
                                                "<p>Hệ điều hành Linux với giao diện đơn giản, dễ sử dụng và được cài đặt sẵn 36 ứng dụng bản quyền phổ biến cả trong nước và quốc tế như Netflix, Prime Video, FPT Play, Nhaccuatui… giúp nâng tầm trải nghiệm giải trí mà vẫn đảm bảo an toàn và bảo mật cho người sử dụng.</p>\n"
                                                +
                                                "<h3>Công nghệ âm thanh Dolby Audio chân thực</h3>\n" +
                                                "<p>Thưởng thức trọn vẹn âm thanh với công nghệ Dolby Audio: Được cấp phép bởi Dolby Laboratories – tập đoàn Mỹ với công nghệ mã hóa và lọc âm nổi danh toàn cầu – Casper Tivi với chuẩn âm thanh vòm Dolby Audio mang lại trải nghiệm như ở rạp hát trong chính ngôi nhà của bạn.</p>\n"
                                                +
                                                "<h3>Bộ xử lý thông minh chip 4 nhân</h3>\n" +
                                                "<p>Bộ xử lý thông minh của Tivi Casper sử dụng chip xử lý 4 nhân với vai trò bộ não liên kết hoạt động của mọi con chip bên trong bộ mạch chủ. Chassis phiên bản 64 Bit giúp thực hiện các tác vụ đa nhiệm cùng lúc, đảm bảo tốc độ hoạt động mượt mà và mạnh mẽ của toàn bộ hệ thống.</p>\n"
                                                +
                                                "<h3>Đa dạng cổng kết nối</h3>\n" +
                                                "<p>Để kết nối với các thiết bị ngoại vi tốt hơn, cổng kết nối trên chiếc tivi cũng đa dạng hơn giúp dễ dàng sử dụng tivi với những mục đích khác nhau. Hãy biến chúng trở thành phương tiện giải trí thông minh phục vụ nhu cầu tốt nhất của bạn nhé.</p>\n"
                                                +
                                                "<h3>Tận hưởng điện ảnh trọn vẹn trên Smart tivi</h3>\n" +
                                                "<p>Với sự phát triển của công nghệ, ngày nay ngoài máy tính và điện thoại, người dùng còn có thể xem phim online trên tivi để nâng cao trải nghiệm giải trí. Bạn có thể xem những ứng dụng xem phim online phổ biến và được yêu thích trên Smart tivi như Netflix, iflix, HBO, FPT Play, Fim+.</p>\n"
                                                +
                                                "<p>&nbsp;</p><p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "86599e811a1662b106f4527de4a244d6.jpg",
                                                                "f3c8b355cbfbc33919b1dce1198cacee.jpg",
                                                                "8c19380a99ca6666f422a25bbfa9135e.jpg",
                                                                "61c731675403b94d2ad6f07583d9b78f.jpg",
                                                                "c03bc98bda3def220258fb86364a05f7.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Công nghệ âm thanh", "Dolby Audio" },
                                { "Thương hiệu", "Casper" },
                                { "Xuất xứ thương hiệu", "Thái Lan" },
                                { "Composite video", "Có" },
                                { "Tivi kỹ thuật số (DVB-T2)", "Có" },
                                { "Công nghệ xử lý hình ảnh", "Công nghệ IPS" },
                                { "Model", "43FX6200" },
                                { "Cổng HDMI", "2" },
                                { "Cổng internet (LAN)", "Có" },
                                { "Xuất xứ", "Thái Lan" },
                                { "Độ phân giải", "1920 x 1080 pixels" },
                                { "Kích thước màn hình", "43 inch" },
                                { "Loại Tivi", "Smart Tivi" },
                                { "USB", "2" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Tivi LED Asanzo HD 25 inch 25S200T2",
                                new BigDecimal(2900000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandAsanzo,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("19c98c44335ebd8b0722ee549f88b1d2.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"overflow: hidden; height: 500px;\"><h3>Thiết kế đơn giản, tinh tế</h3>\n"
                                                +
                                                "<p><strong>Tivi LED Asanzo HD 25 inch 25S200T2</strong> tạo ấn tượng với người dùng bằng thiết kế đơn giản nhưng không kém phần sang trọng, tinh tế trong từng đường nét. &nbsp;Tivi có thiết kế nhỏ gọn nên thích hợp với những không gian nhỏ gọn như phòng ăn, phòng ngủ, phòng trọ,...</p>\n"
                                                +
                                                "<h3>Khoảng cách xem tivi an toàn</h3>\n" +
                                                "<p>Để bảo vệ cho đôi mắt luôn khỏe, bạn nên lựa chọn khoảng cách xem tivi an toàn. Đối với tivi 25 inch thì khoảng cách xem hợp lý nhất là từ khoảng 1.3 m đến 1.9 m.</p>\n"
                                                +
                                                "<h3>Màu sắc chân thực, hình ảnh sống động</h3>\n" +
                                                "<p>Tivi LED ASANZO 25 inch 25S200T2 trang bị Chương trình kiểm soát hình ảnh Picture Wizard II mang đến những hình ảnh sống động và màu sắc chân thực nhất. Ngoài ra, độ phân giải HD mang lại những hình ảnh tuyệt đẹp nhất.</p>\n"
                                                +
                                                "<h3>Tần số quét TruMotion 100Hz</h3>\n" +
                                                "<p>Được tích hợp tần số quét TruMotion 100Hz, Tivi LED ASANZO 25 inch 25S200T2 mang lại những thước phim mịn màng, sắc nét ngay cả trong những chuyển động mạnh.</p>\n"
                                                +
                                                "<h3>Âm thanh thật như cuộc sống</h3>\n" +
                                                "<p>Tivi &nbsp;được trang bị&nbsp;chế độ Infinite giúp âm thanh phát ra được từ nhiều hướng. Đồng thời, tổng công suất 10W đem đến dải âm chuẩn hơn, từng bản nhạc, bộ phim sẽ được trình diễn thật mạnh mẽ và hoành tráng.</p>\n"
                                                +
                                                "<h3>Đầu thu kỹ thuật số DVB-T2</h3>\n" +
                                                "<p>Tivi LED ASANZO 25 inch 25S200T2 được tích hợp chuẩn đầu thu kỹ thuật số DVB-T2 cho phép xem miễn phí ít nhất 15 kênh truyền hình kỹ thuật số với chất lượng cao, đường truyền ổn định.</p>\n"
                                                +
                                                "<p>Lưu ý: số kênh thu được phụ thuộc vào vị trí địa lý cũng như chất lượng ăng-ten nhà bạn.</p>\n"
                                                +
                                                "<h3>Siêu tiết kiệm điện</h3>\n" +
                                                "<p>Với chế độ tiết kiệm như: điều khiển ánh sáng nền, chức năng tắt màn hình (giúp bạn có thể tắt màn hình mà vẫn giữ lại âm thanh và nhạc), chế độ Standby, bạn sẽ tiết kiệm được năng lượng điện so với các dòng sản phẩm khác. Tính năng cảm biến thông minh giúp TV có thể tự động điều chỉnh ánh sáng, màu sắc tùy theo điều kiện ánh sáng của môi trường xung quanh. Bên cạnh đó, công nghệ đèn nền LED còn giúp tiết kiệm điện hơn 30% so với những dòng TV LCD thông thường.</p><p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p><div class=\"gradient\"></div></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "25s200t2-2.u2470.d20161227.t155919.914749.jpg",
                                                                "25s200t2-3.u2470.d20161227.t155919.946421.jpg",
                                                                "25s200t2-4.u2470.d20161227.t155919.977103.jpg",
                                                                "25s200t2-5.u2470.d20161227.t155920.9293.jpg",
                                                                "hinh-thum-tivi.u2470.d20161228.t175808.676355.png",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Âm Thanh", "Đang cập nhật" },
                                { "Tổng công suất loa", "10W" },
                                { "Số lượng loa", "2" },
                                { "Công nghệ âm thanh", "Infinite" },
                                { "Thương hiệu", "Asanzo" },
                                { "Xuất xứ thương hiệu", "Việt Nam" },
                                { "Component video", "-" },
                                { "Composite video", "-" },
                                { "Tivi kỹ thuật số (DVB-T2)", "có" },
                                { "Phim", "Đang cập nhật" },
                                { "Tần số quét", "TruMotion 100Hz" },
                                { "Công nghệ xử lý hình ảnh", "Picture Wizard II" },
                                { "Cổng HDMI", "có" },
                                { "Cổng internet (LAN)", "Không" },
                                { "Wifi", "Không" },
                                { "Xuất xứ", "Việt Nam" },
                                { "Hình ảnh", "Đang cập nhật" },
                                { "Độ phân giải", "HD (1366 x 768)" },
                                { "Kích thước không chân/treo tường", "-" },
                                { "Kích thước có chân/đặt bàn", "-" },
                                { "Phụ đề Phim", "Đang cập nhật" },
                                { "Loại Tivi", "LED" },
                                { "USB", "có" },
                                { "VGA", "Có" },
                                { "Xem 3D", "Không" },
                                { "Khối lượng không chân", "-" },
                                { "Khối lượng có chân", "-" }
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Google Tivi Coocaa 4K 50 Inch - Model 50Y72 - Hàng Chính Hãng",
                                new BigDecimal(14000000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandCoocaa,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("73889a77be083e33aa981fb89142c125.jpg"),
                                "<div class=\\\"ToggleContent__View-sc-1dbmfaw-0 wyACs\\\" style=\\\"\\\">\\n\" +\n" +
                                                "                \"<p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "f69d43f195709a1f0afd48709bb4a7c2.jpg",
                                                                "57738d663a97b6b93afd3094c5ef6ff9.jpg",
                                                                "e38c0d99157f5f888ceb6a5daf510640.jpg",
                                                                "3ccf6a501254f11757e3dd879007626d.jpg",
                                                                "590332381a531ac18c12e6f66da5eb60.jpg",
                                                                "5c3c7aca3ad22191b0ec5c9ff4d7f571.jpg",
                                                                "18573e832ea28324910023647ef1dd9c.jpg",
                                                                "581f9076582c294ee9be245e18320511.jpg",
                                                                "934651117eead38893d47d4b840dfd01.jpg",
                                                                "d3200577772780b2563dd0e3c7fbecfe.jpg",
                                                                "790d0deff7a3b1654379d5ca36c0812a.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Thương hiệu", "coocaa" },
                                { "Xuất xứ thương hiệu", "Trung Quốc" },
                                {
                                                "Chất liệu",
                                                "* Tivi Thông Minh Coocaa Hệ Điều Hành Mới Nhất Google Tivi 50\" 50-Y72\n"
                                                                +
                                                                "\n" +
                                                                " Chính Sách Bảo Hành\n" +
                                                                "- Bảo hành chính hãng 24 tháng trên toàn quốc.\n" +
                                                                "\n" +
                                                                "         Hotline: 1800.1180 (miễn phí cuộc gọi)\n" +
                                                                "\n" +
                                                                "         Thời gian: 8h-17h, từ Thứ 2- Thứ 7\n" +
                                                                "\n" +
                                                                "- CÁCH ĐĂNG KÍ/ KIỂM TRA BẢO HÀNH ĐIỆN TỬ:\n" +
                                                                "\n" +
                                                                "         Vui lòng liên hệ CSKH coocaa qua mục chat (trò chuyện) để chúng tôi có thể hướng dẫn bạn tốt nhất.\n"
                                                                +
                                                                "\n" +
                                                                " \n" +
                                                                "\n" +
                                                                "Tính Năng\n" +
                                                                "- Hình Ảnh\n" +
                                                                "\n" +
                                                                "    + Chameleon\n" +
                                                                "\n" +
                                                                "Extreme 2.0: Bộ xử lý chất lượng hình ảnh Chameleon cực 2.0 có thể tối ưu hóa\n"
                                                                +
                                                                "\n" +
                                                                "chất lượng hình ảnh TV đa chiều để đảm bảo cảnh phim thực tế hơn\n"
                                                                +
                                                                "\n" +
                                                                "    + Eye care TV: Sử dụng công nghệ Filmmaker Mode\n"
                                                                +
                                                                "\n" +
                                                                "giúp hạn chế sáng xanh và sự nhấp nháy của màng hình bảo vệ đôi mắt khi xem\n"
                                                                +
                                                                "\n" +
                                                                "tivi.\n" +
                                                                "\n" +
                                                                "    + Màn hình LED chất lượng cao 8,29 megapixel,\n" +
                                                                "\n" +
                                                                "được trang bị công nghệ cải tiến chất lượng hình ảnh mở rộng HDR10 & HLG,\n"
                                                                +
                                                                "\n" +
                                                                "mang đến tầm nhìn rực rỡ tươi mới.\n" +
                                                                "\n" +
                                                                "- Âm Thanh\n" +
                                                                "\n" +
                                                                "    + Dolby\n" +
                                                                "\n" +
                                                                "Digital Plus là công nghệ âm thanh dựa trên Dolby Digital 5.1, đây là công nghệ\n"
                                                                +
                                                                "\n" +
                                                                "âm thanh âm thanh vòm tiên tiến cho phép trải nghiệm tivi như trong rạp chiếu phim hiện đại.\n"
                                                                +
                                                                "\n" +
                                                                "    +Với hai loa monomer 10W, Dolby Audio Sound tối ưu hóa hiệu suất âm thanh, tạo ra âm\n"
                                                                +
                                                                "\n" +
                                                                "thanh phong phú, rõ ràng và hấp dẫn để bạn thưởng thức.\n"
                                                                +
                                                                "\n" +
                                                                "    +Bidirectional Bluetooth 5.1: Bluetooth hai\n" +
                                                                "\n" +
                                                                "chiều, Kết nối điện thoại di động của bạn với Bluetooth của TV để phát nhạc hay\n"
                                                                +
                                                                "\n" +
                                                                "làm bất cứ điều gì bạn muốn.\n" +
                                                                "\n" +
                                                                "- Cấu Hình\n" +
                                                                "\n" +
                                                                "    + Màn hình không giới hạn 4.0, tỷ lệ màn hình cao  \n"
                                                                +
                                                                "\n" +
                                                                "    + Lưng kim loại trải ra: bền và tản nhiệt tốt hơn.\n"
                                                                +
                                                                "\n" +
                                                                "    + Thiết kế liền mạch: Khung kim loại liền mạch, đơn giản, tinh tế\n"
                                                                +
                                                                "\n" +
                                                                "Điều khiển bằng giọng nói từ xa: Thông qua tương tác bằng giọng nói, TV cho bạn biết những thông tin mới nhất như thời tiết, phát nhạc, phim nổi tiếng, để cuộc sống của bạn không còn nhàm chán, bạn có thể trò chuyện bất cứ lúc nào\n"
                                                                +
                                                                "\n" +
                                                                "- Hệ Điều Hành Google Tivi\n" +
                                                                "\n" +
                                                                "    + Hệ thống mới nhất của Google được cập nhật dựa trên Android 11. So với Android, hệ thống mới này được Google thiết kế dành riêng cho người dùng TV.\n"
                                                                +
                                                                "\n" +
                                                                "    + Tài nguyên khổng lồ: Google là nền tảng mạng lớn nhất thế giới, với hơn 8.000\n"
                                                                +
                                                                "\n" +
                                                                "+ ứng dụng phục vụ hơn 190 quốc gia và hỗ trợ hơn 1 tỷ thiết bị sinh thái. Trò chơi đám mây, Hình nền Google, gọi điện video, nhiều tài khoản đăng nhập, hệ sinh thái dành cho trẻ em và nhiều ứng dụng phổ biến, chỉ cần bạn tải xuống là có thể tận hưởng.\n"
                                                                +
                                                                "\n" +
                                                                "    + CC Cast: truyền chia sẻ màn hình từ điện thoại của bạn sang tivi một cách dễ dàng.\n"
                                                                +
                                                                "\n" +
                                                                " \n" +
                                                                "\n" +
                                                                "Thông Số Kỹ Thuật\n" +
                                                                "- Chameleon Extreme 2,0\n" +
                                                                "\n" +
                                                                "- Filmmaker mode\n" +
                                                                "\n" +
                                                                "- DTS Studio Sound\n" +
                                                                "\n" +
                                                                "- Bidirectional Bluetooth 5.1\n" +
                                                                "\n" +
                                                                "- Google Assistant\n" +
                                                                "\n" +
                                                                "- HDMI2.0*3；USB2.0*2\n" +
                                                                "\n" +
                                                                "- Memory 2+16GB\n" +
                                                                "\n" +
                                                                " \n" +
                                                                "\n" +
                                                                "Bộ sản phẩm đầy đủ:\n" +
                                                                "2 x Chân đế, 1 x Remote, 1 x Sách HDSD, 1 x Dây nguồn.\n"
                                                                +
                                                                "\n" +
                                                                " \n" +
                                                                "\n" +
                                                                "Chính Sách Đổi Trả\n" +
                                                                "- Chỉ đổi/trả sản phẩm, từ chối nhận hàng tại thời điểm nhận hàng trong trường hợp sản phẩm giao đến không còn nguyên vẹn, thiếu phụ kiện hoặc nhận được sai hàng. Khi sản phẩm đã được cắm điện sử dụng và/hoặc lắp đặt, và gặp lỗi kĩ thuật, sản phẩm sẽ được hưởng chế độ bảo hành theo đúng chính sách của nhà sản xuất.\n"
                                                                +
                                                                "\n" +
                                                                "- Vui lòng liên hệ CSKH coocaa qua mục chat (trò chuyện) để chúng tôi có thể hướng dẫn bạn tốt nhất.\n"
                                                                +
                                                                "\n" +
                                                                "=> Coocaa luôn cố gắng cung cấp sản phẩm TV chất lượng và dịch vụ tuyệt vời. Nhận xét đánh giá 5 sao sẽ có ý nghĩa cực kỳ lớn đối với chúng tôi!" },
                                { "Model", "50Y72" },
                                { "Xuất xứ", "Indonesia" },
                                { "Loại Tivi", "Google Tivi" },
                                { "Xem 3D", "Không" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Smart Tivi NanoCell LG 4K 55 inch 55NANO77TPA",
                                new BigDecimal(23900000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandLG,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("b5ad48336d11ab28a860925d99b80e20.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><h3>8 triệu điểm ảnh cùng thể hiện trong 1 bức tranh</h3>\n"
                                                +
                                                "<p>Màu sắc thuần khiết được thể hiện tuyệt đẹp trên màn hình 4K đích thực với khoảng 8 triệu điểm ảnh, TV 4K đích thực truyền tải hình ảnh sắc nét hơn và chi tiết hơn rõ rệt so với TV UHD thông thường. Với TV NanoCell, độ phân giải 4K đích thực được bổ sung công nghệ NanoCell để mang lại trải nghiệm 4K vượt qua mọi tiêu chuẩn quốc tế.</p>\n"
                                                +
                                                "<h3>Mọi màu sắc hiện lên trong veo</h3>\n" +
                                                "<p>TV LG NanoCell sử dụng các hạt nano, công nghệ Nano đặc biệt của riêng chúng tôi, để lọc và tinh chỉnh màu sắc, loại bỏ tín hiệu gây nhiễu khỏi các bước sóng RGB. Nghĩa là, chỉ những màu sắc tinh khiết, chính xác mới được hiển thị trên màn hình. Nhờ đó hình ảnh tạo ra sẽ tươi đẹp hơn, chân thực hơn, giúp nội dung của bạn trở nên sống động.</p>\n"
                                                +
                                                "<h3>Kinh ngạc từ mọi góc nhìn</h3>\n" +
                                                "<p>Màu sắc tuyệt đối của TV NanoCell đảm bảo rằng màu sắc được hiển thị chính xác cho hình ảnh trung thực ngay cả khi nhìn từ góc rộng.</p>\n"
                                                +
                                                "<h3>Thiết kế tinh tế, thêm gu thẩm mỹ</h3>\n" +
                                                "<p>Tác phẩm nghệ thuật đẹp như tranh vẽ không chỉ dành để treo trên các bức tường nữa. Chân đỡ Gallery cho phép bạn tự do đặt TV và biến ngôi nhà của bạn thành một phòng trưng bày hiện đại.</p>\n"
                                                +
                                                "<h3>Chất lượng ngang tầm với màn chiếu lớn</h3>\n" +
                                                "<p>Pure Colors và một loạt các công nghệ màn hình mới nhất mang cả rạp phim về nhà bạn với LG NanoCell TV. Với công nghệ HDR nâng cao của chúng tôi, các công nghệ được nâng cấp của Dolby, và một chế độ điện ảnh mới vừa được hãng công bố sẽ mang đến trải nghiệm điện ảnh thực sự.</p>\n"
                                                +
                                                "<h3>Nơi hội tụ những gì bạn yêu thích</h3>\n" +
                                                "<p>Truy cập Ứng dụng Apple TV, Disney+ và Netflix. Chọn từ những bộ phim mới nhất, chương trình TV, phim tài liệu và thể thao trực tiếp, và tìm tất cả nội dung ở cùng một nơi.</p>\n"
                                                +
                                                "<h3>Filmmaker Mode - Hiện thực hóa góc nhìn của đạo diễn</h3>\n" +
                                                "<p>Filmmaker Mode tắt tính năng làm mịn chuyển động trong khi vẫn giữ nguyên tỷ lệ khung hình, màu sắc và tốc độ khung hình ban đầu. Khả năng này mang đến một cách chính xác tầm nhìn ban đầu của đạo diễn, vì vậy bạn trải nghiệm bộ phim đúng như dự định của đạo diễn.</p>\n"
                                                +
                                                "<h3>HDR 10 Pro - Tận hưởng trọn vẹn các nội dung giải trí</h3>\n" +
                                                "<p>Công nghệ dải màu động của riêng LG, HDR 10 Pro, điều chỉnh độ sáng để tăng cường màu sắc, thể hiện mọi chi tiết nhỏ nhất, và mang lại độ chi tiết rõ như thật cho mọi hình ảnh - công nghệ này cũng tăng cường nội dung HDR thông thường. Giờ đây tất cả các bộ phim và chương trình yêu thích của bạn sẽ sống động và sôi động hơn từ đầu đến cuối.</p>\n"
                                                +
                                                "<h3>Nano Gaming - Trang hoàng thế giới Game với TV</h3>\n" +
                                                "<p>Từ những hang động tối tăm nhất đến những thế giới mới tươi sáng nhất, LG NanoCell TV trang hoàng trò chơi của bạn với màu sắc sống động. Công nghệ chơi game qua điện toán đám mây (phụ thuộc vào quốc gia, khu vực) và tự động điều chỉnh hình ảnh chất lượng cao mang đến trải nghiệm chơi game thực sự thú vị.</p>\n"
                                                +
                                                "<h3>Trình tối ưu hóa trò chơi - Mức độ kiểm soát chưa từng thấy</h3>\n"
                                                +
                                                "<p>Tất cả các game của bạn đều được lên cấp. Trình tối ưu hóa trò chơi tự động điều chỉnh thiết lập hình ảnh, ttối ưu hóa đồ họa và hiển thị, mang lại trải nghiệm chơi game tốt hơn bất kể bạn đang chơi loại game nào.</p>\n"
                                                +
                                                "<h3>Đối tác với Xbox - Một sự kết hợp bất khả chiến bại</h3>\n" +
                                                "<p>Mở ra thế giới game tự do của bạn. LG là đối tác của Xbox nhằm đảm bảo bạn luôn sẵn sàng chơi những game thế hệ mới. Tận hưởng những gì tinh túy nhất của Xbox nhờ chất lượng hình ảnh tuyệt đẹp và thời gian phản hồi cực nhanh.</p>\n"
                                                +
                                                "<h3>Nano Sport - Thưởng thức những trận đấu đẳng cấp ở chất lượng cao</h3>\n"
                                                +
                                                "<p>LG NanoCell TV mang đến trải nghiệm hồi hộp khi xem các trận đấu. Âm thanh Vòm Bluetooth mang toàn bộ bầu không khí ở sân vận động về nhà bạn, còn Thông báo Thể thao luôn cập nhật kịp thời cho bạn mọi tin tức mới nhất về đội bạn yêu thích.</p>\n"
                                                +
                                                "<h3>Không bao giờ bỏ lỡ trận đấu của đội yêu thích</h3>\n" +
                                                "<p>Thông báo thể thao thông báo cho bạn trước, trong và sau các trận đấu. Bạn sẽ không bao giờ phải lo lắng về việc bỏ lỡ các trận đấu lớn từ các đội yêu thích của mình, ngay cả khi bạn đang xem nội dung khác.</p>\n"
                                                +
                                                "<h3>Âm thanh hoành tráng như tại sân vận động</h3>\n" +
                                                "<p>Dễ dàng kết nối loa Bluetooth để có trải nghiệm âm thanh vòm không dây thực sự. Tất cả các trải nghiệm âm thanh đều trở nên phong phú hơn và thực tế hơn, mang lại bầu không tại sân vận động về phòng khách nhà bạn.</p>\n"
                                                +
                                                "<h3>Bộ xử lý lõi tứ 4K - Nâng cấp chất lượng các nội dung bạn xem</h3>\n"
                                                +
                                                "<p>Bộ xử lý giúp loại bỏ các tín hiệu nhiễu và tái tạo lại hình ảnh với màu sắc sống động cùng độ tương phản cao hơn. Các hình ảnh với độ phân giải thấp sẽ được nâng cấp và tái tạo lại để đạt chất lượng tiệm cận 4K nhất có thể.</p>\n"
                                                +
                                                "<h3>AI ThinQ - Định nghĩa tính năng thông minh của bạn là gì? Đây là câu trả lời</h3>\n"
                                                +
                                                "<p>LG ThinQ ở đây để tối ưu hóa trải nghiệm TV của bạn. Chọn trợ lý giọng nói yêu thích của bạn, điều khiển TV bằng giọng nói, màn hình chính với giao diện hoàn toàn mới, tất cả nhằm mang đến sự thuận tiện khi điều khiển TV, mọi thứ trở nên đơn giản, dễ dàng hơn.</p>\n"
                                                +
                                                "<h3>Màn hình chính mới - Giao diện thiết kế hoàn toàn mới</h3>\n" +
                                                "<p>Màn hình chính được thiết kế mới hiển thị các đề xuất nội dung được cá nhân hóa, cho phép bạn truy cập dễ dàng hơn vào các mục yêu thích và cho phép bạn điều khiển tất cả các thiết bị kết nối ở một nơi.</p>\n"
                                                +
                                                "<h3>Điều khiển từ xa Magic Remote mới - Giống như một cây đũa thần</h3>\n"
                                                +
                                                "<p>Magic Remote được thiết kế lại có một thiết kế tiện lợi dễ cầm, và hệ thống trỏ và cuộn của thiết bị cho phép tìm kiếm nhanh hơn. AI tích hợp mang đến khả năng truy cập dễ dàng vào các dịch vụ, trong khi phím nóng cho các nhà cung cấp nội dung lớn mang đến cho bạn các phím tắt cho tất cả các mục yêu thích của mình. Bên cạnh tất cả những tính năng đó, bạn có Magic Tap, một thủ thuật mới thông minh kết nối điện thoại của bạn với TV.</p>\n"
                                                +
                                                "<h3>Điều khiển bằng giọng nói - Trung tâm kiểm soát thiết bị thông minh của bạn</h3>\n"
                                                +
                                                "<p>LG ThinQ cho phép ra lệnh đơn giản và điều khiển hệ sinh thái Home IoT của bạn với khả năng nhận dạng giọng nói tự nhiên*. Bạn có thể điều khiển TV LG NanoCell bằng giọng nói và truy cập giải trí nhanh hơn.</p>\n"
                                                +
                                                "<p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "0f559d9c05a54a4eb206043166cc492b.jpg",
                                                                "bcab043fc853b12622106119e7c77eeb.jpg",
                                                                "b22acb1c4279849c51181f7a8ad78f2f.jpg",
                                                                "cda7f847f1843daf4801ae101e605378.jpg",
                                                                "cb2348956a7ed1de7f1fc6cc76e283de.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Tổng công suất loa", "20W" },
                                { "Số lượng loa", "2" },
                                {
                                                "Công nghệ âm thanh",
                                                "AI Sound / Pro, AI Acoustic Tuning, Clear Voice III, Sound Alive, Sound Mode Sync" },
                                { "Thương hiệu", "LG" },
                                { "Xuất xứ thương hiệu", "Hàn Quốc" },
                                { "Điều khiển tivi bằng điện thoại", "Có" },
                                { "Hệ điều hành, giao diện", "webOS" },
                                {
                                                "Công nghệ xử lý hình ảnh",
                                                "4K Upscaling, HDR10 Pro, FILMMAKER MODE, 4K Upscaler, HGIG Mode, Active HDR, HLG," },
                                { "Model", "55NANO77TPA" },
                                { "Kết nối không dây", "Screen share, Airplay2" },
                                { "Cổng HDMI", "3 Cổng" },
                                { "Cổng internet (LAN)", "Có" },
                                { "Wifi", "Có" },
                                { "Xuất xứ", "Indonesia" },
                                { "Năm ra mắt", "2021" },
                                { "Remote thông minh", "Có" },
                                { "Độ phân giải", "3840 x 2160 pixels" },
                                { "Kích thước màn hình", "55 inch" },
                                { "Kích thước không chân/treo tường", "1235 x 715 x 58.1 mm" },
                                { "Kích thước có chân/đặt bàn", "1235 x 774 x 232 mm" },
                                { "Loại Tivi", "Smart Tivi" },
                                { "USB", "2 Cổng" },
                                { "Khối lượng không chân", "14.8kg" },
                                { "Khối lượng có chân", "15kg" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Google Tivi Coocaa HD 32 Inch - 32Z72 Youtube Netfilx Smart TV 2025 new tv - Hàng Chính Hãng",
                                new BigDecimal(6000000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandCoocaa,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("09dea12b4a1258c8114d38a04708c33f.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\">\n" +
                                                "<p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "428a47f91bf21446fd5219cc32905022.jpg",
                                                                "8dacf2c93bca3e74d276e58a38ac6f29.jpg",
                                                                "8a7b17ad38be726de940b2a02058de2f.jpg",
                                                                "e4b742ee1c14d9970ee65c758de509f9.jpg",
                                                                "a587c09a67f502369c7f1d69137b6b47.jpg",
                                                                "0e812d1247f4fbbdddf04ad79007bafb.jpg",
                                                                "b9c10aa0f4db16aa04e3db91fcc2b2bd.jpg",
                                                                "7601c032dc7f572221b07bcf1796e562.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Thương hiệu", "coocaa" },
                                { "Xuất xứ thương hiệu", "Trung Quốc" },
                                { "Model", "V-32Y72" },
                                { "Xuất xứ", "Indonesia" },
                                { "Xem 3D", "Không" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Google Tivi Sony 4K 65 inch KD-65X75K - Model 2025",
                                new BigDecimal(15880000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandSony,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("c8ace55e4ba064074aba68c12e30519d.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><h3>Cảm giác thật với màu sắc và độ tương phản đẹp</h3>\n"
                                                +
                                                "<p><strong>Google Tivi Sony 4K 65 inch KD-65X75K</strong> với bộ xử lý mạnh mẽ của chúng tôi trên TV Sony BRAVIA 4K này sử dụng các thuật toán nâng cao để giảm nhiễu và tăng độ chi tiết. Với tín hiệu 4K rõ ràng hơn nữa, mọi thứ bạn xem đều gần hơn với chất lượng 4K, đầy đủ màu sắc và độ tương phản sống động như thật.</p>\n"
                                                +
                                                "<h3>Độ phân giải 4K siêu cao cho mọi nội dung bạn xem</h3>\n" +
                                                "<p>Xem hình ảnh 4K tuyệt đẹp, phong phú với chi tiết và kết cấu thế giới thực, được cung cấp độc quyền bởi Bộ xử lý 4K X1 của chúng tôi. Hình ảnh quay ở độ phân giải 2K và thậm chí là HD được nâng cấp lên gần mức chi tiết 4K bằng 4K X-Reality PRO bằng cách sử dụng cơ sở dữ liệu 4K độc đáo.</p>\n"
                                                +
                                                "<h3>Âm thanh tinh khiết, tự nhiên mà không bị ảnh hưởng</h3>\n" +
                                                "<p>Công nghệ Clear Phase phân tích phản hồi của loa vật lý và so sánh nó với tín hiệu âm thanh gốc trên nhiều dải tần. Dữ liệu này được sử dụng để loại bỏ bất kỳ đỉnh hoặc giảm nào trong phản ứng tự nhiên của loa để mang lại âm thanh thuần khiết, tự nhiên với khả năng tái tạo đồng đều, mượt mà ở tất cả các tần số.</p>\n"
                                                +
                                                "<h3>Bạn muốn xem gì, hãy nói để tìm kiếm</h3>\n" +
                                                "<p>Tìm kiếm bằng giọng nói để nhanh chóng tìm nội dung hấp dẫn. Bạn có thể xem chương trình hoặc phim khi gọi tên hoặc dùng lệnh tìm kiếm bằng giọng nói như “tìm phim hành động”. Nhấn vào nút thoại trên điều khiển từ xa để bắt đầu.</p>\n"
                                                +
                                                "<h3>Sẵn sàng hoạt động trong những điều kiện khắc nghiệt nhất</h3>\n" +
                                                "<p>Ti-vi Sony vô cùng bền bỉ khi được chế tạo bằng công nghệ X-Protection PRO mới có nhiều cải tiến. Không chỉ được trang bị khả năng chống bụi và chống ẩm vượt trội, những ti-vi này còn đạt tiêu chuẩn cao nhất trong các bài kiểm tra chống sét của Sony, nghĩa là TV của bạn được bảo vệ khi sét đánh và điện áp tăng vọt. Hãy tiếp tục thưởng thức nội dung giải trí liền mạch với một chiếc TV bền bỉ hơn với thời gian.</p>\n"
                                                +
                                                "<h3>Tiết kiệm năng lượng, thân thiện với môi trường</h3>\n" +
                                                "<p>TV BRAVIA XR của chúng tôi không chỉ được thiết kế cho trải nghiệm xem đắm chìm mà còn lưu ý đến môi trường. Chúng tôi cam kết bảo vệ môi trường trong quá trình sản xuất TV của mình. Chế độ Tắt hiển thị giúp tiết kiệm năng lượng bằng cách tắt hiển thị hình ảnh mà không đặt TV ở chế độ chờ hoàn toàn khi chỉ nghe âm thanh.</p>\n"
                                                +
                                                "<p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "389de57c4e696050de4fe0c41ae884a7.jpg",
                                                                "b7f1215748d236bf5863930cc0049911.jpg",
                                                                "524d4dbe5242cb53f66757700b8d0744.jpg",
                                                                "8949d8a95d5bdcccd006cb51018eb330.jpg",
                                                                "e68dc83462378ce6ab8606920dadde1a.jpg",
                                                                "8480076d5cddf309f4eb988911fbb256.jpg",
                                                                "16cee6b94d4407c74999479feec89508.jpg",
                                                                "e27ca4f13ff3d2ff08f375b8352a583d.jpg",
                                                                "eff46b376f67db17b3c9e69693ac4791.jpg",
                                                                "cb9d686ee9e32983b26e3670a5013a20.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Tổng công suất loa", "20W" },
                                {
                                                "Công nghệ âm thanh",
                                                "Bộ khuếch đại âm thanh S-Master Digital Amplifier, Dolby Atmos, DTS Digital Surround, Tăng chất lượng âm thanh với loa X-Balanced, Âm thanh vòm S-Force Front Surround" },
                                { "Thương hiệu", "Sony" },
                                { "Xuất xứ thương hiệu", "Nhật Bản" },
                                {
                                                "Điều khiển bằng giọng nói",
                                                "Google Assistant có tiếng Việt, Tìm kiếm giọng nói trên YouTube bằng tiếng Việt" },
                                { "Điều khiển tivi bằng điện thoại", "Ứng dụng Android TV" },
                                { "Tivi kỹ thuật số (DVB-T2)", "Có" },
                                { "Loại/ Công nghệ màn hình", "LED nền (Direct LED)" },
                                { "Tần số quét", "50Hz" },
                                { "Hệ điều hành, giao diện", "Google TV" },
                                { "Công nghệ xử lý hình ảnh",
                                                "Bộ xử lý X1 4K HDR, Chuyển động mượt Motionflow XR 200" },
                                { "Model", "KD-65X75K" },
                                { "Cổng HDMI", "4" },
                                { "Cổng internet (LAN)", "Có" },
                                { "Wifi", "Có" },
                                { "Xuất xứ", "Việt Nam" },
                                { "Remote thông minh", "Remote tích hợp micro tìm kiếm bằng giọng nói (RMF-TX520P)" },
                                { "Độ phân giải", "UHD 4K (3840 x 2160)" },
                                { "Chiếu hình từ điện thoại lên TV", "Chromecast" },
                                { "Kích thước màn hình", "65 inch" },
                                { "Kích thước không chân/treo tường", "Ngang 146.3 cm - Cao 85.2 cm - Dày 8.7 cm" },
                                { "Kích thước có chân/đặt bàn", "Ngang 146.3 cm - Cao 91.4 cm - Dày 33.7 cm" },
                                { "Loại Tivi", "Google tivi" },
                                { "Các ứng dụng sẵn có",
                                                "Clip TV, FPT Play, Galaxy Play (Fim+), Netflix, VieON, VTVcab ON, YouTube" },
                                { "USB", "2" },
                                { "Khối lượng không chân", "21.4kg" },
                                { "Khối lượng có chân", "22.1kg" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Smart Tivi Samsung 43 InchSmart Tivi Samsung 43 Inch UA43T6500AKXXV - Hàng Chính Hãng",
                                new BigDecimal(10769000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("2163ac7808fe1e0d68de7980df8fa7e1.png"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><p>THÔNG SỐ KỸ THUẬT:</p>\n"
                                                +
                                                "<ul><li>Loại Tivi:&nbsp;Smart Tivi</li>\n" +
                                                "<li>Kích cỡ màn hình:&nbsp;43 inch</li>\n" +
                                                "<li>Độ phân giải:&nbsp;Full HD&nbsp;</li>\n" +
                                                "<li>Loại màn hình:&nbsp;LED viền (Edge LED), IPS LCD</li>\n" +
                                                "<li>Công nghệ hình ảnh: Chế độ hình ảnh tự nhiên Natural Mode,&nbsp;Chế độ phim Film mode,&nbsp;HDR,&nbsp;Hyper Real Engine,&nbsp;Khử nhiễu hình ảnh Digital Clean View,&nbsp;Kiểm soát đèn nền Micro Dimming Pro,&nbsp;Lọc nhiễu hình ảnh Ultra Clean View,&nbsp;Nâng cấp độ tương phản Contrast Enhancer,&nbsp;PurColor,&nbsp;Độ tương phản cao - Mega Contrast</li>\n"
                                                +
                                                "<li>Tần số quét thực:&nbsp;50 Hz</li>\n" +
                                                "<li>Công nghệ âm thanh:&nbsp;Dolby Digital Plus</li>\n" +
                                                "<li>Tổng công suất loa:&nbsp;20W</li>\n" +
                                                "<li>Hệ điều hành:&nbsp;TizenOS 5.5</li>\n" +
                                                "<li>Điều khiển bằng giọng nói:&nbsp;Bixby (Chưa có tiếng Việt)Tìm kiếm giọng nói trên YouTube bằng tiếng Việt</li>\n"
                                                +
                                                "<li>Chiếu hình từ điện thoại lên TV:&nbsp;AirPlay 2, Screen Mirroring</li>\n"
                                                +
                                                "<li>Remote thông minh:&nbsp;One Remote đa nhiệm nhỏ gọn</li>\n" +
                                                "<li>Điều khiển tivi bằng điện thoại:&nbsp;SmartThings</li>\n" +
                                                "<li>Kết nối Internet:&nbsp;Cổng mạng LAN, Wifi</li>\n" +
                                                "<li>Kết nối không dây:&nbsp;Bluetooth (Kết nối loa, thiết bị di động)</li>\n"
                                                +
                                                "<li>USB:&nbsp;1 cổng USB A</li>\n" +
                                                "<li>Cổng nhận hình ảnh, âm thanh:&nbsp;2 cổng HDMI có 1 cổng HDMI ARC, 1 cổng Composite, 1 cổng Component</li>\n"
                                                +
                                                "<li>Cổng xuất âm thanh:&nbsp;1 cổng Optical (Digital Audio), 1 cổng ARC</li>\n"
                                                +
                                                "<li>Kích thước có chân, đặt bàn:&nbsp;Ngang 97.99 cm - Cao 59.66 cm - Dày 21.7 cm</li>\n"
                                                +
                                                "<li>Khối lượng có chân:&nbsp;8.2 kg</li>\n" +
                                                "<li>Kích thước không chân, treo tường:&nbsp;Ngang 97.99 cm - Cao 56.5 cm - Dày 7.73 cm</li>\n"
                                                +
                                                "<li>Khối lượng không chân:&nbsp;8 kg</li>\n" +
                                                "</ul><p>&nbsp;</p>\n" +
                                                "<p>ĐẶC ĐIỂM NỔI BẬT:</p>\n" +
                                                "<p>- Thiết kế sang trọng, trang nhã với độ mỏng tối ưu, có chân đế vững chắc, tinh giản nâng tầm sang trọng và đẳng cấp cho không gian sống.</p>\n"
                                                +
                                                "<p>- Độ phân giải Full HD rõ nét gấp 2 lần so với độ phân giải HD.</p>\n"
                                                +
                                                "<p>- Dải màu sắc phong phú kiến tạo hình ảnh sống động nổi bật từ công nghệ HDR cho phép bạn đắm chìm vào từng phân cảnh sống động như thật. Mang đến những khung hình rực rỡ nổi bật với độ sắc nét đến từng chi tiết.</p>\n"
                                                +
                                                "<p>- Độ sáng và độ nét của hình ảnh trên tivi được tăng cường tối đa nhờ công nghệ Digital Clean View.</p>\n"
                                                +
                                                "<p>- Dải màu sắc hiển thị rộng lớn và rực rỡ nhờ công nghệ PurColor, giúp bạn đắm chìm trong từng khung hình sống động và chân thực như bước ra từ cuộc sống.</p>\n"
                                                +
                                                "<p>- Công nghệ Contrast Enhancer giúp tăng cường độ tương phản hình ảnh tối đa, phủ nhiều lớp sáng tối lên toàn bộ màn hình giúp từng khu vực, vật thể trong khung hình trở nên sống động và chân thật hơn.</p>\n"
                                                +
                                                "<p>- Âm thanh trong trẻo với công nghệ âm thanh Dolby Digital Plus.</p>\n"
                                                +
                                                "<p>- Ứng dụng SmartThings hiện đại cho phép điều khiển tivi qua điện thoại dễ dàng.</p>\n"
                                                +
                                                "<p>- Chiếu màn hình điện thoại Android, IOS lên tivi qua tính năng Screen Mirroring và AirPlay 2 hiện đại.</p>\n"
                                                +
                                                "<p>&nbsp;</p>\n" +
                                                "<p>* Giá trên chưa bao gồm chi phí lắp đặt và vật tư linh kiện.</p>\n"
                                                +
                                                "<p>ĐIỆN MÁY TIỆN LỢI S52 - Nơi mua sắm điện máy tiện lợi dành cho giới trẻ</p><p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "97ed02277b07b00f9fa31c6d52d4a084.jpg",
                                                                "94952fc182c1d460713b9bc8e42f8637.jpg",
                                                                "d541a346c7dbe17ac02a511811e369c2.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Thương hiệu", "Samsung" },
                                { "Xuất xứ thương hiệu", "Hàn Quốc" },
                                { "Xuất xứ", "Việt Nam" },
                                { "Xem 3D", "Không" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Smart Tivi Samsung 4K 43 inch UA43AU7002",
                                new BigDecimal(8910000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("d6a01751c78f0a6dfb4b8f92d329bc04.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><h3>Hiển thị màu sắc sống động và hình ảnh chân thực - Công nghệ PurColor</h3>\n"
                                                +
                                                "<p>Choáng ngợp trước dải màu sắc hiển thị rộng lớn và rực rỡ từ công nghệ PurColor. Đắm chìm trong từng khung hình sống động và chân thực như bước ra từ cuộc sống.</p>\n"
                                                +
                                                "<h3>Cảm nhận từng chi tiết hình ảnh chuẩn 4K -&nbsp; Bộ xử lý hình ảnh Crystal 4K</h3>\n"
                                                +
                                                "<p>Nâng cấp mọi nội dung bạn yêu thích lên chuẩn 4K ấn tượng. Chiêm ngưỡng sắc màu hiển thị sống động và chân thực với công nghệ Color Mapping tiên tiến.</p>\n"
                                                +
                                                "<h3>Rõ khung hình, mượt chuyển động - Công nghệ Motion Xcelerator</h3>\n"
                                                +
                                                "<p>Trong các phân cảnh đua xe hoặc rượt đuổi, các khung hình thay đổi liên tục dễ dẫn đến hiện tượng \"bóng ma\" trên màn hình. Công nghệ Motion Xcelerator sẽ tự động bổ sung thêm khung hình để nội dung rõ nét và mượt mà hơn.</p>\n"
                                                +
                                                "<h3>Chất lượng hình ảnh chân thực với độ phân giải UHD 4K&nbsp;</h3>\n"
                                                +
                                                "<p>Sở hữu gấp 4 lần điểm ảnh so với TV FHD thông thường, Smart TV UHD 4K mang đến khung hình sắc nét và sống động, cho bạn thưởng thức từng chi tiết dù là nhỏ nhất.</p>\n"
                                                +
                                                "<h3>Hoàn hảo trong từng khung hình sáng và tối - Công nghệ HDR</h3>\n"
                                                +
                                                "<p>Cải thiện độ sáng TV vượt trội với công nghệ HDR đỉnh cao. Cung cấp dải màu sắc lớn và hiển thị chi tiết, sắc nét hình ảnh ngay cả trong khung hình tối sâu thẳm.</p>\n"
                                                +
                                                "<h3><br>Bộ đôi hoàn hảo TV &amp; loa thanh - Công nghệ Q-Symphony</h3>\n"
                                                +
                                                "<p>Thăng hoa cùng những thanh âm tuyệt vời, được đồng bộ liền mạch giữa loa thanh Samsung và TV. Công nghệ Q-Symphony tận dụng loa TV và loa thanh, kiến tạo không gian giải trí đỉnh cao, bao trùm mọi giác quan.</p>\n"
                                                +
                                                "<h3>Viền mỏng ấn tượng cho trải nghiệm rộng mở - Thiết kế 3 cạnh không viền</h3>\n"
                                                +
                                                "<p>Với ngôn ngữ thiết kế theo phong cách tinh giản, Smart TV UHD 4K như một bức tranh thuần khiết không viền mang đến cho bạn trải nghiệm xem hoàn hảo và đậm chất điện ảnh.</p>\n"
                                                +
                                                "<h3>Học và làm việc tại gia - Kết nối dễ dàng với đa thiết bị</h3>\n" +
                                                "<p>Tận hưởng tiện ích kết nối vượt trội từ Smart TV UHD 4K. Cho bạn dễ dàng truy cập vào máy tính, laptop và thiết bị di động ngay trên TV.</p>\n"
                                                +
                                                "<p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "d03fe4ee47b5821aa4d7c0d72a2dbff8.jpg",
                                                                "d9fa14cdb4d8a590a51f6e6b28057596.jpg",
                                                                "c75c77017a4c2fee07468ddf820e0672.jpg",
                                                                "0fdd1cd576e588fab8c0566f7a43c334.jpg",
                                                                "be20a5f81dd8c6c942102d54ba4e644d.jpg",
                                                                "e46b0ceb19ab72222a1f6ad0d09a6533.jpg",
                                                                "e0b2cb05cf75f9c4ca029f635290ebe0.jpg",
                                                                "7b193e73cc90bfba4e019e0e58d477db.jpg",
                                                                "b7ddc0ea50df541f59e0117a193ee4f2.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Tổng công suất loa", "20W" },
                                { "Số lượng loa", "2" },
                                { "Công nghệ âm thanh", "Dolby Digital Plus, Q-Symphony, Blutooth Audio" },
                                { "Thương hiệu", "Samsung" },
                                { "Xuất xứ thương hiệu", "Hàn Quốc" },
                                { "Tivi kỹ thuật số (DVB-T2)", "Có" },
                                { "Hệ điều hành, giao diện", "Tizen Os" },
                                {
                                                "Công nghệ xử lý hình ảnh",
                                                "Crystal Processor 4K, Micro Dimming, HLG (Hybrid Log Gamma), Tương thích HDR10+, Filmmaker Mode" },
                                { "Model", "UA43AU7002" },
                                { "Cổng HDMI", "3 Cổng" },
                                { "Cổng internet (LAN)", "Có" },
                                { "Xuất xứ", "Việt Nam" },
                                { "Độ phân giải", "3840 x 2160 pixels" },
                                { "Kích thước màn hình", "43 inch" },
                                { "Loại Tivi", "Smart Tivi 4K" },
                                { "USB", "1 Cổng" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Smart Tivi NanoCell LG 4K 55 inch 55NANO86TPA",
                                new BigDecimal(27900000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandLG,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("d648dfd120323cd4f6f32d90874d0265.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><h3>8 triệu điểm ảnh cùng thể hiện trong 1 bức tranh</h3>\n"
                                                +
                                                "<p>Màu sắc Pure Colors truyệt đẹp trên màn hình Real 4K của Tivi NanoCell. Với khoảng 8 triệu điểm ảnh, Tivi 4K đích thực truyền tải hình ảnh sắc nét hơn và chi tiết hơn rõ rệt so với Tivi HD thông thường.</p>\n"
                                                +
                                                "<p>Với Tivi NanoCell, độ phân giải Real 4K được bổ sung công nghệ NanoCell để mang lại trải nghiệm 4K vượt qua mọi tiêu chuẩn quốc tế.</p>\n"
                                                +
                                                "<h3>Mọi màu sắc hiện lên trong veo</h3>\n" +
                                                "<p>Tivi LG NanoCell sử dụng các hạt nano, công nghệ Nano đặc biệt của riêng chúng tôi, để lọc và tinh chỉnh màu sắc, loại bỏ tín hiệu gây nhiễu khỏi các bước sóng RGB.</p>\n"
                                                +
                                                "<p>Nghĩa là, chỉ những màu sắc tinh khiết, chính xác mới được hiển thị trên màn hình. Nhờ đó hình ảnh tạo ra sẽ tươi đẹp hơn, chân thực hơn, giúp nội dung của bạn trở nên sống động.</p>\n"
                                                +
                                                "<h3>Tivi thiết kế tinh tế, đậm chất nghệ thuật</h3>\n" +
                                                "<p>LG NanoCell Tivi được thiết kế nhằm đáp ứng đa dạng tính thẩm mỹ cho nội thất tại gia. Với thiết kế tối giản, kiểu dáng đẹp, Tivi nằm chắc chắn trên tường nhà như một tác phẩm nghệ thuật, tăng thêm vẻ đẹp đồng thời tiết kiệm tối đa không gian nhà bạn.</p>\n"
                                                +
                                                "<h3>Nano Cinema - Chất lượng ngang tầm với màn chiếu lớn</h3>\n" +
                                                "<p>Pure Colors và một loạt các công nghệ màn hình mới nhất mang cả rạp phim về nhà bạn với LG NanoCell Tivi. Với công nghệ HDR nâng cao của chúng tôi, các công nghệ được nâng cấp của Dolby, và một chế độ điện ảnh mới vừa được hãng công bố sẽ mang đến trải nghiệm điện ảnh thực sự.</p>\n"
                                                +
                                                "<h3>Trung tâm giải trí</h3>\n" +
                                                "<p>Truy cập Ứng dụng Disney+ và Netflix. Tận hưởng từ những bộ phim mới nhất đến chương trình Tivi hay phim tài liệu hoặc xem thể thao trực tuyến, mọi thứ đều hội tụ tại kho ứng dụng đa dạng.</p>\n"
                                                +
                                                "<h3>Dolby Vision IQ và Dolby Atmos&nbsp;Nâng tầm trải nghiệm xem</h3>\n"
                                                +
                                                "<p>Dolby Vision IQ tự động điều chỉnh các cài đặt hình ảnh dựa trên thể loại nội dung và môi trường xung quanh, còn Dolby Atmos mang lại âm thanh vòm đa chiều - một sự kết hợp mạnh mẽ tạo nên những thước phim điện ảnh ngoạn mục.</p>\n"
                                                +
                                                "<h3>CHẾ ĐỘ FILMMAKER MODE&nbsp;Truyền tài chân thực góc nhìn của đạo diễn</h3>\n"
                                                +
                                                "<p>FILMMAKER MODE tắt tính năng làm mịn chuyển động trong khi vẫn giữ nguyên tỷ lệ khung hình, màu sắc và tốc độ khung hình ban đầu.</p>\n"
                                                +
                                                "<p>Khả năng này mang đến một cách chính xác tầm nhìn ban đầu của đạo diễn, vì vậy bạn sẽ được trải nghiệm bộ phim đúng như dự định của đạo diễn.</p>\n"
                                                +
                                                "<h3>HDR 10 Pro&nbsp;Nâng cấp mọi trải nghiệm xem</h3>\n" +
                                                "<p>Công nghệ dải động của riêng LG, HDR 10 Pro, điều chỉnh độ sáng để tăng cường màu sắc, thể hiện mọi chi tiết nhỏ nhất, và mang lại độ rõ như thật cho mọi hình ảnh - công nghệ này cũng tăng cường nội dung HDR thông thường. Giờ đây tất cả các bộ phim và chương trình yêu thích của bạn sẽ sống động và sôi động hơn từ đầu đến cuối.</p>\n"
                                                +
                                                "<h3>Nano Gaming - Trang hoàng thế giới Game với Tivi</h3>\n" +
                                                "<p>Từ những hang động tối tăm nhất đến những thế giới mới tươi sáng nhất, LG NanoCell Tivi trang hoàng trò chơi của bạn với màu sắc sống động. Công nghệ chơi game qua điện toán đám mây (phụ thuộc vào quốc gia, khu vực) và tự động điều chỉnh hình ảnh chất lượng cao mang đến trải nghiệm chơi game thực sự thú vị.</p>\n"
                                                +
                                                "<h3>Tối ưu hóa trò chơi -&nbsp;Mức độ kiểm soát chưa từng thấy</h3>\n"
                                                +
                                                "<p>Trình tối ưu hóa trò chơi đưa tất cả các cài đặt trò chơi của bạn ở một nơi. Ứng dụng tự động điều chỉnh hình ảnh để tối ưu hóa đồ họa bất kể loại trò chơi bạn đang chơi là gì. Bạn cũng có thể chuyển đổi cả hai công nghệ NVIDIA G-SYNC và AMD FreeSync để tất cả các trò chơi của bạn rõ ràng hơn và mượt mà hơn với độ trễ, giật hình và trộn hình thấp hơn.</p>\n"
                                                +
                                                "<h3>Đối tác với Xbox&nbsp;- Một sự kết hợp bất khả chiến bại</h3>\n" +
                                                "<p>Mở ra thế giới game tự do của bạn. LG là đối tác của Xbox nhằm đảm bảo bạn luôn sẵn sàng chơi nhữngi game thế hệ mới. Tận hưởng những gì tinh túy nhất của Xbox nhờ chất lượng hình ảnh tuyệt đẹp và thời gian phản hồi cực nhanh.</p>\n"
                                                +
                                                "<h3>Nano Sport - Thưởng thức những trận đấu đẳng cấp ở chất lượng cao</h3>\n"
                                                +
                                                "<p>LG NanoCell Tivi mang đến trải nghiệm hồi hộp khi xem các trận đấu. Âm thanh Vòm Bluetooth mang toàn bộ bầu không khí ở sân vận động về nhà bạn, còn Thông báo Thể thao luôn cập nhật kịp thời cho bạn mọi tin tức mới nhất về đội bạn yêu thích.</p>\n"
                                                +
                                                "<h3>Thông&nbsp;báo thể thao&nbsp;- Không bao giờ bỏ lỡ trận đấu của câu lạc bộ yêu thích</h3>\n"
                                                +
                                                "<p>Thông báo thể thao thông báo cho bạn trước, trong và sau các trận đấu. Bạn sẽ không bao giờ phải lo lắng về việc bỏ lỡ các trận đấu lớn từ các đội yêu thích của mình, ngay cả khi bạn đang xem nội dung khác.</p>\n"
                                                +
                                                "<h3>Bluetooth Surround - Âm thanh vòm bluetooth chân thực&nbsp;hoành tráng như tại sân vận động</h3>\n"
                                                +
                                                "<p>Dễ dàng kết nối loa Bluetooth để có trải nghiệm âm thanh vòm không dây thực sự. Tất cả các trải nghiệm âm thanh đều trở nên phong phú hơn và thực tế hơn, mang lại bầu không tại sân vận động về phòng khách nhà bạn.</p>\n"
                                                +
                                                "<h3>Bộ xử lý α7&nbsp;Gen4 AI 4K - Bộ não sẽ thổi bay tâm trí của bạn</h3>\n"
                                                +
                                                "<p>Cốt lõi của mỗi Tivi LG OLED là Bộ xử lý AI α7&nbsp;Gen4 4K, bộ não của Tivi, có khả năng tự động phát hiện và tối ưu hóa nội dung bạn đang xem để có trải nghiệm hình ảnh và âm thanh đỉnh cao.</p>\n"
                                                +
                                                "<h3>AI Picture&nbsp;- Nâng cấp hình ảnh chuyên nghiệp hơn</h3>\n" +
                                                "<p>AI Picture tự động xác định thể loại nội dung bạn đang xem và loại bỏ nhiễu để tối ưu hóa chất lượng hình ảnh. Điều này mang lại hình ảnh chất lượng cao, sắc nét hơn, cải thiện trải nghiệm xem Tivi bất kể bạn xem nội dung gì.</p>\n"
                                                +
                                                "<h3>AI Sound -&nbsp;Trải nghiệm âm thanh tinh chỉnh, sống động hơn</h3>\n"
                                                +
                                                "<p>AI Sound phân tích nguồn âm thanh và tối ưu hóa âm thanh dựa trên thể loại nội dung bạn đang xem. Điều này mang lại âm thanh rõ ràng hơn, từ giọng hát dễ phân biệt hơn cho đến các hiệu ứng âm thanh tự nhiên, sống động như thật.</p>\n"
                                                +
                                                "<h3>Trí tuệ nhân tạo thông minh vượt bậc ThinQ AI</h3>\n" +
                                                "<p>LG ThinQ ở đây để tối ưu hóa trải nghiệm Tivi của bạn. Chọn trợ lý giọng nói yêu thích của bạn, điều khiển Tivi bằng giọng nói, màn hình chính với giao diện hoàn toàn mới, tất cả nhằm mang đến sự thuận tiện khi điều khiển Tivi, mọi thứ trở nên đơn giản, dễ dàng hơn.</p>\n"
                                                +
                                                "<h3>Màn hình chính mới&nbsp;- Giao diện thiết kế hoàn toàn mới</h3>\n"
                                                +
                                                "<p>Màn hình chính được thiết kế mới hiển thị các đề xuất nội dung được cá nhân hóa, cho phép bạn truy cập dễ dàng hơn vào các mục yêu thích và cho phép bạn điều khiển tất cả các thiết bị kết nối ở một nơi.</p>\n"
                                                +
                                                "<h3>Điều khiển từ xa ma thuật mới</h3>\n" +
                                                "<p>Magic Remote được thiết kế lại giúp bạn cầm lên một cách dễ dàng, hệ thống trỏ và cuộn đặc trưng của LG Tivi cho phép tìm kiếm nhanh hơn.</p>\n"
                                                +
                                                "<p>Các tính năng AI tích hợp mang đến khả năng truy cập dễ dàng vào các dịch vụ, tinh chỉnh phím tắt cho tất cả các mục yêu thích để giúp bạn nhanh chóng truy cập đến từng ứng dung của các nhà phát hành đình đám nhất. Bên cạnh tất cả những tính năng đó, bạn có Magic Tap, một thủ thuật mới thông minh kết nối điện thoại của bạn với Tivi.</p>\n"
                                                +
                                                "<h3>Ra lệnh bằng giọng nói</h3>\n" +
                                                "<p>LG ThinQ cho phép ra lệnh đơn giản và điều khiển hệ sinh thái Home IoT (thiết bị thông minh trong nhà) của bạn với khả năng nhận dạng giọng nói tự nhiên.</p>\n"
                                                +
                                                "<p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "ffbc5b0d55d1eb02dbeb5cea95c93429.jpg",
                                                                "0540394f4bf24c01eb958b69e220ed0d.jpg",
                                                                "b71f0062e14576db7389d9e44301e979.jpg",
                                                                "6798fa2af6e43cb588c0021f3dbcac16.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Tổng công suất loa", "20W" },
                                { "Số lượng loa", "2" },
                                {
                                                "Công nghệ âm thanh",
                                                "DOLBY ATMOS, AI Acoustic Tuning, Bluetooth Surround Ready, Sound Mode Sync, AI Sound, Sound Alive, Clear Voice III" },
                                { "Thương hiệu", "LG" },
                                { "Xuất xứ thương hiệu", "Hàn Quốc" },
                                { "Điều khiển tivi bằng điện thoại", "Có" },
                                { "Tivi kỹ thuật số (DVB-T2)", "Có" },
                                { "Hệ điều hành, giao diện", "webOS" },
                                {
                                                "Công nghệ xử lý hình ảnh",
                                                "AI Picture - Face Enhancing, Dolby Vision IQ, Tự động điều chỉnh độ sáng bởi AI, AI Upscaling, Image Enhancing, Cinema HDR, HDR10 Pro, FILMMAKER MODE, HLG, HDR Dynamic Tone Mapping, 4K Upscaler, HGIG Mode" },
                                { "Model", "55NANO86TPA" },
                                { "Kết nối không dây", "Airplay2, Screen share, Mobile Connectivity" },
                                { "Cổng HDMI", "4 Cổng" },
                                { "Cổng internet (LAN)", "Có" },
                                { "Wifi", "Có (802.11ac)" },
                                { "Xuất xứ", "Indonesia" },
                                { "Năm ra mắt", "2021" },
                                { "Remote thông minh", "Có" },
                                { "Độ phân giải", "3840 x 2160 pixels" },
                                { "Kích thước màn hình", "55 inch" },
                                { "Kích thước không chân/treo tường", "1233 x 716 x 44.2 mm" },
                                { "Kích thước có chân/đặt bàn", "1233 x 781 x 264 mm" },
                                { "Loại Tivi", "Smart tivi 4K" },
                                {
                                                "Tương tác thông minh",
                                                "Trợ lý ảo Google Assistant, Apple Homekit, Google Home Connection, Amazon Echo Connection, Amazon Alexa, Trợ lý ảo Google Assistant, Bảng điều khiển nhà, AI Home, Nhận diện mệnh lệnh giọng nói - Intelligent Voice Recognition" },
                                { "USB", "3 Cổng" },
                                { "Khối lượng không chân", "16.3kg" },
                                { "Khối lượng có chân", "17.9kg" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Smart Tivi Samsung Crystal UHD 4K 50 inch UA50AU7700KXXV - Hàng chính hãng - Giao toàn quốc",
                                new BigDecimal(14990000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("a5025f22245a888945436508ce07dca0.png"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><p>Thông Tin Chi Tiết</p>\n"
                                                +
                                                "<p>Tổng công suất loa: 20W</p>\n" +
                                                "<p>Số lượng loa: 2</p>\n" +
                                                "<p>Công nghệ âm thanh: Dolby Digital Plus; Q-Symphony; Blutooth Audio</p>\n"
                                                +
                                                "<p>Tivi kỹ thuật số (DVB-T2): DVB-T2 (*VN: DVB-T2C)</p>\n" +
                                                "<p>Loại sản phẩm: LED</p>\n" +
                                                "<p>Độ phân giải: UHD 4K (3840 x 2160)</p>\n" +
                                                "<p>Kích thước màn hình: 50 inch</p>\n" +
                                                "<p>Kích thước không chân/treo tường: 1116.8 x 644.2 x 59.9 mm</p>\n" +
                                                "<p>Kích thước có chân/đặt bàn: 1116.8 x 719.1 x 250.2 mm</p>\n" +
                                                "<p>USB: 1</p>\n" +
                                                "<p>Khối lượng không chân: 11.4 kg</p>\n" +
                                                "<p>Khối lượng có chân: 11.6 kg</p>\n" +
                                                "<p>Âm Thanh</p>\n" +
                                                "<p>Tổng Công Suất Loa: 20W</p>\n" +
                                                "<p>Số Lượng Loa: 2</p>\n" +
                                                "<p>Trình Duyệt Web: Có</p>\n" +
                                                "<p>Screen Mirroring: Có</p>\n" +
                                                "<p>Kết nối bàn phím, chuột:Có</p>\n" +
                                                "<p>Tiện Ích</p>\n" +
                                                "<p>Tiết Kiệm Điện: Có</p>\n" +
                                                "<p>Ngôn Ngữ Hiển Thị: Đa Ngôn Ngữ</p>\n" +
                                                "<p>Tivi kỹ thuật số (DVB-T2): Có (cần ăng-ten)</p>\n" +
                                                "<p>Cổng Kết Nối</p>\n" +
                                                "<p>HDMI: Có x3</p>\n" +
                                                "<p>Audio Out: Có</p>\n" +
                                                "<p>LAN: Có</p>\n" +
                                                "<p>Kết Nối Wifi: Có</p>\n" +
                                                "<p>2. Thông tin bảo hành:</p>\n" +
                                                "<p>- Bộ sản phẩm bao gồm: Tivi, Chân đế, Adapter</p>\n" +
                                                "<p>- Bảo hành 12 tháng&nbsp;</p>\n" +
                                                "<p>- Trung tâm bảo hành:</p>\n" +
                                                "<p>3. Công lắp đặt:</p>\n" +
                                                "<p>- Chi phí vật tư: Nhân viên sẽ thông báo phí vật tư (ống đồng, dây điện ) khi khảo sát lắp đặt (Bảng kê xem tại ảnh 2). Khách hàng sẽ thanh toán trực tiếp cho nhân viên kỹ thuật sau khi việc lắp đặt hoàn thành - chi phí này sẽ không hoàn lại trong bất cứ trường hợp nào.</p>\n"
                                                +
                                                "<p>- Đơn vị vận chuyển giao hàng cho bạn KHÔNG có nghiệp vụ lắp đặt sản phẩm.</p>\n"
                                                +
                                                "<p>- Tìm hiểu thêm về Dịch vụ lắp đặt:</p>\n" +
                                                "<p>- Quy định đổi trả: Chỉ đổi/trả sản phẩm, từ chối nhận hàng tại thời điểm nhận hàng trong trường hợp sản phẩm giao đến không còn nguyên vẹn, thiếu phụ kiện hoặc nhận được sai hàng. Khi sản phẩm đã được cắm điện sử dụng và/hoặc lắp đặt, và gặp lỗi kĩ thuật, sản phẩm sẽ được hưởng chế độ bảo hành theo đúng chính sách của nhà sản xuất.</p><p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "68a04257d210500cbf936844ef6025d1.png",
                                                                "f7bbfb3229ed61d5d5e600d7c4d99666.png",
                                                                "b74f30faec748a0216e75abda98f7ad4.png",
                                                                "b9e5b7682a28b0f2fc630cdbd8d15bfd.png",
                                                                "51137bab06719944ba92a7bd3908da65.png",
                                                                "d527c37d9319e546e785b6325841a66e.png",
                                                                "b5bd3ed9b9663fa12ac263584e9f891b.png",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Thương hiệu", "Samsung" }, { "Xuất xứ thương hiệu", "Việt Nam" },
                                { "Xuất xứ", "Việt Nam" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Smart Tivi Samsung Crystal UHD 4K 43 Inch UA43AU8000KXXV - Hàng Chính Hãng",
                                new BigDecimal(16642000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("0a8a9d5cd03621f6814059f92b6180e1.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\">\n" +
                                                "  <p>- Miễn phí lắp đặt</p>\n" +
                                                "   <p>- Chỉ tính phí vật tư phát sinh</p>\n" +
                                                "   <p>***</p>\n" +
                                                "   <p>Thông số kỹ thuật:</p>\n" +
                                                "   <p>TỔNG QUAN</p>\n" +
                                                "   <p>- Kích thước màn hình: 43\"</p>\n" +
                                                "   <p>- Độ phân giải: 4K (3840 x 2160 px)</p>\n" +
                                                "   <p>CÔNG NGHỆ HÌNH ẢNH VÀ M THANH</p>\n" +
                                                "   <p>- Chip xử lý: Crystal Processor 4K</p>\n" +
                                                "   <p>- Công nghệ hình ảnh:</p>\n" +
                                                "   <p>• Công nghệ HDR</p>\n" +
                                                "   <p>• Hỗ trợ hiển thị hình ảnh chất lượng cao HDR 10+</p>\n" +
                                                "   <p>• Dynamic Crystal Color</p>\n" +
                                                "   <p>• UHD Dimming</p>\n" +
                                                "   <p>• Contrast Enhancer</p>\n" +
                                                "   <p>• Auto Motion Plus</p>\n" +
                                                "   <p>• Motion Xlerator Turbo</p>\n" +
                                                "   <p>• Dynamic Crystal Color</p>\n" +
                                                "   <p>- Tần số quét: 60Hz</p>\n" +
                                                "   <p>- Chỉ số chuyển động: 100</p>\n" +
                                                "   <p>- Công nghệ âm thanh:</p>\n" +
                                                "   <p>• Dolby Digital Plus</p>\n" +
                                                "   <p>• Q-Symphony Lite</p>\n" +
                                                "   <p>• Adaptive Sound</p>\n" +
                                                "   <p>CỔNG KẾT NỐI</p>\n" +
                                                "   <p>- Bluetooth: Có, Bluetooth 4.2</p>\n" +
                                                "   <p>- Wifi: Có, Wifi 5G</p>\n" +
                                                "   <p>- Internet: Có</p>\n" +
                                                "   <p>- LAN: Có</p>\n" +
                                                "   <p>- Cổng HDMI: 3 cổng</p>\n" +
                                                "   <p>- Cổng AV: 1 cổng</p>\n" +
                                                "   <p>- Cổng RF In: Không</p>\n" +
                                                "   <p>- Cổng âm thanh: eARC, Digital Audio Out</p>\n" +
                                                "   <p>- USB: 2 cổng</p>\n" +
                                                "   <p>- Tích hợp đầu thu kỹ thuật số: Analog/ DVB-T2</p>\n" +
                                                "   <p>MÔ TẢ SẢN PHẨM</p>\n" +
                                                "   <p>Thiết kế thanh mảnh</p>\n" +
                                                "   <p>Màu sắc hiển thị rực rỡ</p>\n" +
                                                "   <p>• Công nghệ Crystal Display có tác dụng tối ưu hóa màu sắc hiển thị, giúp cho từng khung hình đều được hiển thị trên dải màu rộng, từ đó mang lại cho bạn những trải nghiệm giải trí sống động đến bất ngờ.\\</p>\n"
                                                +
                                                "   <p>Độ phân giải 4K</p>\n" +
                                                "   <p>• Samsung Crystal UHD 4K 43 inch UA43AU8000KXXV có độ phân giải hình ảnh ở mức UHD 4K, với số lượng điểm ảnh cao gấp 4 lần so với các tivi có độ phân giải FullHD, cho phép bạn thưởng thức được những khung hình có độ chi tiết cao hơn, rõ nét hơn.</p>\n"
                                                +
                                                "   <p>Tăng cường độ sáng</p>\n" +
                                                "   <p>Nhờ vào công nghệ HDR cùng với sự hỗ trợ đắc lực của HDR10+, dải màu sắc và độ sáng của hình ảnh sẽ được cải thiện rõ rệt, giúp bạn quan sát rõ cả những khung hình có độ sáng thấp.</p>\n"
                                                +
                                                "   <p>Hiển thị mượt mà mọi chuyển động với màn hình tần số quét cao</p>\n"
                                                +
                                                "   <p>• Khi thể hiện những cảnh hành động, khung hình sẽ thay đổi liên tục và dễ dẫn đến hiện tượng \"bóng ma\" trên màn hình. Công nghệ Motion Xcelerator Turbo trên Samsung Crystal UHD 4K 43 inch UA43AU8000KXXV sẽ tự động bổ sung thêm khung hình để nội dung được hiển thị rõ nét và mượt mà hơn.</p>\n"
                                                +
                                                "   <p>Điều khiển bằng giọng nói:</p>\n" +
                                                "   <p>• Samsung UA43AU8000KXXV tích hợp trợ lý ảo Bixby và Google Assistant, cho phép bạn dễ dàng mở các chức năng của tivi, cũng như tìm kiếm các nội dung yêu thích trên internet.</p>\n"
                                                +
                                                "   <p>KÍCH THƯỚC VÀ TRỌNG LƯỢNG</p>\n" +
                                                "   <p>- Kích thước có chân đế (Rộng x Cao x Sâu): 965.5 x 599.8 x 205.6 mm</p>\n"
                                                +
                                                "   <p>- Kích thước không chân đế (Rộng x Cao x Sâu): 965.5 x 559.8 x 25.7 mm</p>\n"
                                                +
                                                "   <p>- Kích thước nguyên thùng (Rộng x Cao x Sâu): 1093 x 677 x 129 mm</p>\n"
                                                +
                                                "   <p>- Trọng lượng có chân đế: 8.9 kg</p>\n" +
                                                "   <p>- Trọng lượng không chân đế: 8.4 kg</p>\n" +
                                                "   <p>- Trọng lượng nguyên thùng: 12.1 kg</p>\n" +
                                                "   <p>BẢO HÀNH VÀ XUẤT XỨ</p>\n" +
                                                "   <p>- Bảo hành: 24 tháng</p>\n" +
                                                "   <p>- Hãng Sản Xuất: SAMSUNG</p>\n" +
                                                "   <p>- Năm Sản Xuất: 2021</p>\n" +
                                                "  <p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "7120a751ba291ddbc60f423a4e7780ae.jpg",
                                                                "76321ab1e866c73aa9ac7dbd529db190.jpg",
                                                                "fd5c18dff93b37c66dc5d2d448e37aad.jpg",
                                                                "33e238fe395077b3555f9cdd44a8f649.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Thương hiệu", "Samsung" }, { "Xuất xứ thương hiệu", "Việt Nam" },
                                { "Xuất xứ", "Việt Nam" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Tivi SAMSUNG UA65AU8000KXXV Smart Tv UHD 4K Tv Android 65 Inch Điều Khiển Bằng Giọng Nói - Hàng Chính Hãng",
                                new BigDecimal(19940000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("448951eb566953f7a25266b44d729e89.png"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\">\n" +
                                                "  <p>Đặc điểm nổi bật:</p>\n" +
                                                "   <p>Màn hình Airslim UHD 4K hiển thị hình ảnh sắc nét gấp 4 lần Full HD</p>\n"
                                                +
                                                "   <p>Công nghệ Crystal 4K tối ưu hóa nâng cấp độ chi tiết của từng gam màu</p>\n"
                                                +
                                                "   <p>Cảm nhận chi tiết và sắc thái hoàn mỹ được hỗ trợ công nghệ HDR10+</p>\n"
                                                +
                                                "   <p>Công nghệ UHD Dimming tăng cường độ chi tiết hiển thị của hình ảnh</p>\n"
                                                +
                                                "   <p>Trải nghiệm hình ảnh mượt mà nhờ công nghệ Motion Xcelerator</p>\n"
                                                +
                                                "   <p>Hiển thị chính xác một tỷ sắc màu với công nghệ Dynamic Crystal Color</p>\n"
                                                +
                                                "   <p>Tivi tích hợp Google Assistant Tiếng Việt, kết nối Apple TV và AirPlay 2</p>\n"
                                                +
                                                "   <p>Thiết kế thanh mảnh</p>\n" +
                                                "   <p>Màu sắc hiển thị rực rỡ</p>\n" +
                                                "   <p>Công nghệ Crystal Display có tác dụng tối ưu hóa màu sắc hiển thị, giúp cho từng khung hình đều được hiển thị trên dải màu rộng, từ đó mang lại cho bạn những trải nghiệm giải trí sống động đến bất ngờ.</p>\n"
                                                +
                                                "   <p>Độ phân giải 4K</p>\n" +
                                                "   <p>Samsung Crystal UHD 4K 65 inch UA65AU8000KXXV có độ phân giải hình ảnh ở mức UHD 4K, với số lượng điểm ảnh cao gấp 4 lần so với các tivi có độ phân giải FullHD, cho phép bạn thưởng thức được những khung hình có độ chi tiết cao hơn, rõ nét hơn.</p>\n"
                                                +
                                                "   <p>Tăng cường độ sáng</p>\n" +
                                                "   <p>Nhờ vào công nghệ HDR cùng với sự hỗ trợ đắc lực của HDR10+, dải màu sắc và độ sáng của hình ảnh sẽ được cải thiện rõ rệt, giúp bạn quan sát rõ cả những khung hình có độ sáng thấp.</p>\n"
                                                +
                                                "   <p>Hiển thị mượt mà mọi chuyển động với màn hình tần số quét cao</p>\n"
                                                +
                                                "   <p>Khi thể hiện những cảnh hành động, khung hình sẽ thay đổi liên tục và dễ dẫn đến hiện tượng \"bóng ma\" trên màn hình. Công nghệ Motion Xcelerator Turbo trên Samsung Crystal UHD 4K 65 inch UA65AU8000KXXV sẽ tự động bổ sung thêm khung hình để nội dung được hiển thị rõ nét và mượt mà hơn.</p>\n"
                                                +
                                                "   <p>Bộ đôi hoàn hảo với loa thanh Samsung</p>\n" +
                                                "   <p>Nếu đang sở hữu một chiếc loa thanh Samsung, tivi UA65AU8000KXXV sẽ trở thành 1 bộ đôi hoàn hảo nhờ vào sự hòa quyện của loa trước, loa hai bên và bộ loa đánh trần để tạo nên một không gian giải trí mang tính đỉnh cao, như thể bạn bạn cả rạp hát vào tận nhà của mình.</p>\n"
                                                +
                                                "   <p>Ngoài ra, công nghệ Adaptive Sound sẽ giúp tự điều chỉnh âm thanh sao cho phù hợp với từng hoàn cảnh cụ thể, như tin tức, âm nhạc, phim ảnh, </p>\n"
                                                +
                                                "   <p>Điều khiển bằng giọng nói</p>\n" +
                                                "   <p>Samsung UA65AU8000KXXV tích hợp trợ lý ảo Bixby và Google Assistant, cho phép bạn dễ dàng mở các chức năng của tivi, cũng như tìm kiếm các nội dung yêu thích trên internet.</p>\n"
                                                +
                                                "   <p>------------------------------------------</p>\n" +
                                                "  <p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "11c8b5ceda7bbb2ede4bff3dff76c2bf.png",
                                                                "8d55c4bf4349917265abb966ab19c6bd.png",
                                                                "7e520b45376e557def6fa6fc51d567e1.png",
                                                                "c5e6371685772990c8f08316354d8136.png",
                                                                "10f65c57d94131861f875511095880bb.png",
                                                                "7c3d6ca1dd40dd80badc1be079e02ed4.png",
                                                                "786852a38e9b9da79703c9ac7f823949.png",
                                                                "dd13acbefc58f6cf96301c8279417d1e.png",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Thương hiệu", "Samsung" },
                                { "Xuất xứ thương hiệu", "Hàn Quốc" },
                                { "Xuất xứ", "Hàn Quốc" },
                                { "Loại Tivi", "Smart TV" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Google Tivi Sony 4K 55 inch KD-55X75K - Model 2025",
                                new BigDecimal(15900000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandSony,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("339ee5ca9c6a0465dc9ce744a95b4135.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\">\n" +
                                                "<h3>Cảm giác thật với màu sắc và độ tương phản đẹp</h3>\n" +
                                                "<p><strong>Google Tivi Sony 4K 55 inch KD-55X75K</strong> với bộ xử lý mạnh mẽ của chúng tôi trên TV Sony BRAVIA 4K này sử dụng các thuật toán nâng cao để giảm nhiễu và tăng độ chi tiết. Với tín hiệu 4K rõ ràng hơn nữa, mọi thứ bạn xem đều gần hơn với chất lượng 4K, đầy đủ màu sắc và độ tương phản sống động như thật.</p>\n"
                                                +
                                                "<h3>Độ phân giải 4K siêu cao cho mọi nội dung bạn xem</h3>\n" +
                                                "<p>Xem hình ảnh 4K tuyệt đẹp, phong phú với chi tiết và kết cấu thế giới thực, được cung cấp độc quyền bởi Bộ xử lý 4K X1 của chúng tôi. Hình ảnh quay ở độ phân giải 2K và thậm chí là HD được nâng cấp lên gần mức chi tiết 4K bằng 4K X-Reality PRO bằng cách sử dụng cơ sở dữ liệu 4K độc đáo.</p>\n"
                                                +
                                                "<h3>Âm thanh tinh khiết, tự nhiên mà không bị ảnh hưởng</h3>\n" +
                                                "<p>Công nghệ Clear Phase phân tích phản hồi của loa vật lý và so sánh nó với tín hiệu âm thanh gốc trên nhiều dải tần. Dữ liệu này được sử dụng để loại bỏ bất kỳ đỉnh hoặc giảm nào trong phản ứng tự nhiên của loa để mang lại âm thanh thuần khiết, tự nhiên với khả năng tái tạo đồng đều, mượt mà ở tất cả các tần số.</p>\n"
                                                +
                                                "<h3>Bạn muốn xem gì, hãy nói để tìm kiếm</h3>\n" +
                                                "<p>Tìm kiếm bằng giọng nói để nhanh chóng tìm nội dung hấp dẫn. Bạn có thể xem chương trình hoặc phim khi gọi tên hoặc dùng lệnh tìm kiếm bằng giọng nói như “tìm phim hành động”. Nhấn vào nút thoại trên điều khiển từ xa để bắt đầu.</p>\n"
                                                +
                                                "<h3>Sẵn sàng hoạt động trong những điều kiện khắc nghiệt nhất</h3>\n" +
                                                "<p>Ti-vi Sony vô cùng bền bỉ khi được chế tạo bằng công nghệ X-Protection PRO mới có nhiều cải tiến. Không chỉ được trang bị khả năng chống bụi và chống ẩm vượt trội, những ti-vi này còn đạt tiêu chuẩn cao nhất trong các bài kiểm tra chống sét của Sony, nghĩa là TV của bạn được bảo vệ khi sét đánh và điện áp tăng vọt. Hãy tiếp tục thưởng thức nội dung giải trí liền mạch với một chiếc TV bền bỉ hơn với thời gian.</p>\n"
                                                +
                                                "<h3>Tiết kiệm năng lượng, thân thiện với môi trường</h3>\n" +
                                                "<p>TV BRAVIA XR của chúng tôi không chỉ được thiết kế cho trải nghiệm xem đắm chìm mà còn lưu ý đến môi trường. Chúng tôi cam kết bảo vệ môi trường trong quá trình sản xuất TV của mình. Chế độ Tắt hiển thị giúp tiết kiệm năng lượng bằng cách tắt hiển thị hình ảnh mà không đặt TV ở chế độ chờ hoàn toàn khi chỉ nghe âm thanh.</p>\n"
                                                +
                                                "<p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "9f05df66714168668eed6a5606e09045.jpg",
                                                                "c31d45841dab25634b45fa6b01f9b3e4.jpg",
                                                                "f33e50f5078576117758c356f49e8c6d.jpg",
                                                                "b98380d52d35a627225bf525169bfb23.jpg",
                                                                "7ac5a6b5dca55e644645f7641a65f9af.jpg",
                                                                "5ec23609645eef2987d6448df4336a6c.jpg",
                                                                "28f1ce49015d4a7fb17c0a156fbe5cc9.jpg",
                                                                "f7ddbb0f9ff3380db31d5e33e647be77.jpg",
                                                                "2258315939facf0ea44eae39ffe00234.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Tổng công suất loa", "20W" },
                                {
                                                "Công nghệ âm thanh",
                                                "Bộ khuếch đại âm thanh S-Master Digital Amplifier, Dolby Atmos, DTS Digital Surround, Tăng chất lượng âm thanh với loa X-Balanced, Âm thanh vòm S-Force Front Surround" },
                                { "Thương hiệu", "Sony" },
                                { "Xuất xứ thương hiệu", "Nhật Bản" },
                                {
                                                "Điều khiển bằng giọng nói",
                                                "Google Assistant có tiếng Việt, Tìm kiếm giọng nói trên YouTube bằng tiếng Việt" },
                                { "Điều khiển tivi bằng điện thoại", "Ứng dụng Android TV" },
                                { "Tivi kỹ thuật số (DVB-T2)", "Có" },
                                { "Loại/ Công nghệ màn hình", "LED nền (Direct LED)" },
                                { "Tần số quét", "50Hz" },
                                { "Hệ điều hành, giao diện", "Google TV" },
                                { "Công nghệ xử lý hình ảnh",
                                                "Bộ xử lý X1 4K HDR, Chuyển động mượt Motionflow XR 200" },
                                { "Model", "KD-55X75K" },
                                { "Cổng HDMI", "3" },
                                { "Cổng internet (LAN)", "Có" },
                                { "Wifi", "Có" },
                                { "Xuất xứ", "Malaysia" },
                                { "Remote thông minh", "Remote tích hợp micro tìm kiếm bằng giọng nói (RMF-TX520P)" },
                                { "Độ phân giải", "UHD 4K (3840 x 2160)" },
                                { "Chiếu hình từ điện thoại lên TV", "Chromecast" },
                                { "Kích thước màn hình", "55 inch" },
                                { "Kích thước không chân/treo tường", "Ngang 124.3 cm - Cao 73 cm - Dày 8.4 cm" },
                                { "Kích thước có chân/đặt bàn", "Ngang 124.3 cm - Cao 79 cm - Dày 29 cm" },
                                { "Loại Tivi", "Google tivi" },
                                { "Các ứng dụng sẵn có",
                                                "Clip TV, FPT Play, Galaxy Play (Fim+), Netflix, VieON, VTVcab ON, YouTube" },
                                { "USB", "2" },
                                { "Khối lượng không chân", "13.9kg" },
                                { "Khối lượng có chân", "14.5kg" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Smart Tivi Crystal Samsung 4K 55 inch UA55AU8000",
                                new BigDecimal(13320000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("250ccdf4c9c532208d86ecaecd593fa2.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><h3>Công Nghệ Dynamic Crystal Color Chân Thực Sắc Màu Với Trạng Thái Tinh Khiết Của Tinh Thể Mịn</h3>\n"
                                                +
                                                "<p><strong>Smart Tivi Crystal Samsung 4K 55 inch UA55AU8000 Mới 2021</strong> - Thưởng thức những khung hình chân thực với tỷ sắc màu sống động. Công nghệ Dynamic Crystal Color truyền tải tinh tế từng biến chuyển sắc màu, cho bạn thưởng thức mọi thay đổi dù là nhỏ nhất.</p>\n"
                                                +
                                                "<h3>Bộ Xử Lý Crystal 4K Cảm Nhận Sắc Màu Cuộc Sống Với 4K Quyền Năng</h3>\n"
                                                +
                                                "<p>Nâng cấp mọi nội dung yêu thích lên chuẩn 4K ấn tượng. Bạn thoả sức chiêm ngưỡng những sắc thái màu nguyên bản, thật như cuộc sống với công nghệ ánh xạ màu tiên tiến.</p>\n"
                                                +
                                                "<h3>Công Nghệ Motion Xcelerator Rõ Khung Hình, Mượt Chuyển Động</h3>\n"
                                                +
                                                "<p>Trong các phân cảnh đua xe hoặc rượt đuổi, các khung hình thay đổi liên tục dễ dẫn đến hiện tượng \"bóng ma\" trên màn hình. Công nghệ Motion Xcelerator sẽ tự động bổ sung thêm khung hình để nội dung rõ nét và mượt mà hơn.</p>\n"
                                                +
                                                "<h3>Chất Lượng Hình Ảnh Chân Thực Với Độ Phân Giải UHD 4K</h3>\n" +
                                                "<p>Sở hữu gấp 4 lần điểm ảnh so với TV FHD thông thường, TV UHD 4K mang đến khung hình sắc nét và sống động, cho bạn thưởng thức từng chi tiết dù là nhỏ nhất.</p>\n"
                                                +
                                                "<h3>Công Nghệ HDR Hoàn Hảo Trong Từng Khung Hình Sáng Và Tối</h3>\n" +
                                                "<p>Công nghệ HDR cải thiện độ sáng hiển thị, cung cấp dải màu sắc lớn và những chi tiết sắc nét ngay cả trong khung hình tối sâu thẳm.</p>\n"
                                                +
                                                "<h3>Công Nghệ Q-Symphony Bộ Đôi Hoàn Hảo TV &amp; Loa Thanh</h3>\n" +
                                                "<p>Thăng hoa cùng những thanh âm tuyệt vời, được đồng bộ liền mạch giữa loa thanh Samsung và TV. Công nghệ Q-Symphony tận dụng loa TV và loa thanh, kiến tạo không gian giải trí đỉnh cao, bao trùm mọi giác quan.</p>\n"
                                                +
                                                "<h3>Thiết Kế AirSlim Tuyệt Tác Thiết Kế Thanh Mảnh</h3>\n" +
                                                "<p>Chiêm ngưỡng thiết kế TV Crystal UHD với độ mỏng ấn tượng chưa từng có, dễ dàng kết hợp với mọi không gian tạo nên tổng thể liền mạch thu hút mọi ánh nhìn.</p>\n"
                                                +
                                                "<h3>Tính Năng Multiple Voice Assistants Chọn Ngay Trợ Lý Giọng Nói Của Riêng Bạn</h3>\n"
                                                +
                                                "<p>Lần đầu tiên, Samsung TV tích hợp cả 3 trợ lý giọng nói: Bixby, Amazon Alexa và Google Assistant. Cho bạn thoải mái lựa chọn trợ lý yêu thích, cung cấp trải nghiệm giải trí tối ưu và khả năng điều khiển liền mạch ngôi nhà thông minh của bạn.</p>\n"
                                                +
                                                "<h3>Từ Máy Tính Đến TV Học &amp; Làm Ngay Tại Gia</h3>\n" +
                                                "<p>Tận hưởng tiện ích kết nối vượt trội từ Smart TV. Cho bạn dễ dàng truy cập vào máy tính, laptop và thiết bị di động ngay trên TV.</p>\n"
                                                +
                                                "<p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "9268e95d9a92fd50c0c206594fe7ce78.jpg",
                                                                "d807d858e9528a88c1d3f3029e20590e.jpg",
                                                                "30b52a3f2722e16d0b56ee7596d5e07e.jpg",
                                                                "80d87f2b061992b46b5320f90de1eabc.jpg",
                                                                "3b4f3ef72213df08800b95a02a877590.jpg",
                                                                "6ffe5ac6d7be665b05bd22d648a45df0.jpg",
                                                                "83da88f45c5e355c3724dc180ce32749.jpg",
                                                                "672ded55289c7aca483e871515665b8e.jpg",
                                                                "0c8218e872cd7488424d470acf4f1971.jpg",
                                                                "0afa7011ecac16a3f1cea8f565a42cf3.jpg",
                                                                "7a43a516abd64b1dc173a9600b47207c.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Tổng công suất loa", "20W" },
                                { "Số lượng loa", "2" },
                                { "Công nghệ âm thanh", "Dolby Digital Plus, Q-Symphony, Blutooth Audio" },
                                { "Thương hiệu", "Samsung" },
                                { "Xuất xứ thương hiệu", "Hàn Quốc" },
                                { "Tivi kỹ thuật số (DVB-T2)", "DVB-T2 (*VN: DVB-T2C)" },
                                { "Hệ điều hành, giao diện", "Tizen" },
                                { "Công nghệ xử lý hình ảnh", "Crystal Processor 4K, HDR, HDR 10+, HLG" },
                                { "Model", "UA55AU8000" },
                                { "Cổng HDMI", "3" },
                                { "Cổng internet (LAN)", "Có" },
                                { "Wifi", "WiFi5" },
                                { "Xuất xứ", "Việt Nam" },
                                { "Năm ra mắt", "2021" },
                                { "Độ phân giải", "3840 x 2160 pixels" },
                                { "Kích thước màn hình", "55 inch" },
                                { "Kích thước không chân/treo tường", "1232.1 x 708.8 x 25.7 mm" },
                                { "Kích thước có chân/đặt bàn", "1232.1 x 747.8 x 228.8 mm" },
                                { "Loại Tivi", "Smart TV 4K" },
                                { "USB", "2" },
                                { "Xem 3D", "Không" },
                                { "Khối lượng không chân", "15.5 kg" },
                                { "Khối lượng có chân", "16.2 kg" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Máy chiếu Smart TV Samsung 100 inch bỏ túi The Freestyle - SP-LSP3BLAXXV",
                                new BigDecimal(24900000),
                                1000L,
                                cv.categoryCamera,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("e5e6d33cd8b1810a86b62fad8a509257.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><h3>Niềm vui từ mọi góc độ</h3>\n"
                                                +
                                                "<p><strong>Máy chiếu công nghệ bỏ túi Smart Tivi Samsung The Freestyle SP-LSP3</strong> thiết kế gọn nhẹ, mạnh mẽ và tối giản bất ngờ.</p>\n"
                                                +
                                                "<h3>Chọn góc chiếu bạn muốn -&nbsp;Khả năng điều chỉnh linh hoạt 180˚</h3>\n"
                                                +
                                                "<p>Đắm chìm vào nội dung yêu thích. Dù di chuyển góc chiếu từ tường lên trần nhà, tất cả đều dễ dàng chỉ cần xoay và dịch chuyển.</p>\n"
                                                +
                                                "<h3>Thiết kế gọn nhẹ - Nằm gọn trong tay</h3>\n" +
                                                "<p>Gặp gỡ máy chiếu The Freestyle. Mọi điều bạn muốn đều hội tụ trong chiếc máy chiếu nhỏ gọn, tiện lợi.</p>\n"
                                                +
                                                "<h3>Sẵn sàng cho mọi hành trình -&nbsp;Thoải mái di chuyển</h3>\n" +
                                                "<p>Gọn nhẹ. Dễ mang. Hãy để máy chiếu The Freestyle đồng hành cùng bạn trên mọi chuyến đi để tận hưởng mọi nội dung trên màn hình lớn.</p>\n"
                                                +
                                                "<h3>Kết nối dễ dàng với Pin sạc dự phòng -&nbsp;Cáp nguồn USB Type-C</h3>\n"
                                                +
                                                "<p>Kết nối máy chiếu The Freestyle với Pin sạc dự phòng bằng cáp USB-C để sử dụng di động dễ dàng. Máy chiếu The Freestyle tương thích với pin dự phòng có tính năng USB-PD và đầu ra 50W/20V trở lên.</p>\n"
                                                +
                                                "<h3>Tự điều chỉnh hình ảnh như một phép màu -&nbsp;Tính năng Auto Keystone</h3>\n"
                                                +
                                                "<p>Với khả năng tự động điều chỉnh hình ảnh bị méo, máy chiếu The Freestyle mang tới chất lượng hình ảnh đỉnh cao, chuẩn điện ảnh. Tất cả bạn cần chỉ là bật máy lên và thưởng thức.</p>\n"
                                                +
                                                "<h3>Tự động điều chỉnh cho hình ảnh sắc nét -&nbsp;Tính năng Auto Focus</h3>\n"
                                                +
                                                "<p>Tự động điều chỉnh độ nét chỉ trong vài giây, máy chiếu The Freestyle mang đến chất lượng hình ảnh sắc nét, tái hiện nội dung đầy đủ nhất.</p>\n"
                                                +
                                                "<h3>Mặt chiếu gồ ghề không thành vấn đề -&nbsp;Tự động san lấp bề mặt chiếu giúp hình ảnh bằng phẳng hơn</h3>\n"
                                                +
                                                "<p>Tính năng Tự động san bằng đảm bảo tái hiện hình ảnh luôn bằng phẳng trên mọi bề mặt — dù bạn trình chiếu trên bãi cắm trại đầy đá, giường mềm hay bất kỳ nơi nào.</p>\n"
                                                +
                                                "<h3>Màn hình ma thuật, kích thước tới 100 inch -&nbsp;Vùng chiếu từ 30 đến 100 inch</h3>\n"
                                                +
                                                "<p>Trái ngược với kích thước nhỏ gọn, máy chiếu The Freestyle có thể trình chiếu hình ảnh với kích thước vùng chiếu lớn từ 30 đến 100 inch, tạo nên một rạp chiếu phim di động ở bất kể nơi đâu.</p>\n"
                                                +
                                                "<h3>Xem ở vị trí thoải mái nhất -&nbsp;Tăng kích thước và thay đổi vị trí dễ dàng</h3>\n"
                                                +
                                                "<p>Dễ dàng điều chỉnh kích thước màn hình chiếu mà không cần phải di chuyển máy. Khả năng thu nhỏ khung hình xuống 50% và di chuyển theo 4 chiều giúp bạn có được vị trí xem hoàn hảo nhất.</p>\n"
                                                +
                                                "<h3>Tái hiện màu sắc chân thực -&nbsp;Điều chỉnh màu sắc thông minh</h3>\n"
                                                +
                                                "<p>Bạn không có tường hay tấm chiếu màu trắng? Với khả năng tối ưu hóa hình chiếu dựa trên màu tường, máy chiếu The Freestyle đảm bảo chất lượng hình ảnh trọn vẹn và nâng cao trải nghiệm xem của bạn.</p>\n"
                                                +
                                                "<h3>Âm thanh 360˚đa hướng -&nbsp;Loa 360 độ (5W)</h3>\n" +
                                                "<p>Loa tích hợp mạnh mẽ của máy chiếu The Freestyle mang đến âm thanh 360 độ phong phú, cho trải nghiệm âm thanh sống động từ bất cứ đâu.</p>\n"
                                                +
                                                "<h3>Tận hưởng từng khoảnh khắc với chất lượng hình ảnh rực rỡ</h3>\n" +
                                                "<p>Tương thích 1080p Full HD / PurColor / HDR. Đắm mình trong độ phân giải full HD và màu sắc sống động như bạn đang ở ngay trong khung cảnh.</p>\n"
                                                +
                                                "<h3>Truy cập nội dung yêu thích với cách thức thông minh nhất với Smart TV từ Tizen</h3>\n"
                                                +
                                                "<p>Bạn có cả thế giới nội dung với các ứng dụng OTT được chứng nhận cũng như các dịch vụ của riêng Samsung ở trong tay, mọi lúc, mọi nơi.</p>\n"
                                                +
                                                "<h3>Điều khiển bằng giọng nói trực quan -&nbsp;Hỗ trợ nhiều Trợ lý Giọng nói</h3>\n"
                                                +
                                                "<p>Tích hợp Samsung Bixby và Amazon Alexa, máy chiếu The Freestyle nhận mọi hiệu lệnh của bạn qua giọng nói nhanh chóng, dễ dàng.</p>\n"
                                                +
                                                "<h3>Phản chiếu nội dung di động trên màn hình lớn</h3>\n" +
                                                "<p>Dễ dàng truyền phát nội dung trực tiếp từ điện thoại của bạn lên máy chiếu với ứng dụng SmartThings cho Android và iOS hoặc AirPlay 2 với thiết bị Apple.</p>\n"
                                                +
                                                "<h3>Phản chiếu chỉ với 1 chạm - Tính năng Tap View</h3>\n" +
                                                "<p>Chỉ cần chạm điện thoại thông minh vào máy chiếu The Freestyle, nội dung trên điện thoại sẽ được phản chiếu ngay tức thì.</p>\n"
                                                +
                                                "<h3>Kết nối và xem TV từ bất kỳ phòng nào</h3>\n" +
                                                "<p>Với tính năng Samsung TV Access, cho dù màn hình TV của bạn đang bật hay tắt. Bạn vẫn có thể phản chiếu, điều khiển và xem TV qua máy chiếu The Freestyle.</p>\n"
                                                +
                                                "<h3>Nội dung nhúng đa dạng cho bầu không khí hoàn hảo</h3>\n" +
                                                "<p>Chế độ môi trường xung quanh. Biến không gian của bạn thành bất cứ nơi nào bạn muốn. Chọn cài đặt từ ánh sáng, lăng kính và cảnh hoặc một trong những bức ảnh của riêng bạn để tạo nên không gian bạn muốn — khả năng là vô tận.</p>\n"
                                                +
                                                "<h3>Phụ kiện tương thích với máy chiếu The Freestyle</h3>\n" +
                                                "<p>Phụ kiện máy chiếu The Freestyle. Trải nghiệm máy chiếu The Freestyle theo cách mới với những phụ kiện độc quyền.</p>\n"
                                                +
                                                "<p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "ca5eae84826aafd7554ec5a0608b95f1.png",
                                                                "1d91abfc38372366bd883fa9e2d30a24.png",
                                                                "060e3c930bf2d9f4fd6913bd94164dfb.png",
                                                                "d9198a1ffb9b088a7638323a5157585f.png",
                                                                "c895051cf54b144e70d1719e50208c3a.png",
                                                                "d133b57ce77c5b1ab0530593c5e07b05.png",
                                                                "9b061a1bdd1fd4ad552dc60162f1ff39.png",
                                                                "8d99631a1f27e6ac3af701a2e6f1d9ae.png",
                                                                "78ed274cc66773da17426ee05e29f6ed.png",
                                                                "a4bdebafdca1f0417b847dd124c41f65.png",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Thương hiệu", "Samsung" },
                                { "Xuất xứ thương hiệu", "Hàn Quốc" },
                                { "Model", "The Freestyle SP-LSP3" },
                                { "Xuất xứ", "Trung Quốc" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Google Tivi Sony 2K 32 inch KD-32W830K - Model 2025",
                                new BigDecimal(8900000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandSony,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("4497c6a22e6dd183c9ee73c3d902941d.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><p><strong><em>Google Tivi Sony 2K 32 inch KD-32W830K&nbsp;trang bị các công nghệ xử lý hình ảnh, âm thanh hiện đại như nâng cấp hình ảnh X-Reality PRO, chuyển động mượt Motionflow XR 200, Dolby Atmos, DTS Digital Surround, đặc biệt hệ điều hành Google TV đáp ứng đa dạng nhu cầu giải trí của người sử dụng.</em></strong></p>\n"
                                                +
                                                "<h3>Thiết kế</h3>\n" +
                                                "<p>-&nbsp;Google Tivi Sony&nbsp;sở hữu&nbsp;<strong>thiết kế màn hình phẳng, viền đen mềm mại</strong>. Chiếc&nbsp;tivi Sony&nbsp;có&nbsp;<strong>kích thước màn hình 32 inch</strong>&nbsp;tivi được nâng đỡ chắc chắn trên chân đế chữ V úp ngược bằng nhựa.</p>\n"
                                                +
                                                "<p>-&nbsp;Google tivi 2K&nbsp;là sự chọn phù hợp cho các không gian phòng khách, phòng ngủ nhỏ, đặc biệt phù hợp với không gian phòng khách sạn, nhà nghỉ,</p>\n"
                                                +
                                                "<p><em>*Hình ảnh chỉ mang tính minh hoạ sản phẩm</em></p>\n" +
                                                "<h3>Công nghệ hình ảnh</h3>\n" +
                                                "<p>-&nbsp;Google tivi&nbsp;sở hữu&nbsp;<strong>độ phân giải 2K</strong>&nbsp;với hơn 2 triệu điểm ảnh.</p>\n"
                                                +
                                                "<p>-&nbsp;<strong>X-Reality PRO</strong>&nbsp;giúp nâng cao chất lượng hiển thị của hình ảnh và video sắc nét hơn, giảm nhiễu, cải thiện độ tương phản các sắc đậm nhạt trong mỗi khung hình.</p>\n"
                                                +
                                                "<p>-&nbsp;<strong>Công nghệ tăng cường màu sắc Live Colour</strong>&nbsp;tái hiện từng khung hình tự nhiên, màu sắc tươi mới&nbsp;mang đến trải nghiệm hình ảnh chân thực và sống động.</p>\n"
                                                +
                                                "<p>-&nbsp;<strong>Chuyển động mượt Motionflow XR 200</strong>&nbsp;công nghệ chèn khung nền đen giúp hình ảnh chuyển động hiển thị mượt mà, sắc nét.</p>\n"
                                                +
                                                "<p><em>*Hình ảnh chỉ mang tính minh hoạ sản phẩm</em></p>\n" +
                                                "<h3>Công nghệ âm thanh</h3>\n" +
                                                "<p>-&nbsp;<strong>Dolby Atmos,&nbsp;S-Force Front Surround</strong>&nbsp;giả lập hiệu ứng âm thanh vòm sống động.</p>\n"
                                                +
                                                "<p>-&nbsp;<strong>Loa X-Balance</strong><strong>d&nbsp;</strong>chất lượng âm thanh cao và áp suất âm thanh mạnh mẽ, đem đến trải nghiệm nghe nhạc phong phú, đắm chìm và thỏa mãn hơn.</p>\n"
                                                +
                                                "<p><em>*Hình ảnh chỉ mang tính minh hoạ sản phẩm</em></p>\n" +
                                                "<h3>Tiện ích</h3>\n" +
                                                "<p>-&nbsp;<strong>Điều khiển, tìm kiếm giọng nói tiếng Việt</strong>&nbsp;trên tivi với remote tích hợp micro (RMF-TX520P) cùng trợ lý Google Asistant.</p>\n"
                                                +
                                                "<p>- Chiếu màn hình điện thoại lên tivi thông qua&nbsp;<strong>ứng dụng&nbsp;Chromecast.</strong></p>\n"
                                                +
                                                "<p><em>Nhìn chung,&nbsp;Google Tivi Sony 2K 32 inch KD-32W830K là mẫu Google tivi có kích thước màn hình nhỏ gọn được trang bị các công nghệ xử lý hình ảnh, âm thanh hiện đại mang lại trải nghiệm nghe nhìn tốt.</em></p><p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "35e98655d18391c79c86fbc39f98eb0c.png",
                                                                "4bab5feae0e94bbc31daedc63c064ccb.jpg",
                                                                "244c456369dea576832ab17cab705922.png",
                                                                "c3f8e89bcad2e2a1118e6b5ca6ec2cc8.png",
                                                                "84e4260a07e6b594fc62265b2a538ee7.png",
                                                                "799716f422d6e562e116f2aef4d649bb.png",
                                                                "08845c894dc7d0297dd9992ea0c1740d.png",
                                                                "6a05c0cdcb28a6b556961917d1f02c6f.png",
                                                                "e1b0547230cd60e1a6179363e985a269.png",
                                                                "2338637037b1bc1124e9af49a42e76b1.jpg",
                                                                "1733390bafe55c84e06145baae4360f7.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Thương hiệu", "Sony" },
                                { "Xuất xứ thương hiệu", "Nhật Bản" },
                                { "Hệ điều hành, giao diện", "Google TV" },
                                {
                                                "Công nghệ xử lý hình ảnh",
                                                "Chuyển động mượt Motionflow XR 200 Màu sắc sống động Live Colour Nâng cấp hình ảnh X-Reality PRO" },
                                { "Model", "KD-32W830K" },
                                { "Kết nối không dây", "Cổng mạng LAN, Wifi" },
                                { "Cổng HDMI", "Có" },
                                { "Xuất xứ", "Việt Nam" },
                                { "Xem 3D", "Không" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Smart Tivi Samsung 4K 50 inch UA50AU7700",
                                new BigDecimal(17900000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("09bbf1fad770994003014fcaa748b37d.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><h3>Trải Nghiệm Nội Dung 4K Hoàn Mỹ Trên Thế Hệ Smart TV Mới</h3>\n"
                                                +
                                                "<h3>PurColor Tái Hiện Sắc Màu Cuộc Sống, Rực Rỡ Và Sống Động Hơn</h3>\n"
                                                +
                                                "<p><strong>Smart Tivi Samsung 4K 50 inch UA50AU7700 Mới 2021</strong> - Choáng ngợp trước dải màu sắc hiển thị rộng lớn và rực rỡ từ công nghệ PurColor. Đắm chìm trong từng khung hình sống động và chân thực như bước ra từ cuộc sống.</p>\n"
                                                +
                                                "<h3>Bộ Xử Lý Crystal 4K Cảm Nhận Màu Sắc Chân Thực Trong Khung Hình 4K Sống Động</h3>\n"
                                                +
                                                "<p>Nâng cấp mọi nội dung bạn yêu thích lên chuẩn 4K ấn tượng. Chiêm ngưỡng sắc màu hiển thị sống động và chân thực với công nghệ Color Mapping tiên tiến.</p>\n"
                                                +
                                                "<h3>Công Nghệ Motion Xcelerator Rõ Khung Hình, Mượt Chuyển Động</h3>\n"
                                                +
                                                "<p>Trong các phân cảnh đua xe hoặc rượt đuổi, các khung hình thay đổi liên tục dễ dẫn đến hiện tượng \"bóng ma\" trên màn hình. Công nghệ Motion Xcelerator sẽ tự động bổ sung thêm khung hình để nội dung rõ nét và mượt mà hơn.</p>\n"
                                                +
                                                "<h3>Chất Lượng Hình Ảnh Chân Thực Với Độ Phân Giải 4K</h3>\n" +
                                                "<p>Sở hữu gấp 4 lần điểm ảnh so với TV FHD thông thường, Smart TV UHD 4K mang đến khung hình sắc nét và sống động, cho bạn trải nghiệm xem chân thật nhất.</p>\n"
                                                +
                                                "<h3>Công Nghệ HDR Hoàn Hảo Trong Từng Khung Hình Sáng Và Tối</h3>\n" +
                                                "<p>Cải thiện độ sáng TV vượt trội với công nghệ HDR đỉnh cao. Cung cấp dải màu sắc lớn và hiển thị chi tiết, sắc nét hình ảnh ngay cả trong khung hình tối sâu thẳm.</p>\n"
                                                +
                                                "<h3>Công Nghệ Q-Symphony Bộ Đôi Hoàn Hảo TV &amp; Soundbar</h3>\n" +
                                                "<p>Thăng hoa cùng những thanh âm tuyệt vời, được đồng bộ liền mạch giữa loa thanh Samsung và TV. Công nghệ Q-Symphony tận dụng loa TV và loa thanh, kiến tạo không gian giải trí đỉnh cao, bao trùm mọi giác quan.</p>\n"
                                                +
                                                "<h3>Thiết Kế 3 Cạnh Không Viền&nbsp;Tuyệt Tác Thiết Kế Ấn Tượng</h3>\n"
                                                +
                                                "<p>Với ngôn ngữ thiết kế theo phong cách tinh giản, Smart TV UHD 4K như một bức tranh thuần khiết không viền mang đến cho bạn trải nghiệm xem hoàn hảo và đậm chất điện ảnh.</p>\n"
                                                +
                                                "<h3>Giải Pháp Giấu Dây Tiện Lợi Gọn Gàng Không Gian Bài Trí</h3>\n" +
                                                "<p>Mang đến vẻ đẹp liền mạch cho TV với giải pháp giấu dây tiện lợi, giữ cho dây cáp luôn gọn gàng và nâng tầm thẩm mỹ cho không gian xung quanh.</p>\n"
                                                +
                                                "<h3>Từ Máy Tính Đến TV Học &amp; Làm Ngay Tại Gia</h3>\n" +
                                                "<p>Tận hưởng tiện ích kết nối vượt trội từ Smart TV UHD 4K. Cho bạn dễ dàng truy cập vào máy tính, laptop và thiết bị di động ngay trên TV.</p>\n"
                                                +
                                                "<p>*Kết nối từ xa với máy tính chỉ hỗ trợ từ hệ điều hành Windows 10 Professional và từ Mac OS 10.5. *Kết nối điện thoại thông minh với Samsung DeX yêu cầu thiết bị di động tương thích. *Tính khả dụng của dịch vụ có thể thay đổi mà không cần thông báo trước. *Yêu cầu kết nối Internet trên TV và máy tính.</p>\n"
                                                +
                                                "<h3>Tính Năng Tap View Kết Nối Chỉ Với Một Chạm</h3>\n" +
                                                "<p>Chạm nhẹ vào cạnh TV để gửi tín hiệu và phát nhanh chóng mọi nội dung từ điện thoại lên TV. Thỏa sức thưởng thức những bản nhạc say đắm hay tận hưởng các bộ phim yêu thích với khả năng kết nối liền mạch giữa hai thiết bị.</p>\n"
                                                +
                                                "<p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "d82d9d83169dc12c113ebd7d3e321636.jpg",
                                                                "add8c7e0c6ba8c11ae5e5601ec30bc02.jpg",
                                                                "9ce3b9266c9fb1409f09e3ee7283655a.jpg",
                                                                "4da3315b3bba08a991138c9763105c84.jpg",
                                                                "77e64f11b8bd51109861664ce13012b3.jpg",
                                                                "f24c5a38f98ab9f5daa257deb5ba91bb.jpg",
                                                                "01745af94e1f9025039c48bb48b3c568.jpg",
                                                                "c6a8ea09b4494316ab7262aa6e93facc.jpgv",
                                                                "0cb001ba93a386ada4217d8f18ff2284.jpg",
                                                                "700c37734bccee47d8c79831583a901d.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                {
                                                "Tổng công suất loa",
                                                "20W",
                                                "Số lượng loa",
                                                "2",
                                                "Công nghệ âm thanh",
                                                "Dolby Digital Plus; Q-Symphony; Blutooth Audio",
                                                "Thương hiệu",
                                                "Samsung",
                                                "Xuất xứ thương hiệu",
                                                "Hàn Quốc",
                                                "Tivi kỹ thuật số (DVB-T2)",
                                                "DVB-T2 (*VN: DVB-T2C)",
                                                "Hệ điều hành, giao diện",
                                                "Tizen OS",
                                                "Công nghệ xử lý hình ảnh",
                                                "Crystal Processor 4K; HDR; HDR 10+; HLG; Pur Color; UHD Dimming; Auto Motion Plus",
                                                "Model",
                                                "UA50AU7700",
                                                "Cổng HDMI",
                                                "3",
                                                "Cổng internet (LAN)",
                                                "Có",
                                                "Wifi",
                                                "Yes (WiFi5)",
                                                "Xuất xứ",
                                                "Việt Nam",
                                                "Năm ra mắt",
                                                "2021",
                                                "Độ phân giải",
                                                "UHD 4K (3840 x 2160)",
                                                "Kích thước màn hình",
                                                "50 inch",
                                                "Kích thước không chân/treo tường",
                                                "1116.8 x 644.2 x 59.9 mm",
                                                "Kích thước có chân/đặt bàn",
                                                "1116.8 x 719.1 x 250.2 mm",
                                                "Loại Tivi",
                                                "Smart Tivi",
                                                "USB",
                                                "1",
                                                "Khối lượng không chân",
                                                "11.4 kg",
                                                "Khối lượng có chân",
                                                "11.6 kg" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Smart TV TCL Android 8.0 40 inch Full HD .wifi - 40L61 - HDR Dolby, Chromecast, T-cast, AI+IN., Màn hình tràn viền",
                                new BigDecimal(7490000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandTCL,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("06abbd92042e6ed21ca49c13db13d72f.jpg"),
                                "<div class=\\\"ToggleContent__View-sc-1dbmfaw-0 wyACs\\\" style=\\\"\\\">\\n\" +\n" +
                                                "                \"<p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "8981e981a2641132214ae58aedad4975.jpg",
                                                                "c085a0c7f093a8f197ac956d432513cd.jpg",
                                                                "6b5b4e3b053eeaebe4db72e0f7aacc4f.png",
                                                                "bc1db82ad623a998218aa36a7a7843ce.png",
                                                                "060c38a34b89b3660a70d9a74493d481.png",
                                                                "57e2c70c5ef54b0f91e4ed5d6751f64c.png",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Âm Thanh", "Dolby MS12" },
                                { "Tổng công suất loa", "16W" },
                                { "Công nghệ âm thanh", "Dolby MS12" },
                                { "Thương hiệu", "TCL" },
                                { "Xuất xứ thương hiệu", "Trung Quốc" },
                                { "Điều khiển tivi bằng điện thoại", "Bằng ứng dụng MagiConnect" },
                                { "Tần số quét", "60 Hz" },
                                { "Hệ điều hành, giao diện", "Android 8.0" },
                                { "Công nghệ xử lý hình ảnh", "Micro Dimming, HDR" },
                                { "Model", "40L61" },
                                { "Kết nối bàn phím, chuột", "Có" },
                                { "Kết nối không dây", "Bluetooth, Wifi" },
                                { "Cổng HDMI", "2" },
                                { "Wifi", "Wifi" },
                                { "Xuất xứ", "Việt Nam" },
                                { "Năm ra mắt", "2019" },
                                { "Hình ảnh", "Full HD" },
                                { "Kích thước không chân/treo tường", "Ngang 90.1 cm - Cao 51.63 cm - Dày 7.5 cm" },
                                { "Loại Tivi", "Smart TV, AI" },
                                {
                                                "Tương tác thông minh",
                                                "Chiếu màn hình Chromecast, Chiếu màn hình Screen Mirroring, Chiếu màn hình AirPlay qua ứng dụng AirScreen" },
                                { "Các ứng dụng sẵn có", "Youtube, Netflix, Trình duyệt web, kho ứng dụng" },
                                { "USB", "1" },
                                { "Xem 3D", "Không" },
                                { "Khối lượng không chân", "6.31 kg" },
                                { "Khối lượng có chân", "6.37 kg" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Smart Tivi Crystal Samsung 4K 43 inch UA43AU8000",
                                new BigDecimal(15000000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("c55d0d49d0f54b88ac84d768ede9d532.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"overflow: hidden; height: 500px;\"><h3>Công Nghệ Dynamic Crystal Color Chân Thực Sắc Màu Với Trạng Thái Tinh Khiết Của Tinh Thể Mịn</h3>\n"
                                                +
                                                "<p>Smart Tivi Crystal Samsung 4K 43 inch UA43AU8000 - Thưởng thức những khung hình chân thực với tỷ sắc màu sống động. Công nghệ Dynamic Crystal Color truyền tải tinh tế từng biến chuyển sắc màu, cho bạn thưởng thức mọi thay đổi dù là nhỏ nhất.</p>\n"
                                                +
                                                "<h3>Bộ Xử Lý Crystal 4K Cảm Nhận Sắc Màu Cuộc Sống Với 4K Quyền Năng</h3>\n"
                                                +
                                                "<p>Nâng cấp mọi nội dung yêu thích lên chuẩn 4K ấn tượng. Bạn thoả sức chiêm ngưỡng những sắc thái màu nguyên bản, thật như cuộc sống với công nghệ ánh xạ màu tiên tiến.</p>\n"
                                                +
                                                "<h3>Công Nghệ Motion Xcelerator Rõ Khung Hình, Mượt Chuyển Động</h3>\n"
                                                +
                                                "<p>Trong các phân cảnh đua xe hoặc rượt đuổi, các khung hình thay đổi liên tục dễ dẫn đến hiện tượng \"bóng ma\" trên màn hình. Công nghệ Motion Xcelerator sẽ tự động bổ sung thêm khung hình để nội dung rõ nét và mượt mà hơn.</p>\n"
                                                +
                                                "<h3>Chất Lượng Hình Ảnh Chân Thực Với Độ Phân Giải UHD 4K</h3>\n" +
                                                "<p>Sở hữu gấp 4 lần điểm ảnh so với TV FHD thông thường, TV UHD 4K mang đến khung hình sắc nét và sống động, cho bạn thưởng thức từng chi tiết dù là nhỏ nhất.</p>\n"
                                                +
                                                "<h3>Công Nghệ HDR Hoàn Hảo Trong Từng Khung Hình Sáng Và Tối</h3>\n" +
                                                "<p>Công nghệ HDR cải thiện độ sáng hiển thị, cung cấp dải màu sắc lớn và những chi tiết sắc nét ngay cả trong khung hình tối sâu thẳm.</p>\n"
                                                +
                                                "<h3>Công Nghệ Q-Symphony Bộ Đôi Hoàn Hảo TV &amp; Loa Thanh</h3>\n" +
                                                "<p>Thăng hoa cùng những thanh âm tuyệt vời, được đồng bộ liền mạch giữa loa thanh Samsung và TV. Công nghệ Q-Symphony tận dụng loa TV và loa thanh, kiến tạo không gian giải trí đỉnh cao, bao trùm mọi giác quan.</p>\n"
                                                +
                                                "<h3>Thiết Kế AirSlim Tuyệt Tác Thiết Kế Thanh Mảnh</h3>\n" +
                                                "<p>Chiêm ngưỡng thiết kế TV Crystal UHD với độ mỏng ấn tượng chưa từng có, dễ dàng kết hợp với mọi không gian tạo nên tổng thể liền mạch thu hút mọi ánh nhìn.</p>\n"
                                                +
                                                "<p>Tinh gọn không gian, nâng tầm thẩm mỹ. Giá treo Slim Fit Wall-Mount dễ dàng lắp đặt giúp TV áp sát vào tường, hài hoà tự nhiên trong không gian ngôi nhà.&nbsp;</p>\n"
                                                +
                                                "<p>Giải pháp tiện lợi giúp dây cáp được ẩn đi gọn gàng, mang đến tổng thể tinh tế, hài hoà cho TV của bạn.</p>\n"
                                                +
                                                "<h3>Tính Năng Multiple Voice Assistants Chọn Ngay Trợ Lý Giọng Nói Của Riêng Bạn</h3>\n"
                                                +
                                                "<p>Lần đầu tiên, Samsung TV tích hợp cả 3 trợ lý giọng nói: Bixby, Amazon Alexa và Google Assistant. Cho bạn thoải mái lựa chọn trợ lý yêu thích, cung cấp trải nghiệm giải trí tối ưu và khả năng điều khiển liền mạch ngôi nhà thông minh của bạn.</p>\n"
                                                +
                                                "<h3>Từ Máy Tính Đến TV Học &amp; Làm Ngay Tại Gia</h3>\n" +
                                                "<p>Tận hưởng tiện ích kết nối vượt trội từ Smart TV.</p>\n" +
                                                "<p>Cho bạn dễ dàng truy cập vào máy tính, laptop và thiết bị di động ngay trên TV.</p>\n"
                                                +
                                                "<p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p><div class=\"gradient\"></div></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "588db7aaff67d0dac59d2cac6c73bd4c.jpg",
                                                                "cdccf83c1f4906f69c6ead808f7d01fa.jpg",
                                                                "403b444a9c5943e40754d7fb12188b40.jpg",
                                                                "d24700937d118370be14d3b33c9c6a6a.jpg",
                                                                "dd1b51733befcf88422363d7973bce1f.jpg",
                                                                "9e48eaf7e9174cc11c521ddacf774343.jpg",
                                                                "bb6c0c3d7600855e4333b44b8e53a6c9.jpg",
                                                                "ccdcaf7ade961d5f138a34030f498ce7.jpg",
                                                                "2ab4b3870c0c49e5d6510dc9f1d03bc3.jpg",
                                                                "848233cd5d5d3de395cbc61db54535f0.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Tổng công suất loa", "20W" },
                                { "Số lượng loa", "2" },
                                { "Công nghệ âm thanh", "Dolby Digital Plus, Q-Symphony" },
                                { "Thương hiệu", "Samsung" },
                                { "Xuất xứ thương hiệu", "Hàn Quốc" },
                                { "Composite video", "Có" },
                                { "Tivi kỹ thuật số (DVB-T2)", "Có" },
                                { "Hệ điều hành, giao diện", "Tizen" },
                                {
                                                "Công nghệ xử lý hình ảnh",
                                                "Crystal Processor 4K, HDR 10+, Mega Contrast, Dynamic Crystal Color, UHD Dimming" },
                                { "Model", "UA43AU8000" },
                                { "Cổng HDMI", "3" },
                                { "Cổng internet (LAN)", "Có" },
                                { "Wifi", "Có" },
                                { "Xuất xứ", "Việt Nam" },
                                { "Remote thông minh",
                                                "Điều khiển tivi bằng giọng nói với One Remote, hỗ trợ trợ lý ảo Bixby" },
                                { "Độ phân giải", "UHD 4K (3840 x 2160)" },
                                { "Kích thước màn hình", "43 inch" },
                                { "Kích thước không chân/treo tường", "965.5 x 559.8 x 25.7 mm" },
                                { "Kích thước có chân/đặt bàn", "965.5 x 559.8 x 25.7 mm" },
                                { "Loại Tivi", "Smart Tivi" },
                                { "USB", "2" },
                                { "Khối lượng không chân", "8.4 kg" },
                                { "Khối lượng có chân", "8.9 kg" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Smart Tivi Samsung 4K 55 inch UA55AU7002",
                                new BigDecimal(000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandSamsung,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("b324e1c9e7f03487e0ce50631727179e.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"overflow: hidden; height: 500px;\"><h3>Công nghệ PurColor</h3>\n"
                                                +
                                                "<p>Choáng ngợp trước dải màu sắc hiển thị rộng lớn và rực rỡ từ công nghệ PurColor. Đắm chìm trong từng khung hình sống động và chân thực như bước ra từ cuộc sống.</p>\n"
                                                +
                                                "<h3>Bộ xử lý hình ảnh Crystal 4K</h3>\n" +
                                                "<p>Nâng cấp mọi nội dung bạn yêu thích lên chuẩn 4K ấn tượng. Chiêm ngưỡng sắc màu hiển thị sống động và chân thực với công nghệ Color Mapping tiên tiến.</p>\n"
                                                +
                                                "<h3>Công nghệ Motion Xcelerator</h3>\n" +
                                                "<p>Trong các phân cảnh đua xe hoặc rượt đuổi, các khung hình thay đổi liên tục dễ dẫn đến hiện tượng \"bóng ma\" trên màn hình. Công nghệ Motion Xcelerator sẽ tự động bổ sung thêm khung hình để nội dung rõ nét và mượt mà hơn.</p>\n"
                                                +
                                                "<h3>Độ phân giải 4K</h3>\n" +
                                                "<p>Sở hữu gấp 4 lần điểm ảnh so với TV FHD thông thường, Smart TV UHD 4K mang đến khung hình sắc nét và sống động, cho bạn thưởng thức từng chi tiết dù là nhỏ nhất.</p>\n"
                                                +
                                                "<h3>Công nghệ HDR</h3>\n" +
                                                "<p>Cải thiện độ sáng TV vượt trội với công nghệ HDR đỉnh cao. Cung cấp dải màu sắc lớn và hiển thị chi tiết, sắc nét hình ảnh ngay cả trong khung hình tối sâu thẳm.</p>\n"
                                                +
                                                "<h3>Công nghệ Q-Symphony</h3>\n" +
                                                "<p>Thăng hoa cùng những thanh âm tuyệt vời, được đồng bộ liền mạch giữa loa thanh Samsung và TV. Công nghệ Q-Symphony tận dụng loa TV và loa thanh, kiến tạo không gian giải trí đỉnh cao, bao trùm mọi giác quan.</p>\n"
                                                +
                                                "<h3>Thiết kế 3 cạnh không viền</h3>\n" +
                                                "<p>Với ngôn ngữ thiết kế theo phong cách tinh giản, Smart TV UHD 4K như một bức tranh thuần khiết không viền mang đến cho bạn trải nghiệm xem hoàn hảo và đậm chất điện ảnh.</p>\n"
                                                +
                                                "<h3>Kết nối dễ dàng với đa thiết bị</h3>\n" +
                                                "<p>Tận hưởng tiện ích kết nối vượt trội từ Smart TV UHD 4K. Cho bạn dễ dàng truy cập vào máy tính, laptop và thiết bị di động ngay trên TV.</p>\n"
                                                +
                                                "<p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p><div class=\"gradient\"></div></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "b324e1c9e7f03487e0ce50631727179e.jpg",
                                                                "5b0a37e7149435b53b3af6ec1a039555.jpg",
                                                                "5f03bdeca18cca1f1de698db9fa8cf95.jpg",
                                                                "762b86e709fc97e143273a1e1543fd80.jpg",
                                                                "f3bca6f8f129320bf7dfe1e852dabf00.jpg",
                                                                "697f95324f059c5ec177f21ede090c40.jpg",
                                                                "b0e5eb80b00b9c364b3d27a91fb534d1.jpg",
                                                                "64bd43437889e6abbb16c507737738e9.jpg",
                                                                "09829178e40a9e172923a118c4633b59.jpg",
                                                                "e8f7cd56124e5787a2c295d0a0698414.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Tổng công suất loa", "20W" },
                                { "Công nghệ âm thanh", "Adaptive Sound, Q-Symphony kết hợp loa tivi với loa thanh" },
                                { "Thương hiệu", "Samsung" },
                                { "Xuất xứ thương hiệu", "Hàn Quốc" },
                                { "Điều khiển bằng giọng nói", "không hỗ trợ" },
                                { "Điều khiển tivi bằng điện thoại", "SmartThings" },
                                { "Loại/ Công nghệ màn hình", "LED viền (Edge LED), VA LCD" },
                                { "Tần số quét", "60Hz" },
                                { "Hệ điều hành, giao diện", "Tizen" },
                                {
                                                "Công nghệ xử lý hình ảnh",
                                                "Bộ xử lý Crystal 4K, Chuyển động mượt Motion Xcelerator,Giảm độ trễ chơi game Auto Low Latency Mode (ALLM), HDR10+, Kiểm soát đèn nền UHD Dimming, Nâng cấp độ tương phản Contrast Enhancer, PurColor" },
                                { "Model", "UA55AU7002" },
                                { "Cổng HDMI", "3" },
                                { "Cổng internet (LAN)", "Có" },
                                { "Wifi", "Có" },
                                { "Xuất xứ", "Việt Nam" },
                                { "Độ phân giải", "UHD 4K (3840 x 2160)" },
                                { "Chiếu hình từ điện thoại lên TV", "AirPlay 2, Screen Mirroring" },
                                { "Kích thước màn hình", "55 inch" },
                                { "Kích thước không chân/treo tường", "Ngang 123.06 cm - Cao 70.9 cm - Dày 7.39 cm" },
                                { "Kích thước có chân/đặt bàn", "Ngang 123.06 cm - Cao 78.63 cm - Dày 25.82 cm" },
                                { "Loại Tivi", "Smart Tivi" },
                                {
                                                "Các ứng dụng sẵn có",
                                                "Clip TV, FPT Play, Galaxy Play (Fim+), MP3 Zing, MyTV, Netflix, POPS Kids, Spotify, Trình duyệt web, VieON, YouTube" },
                                { "USB", "1" },
                                { "Khối lượng không chân", "10.6kg" },
                                { "Khối lượng có chân", "10.7kg" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Smart TV HD Coocaa 32 Inch Wifi - Model 32S3U - Hàng chính hãng",
                                new BigDecimal(2990000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandCoocaa,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("37cdfcdf2f09100d63209ad62f78c6f0.jpg"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\">\n" +
                                                " \n" +
                                                " \n" +
                                                "  <ul>\n" +
                                                "   <p>️ Bluetooth conectivity</p>\n" +
                                                "   <p>230nit chromecast</p>\n" +
                                                "   <p>Youtube/netflix/prime video google store</p>\n" +
                                                "   <p>️ Tivi thông minh coocaa 32\" 32S3U</p>\n" +
                                                "   <p>_ CC cast , youtube, Swaiot Home, Game, Boundless screen, Dolby audio</p>\n"
                                                +
                                                "   <p>_ CC Cast truyền chia sẻ màn hình</p>\n" +
                                                "   <p>_ Swaiot home - cuộc sống thông minh bắt đầu từ đây:</p>\n" +
                                                "   <p>Bạn có thể dễ dàng điều khiển TV từ điện thoại thông minh hoặc tìm hiểu về các điều kiện của thiết bị IoT thông qua TV để trải nghiệm tốt hơn và tận hưởng một phong cách sống thông minh.</p>\n"
                                                +
                                                "   <p>_ Open Browser - Lướt màn hình lớn không giới hạn:</p>\n" +
                                                "   <p>_ Chip thuật toán có thể tự động điều chỉnh về màu sắc, độ nhiễu, chuyển động, độ nét, độ tương phản, mang đến cho người dùng trải nghiệm tuyệt vời về chất lượng hình ảnh.</p>\n"
                                                +
                                                "   <p>_ Tương phản cục bộ: cải thiện độ tương phản và chuyển màu của hình ảnh cũng như tối ưu hóa việc thể hiện màu sắc để hiển thị đầy đủ từng chi tiết.</p>\n"
                                                +
                                                "   <p>_ Giảm nhiễu tốt hơn, khôi phục hình ảnh gốc</p>\n" +
                                                "   <p>_ Chế độ bảo vệ mắt giúp giảm ánh sáng xanh, mang lại trải nghiệm thoải mái hơn cho dù bạn đang xem phim hay chơi bất kỳ trò chơi nào.</p>\n"
                                                +
                                                "   <p>_ Hiệu ứng âm thanh Dolby hấp dẫn:</p>\n" +
                                                "   <p>_ Tỉ lệ màn hình gần như 99,478% tỉ lệ bezel đem lại góc nhìn đắm chìm hơn.</p>\n"
                                                +
                                                "   <p>_ Lưng kim loại trải ra: bền và tản nhiệt tốt hơn.</p>\n" +
                                                "   <p>️ Bộ sản phẩm đầy đủ:</p>\n" +
                                                "   <p>️ Bảo hành chính hãng 24 tháng trên toàn quốc.</p>\n" +
                                                "   <p>Thời gian: 8h-17h, từ Thứ 2- Thứ 7</p>\n" +
                                                "   <p>️ CÁCH ĐĂNG KÍ/ KIỂM TRA BẢO HÀNH ĐIỆN TỬ:</p>\n" +
                                                "   <p>️ Coocaa luôn cố gắng cung cấp sản phẩm TV chất lượng và dịch vụ tuyệt vời. Nhận xét đánh giá 5 sao sẽ có ý nghĩa cực kỳ lớn đối với chúng tôi!</p>\n"
                                                +
                                                "   <p>- Chi phí vật tư: Nhân viên sẽ thông báo phí vật tư (ống đồng, dây điện ) khi khảo sát lắp đặt (Bảng kê xem tại ảnh 2). Khách hàng sẽ thanh toán trực tiếp cho nhân viên kỹ thuật sau khi việc lắp đặt hoàn thành - chi phí này sẽ không hoàn lại trong bất cứ trường hợp nào.</p>\n"
                                                +
                                                "   <p>- Đơn vị vận chuyển giao hàng cho bạn KHÔNG có nghiệp vụ lắp đặt sản phẩm.</p>\n"
                                                +
                                                "   <p>- Tìm hiểu thêm về Dịch vụ lắp đặt:</p>\n" +
                                                "   <p>- Quy định đổi trả: Chỉ đổi/trả sản phẩm, từ chối nhận hàng tại thời điểm nhận hàng trong trường hợp sản phẩm giao đến không còn nguyên vẹn, thiếu phụ kiện hoặc nhận được sai hàng. Khi sản phẩm đã được cắm điện sử dụng và/hoặc lắp đặt, và gặp lỗi kĩ thuật, sản phẩm sẽ được hưởng chế độ bảo hành theo đúng chính sách của nhà sản xuất.</p>\n"
                                                +
                                                "  </ul>\n" +
                                                " \n" +
                                                "<p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "13bf40a7c7ca5a3478fcc6262a448dd9.jpg",
                                                                "bb84e5cd98242066a77e808f9a14f3e5.jpg",
                                                                "ca54c4d06753d1ec21f194e690d2ade7.jpg",
                                                                "dbd39e2fbb5967a96f6f4a89a03b2309.jpg",
                                                                "39c53772cbd60257f5277f299ecbd91b.jpg",
                                                                "ec51d1b225302c55e988ac86745b38ef.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Thương hiệu", "coocaa" }, { "Xuất xứ thương hiệu", "Việt Nam" },
                                { "Xuất xứ", "Việt Nam" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);

                i++;
                products[i] = new Product(
                                "Android Tivi Xiaomi HD 32 inch A2 L32M7-EAVN - Model 2025",
                                new BigDecimal(4900000),
                                1000L,
                                cv.categoryTivi,
                                bv.brandXiaomi,
                                shop[generator.nextInt(maxIndexShop + 1)],
                                lv.locationHCM,
                                Image.createImageProduct("e4b71a3157be291109348bc322d5a1cd.png"),
                                "<div class=\"ToggleContent__View-sc-1dbmfaw-0 wyACs\" style=\"\"><h3>Thiết kế tinh tế, loa âm thanh sống động</h3>\n"
                                                +
                                                "<p>Tivi Xiaomi A2 32 inch sở hữu ngoại hình hiện đại, tinh tế và viền màn hình siêu mỏng. Đây là một trong những chiếc Tivi sáng giá của nhà Xiaomi, bởi trang bị rất nhiều điểm nổi bật. Nếu bạn quan tâm tới sản phẩm TV 32 inch này, thì hãy tham khảo phần nội dung phía bên dưới nhé.</p>\n"
                                                +
                                                "<h3>Thiết kế Unibody, màn hình không viền</h3>\n" +
                                                "<p>Smart Tivi Xiaomi Mi A2 32 inch có tỷ lệ màn hình khá lớn so với thân máy, mang đến không gian rộng, tăng tính trải nghiệm xem phim chân thực hơn rất nhiều. Về thiết kế mặt sau, sản phẩm hoàn thiện nguyên khối, chất liệu kim loại, tạo nên sự tinh tế.</p>\n"
                                                +
                                                "<p>Ngoài ra, thiết bị cung cấp điều khiển Bluetooth 360 độ, tích hợp trợ lý ảo Google Assistant, để người dùng tìm kiếm nhanh chóng bằng giọng nói. Bên cạnh đó TV Xiaomi cài sẵn giao diện Android TV, cung cấp hơn 400.000 bộ phim, 5000 ứng dụng.</p>\n"
                                                +
                                                "<h3>Loa âm thanh kép sống động</h3>\n" +
                                                "<p>Sản phẩm trang bị 2 loa với công suất 10W lấp đầy âm thanh trong căn phòng của bạn, và đặc biệt thiết bị kết hợp cả công nghệ Dolby Audio, DTS-X, DTS Virtual: X tạo nên hiệu ứng lan tỏa nguồn âm theo chiều hướng khác nhau, đem đến trải nghiệm như rạp chiếu phim.</p><p>Giá sản phẩm trên đây đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như phí vận chuyển, phụ phí hàng cồng kềnh, thuế nhập khẩu (đối với đơn hàng giao từ nước ngoài có giá trị trên 1 triệu đồng).....</p></div>");
                products[i] = this.productRepo.save(products[i]);
                products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                                Image.createImageProductGallery(
                                                new String[] {
                                                                "8de91d3ad095a0f659987dd0ed1b3a14.jpg",
                                                                "315b53dff3c3c10591491d58e98f283c.jpg",
                                                }))));
                products[i] = this.productRepo.save(products[i]);
                descriptions = new HashMap<>();
                descriptions = Stream.of(new String[][] {
                                { "Tổng công suất loa", "20 W" },
                                { "Công nghệ âm thanh", "Dolby Audio , DTS-X, DTS Virtual: X Sound" },
                                { "Thương hiệu", "Xiaomi" },
                                { "Xuất xứ thương hiệu", "Trung Quốc" },
                                { "Tivi kỹ thuật số (DVB-T2)", "Có" },
                                { "Tần số quét", "60Hz" },
                                { "Hệ điều hành, giao diện", "AndroidTV" },
                                { "Model", "L32M7-EAVN" },
                                { "Kết nối không dây", "Bluetooth 5.0" },
                                { "Cổng HDMI", "2 x HDMI" },
                                { "Cổng internet (LAN)", "Có" },
                                { "Wifi", "Wi-Fi 2,4 GHz / 5 GHz" },
                                { "Xuất xứ", "Việt Nam" },
                                { "Độ phân giải", "1366 x 768 pixels" },
                                { "Chiếu hình từ điện thoại lên TV", "Miracast, Chromecast" },
                                { "Kích thước màn hình", "32 inch" },
                                { "Kích thước không chân/treo tường", "715,7 × 82,5 × 423,7 mm" },
                                { "Kích thước có chân/đặt bàn", "715,7 × 190,4 × 471,0 mm" },
                                { "Loại Tivi", "Smart tivi" },
                                { "USB", "2 x USB 2.0" },
                                { "Khối lượng không chân", "3,9 kg" },
                                { "Khối lượng có chân", "3,94 kg" },
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                this.productService.createDescription(products[i], descriptions);
                //
                // i++;
                // products[i] =
                // new Product(
                // "",
                // new BigDecimal(000),
                // 1000L,
                // cv.categoryTivi,
                // bv.brand,
                // shop[generator.nextInt(maxIndexShop + 1)],
                // lv.locationHCM,
                // Image.createImageProduct(""),
                // "");
                // products[i] = this.productRepo.save(products[i]);
                // products[i].setImageGallery(new HashSet<>(this.imageRepo.saveAllAndFlush(
                // Image.createImageProductGallery(
                // new String[]{
                // "",
                // "",
                // "",
                // "",
                // "",
                // "",
                // }))));
                // products[i] = this.productRepo.save(products[i]);
                // descriptions = new HashMap<>();
                // descriptions = Stream.of(new String[][]{
                // {""},
                // }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
                // this.productService.createDescription(products[i], descriptions);

                return i;
        }

        private int createCustomer(AuthRegisterDTO[] authRegisterCustomers, User[] customers) {
                int i = 0;

                authRegisterCustomers[i] = new AuthRegisterDTO("Trình",
                                "Khánh Duy",
                                EGender.MALE,
                                "039500011" + i,
                                "customer" + i + "@gmail.com",
                                Utils.DEFAULT_PASSWORD);
                this.userService.registerUser(authRegisterCustomers[i], false);
                customers[i] = this.userRepo.findUserByPhone(authRegisterCustomers[i].getPhone()).orElse(null);
                if (customers[i] != null) {
                        Location location = new Location("Phường 15", "Quận Bình Thạnh", "Hồ Chí Minh");
                        location = this.locationService.saveLocation(location);
                        Address address = new Address(customers[i], location, "Hẻm 2/9 Tú Mỡ");
                        address = this.addressService.saveAddress(address);
                        customers[i] = this.userRepo.findById(customers[i].getId()).orElse(null);
                }

                i++;
                authRegisterCustomers[i] = new AuthRegisterDTO("Lê",
                                "Triều Quý",
                                EGender.MALE,
                                "039500011" + i,
                                "customer" + i + "@gmail.com",
                                Utils.DEFAULT_PASSWORD);
                this.userService.registerUser(authRegisterCustomers[i], false);
                customers[i] = this.userRepo.findUserByPhone(authRegisterCustomers[i].getPhone()).orElse(null);
                if (customers[i] != null) {
                        Location location = new Location("Phường 02", "Quận Bình Thạnh", "Hồ Chí Minh");
                        location = this.locationService.saveLocation(location);
                        Address address = new Address(customers[i], location, "Hẻm 108 Lê Tự Tài");
                        address = this.addressService.saveAddress(address);
                        customers[i] = this.userRepo.findById(customers[i].getId()).orElse(null);
                }

                i++;
                authRegisterCustomers[i] = new AuthRegisterDTO("Ngô",
                                "Hảo Hán",
                                EGender.MALE,
                                "039500011" + i,
                                "customer" + i + "@gmail.com",
                                Utils.DEFAULT_PASSWORD);
                this.userService.registerUser(authRegisterCustomers[i], false);
                customers[i] = this.userRepo.findUserByPhone(authRegisterCustomers[i].getPhone()).orElse(null);
                if (customers[i] != null) {
                        Location location = new Location("Phường 01", "Quận Bình Thạnh", "Hồ Chí Minh");
                        location = this.locationService.saveLocation(location);
                        Address address = new Address(customers[i], location, "Đường Số 1");
                        address = this.addressService.saveAddress(address);
                        customers[i] = this.userRepo.findById(customers[i].getId()).orElse(null);
                }

                i++;
                authRegisterCustomers[i] = new AuthRegisterDTO("Tạ",
                                "Quang Lê",
                                EGender.MALE,
                                "039500011" + i,
                                "customer" + i + "@gmail.com",
                                Utils.DEFAULT_PASSWORD);
                this.userService.registerUser(authRegisterCustomers[i], false);
                customers[i] = this.userRepo.findUserByPhone(authRegisterCustomers[i].getPhone()).orElse(null);
                if (customers[i] != null) {
                        Location location = new Location("Phường 03", "Quận Bình Thạnh", "Hồ Chí Minh");
                        location = this.locationService.saveLocation(location);
                        Address address = new Address(customers[i], location, "Hẻm 119/7 Đường Số 7");
                        address = this.addressService.saveAddress(address);
                        customers[i] = this.userRepo.findById(customers[i].getId()).orElse(null);
                }

                i++;
                authRegisterCustomers[i] = new AuthRegisterDTO("Phan",
                                "Trúc Mai",
                                EGender.FEMALE,
                                "039500011" + i,
                                "customer" + i + "@gmail.com",
                                Utils.DEFAULT_PASSWORD);
                this.userService.registerUser(authRegisterCustomers[i], false);
                customers[i] = this.userRepo.findUserByPhone(authRegisterCustomers[i].getPhone()).orElse(null);
                if (customers[i] != null) {
                        Location location = new Location("Phường 17", "Quận Bình Thạnh", "Hồ Chí Minh");
                        location = this.locationService.saveLocation(location);
                        Address address = new Address(customers[i], location, "Hẻm 7");
                        address = this.addressService.saveAddress(address);
                        customers[i] = this.userRepo.findById(customers[i].getId()).orElse(null);
                }

                i++;
                authRegisterCustomers[i] = new AuthRegisterDTO("Phùng",
                                "Khắc Mai",
                                EGender.MALE,
                                "039500011" + i,
                                "customer" + i + "@gmail.com",
                                Utils.DEFAULT_PASSWORD);
                this.userService.registerUser(authRegisterCustomers[i], false);
                customers[i] = this.userRepo.findUserByPhone(authRegisterCustomers[i].getPhone()).orElse(null);
                if (customers[i] != null) {
                        Location location = new Location("Phường 21", "Quận Bình Thạnh", "Hồ Chí Minh");
                        location = this.locationService.saveLocation(location);
                        Address address = new Address(customers[i], location, "Hẻm 15/13 Nguyễn Du");
                        address = this.addressService.saveAddress(address);
                        customers[i] = this.userRepo.findById(customers[i].getId()).orElse(null);
                }

                i++;
                authRegisterCustomers[i] = new AuthRegisterDTO("Lý",
                                "Nhã Kỳ",
                                EGender.FEMALE,
                                "039500011" + i,
                                "customer" + i + "@gmail.com",
                                Utils.DEFAULT_PASSWORD);
                this.userService.registerUser(authRegisterCustomers[i], false);
                customers[i] = this.userRepo.findUserByPhone(authRegisterCustomers[i].getPhone()).orElse(null);
                if (customers[i] != null) {
                        Location location = new Location("Phường 22", "Quận Bình Thạnh", "Hồ Chí Minh");
                        location = this.locationService.saveLocation(location);
                        Address address = new Address(customers[i], location, "Hẻm 47 Bùi Đình Túy");
                        address = this.addressService.saveAddress(address);
                        customers[i] = this.userRepo.findById(customers[i].getId()).orElse(null);
                }

                i++;
                authRegisterCustomers[i] = new AuthRegisterDTO("Trần",
                                "Lệ Thủy",
                                EGender.FEMALE,
                                "039500011" + i,
                                "customer" + i + "@gmail.com",
                                Utils.DEFAULT_PASSWORD);
                this.userService.registerUser(authRegisterCustomers[i], false);
                customers[i] = this.userRepo.findUserByPhone(authRegisterCustomers[i].getPhone()).orElse(null);
                if (customers[i] != null) {
                        Location location = new Location("Phường 19", "Quận Bình Thạnh", "Hồ Chí Minh");
                        location = this.locationService.saveLocation(location);
                        Address address = new Address(customers[i], location, "Hẻm 456/31 Cao Thắng");
                        address = this.addressService.saveAddress(address);
                        customers[i] = this.userRepo.findById(customers[i].getId()).orElse(null);
                }

                i++;
                authRegisterCustomers[i] = new AuthRegisterDTO("Bùi",
                                "Khánh Ly",
                                EGender.FEMALE,
                                "039500011" + i,
                                "customer" + i + "@gmail.com",
                                Utils.DEFAULT_PASSWORD);
                this.userService.registerUser(authRegisterCustomers[i], false);
                customers[i] = this.userRepo.findUserByPhone(authRegisterCustomers[i].getPhone()).orElse(null);
                if (customers[i] != null) {
                        Location location = new Location("Phường 28", "Quận Bình Thạnh", "Hồ Chí Minh");
                        location = this.locationService.saveLocation(location);
                        Address address = new Address(customers[i], location, "Hẻm 6 Nguyễn Trung Trực");
                        address = this.addressService.saveAddress(address);
                        customers[i] = this.userRepo.findById(customers[i].getId()).orElse(null);
                }

                return i;
        }

        private int createShipper(AuthRegisterDTO[] authRegisterCustomers, User[] shippers) {
                int i = 0;

                authRegisterCustomers[i] = new AuthRegisterDTO("Trình",
                                "Khánh Duy",
                                EGender.MALE,
                                "039434444" + i,
                                "shipper" + i + "@gmail.com",
                                Utils.DEFAULT_PASSWORD);
                authRegisterCustomers[i].setRole(ERole.ROLE_SHIPPER);
                this.userService.registerUser(authRegisterCustomers[i], false);
                shippers[i] = this.userRepo.findUserByPhone(authRegisterCustomers[i].getPhone()).orElse(null);
                if (shippers[i] != null) {
                        Location location = new Location("Phường Linh Trung", "Thành Phố Thủ Đức", "Hồ Chí Minh");
                        location = this.locationService.saveLocation(location);
                        Address address = new Address(shippers[i], location, "Đường số 8");
                        address = this.addressService.saveAddress(address);
                        shippers[i] = this.userRepo.findById(shippers[i].getId()).orElse(null);
                }
                i++;

                authRegisterCustomers[i] = new AuthRegisterDTO("Lê",
                                "Thiếu Úy",
                                EGender.MALE,
                                "039434444" + i,
                                "shipper" + i + "@gmail.com",
                                Utils.DEFAULT_PASSWORD);
                authRegisterCustomers[i].setRole(ERole.ROLE_SHIPPER);
                this.userService.registerUser(authRegisterCustomers[i], false);
                shippers[i] = this.userRepo.findUserByPhone(authRegisterCustomers[i].getPhone()).orElse(null);
                if (shippers[i] != null) {
                        Location location = new Location("Phường Bình Thọ", "Thành Phố Thủ Đức", "Hồ Chí Minh");
                        location = this.locationService.saveLocation(location);
                        Address address = new Address(shippers[i], location, "Đường Võ Văn Ngân");
                        address = this.addressService.saveAddress(address);
                        shippers[i] = this.userRepo.findById(shippers[i].getId()).orElse(null);
                }
                i++;

                return i;
        }

        private int createMainComments(
                        Comment[] mainComments, Product[] products, int maxIndexProduct, User[] customers,
                        int maxIndexCustomer) {
                int i = 0;
                mainComments[i] = new Comment();
                mainComments[i].setMainComment(products[0], customers[generator.nextInt(maxIndexCustomer + 1)],
                                "I was just daydreaming.");
                mainComments[i] = this.commentRepo.save(mainComments[i]);

                i++;

                return i;
        }

        private int createChildComments(
                        Comment[] childComments, Comment[] mainComments, int maxIndexMainComment,
                        User[] customers, int maxIndexCustomer) {
                int i = 0;

                childComments[i] = new Comment();
                childComments[i].setChildComment(mainComments[generator.nextInt(maxIndexMainComment + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Hey, how’s it going?");
                childComments[i] = this.commentRepo.save(childComments[i]);

                i++;

                return i;
        }

        private int createMainFeedbacks(
                        Feedback[] mainFeedbacks, Product[] products, int maxIndexProduct, User[] customers,
                        int maxIndexCustomer) {
                int i = 0;

                StringBuilder sb = new StringBuilder();
                String strLine = "";
                Map<Integer, Map.Entry<Integer, String>> sentiments = new HashMap<>();
                int counter = 0;
                try {
                        BufferedReader br = new BufferedReader(new FileReader("files/sentiment.txt"));
                        strLine = br.readLine().substring(1);
                        do {
                                strLine = strLine.trim();
                                if (strLine.length() > 1) {
                                        sentiments.put(counter, new AbstractMap.SimpleEntry<>(
                                                        (int) strLine.charAt(0) - 48, strLine.substring(1).trim()));
                                        counter++;
                                }
                        } while ((strLine = br.readLine()) != null);
                        for (Map.Entry<Integer, Map.Entry<Integer, String>> entry : sentiments.entrySet()) {
                                int sentiment = entry.getValue().getKey();
                                String review = entry.getValue().getValue();
                                Feedback feedback = new Feedback(products[generator.nextInt(maxIndexProduct)],
                                                customers[generator.nextInt(maxIndexCustomer)], review,
                                                (int) (Math.random() * 5 + 1), ESentiment.values()[sentiment]);
                                mainFeedbacks[i] = this.feedbackRepo.save(feedback);
                                i++;
                        }
                        br.close();
                } catch (FileNotFoundException e) {
                        System.err.println("File not found");
                } catch (IOException e) {
                        System.err.println("Unable to read the file.");
                }

                return i;
        }

        private int createChildFeedbacks(
                        Comment[] childFeedbacks, Feedback[] mainFeedbacks, int maxIndexMainFeedback,
                        User[] customers, int maxIndexCustomer) {
                int i = 0;

                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[0], customers[generator.nextInt(maxIndexCustomer + 1)],
                                "What a relief.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[0], customers[generator.nextInt(maxIndexCustomer + 1)],
                                "What the hell are you doing?");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[0], customers[generator.nextInt(maxIndexCustomer + 1)],
                                "You're a life saver.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[0], customers[generator.nextInt(maxIndexCustomer + 1)],
                                "I know I can count on you");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[0], customers[generator.nextInt(maxIndexCustomer + 1)],
                                "Get your head out of your ass!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[0], customers[generator.nextInt(maxIndexCustomer + 1)],
                                "That's a lie!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[0], customers[generator.nextInt(maxIndexCustomer + 1)],
                                "Do as I say.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "This is the limit!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Explain to me why.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Ask for it!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "In the nick of time");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "No litter.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Go for it!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "What a jerk!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "How cute!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "None of your business");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Don't peep!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Say cheese!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Be good !");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Bottom up!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Me? Not likely!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Scratch one’s head");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Take it or leave it!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Hell with haggling!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Mark my words!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Bored to death!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "What a relief!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Enjoy your meal!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "It serves you right!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                "The more, the merrier!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                "Beggars can’t be choosers!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Boys will be boys!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Good job!= well done!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Just for fun!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Try your best!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Make some noise!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Congratulations!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Rain cats and dogs");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Love me love my dog.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Always the same.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Hit it off.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Hit or miss.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Add fuel to the fire.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                "To eat well and can dress beautifully.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                "Don’t mention it! = You’re welcome = That’s all right!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Just kidding.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Enjoy your meal!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "No, not a bit.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Nothing particular!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "After you.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                "Have I got your word on that?");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "The same as usual!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Almost!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                "You‘ll have to step on it.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "I’m in a hurry.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                "What the hell is going on?");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Sorry for bothering!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                "Give me a certain time!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                "It’s a kind of once-in-life!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                "Out of sight, out of mind!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "The God knows!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                "Women love through ears, while men love through eyes!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Poor you/me/him/her…!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Go away!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Let me see.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "None your business.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Mark my words!  ");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "What’s up?");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "How’s it going?");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                "What have you been doing?");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Nothing much.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "What’s on your mind?");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "I was just thinking.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                "I was just daydreaming.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                "It’s none of your business.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Is that so?");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "How come?");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Absolutely!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Definitely!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Of course!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                "You better believe it!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "I guess so");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                "There’s no way to know.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "I can’t say for sure.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)],
                                "This is too good to be true!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "No way!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "I got it.");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Right on!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "I did it!");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "Got a minute?");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "About when?");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                i++;
                childFeedbacks[i] = new Comment();
                childFeedbacks[i].setChildFeedback(mainFeedbacks[generator.nextInt(maxIndexMainFeedback + 1)],
                                customers[generator.nextInt(maxIndexCustomer + 1)], "About when?");
                childFeedbacks[i] = this.commentRepo.save(childFeedbacks[i]);

                return i;
        }

        static class CategoryVariable {
                public CategoryVariable() {

                }

                Category categoryOther;
                Category categoryPhone;
                Category categoryLaptop;
                Category categoryTablet;
                Category categoryAccessory;
                Category categorySmartWatch;
                Category categoryWatch;
                Category categoryPCPrinter;
                Category categorySim;
                Category categoryNetwork;
                Category categoryBumper;
                Category categoryMouse;
                Category categoryKeyboard;
                Category categoryLoudSpeaker;
                Category categoryHeadphone;
                Category categoryBackupCharger;
                Category categoryChargingCable;
                Category categoryComputerSet;
                Category categoryPrinter;
                Category categoryComputerScreen;
                Category categoryCamera;
                Category categorySmartHouse;
                Category categoryRefrigerator;
                Category categoryTivi;
        }

        public void createCategory(CategoryVariable cv, BrandVariable bv) {
                cv.categoryOther = new Category("Các sản phẩm khác", null, null, null);
                cv.categoryOther = this.categoryRepo.save(cv.categoryOther);

                Set<Brand> brandPhones = new HashSet<>(
                                Arrays.asList(bv.brandIphone, bv.brandSamsung, bv.brandOppo, bv.brandXiaomi,
                                                bv.brandVivo,
                                                bv.brandRealme,
                                                bv.brandNokia, bv.brandMobell, bv.brandItel, bv.brandMasstel));
                cv.categoryPhone = new Category("Điện Thoại",
                                Image.createImageCategory("498ugyugr9ykbnveumsa86b5wc6e0qkt.png"),
                                Image.createImageCategory("0aa430f078094cb39c73ddd65273c7ed.png"), brandPhones);
                cv.categoryPhone = this.categoryRepo.save(cv.categoryPhone);

                Set<Brand> brandLaptops = new HashSet<>(
                                Arrays.asList(bv.brandMacbook, bv.brandAsus, bv.brandHP, bv.brandLenovo, bv.brandAcer,
                                                bv.brandDell,
                                                bv.brandMSI, bv.brandItel));
                cv.categoryLaptop = new Category("Laptop",
                                Image.createImageCategory("usq395chzsb8zxm5twkb04sir1w8z1ah.png"),
                                Image.createImageCategory("b611aa2b2f774670876a9d13e51cc7a3.png"), brandLaptops);
                cv.categoryLaptop = this.categoryRepo.save(cv.categoryLaptop);

                Set<Brand> brandTablets = new HashSet<>(
                                Arrays.asList(bv.brandIpad, bv.brandSamsung, bv.brandXiaomi, bv.brandOppo,
                                                bv.brandLenovo,
                                                bv.brandMasstel,
                                                bv.brandNokia, bv.brandHuawei));
                cv.categoryTablet = new Category("Máy Tính Bảng",
                                Image.createImageCategory("d1easb1ubyaf0rjgawucpkv9hd1464rc.png"),
                                Image.createImageCategory("cbc08bdddbe04379902dbd6ee1bfa688.png"), brandTablets);
                cv.categoryTablet = this.categoryRepo.save(cv.categoryTablet);

                Set<Brand> brandAccessories = new HashSet<>(
                                Arrays.asList(bv.brandApple, bv.brandSamsung, bv.brandSony, bv.brandLG, bv.brandFPT,
                                                bv.brandMicrosoft,
                                                bv.brandLogitech, bv.brandSanDisk, bv.brandPanasonic));
                cv.categoryAccessory = new Category("Phụ Kiện",
                                null,
                                Image.createImageCategory("a04dbfd06a2a4df6aeb25d23c4420544.png"),
                                brandAccessories);
                cv.categoryAccessory = this.categoryRepo.save(cv.categoryAccessory);

                Set<Brand> brandSmartWatch = new HashSet<>(
                                Arrays.asList(bv.brandWatch, bv.brandSamsung, bv.brandXiaomi, bv.brandOppo,
                                                bv.brandRealme,
                                                bv.brandMasstel,
                                                bv.brandAsus, bv.brandItel));
                cv.categorySmartWatch = new Category("Đồng Hồ Thông Minh",
                                Image.createImageCategory("bt81mbkyr335bj7oy8b19jy1i8z6tsgi.png"),
                                Image.createImageCategory("ea0309102f4849c6b52fa1b0e12fb99f.png"), brandSmartWatch);
                cv.categorySmartWatch = this.categoryRepo.save(cv.categorySmartWatch);

                Set<Brand> brandWatchs = new HashSet<>(
                                Arrays.asList(bv.brandCasio, bv.brandOrient, bv.brandCitizen, bv.brandMovado,
                                                bv.brandFossil,
                                                bv.brandGShock,
                                                bv.brandELIO, bv.brandMVW, bv.brandNakzen, bv.brandEsprit,
                                                bv.brandDKNY));
                cv.categoryWatch = new Category("Đồng Hồ",
                                Image.createImageCategory("co54eesb7422xielx80uysc0umbweg26.png"),
                                Image.createImageCategory("bcd09523358047fc9315d42d6d010009.png"), brandWatchs);
                cv.categoryWatch = this.categoryRepo.save(cv.categoryWatch);

                Set<Brand> brandPCPrinter = new HashSet<>(
                                Arrays.asList(bv.brandApple, bv.brandHP, bv.brandAsus, bv.brandMSI, bv.brandLenovo,
                                                bv.brandDell));
                cv.categoryPCPrinter = new Category("PC, Máy In",
                                null,
                                Image.createImageCategory("4f82b467c6bf4d03a5ea8a5c2a8f68a0.png"),
                                brandPCPrinter);
                cv.categoryPCPrinter = this.categoryRepo.save(cv.categoryPCPrinter);

                Set<Brand> brandSims = new HashSet<>(
                                Arrays.asList(bv.brandViettel, bv.brandVietNamMobile, bv.brandVinaPhone,
                                                bv.brandMobiPhone));
                cv.categorySim = new Category("Sim, Thẻ Cào",
                                Image.createImageCategory("qkakigfur2i924gkdsxkek3djx0znoxv.png"),
                                null,
                                brandSims);
                cv.categorySim = this.categoryRepo.save(cv.categorySim);

                Set<Brand> brandNetworks = new HashSet<>(
                                Arrays.asList(bv.brandTPLink, bv.brandTenda, bv.brandLinkSYS, bv.brandToToLink,
                                                bv.brandXiaomi,
                                                bv.brandAsus,
                                                bv.brandDLink, bv.brandMercuSYS));
                cv.categoryNetwork = new Category("Thiết Bị Mạng",
                                Image.createImageCategory("gze1k5m9g5q7wo1pbl0291uwrpjoo7nj.png"),
                                null,
                                brandNetworks);
                cv.categoryNetwork.setParentCategory(cv.categoryAccessory);
                cv.categoryNetwork = this.categoryRepo.save(cv.categoryNetwork);

                Set<Brand> brandBumpers = new HashSet<>(Arrays.asList(bv.brandIpad, bv.brandSamsung));
                cv.categoryBumper = new Category("Ốp Lưng",
                                Image.createImageCategory("ax01oeggq8usqsmjz5mdnxtuxaod7814.png"), null,
                                brandBumpers);
                cv.categoryBumper.setParentCategory(cv.categoryAccessory);
                cv.categoryBumper = this.categoryRepo.save(cv.categoryBumper);

                Set<Brand> brandMouses = new HashSet<>(
                                Arrays.asList(bv.brandLogitech, bv.brandAsus, bv.brandMicrosoft));
                cv.categoryMouse = new Category("Chuột Máy Tính",
                                Image.createImageCategory("j2ac6yc8u4xincm095m893kzrvng35a1.png"),
                                null,
                                brandMouses);
                cv.categoryMouse.setParentCategory(cv.categoryAccessory);
                cv.categoryMouse = this.categoryRepo.save(cv.categoryMouse);

                Set<Brand> brandKeyboards = new HashSet<>(Arrays.asList(bv.brandMSI, bv.brandMicrosoft));
                cv.categoryKeyboard = new Category("Bàn Phím",
                                Image.createImageCategory("984ipq5qaeoicupodw7at32lqvory604.png"),
                                null,
                                brandKeyboards);
                cv.categoryKeyboard.setParentCategory(cv.categoryAccessory);
                cv.categoryKeyboard = this.categoryRepo.save(cv.categoryKeyboard);

                Set<Brand> brandLoudSpeakers = new HashSet<>(Arrays.asList(bv.brandJBL, bv.brandSony));
                cv.categoryLoudSpeaker = new Category("Loa",
                                Image.createImageCategory("olek7max5x8vlk0pn1l6ud5uhizwgdh0.png"), null,
                                brandLoudSpeakers);
                cv.categoryLoudSpeaker.setParentCategory(cv.categoryAccessory);
                cv.categoryLoudSpeaker = this.categoryRepo.save(cv.categoryLoudSpeaker);

                Set<Brand> brandHeadPhones = new HashSet<>(
                                Arrays.asList(bv.brandApple, bv.brandSony, bv.brandSamsung, bv.brandMozard));
                cv.categoryHeadphone = new Category("Tai Nghe",
                                Image.createImageCategory("hkitjl9vpp3owitkkb5foprtlqrs5454.png"),
                                null,
                                brandHeadPhones);
                cv.categoryHeadphone.setParentCategory(cv.categoryAccessory);
                cv.categoryHeadphone = this.categoryRepo.save(cv.categoryHeadphone);

                Set<Brand> brandBackupChargers = new HashSet<>(Arrays.asList(bv.brandAnker, bv.brandBelkin));
                cv.categoryBackupCharger = new Category("Sạc Dự Phòng",
                                Image.createImageCategory("31n6ah58dicld5n5aihexs6vdr1g8bba.png"),
                                null,
                                brandBackupChargers);
                cv.categoryBackupCharger.setParentCategory(cv.categoryAccessory);
                cv.categoryBackupCharger = this.categoryRepo.save(cv.categoryBackupCharger);

                Set<Brand> brandCharginCables = new HashSet<>(
                                Arrays.asList(bv.brandApple, bv.brandSamsung, bv.brandAnker, bv.brandXMobile,
                                                bv.brandOppo,
                                                bv.brandBelkin,
                                                bv.brandSony));
                cv.categoryChargingCable = new Category("Cáp Sạc",
                                Image.createImageCategory("unxowi1qobpazcjpvf6g2rr0yzzy9s26.png"),
                                null,
                                brandCharginCables);
                cv.categoryChargingCable.setParentCategory(cv.categoryAccessory);
                cv.categoryChargingCable = this.categoryRepo.save(cv.categoryChargingCable);

                Set<Brand> brandComputerSets = new HashSet<>(
                                Arrays.asList(bv.brandApple, bv.brandHP, bv.brandAsus, bv.brandLenovo, bv.brandMSI));
                cv.categoryComputerSet = new Category("Máy Tính Bộ",
                                Image.createImageCategory("16qg9ng02rvdk13qfq04fftt6dvu1cz6.png"),
                                null,
                                brandComputerSets);
                cv.categoryComputerSet.setParentCategory(cv.categoryPCPrinter);
                cv.categoryComputerSet = this.categoryRepo.save(cv.categoryComputerSet);

                Set<Brand> brandPrinters = new HashSet<>(Arrays.asList(bv.brandCanon, bv.brandBrother, bv.brandHP));
                cv.categoryPrinter = new Category("Máy In",
                                Image.createImageCategory("q19in86smktb2xhfjz3q7cuvc6il0gwx.png"), null,
                                brandPrinters);
                cv.categoryPrinter.setParentCategory(cv.categoryPCPrinter);
                cv.categoryPrinter = this.categoryRepo.save(cv.categoryPrinter);

                Set<Brand> brandComputerScreens = new HashSet<>(
                                Arrays.asList(bv.brandAsus, bv.brandSamsung, bv.brandLenovo, bv.brandViewSonic,
                                                bv.brandDell,
                                                bv.brandMSI));
                cv.categoryComputerScreen = new Category("Màn Hình Máy Tính",
                                Image.createImageCategory("vdmt24263lcb3jtzk3r5cw4liduez6x4.png"),
                                null,
                                brandComputerScreens);
                cv.categoryComputerScreen.setParentCategory(cv.categoryPCPrinter);
                cv.categoryComputerScreen = this.categoryRepo.save(cv.categoryComputerScreen);

                cv.categoryCamera = new Category("Camera, Webcam",
                                Image.createImageCategory("9s0n62iw3w68p2uymk2fpjp6dh28p5mr.png"), null,
                                null);
                cv.categoryCamera.setParentCategory(cv.categoryAccessory);
                cv.categoryCamera = this.categoryRepo.save(cv.categoryCamera);

                Set<Brand> brandSmartHouses = new HashSet<>(
                                Arrays.asList(bv.brandApple, bv.brandXiaomi, bv.brandFPT, bv.brandTPLink));
                cv.categorySmartHouse = new Category("Thiết Bị Nhà Thông Minh",
                                Image.createImageCategory("br4rvt30ehot68kpvpgel3a36x8egefz.png"),
                                null,
                                brandSmartHouses);
                cv.categorySmartHouse.setParentCategory(cv.categoryAccessory);
                cv.categorySmartHouse = this.categoryRepo.save(cv.categorySmartHouse);

                Set<Brand> brandRefrigerators = new HashSet<>(
                                Arrays.asList(bv.brandSamsung, bv.brandPanasonic, bv.brandSharp, bv.brandAqua,
                                                bv.brandHitaChi,
                                                bv.brandHoaPhat,
                                                bv.brandElectrolux));
                cv.categoryRefrigerator = new Category("Tủ lạnh", Image.createImageCategory("download.jpg"), null,
                                brandRefrigerators);
                cv.categoryRefrigerator.setParentCategory(cv.categoryAccessory);
                cv.categoryRefrigerator = this.categoryRepo.save(cv.categoryRefrigerator);

                Set<Brand> brandTivis = new HashSet<>(Arrays.asList(bv.brandLG, bv.brandSamsung, bv.brandCoocaa,
                                bv.brandCasper,
                                bv.brandTCL,
                                bv.brandAsanzo));
                cv.categoryTivi = new Category("Tivi",
                                Image.createImageCategory("television-logo-design_412311-3881.webp"), null,
                                brandTivis);
                cv.categoryTivi.setParentCategory(cv.categoryAccessory);
                cv.categoryTivi = this.categoryRepo.save(cv.categoryTivi);

                // Set<Brand> brand =
                // new HashSet<>(Arrays.asList());
                // cv.category =
                // new Category("", Image.createImageCategory(""), null, brand);
                // cv.category.setParentCategory(cv.categoryAccessory);
                // cv.category = this.categoryRepo.save(cv.category);
        }

        static class BrandVariable {
                public BrandVariable() {

                }

                // = new Image("IMAGE_BRAND/", EImageType.IMAGE_BRAND);
                Brand brandOther;
                Brand brandIphone;
                Brand brandSamsung;
                Brand brandOppo;
                Brand brandXiaomi;
                Brand brandVivo;
                Brand brandRealme;
                Brand brandNokia;
                Brand brandMobell;
                Brand brandItel;
                Brand brandMasstel;
                Brand brandAcer;
                Brand brandAsus;
                Brand brandDell;
                Brand brandLenovo;
                Brand brandThinkpad;
                Brand brandMacbook;
                Brand brandMSI;
                Brand brandApple;
                Brand brandXMobile;
                Brand brandMozard;
                Brand brandCitizen;
                Brand brandEsprit;
                Brand brandDKNY;
                Brand brandHP;
                Brand brandIpad;
                Brand brandHuawei;
                Brand brandSony;
                Brand brandWatch;
                Brand brandNakzen;
                Brand brandMVW;
                Brand brandELIO;
                Brand brandGShock;
                Brand brandFossil;
                Brand brandMovado;
                Brand brandOrient;
                Brand brandCasio;
                Brand brandLG;
                Brand brandFPT;
                Brand brandMicrosoft;
                Brand brandLogitech;
                Brand brandSanDisk;
                Brand brandPanasonic;
                Brand brandSharp;
                Brand brandAqua;
                Brand brandHitaChi;
                Brand brandHoaPhat;
                Brand brandElectrolux;
                Brand brandVietNamMobile;
                Brand brandViettel;
                Brand brandMobiPhone;
                Brand brandVinaPhone;
                Brand brandCanon;
                Brand brandBrother;
                Brand brandViewSonic;
                Brand brandJBL;
                Brand brandAnker;
                Brand brandBelkin;
                Brand brandTPLink;
                Brand brandMercuSYS;
                Brand brandDLink;
                Brand brandToToLink;
                Brand brandLinkSYS;
                Brand brandTenda;
                Brand brandCoocaa;
                Brand brandCasper;
                Brand brandTCL;
                Brand brandAsanzo;
        }

        public void createBrand(BrandVariable bv) {
                // = new Image("IMAGE_BRAND/", EImageType.IMAGE_BRAND);
                bv.brandOther = new Brand("Other");
                bv.brandOther = this.brandRepo.save(bv.brandOther);

                bv.brandIphone = new Brand("Iphone", Image.createImageBrand("aac9e1ae8404471f86c7b2278c9b5ce5.png"));
                bv.brandIphone = this.brandRepo.save(bv.brandIphone);

                bv.brandSamsung = new Brand("Samsung", Image.createImageBrand("9d64f1945bc74e5390a6a62df8a7edcc.png"));
                bv.brandSamsung = this.brandRepo.save(bv.brandSamsung);

                bv.brandOppo = new Brand("Oppo", Image.createImageBrand("d92539b2d9bf44338851ef13552f0915.jpg"));
                bv.brandOppo = this.brandRepo.save(bv.brandOppo);

                bv.brandXiaomi = new Brand("Xiaomi", Image.createImageBrand("3cdce901606f49198304b68a919659d9.png"));
                bv.brandXiaomi = this.brandRepo.save(bv.brandXiaomi);

                bv.brandVivo = new Brand("Vivo", Image.createImageBrand("da5e5d4f5ee54262b2a1bbe5f4e4cd6f.png"));
                bv.brandVivo = this.brandRepo.save(bv.brandVivo);

                bv.brandRealme = new Brand("Realme", Image.createImageBrand("6b32cec2001441349e0d3037bef9a789.png"));
                bv.brandRealme = this.brandRepo.save(bv.brandRealme);

                bv.brandNokia = new Brand("Nokia", Image.createImageBrand("bb30cb3bf0864695a7c6f72ef639f15e.jpg"));
                bv.brandNokia = this.brandRepo.save(bv.brandNokia);

                bv.brandMobell = new Brand("Mobell", Image.createImageBrand("38ff46febe0045719fd0b72ff6816afd.jpg"));
                bv.brandMobell = this.brandRepo.save(bv.brandMobell);

                bv.brandItel = new Brand("Itel", Image.createImageBrand("6b02cb703a744b0ca19fd0a3220b0264.jpg"));
                bv.brandItel = this.brandRepo.save(bv.brandItel);

                bv.brandMasstel = new Brand("Masstel", Image.createImageBrand("552a5af68a024c79b392433493b4cf9f.png"));
                bv.brandMasstel = this.brandRepo.save(bv.brandMasstel);

                bv.brandAcer = new Brand("Acer", Image.createImageBrand("logo-acer-149x40.png"));
                bv.brandAcer = this.brandRepo.save(bv.brandAcer);

                bv.brandAsus = new Brand("Asus", Image.createImageBrand("logo-asus-149x40.png"));
                bv.brandAsus = this.brandRepo.save(bv.brandAsus);

                bv.brandDell = new Brand("Dell", Image.createImageBrand("logo-dell-149x40.png"));
                bv.brandDell = this.brandRepo.save(bv.brandDell);

                bv.brandLenovo = new Brand("Lenovo", Image.createImageBrand("logo-lenovo-149x40.png"));
                bv.brandLenovo = this.brandRepo.save(bv.brandLenovo);

                bv.brandThinkpad = new Brand("Thinkpad");
                bv.brandThinkpad = this.brandRepo.save(bv.brandThinkpad);

                bv.brandMacbook = new Brand("Macbook", Image.createImageBrand("logo-macbook-149x40.png"));
                bv.brandMacbook = this.brandRepo.save(bv.brandMacbook);

                bv.brandMSI = new Brand("MSI", Image.createImageBrand("5vwoued3v1itbeyl5jjahqtr47c9doxw.png"));
                bv.brandMSI = this.brandRepo.save(bv.brandMSI);

                bv.brandApple = new Brand("Apple", Image.createImageBrand("jt11uip8sxd2ufd7e3ephi2phgq5ult8.jpg"));
                bv.brandApple = this.brandRepo.save(bv.brandApple);

                bv.brandXMobile = new Brand("XMobile", Image.createImageBrand("n4a69p0t7wpu3ukbu1yro3jc141wn5bv.jpg"));
                bv.brandXMobile = this.brandRepo.save(bv.brandXMobile);

                bv.brandMozard = new Brand("Mozard", Image.createImageBrand("f0tfbnlo8mr43xpqoikzn1xog0s6yj9m.jpg"));
                bv.brandMozard = this.brandRepo.save(bv.brandMozard);

                bv.brandCitizen = new Brand("Citizen", Image.createImageBrand("rw7ldrnjj9gf1mboigoiafbssch8l8e5.jpg"));
                bv.brandCitizen = this.brandRepo.save(bv.brandCitizen);

                bv.brandEsprit = new Brand("Esprit", Image.createImageBrand("ndfjjzg3d5r0y79z2y5lsmfdikyd0d3t.jpg"));
                bv.brandEsprit = this.brandRepo.save(bv.brandEsprit);

                bv.brandDKNY = new Brand("DKNY", Image.createImageBrand("5wtd0ga8yx79strn8fbgxp9qg2hlxp5m.jpg"));
                bv.brandDKNY = this.brandRepo.save(bv.brandDKNY);

                bv.brandHP = new Brand("HP", Image.createImageBrand("fniz9nvgs1fg0cflslv32oh2e81gn4o9.png"));
                bv.brandHP = this.brandRepo.save(bv.brandHP);

                bv.brandIpad = new Brand("IPad", Image.createImageBrand("xfazgu3txz34gv3yflvowxyjzc2ywwpy.jpg"));
                bv.brandIpad = this.brandRepo.save(bv.brandIpad);

                bv.brandHuawei = new Brand("Huawei", Image.createImageBrand("18def20cdzopez5q0dy52fu5xm3h951f.jpg"));
                bv.brandHuawei = this.brandRepo.save(bv.brandHuawei);

                bv.brandSony = new Brand("Sony", Image.createImageBrand("7iaqwbntk0a3ofc0u4e9gw1t2lter8ii.jpg"));
                bv.brandSony = this.brandRepo.save(bv.brandSony);

                bv.brandWatch = new Brand("Watch", Image.createImageBrand("ubv7aqlfdww0ebjcdzdk0tk0edpibkin.png"));
                bv.brandWatch = this.brandRepo.save(bv.brandWatch);

                bv.brandNakzen = new Brand("Nakzen", Image.createImageBrand("kpeoty7gx4k72z36w3rett1sytv073ot.png"));
                bv.brandNakzen = this.brandRepo.save(bv.brandNakzen);

                bv.brandMVW = new Brand("MVW", Image.createImageBrand("hjfktdo808ie1oq4091noyjusvow5qsh.jpg"));
                bv.brandMVW = this.brandRepo.save(bv.brandMVW);

                bv.brandELIO = new Brand("EILO", Image.createImageBrand("kv93y3xiori2b6u1l91lrarbc8ap2l45.jpg"));
                bv.brandELIO = this.brandRepo.save(bv.brandELIO);

                bv.brandGShock = new Brand("G-Shock", Image.createImageBrand("8xih56kxlj97ub6gmrd4ldhn4la921ah.jpg"));
                bv.brandGShock = this.brandRepo.save(bv.brandGShock);

                bv.brandFossil = new Brand("Fossil", Image.createImageBrand("ri33nmzk7aelb9fq1d6wsv9nw9jpzymm.jpg"));
                bv.brandFossil = this.brandRepo.save(bv.brandFossil);

                bv.brandMovado = new Brand("Movado", Image.createImageBrand("y3hz8ir13zegwy5rhjq9wl259ya371ap.jpg"));
                bv.brandMovado = this.brandRepo.save(bv.brandMovado);

                bv.brandOrient = new Brand("Orient", Image.createImageBrand("j7abb4strin4nss3jnwb9oju4wa4wxe5.jpg"));
                bv.brandOrient = this.brandRepo.save(bv.brandOrient);

                bv.brandCasio = new Brand("Casio", Image.createImageBrand("svla5twvnomb3j595optofb932b7c8q3.jpg"));
                bv.brandCasio = this.brandRepo.save(bv.brandCasio);

                bv.brandLG = new Brand("LG", Image.createImageBrand("pwl0bhffh0eolwa0f5m9zfbp9zw803ui.png"));
                bv.brandLG = this.brandRepo.save(bv.brandLG);

                bv.brandFPT = new Brand("FPT", Image.createImageBrand("d5n55bo9wmptxotu540koih2t4q6o89g.jpeg"));
                bv.brandFPT = this.brandRepo.save(bv.brandFPT);

                bv.brandMicrosoft = new Brand("Microsoft",
                                Image.createImageBrand("2msdtvpz5rxw5tb8zfvuduwb6d166tdz.jpg"));
                bv.brandMicrosoft = this.brandRepo.save(bv.brandMicrosoft);

                bv.brandLogitech = new Brand("Logitech",
                                Image.createImageBrand("i163w9o5tev99i7bt29wytw6a0c2qjw6.jpg"));
                bv.brandLogitech = this.brandRepo.save(bv.brandLogitech);

                bv.brandSanDisk = new Brand("SanDisk", Image.createImageBrand("4ouvbvxao3breaupz540pvn2qymm90le.png"));
                bv.brandSanDisk = this.brandRepo.save(bv.brandSanDisk);

                bv.brandPanasonic = new Brand("Panasonic",
                                Image.createImageBrand("99ihqv46ybzjky6c3xq2sdzvq26f04xr.png"));
                bv.brandPanasonic = this.brandRepo.save(bv.brandPanasonic);

                bv.brandSharp = new Brand("Sharp",
                                Image.createImageBrand("2560px-Logo_of_the_Sharp_Corporation.svg.png"));
                bv.brandSharp = this.brandRepo.save(bv.brandSharp);

                bv.brandAqua = new Brand("Aqua", Image.createImageBrand("aqua-logo.png"));
                bv.brandAqua = this.brandRepo.save(bv.brandAqua);

                bv.brandHitaChi = new Brand("HitaChi", Image.createImageBrand("Hitachi_logo_PNG4.png"));
                bv.brandHitaChi = this.brandRepo.save(bv.brandHitaChi);

                bv.brandHoaPhat = new Brand("Hòa Phát", Image.createImageBrand("Logo-Hoa-Phat-HPG-H.jpg"));
                bv.brandHoaPhat = this.brandRepo.save(bv.brandHoaPhat);

                bv.brandElectrolux = new Brand("Electrolux", Image.createImageBrand("electrolux.png"));
                bv.brandElectrolux = this.brandRepo.save(bv.brandElectrolux);

                bv.brandVietNamMobile = new Brand("VietNamMobile",
                                Image.createImageBrand("vbp16shygohse0z827hgjwncwlhooqxy.png"));
                bv.brandVietNamMobile = this.brandRepo.save(bv.brandVietNamMobile);

                bv.brandViettel = new Brand("Viettel", Image.createImageBrand("ragfne1htfjbkcqowyzpl00lih1cyxxe.png"));
                bv.brandViettel = this.brandRepo.save(bv.brandViettel);

                bv.brandMobiPhone = new Brand("MobiPhone",
                                Image.createImageBrand("uy721spxh2d5vrn0p2q7j148c9pgt967.png"));
                bv.brandMobiPhone = this.brandRepo.save(bv.brandMobiPhone);

                bv.brandVinaPhone = new Brand("VinaPhone",
                                Image.createImageBrand("l2f1eryngcqms4g8cvl8xik1xloskcw8.png"));
                bv.brandVinaPhone = this.brandRepo.save(bv.brandVinaPhone);

                bv.brandCanon = new Brand("Canon", Image.createImageBrand("yodujh3p7vi970ptzmr1c6rad1bmw19j.jpeg"));
                bv.brandCanon = this.brandRepo.save(bv.brandCanon);

                bv.brandBrother = new Brand("Brother", Image.createImageBrand("a2lzckhwqp6kid2zc3f2g8zdj2whyby0.jpeg"));
                bv.brandBrother = this.brandRepo.save(bv.brandBrother);

                bv.brandViewSonic = new Brand("View Sonic",
                                Image.createImageBrand("cyx4qgygqehamx7zvilvmr6tu4x6qdfx.png"));
                bv.brandViewSonic = this.brandRepo.save(bv.brandViewSonic);

                bv.brandJBL = new Brand("JBL", Image.createImageBrand("vrk7n120tm37ewsbs2q6qympikri6g33.jpeg"));
                bv.brandJBL = this.brandRepo.save(bv.brandJBL);

                bv.brandAnker = new Brand("Anker", Image.createImageBrand("fqd4nqg7ddch28g39j9uhst772gj582e.png"));
                bv.brandAnker = this.brandRepo.save(bv.brandAnker);

                bv.brandBelkin = new Brand("Belkin", Image.createImageBrand("6hk4dnjtyw8j1t00x1g9952p3lhk4rf7.jpeg"));
                bv.brandBelkin = this.brandRepo.save(bv.brandBelkin);

                bv.brandTPLink = new Brand("TP-Link", Image.createImageBrand("jl4rz3c0qx3d0w4cfndvrzi49ug8r9pn.jpg"));
                bv.brandTPLink = this.brandRepo.save(bv.brandTPLink);

                bv.brandMercuSYS = new Brand("Mercu SYS",
                                Image.createImageBrand("pfpfsghoe1c5rgwxmjx8rgvqcxw6n28l.jpg"));
                bv.brandMercuSYS = this.brandRepo.save(bv.brandMercuSYS);

                bv.brandDLink = new Brand("D-Link", Image.createImageBrand("iai7o2ao5j9het34p8m3yj25b7xrdh4k.jpeg"));
                bv.brandDLink = this.brandRepo.save(bv.brandDLink);

                bv.brandToToLink = new Brand("ToTo Link",
                                Image.createImageBrand("lj2f99hzbd63xdt5zmlvw9g8srylwf3e.jpg"));
                bv.brandToToLink = this.brandRepo.save(bv.brandToToLink);

                bv.brandLinkSYS = new Brand("Link-SYS", Image.createImageBrand("fwwo3200zel37t69stjnqhno7i1limgh.jpg"));
                bv.brandLinkSYS = this.brandRepo.save(bv.brandLinkSYS);

                bv.brandTenda = new Brand("Tenda", Image.createImageBrand("11c24dio8b31oowofno2gbuhbluzd5dn.png"));
                bv.brandTenda = this.brandRepo.save(bv.brandTenda);

                bv.brandCoocaa = new Brand("Coocaa", Image.createImageBrand("coocaa-Logo.png"));
                bv.brandCoocaa = this.brandRepo.save(bv.brandCoocaa);

                bv.brandCasper = new Brand("Casper", Image.createImageBrand("Casper_Sleep_logo.svg.png"));
                bv.brandCasper = this.brandRepo.save(bv.brandCasper);

                bv.brandTCL = new Brand("TCL", Image.createImageBrand("Logo_of_the_TCL_Corporation.svg.png"));
                bv.brandTCL = this.brandRepo.save(bv.brandTCL);

                bv.brandAsanzo = new Brand("Asanzo", Image.createImageBrand("logo.webp"));
                bv.brandAsanzo = this.brandRepo.save(bv.brandAsanzo);

                // bv.brandAqua = new Brand("", Image.createImageBrand(""));
                // bv.brand = this.brandRepo.save(bv.brand);

        }

        public int createFullAddress(FullAddress[] fullAddresses) {
                Location[] locations = new Location[500];
                int i = 0;
                locations[i] = new Location("Phường Hiệp Phú", "Thành Phố Thủ Đức", "Thành phố Hồ Chí Minh");
                locations[i] = this.locationService.saveLocation(locations[i]);
                fullAddresses[i] = new FullAddress("Trường Đại học Sư phạm Kỹ thuật Tp. Hồ Chí Minh", locations[i]);

                i++;
                locations[i] = new Location("Phường Phước Long A", "Thành Phố Thủ Đức", "Thành phố Hồ Chí Minh");
                locations[i] = this.locationService.saveLocation(locations[i]);
                fullAddresses[i] = new FullAddress("Trường Tư Thục Ngô Thời Nhiệm", locations[i]);

                i++;
                locations[i] = new Location("Phường Phước Long B", "Thành Phố Thủ Đức", "Thành phố Hồ Chí Minh");
                locations[i] = this.locationService.saveLocation(locations[i]);
                fullAddresses[i] = new FullAddress("Trường Cao Đẳng Công Thương TP.HCM", locations[i]);

                i++;
                locations[i] = new Location("Phường Thảo Điền", "Thành Phố Thủ Đức", "Thành phố Hồ Chí Minh");
                locations[i] = this.locationService.saveLocation(locations[i]);
                fullAddresses[i] = new FullAddress("BIS HCMC, Early Years and Infant Campus", locations[i]);

                i++;
                locations[i] = new Location("Phường Phú Hữu", "Thành Phố Thủ Đức", "Thành phố Hồ Chí Minh");
                locations[i] = this.locationService.saveLocation(locations[i]);
                fullAddresses[i] = new FullAddress("949-939 Đ. Nguyễn Duy Trinh\n", locations[i]);

                i++;
                locations[i] = new Location("Phường Phước Bình", "Thành Phố Thủ Đức", "Thành phố Hồ Chí Minh");
                locations[i] = this.locationService.saveLocation(locations[i]);
                fullAddresses[i] = new FullAddress("Trường Cao đẳng Kinh tế Đối ngoại", locations[i]);

                i++;
                locations[i] = new Location("Phường Long Trường", "Thành Phố Thủ Đức", "Thành phố Hồ Chí Minh");
                locations[i] = this.locationService.saveLocation(locations[i]);
                fullAddresses[i] = new FullAddress("6-32 Đường 7", locations[i]);

                i++;
                locations[i] = new Location("Phường Tăng Nhơn Phú A", "Thành Phố Thủ Đức", "Thành phố Hồ Chí Minh");
                locations[i] = this.locationService.saveLocation(locations[i]);
                fullAddresses[i] = new FullAddress("Trường Đại học Tài chính - Marketing Cơ sở Thủ Đức", locations[i]);

                i++;
                locations[i] = new Location("Phường Tăng Nhơn Phú B", "Thành Phố Thủ Đức", "Thành phố Hồ Chí Minh");
                locations[i] = this.locationService.saveLocation(locations[i]);
                fullAddresses[i] = new FullAddress("Nhà Máy SAMSUNG Khu Công Nghệ Cao", locations[i]);

                i++;
                locations[i] = new Location("Phường Long Thạnh Mỹ", "Thành Phố Thủ Đức", "Thành phố Hồ Chí Minh");
                locations[i] = this.locationService.saveLocation(locations[i]);
                fullAddresses[i] = new FullAddress("Nghĩa Trang Phúc An Viên", locations[i]);

                return i;
        }

        static class LocationVariable {
                public LocationVariable() {
                }

                Location locationHCM;
                Location locationHaNoi;
                Location locationCanTho;
                Location locationDaNang;
                Location locationHaiPhong;
                Location locationAnGiang;
                Location locationLongAn;
                Location locationCaMau;
                Location locationVinhLong;
                Location locationBenTre;
                Location locationDongThap;

        }

        public void createLocation(LocationVariable lv) {
                lv.locationHCM = new Location("Hồ Chí Minh");
                lv.locationHCM = this.locationService.saveLocation(lv.locationHCM);

                lv.locationHaNoi = new Location("Hà Nội");
                lv.locationHaNoi = this.locationService.saveLocation(lv.locationHaNoi);

                lv.locationCanTho = new Location("Cần Thơ");
                lv.locationCanTho = this.locationService.saveLocation(lv.locationCanTho);

                lv.locationDaNang = new Location("Đà Nẵng");
                lv.locationDaNang = this.locationService.saveLocation(lv.locationDaNang);

                lv.locationHaiPhong = new Location("Hải Phòng");
                lv.locationHaiPhong = this.locationService.saveLocation(lv.locationHaiPhong);

                lv.locationAnGiang = new Location("An Giang");
                lv.locationAnGiang = this.locationService.saveLocation(lv.locationAnGiang);

                lv.locationLongAn = new Location("Long An");
                lv.locationLongAn = this.locationService.saveLocation(lv.locationLongAn);

                lv.locationCaMau = new Location("Cà Mau");
                lv.locationCaMau = this.locationService.saveLocation(lv.locationCaMau);

                lv.locationVinhLong = new Location("Vĩnh Long");
                lv.locationVinhLong = this.locationService.saveLocation(lv.locationVinhLong);

                lv.locationBenTre = new Location("Bến Tre");
                lv.locationBenTre = this.locationService.saveLocation(lv.locationBenTre);

                lv.locationDongThap = new Location("Đồng Tháp");
                lv.locationDongThap = this.locationService.saveLocation(lv.locationDongThap);
        }

        static class UserVariable {
                public UserVariable() {

                }

                User admin;
                User admin1;
                User seller;
                User seller1;
                User seller2;
                User[] customers;
                int maxIndexCustomer;
                User[] shippers;
                int maxIndexShipper;
        }

        public void createUser(UserVariable uv, LocationVariable lv) {
                AuthRegisterDTO authAdmin1 = new AuthRegisterDTO("Thái", "Phương", EGender.MALE, "0375189189",
                                "admin@gmail.com",
                                Utils.DEFAULT_PASSWORD);
                this.userService.registerUser(authAdmin1, false);
                uv.admin = this.userRepo.findUserByPhone(authAdmin1.getPhone()).orElse(null);
                if (uv.admin != null) {
                        uv.admin.setRole(ERole.ROLE_ADMIN);
                        uv.admin = this.userRepo.save(uv.admin);
                        Address addressAdmin = new Address(uv.admin, lv.locationLongAn, "Nguyễn Trung");
                        addressAdmin = this.addressService.saveAddress(addressAdmin);
                }

                AuthRegisterDTO authAdmin2 = new AuthRegisterDTO("Nguyễn", "Nguyên", EGender.MALE, "0976080909",
                                "admin1@gmail.com",
                                Utils.DEFAULT_PASSWORD);
                this.userService.registerUser(authAdmin2, false);
                uv.admin1 = this.userRepo.findUserByPhone(authAdmin2.getPhone()).orElse(null);
                if (uv.admin1 != null) {
                        uv.admin1.setRole(ERole.ROLE_ADMIN);
                        uv.admin1 = this.userRepo.save(uv.admin1);
                }

                AuthRegisterDTO[] authRegisterCustomers = new AuthRegisterDTO[500];
                uv.customers = new User[500];

                uv.maxIndexCustomer = createCustomer(authRegisterCustomers, uv.customers);

                AuthRegisterDTO[] authRegisterShippers = new AuthRegisterDTO[500];
                uv.shippers = new User[500];
                uv.maxIndexShipper = createShipper(authRegisterShippers, uv.shippers);
        }

        public int generateInt(int max, int... mins) {
                int min = mins.length > 0 ? mins[0] : 0;
                return generator.nextInt((max - 1) - min + 1) + min;
        }
}
