package gt.electronic.ecommerce.services.impls;

import gt.electronic.ecommerce.dto.response.ShopStatisticResponseDTO;
import gt.electronic.ecommerce.entities.OrderShop;
import gt.electronic.ecommerce.entities.ProductBlackList;
import gt.electronic.ecommerce.entities.Shop;
import gt.electronic.ecommerce.entities.User;
import gt.electronic.ecommerce.exceptions.ResourceAlreadyExistsException;
import gt.electronic.ecommerce.exceptions.ResourceNotFound;
import gt.electronic.ecommerce.exceptions.UserNotPermissionException;
import gt.electronic.ecommerce.models.clazzs.*;
import gt.electronic.ecommerce.models.enums.ERole;
import gt.electronic.ecommerce.models.enums.ESentiment;
import gt.electronic.ecommerce.models.enums.ETimeDistance;
import gt.electronic.ecommerce.models.interfaces.IInfoRating;
import gt.electronic.ecommerce.models.interfaces.IProductBlackList;
import gt.electronic.ecommerce.models.interfaces.IProductSentiment;
import gt.electronic.ecommerce.models.interfaces.IShopStatistic;
import gt.electronic.ecommerce.repositories.*;
import gt.electronic.ecommerce.services.StatisticService;
import gt.electronic.ecommerce.services.UserService;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author quang huy
 * @created 03/12/2025 - 12:08 PM
 */
@Service
@Transactional
public class StatisticServiceImpl implements StatisticService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final String branchName = "Statistic";
    private FeedbackRepository feedbackRepo;

    @Autowired
    public void FeedbackRepository(FeedbackRepository feedbackRepo) {
        this.feedbackRepo = feedbackRepo;
    }

    private OrderShopRepository orderShopRepo;

    @Autowired
    public void OrderShopRepository(OrderShopRepository orderShopRepo) {
        this.orderShopRepo = orderShopRepo;
    }

    private ProcedureRepository procedureRepository;

    @Autowired
    public void ProcedureRepository(ProcedureRepository procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    private ProductRepository productRepo;

    @Autowired
    public void ProductRepository(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    private ShopRepository shopRepo;

    @Autowired
    public void ShopRepository(ShopRepository shopRepo) {
        this.shopRepo = shopRepo;
    }

    private UserService userService;

    @Autowired
    public void UserService(UserService userService) {
        this.userService = userService;
    }

    private ViewRepository viewRepo;

    @Autowired
    public void ViewRepository(ViewRepository viewRepo) {
        this.viewRepo = viewRepo;
    }

    @Override
    public ShopOverview getOverviewByShop(Long shopId) {
        this.LOGGER.info(String.format(Utils.LOG_GET_OBJECT, branchName + " Overview Shop", "ShopId", shopId));
        // check shop name is existed
        Shop shopFound = this.shopRepo.findById(shopId).orElseThrow(() -> new ResourceAlreadyExistsException(
                String.format(
                        Utils.OBJECT_EXISTED_BY_FIELD,
                        Shop.class.getSimpleName(),
                        "Id",
                        shopId)));
        ShopOverview shopOverview = new ShopOverview();
        shopOverview.setTotalProduct(this.productRepo.countAllByShop(shopFound));
        List<IInfoRating> infoRatingList = this.feedbackRepo.getAllInfoRatingByShop(shopFound);
        Long totalVote = 0L;
        double avgStar = 0d;
        for (IInfoRating infoRating : infoRatingList) {
            totalVote += infoRating.getTotalVote();
            avgStar += infoRating.getTotalVote() * infoRating.getStar();
        }
        avgStar = avgStar / totalVote.doubleValue();
        shopOverview.setTotalVote(totalVote);
        shopOverview.setAvgStar(Utils.toBeTruncatedDouble(avgStar));
        shopOverview.setTimeDistanceFromCreateAt(Utils.getTimeDistance(shopFound.getCreatedAt(), null).timeDistance);
        return shopOverview;
    }

    @Override
    public ShopStatisticResponseDTO statisticOrderByShop(
            String loginKey,
            Long shopId,
            Date startDate,
            Date endDate,
            ETimeDistance timeDistance) {

        Shop shopFound = this.shopRepo.findById(shopId)
                .orElseThrow(() -> new ResourceNotFound(String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                        Shop.class.getSimpleName(),
                        "ID",
                        shopId)));
        List<OrderShop> orderList = this.orderShopRepo.findAllByShopAndRangePayDate(shopFound, startDate, endDate);
        Map<String, List<OrderShop>> groupOrderByDate = new HashMap<>();
        for (OrderShop order : orderList) {
            Calendar calendar = Utils.dateToCalendar(order.getCreatedAt());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            if (timeDistance == ETimeDistance.MONTH) {
                calendar.set(Calendar.DAY_OF_MONTH, 0);
            }
            String keyDate = Utils.getStringFromCalendar(calendar, timeDistance);
            List<OrderShop> orderByDays = groupOrderByDate.get(keyDate);
            if (orderByDays == null || orderByDays.size() == 0) {
                groupOrderByDate.put(keyDate, new ArrayList<>(Collections.singleton(order)));
            } else {
                orderByDays.add(order);
                groupOrderByDate.put(keyDate, orderByDays);
            }
        }
        List<GroupOrderByDate> groupOrderByDates = new ArrayList<>();
        for (Map.Entry<String, List<OrderShop>> entry : groupOrderByDate.entrySet()) {
            GroupOrderByDate groupOrder = new GroupOrderByDate();
            groupOrder.setDateStatistic(entry.getKey());
            BigDecimal totalPrice = new BigDecimal(0);
            List<SimpleOrder> orderDetails = new ArrayList<>();
            for (OrderShop order : entry.getValue()) {
                totalPrice = totalPrice.add(order.getTotalPrice());
                SimpleOrder simpleOrder = new SimpleOrder(order.getId(),
                        order.getTotalPrice(),
                        order.getTotalFee(),
                        new BigDecimal(0),
                        order.getTotalPrice());
                orderDetails.add(simpleOrder);
            }
            groupOrder.setTotalPrice(totalPrice);
            groupOrder.setOrderDetails(orderDetails);
            groupOrderByDates.add(groupOrder);
        }
        ShopStatisticResponseDTO responseDTO = new ShopStatisticResponseDTO();
        responseDTO.setGroupOrderByDateList(groupOrderByDates);
        IShopStatistic shopStatistic = this.viewRepo.getShopStatisticByShop(shopId);
        responseDTO.setShopStatistic(shopStatistic);
        return responseDTO;
    }

    @Override
    public ShopSentiment statisticSentimentByShop(String loginKey, Long shopId) {
        this.LOGGER.info(String.format(Utils.LOG_GET_STATISTIC_BY_USER, "Sentiment", "Shop", loginKey));
        User authorFound = this.userService.getUserByLoginKey(loginKey);
        if (Objects.equals(authorFound.getId(), shopId) || authorFound.getRole() == ERole.ROLE_ADMIN) {
            List<IProductSentiment> iProductSentimentList = this.viewRepo.getProductSentiment(null, shopId);
            Map<Long, ProductSentiment> productSentimentMap = new HashMap<>();
            for (IProductSentiment iProductSentiment : iProductSentimentList) {
                if (productSentimentMap.get(iProductSentiment.getProduct_id()) == null) {
                    ProductSentiment productSentiment = new ProductSentiment(iProductSentiment.getProduct_id());
                    if (iProductSentiment.getSentiment() != null) {
                        productSentiment.addSentimentDetail(
                                new SentimentDetail(iProductSentiment.getSentiment(), iProductSentiment.getCount()));
                    }
                    productSentimentMap.put(iProductSentiment.getProduct_id(), productSentiment);
                } else {
                    ProductSentiment productSentiment = productSentimentMap.get(iProductSentiment.getProduct_id());
                    productSentiment.addSentimentDetail(
                            new SentimentDetail(iProductSentiment.getSentiment(), iProductSentiment.getCount()));
                    productSentimentMap.replace(iProductSentiment.getProduct_id(), productSentiment);
                }
            }
            ProductSentiment[] productSentiments = new ProductSentiment[productSentimentMap.size()];
            int i = 0;
            for (Map.Entry<Long, ProductSentiment> productSentimentEntry : productSentimentMap.entrySet()) {
                productSentiments[i] = productSentimentEntry.getValue().updateProductSentiment();
                i++;
            }
            return new ShopSentiment(shopId, productSentiments);
        } else {
            throw new UserNotPermissionException();
        }
    }

    @Override
    public ShopSentiment reportNegativeProductByShop(String loginKey, Long shopId) {
        this.LOGGER.info(String.format(Utils.LOG_GET_ALL_OBJECT_BY_USER, "Negative Product", "Shop", loginKey));
        User authorFound = this.userService.getUserByLoginKey(loginKey);
        if (Objects.equals(authorFound.getShop().getId(), shopId) || authorFound.getRole() == ERole.ROLE_ADMIN) {
            List<IProductBlackList> productBlackListList = this.viewRepo.getProductBlackListByShop(shopId);
            ShopSentiment shopSentiment = new ShopSentiment();
            shopSentiment.setShopId(shopId);
            ProductSentiment[] productSentiments = new ProductSentiment[productBlackListList.size()];
            int i = 0;
            for (IProductBlackList productBlackList : productBlackListList) {
                productSentiments[i] = new ProductSentiment();
                productSentiments[i].setProductId(productBlackList.getProduct_id());
                productSentiments[i].setTotalSentiment(productBlackList.getTotal());
                productSentiments[i].setDate(productBlackList.getScan_at());
                productSentiments[i].setSentiment(ESentiment.SENTIMENT_NEGATIVE.toString());
                SentimentDetail sentimentDetail = new SentimentDetail(ESentiment.SENTIMENT_NEGATIVE.ordinal(),
                        productBlackList.getNeg_total(),
                        productBlackList.getPercent(), ESentiment.SENTIMENT_NEGATIVE.toString());
                productSentiments[i].setSentimentDetails(new SentimentDetail[] { sentimentDetail });
                i++;
            }
            shopSentiment.setProductSentiments(productSentiments);
            return shopSentiment;
        } else {
            throw new UserNotPermissionException();
        }
    }
}
