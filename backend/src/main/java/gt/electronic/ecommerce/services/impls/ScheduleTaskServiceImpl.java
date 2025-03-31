package gt.electronic.ecommerce.services.impls;

import gt.electronic.ecommerce.entities.Message;
import gt.electronic.ecommerce.repositories.MessageRepository;
import gt.electronic.ecommerce.repositories.ProcedureRepository;
import gt.electronic.ecommerce.services.MailService;
import gt.electronic.ecommerce.services.ScheduleTaskService;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ScheduleTaskServiceImpl implements ScheduleTaskService {
    @Value("${app.shop.timeRangeCheckBlackProductMs}")
    private long timeCheckBlackProductMs;

    @Value("${app.shop.timeRangeSessionMs}")
    private long timeRangeSessionMs;

    @Value("${app.shop.minSentiment}")
    private int minSentiment;

    @Value("${app.shop.minNegativePercent}")
    private int minNegativePercent;

    @Value("${app.shop.rangeDayCheckShopPrice}")
    private int rangeDayCheckShopPrice;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    private MailService mailService;

    @Autowired
    public void MailService(MailService mailService) {
        this.mailService = mailService;
    }

    private MessageRepository messageRepo;

    @Autowired
    public void MessageRepository(MessageRepository messageRepo) {
        this.messageRepo = messageRepo;
    }

    ProcedureRepository procedureRepository;

    @Autowired
    public void ProcedureRepository(ProcedureRepository procedureRepository) {
        this.procedureRepository = procedureRepository;
    }


    @Override
    @Scheduled(cron = "0 0 0 * * ?")//0:00:00 per day
//    @Scheduled(fixedRate = 6000, initialDelay = 1000)
    public void checkBlackListProduct() {
        this.LOGGER.warn(String.format(Utils.LOG_UPDATE_PRODUCT_BLACK_LIST_AT, sdf.format(new Date())));
        Date startDateCheckProductBlackList = new Date((new Date()).getTime() - timeCheckBlackProductMs);
        Date startDateNewSession = new Date((new Date()).getTime() - timeRangeSessionMs);
        procedureRepository.updateBlackListProduct(null, startDateCheckProductBlackList, minSentiment,
                                                   minNegativePercent, startDateNewSession);
        procedureRepository.updateBlackProductStatus(new Date());
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")//0:00:00 per day
//    @Scheduled(fixedRate = 604800000, initialDelay = 1000000)
    public void checkShopPricePackage() {
        this.LOGGER.warn(String.format(Utils.LOG_CHECK_SHOP_PRICE_PACKAGE, sdf.format(new Date())));
        rangeDayCheckShopPrice = -29;
        procedureRepository.updateShopPrice(rangeDayCheckShopPrice);
    }

    @Override
    @Scheduled(cron = "0 0 12 * * ?")//12:00:00 per day
//    @Scheduled(fixedRate = 604800000, initialDelay = 1000)
    public void sendMailInMessage() {
        this.LOGGER.warn(String.format(Utils.LOG_SEND_EMAIL_FOR_MESSAGE, sdf.format(new Date())));
        List<Message> list = this.messageRepo.findAllMessageNotSend();
        for (Message message : list) {
            try {
                this.mailService.sendSimpleMessage("phuongdorg@gmail.com", message.getTitle(), message.getBody());
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
//            this.mailService.sendSimpleMessage(message.getToEmail(), message.getTitle(), message.getBody());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            message.setSendAt(new Date());
            this.messageRepo.save(message);
        }
    }


}
