package gt.electronic.ecommerce.services;

public interface ScheduleTaskService {
    void checkBlackListProduct();

    void checkShopPricePackage();

    void sendMailInMessage();
}
