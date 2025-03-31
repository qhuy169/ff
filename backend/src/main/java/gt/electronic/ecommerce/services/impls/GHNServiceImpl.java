package gt.electronic.ecommerce.services.impls;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import gt.electronic.ecommerce.entities.Order;
import gt.electronic.ecommerce.models.clazzs.GHNItem;
import gt.electronic.ecommerce.models.clazzs.GHNRequest;
import gt.electronic.ecommerce.services.GHNService;
import gt.electronic.ecommerce.utils.GHN;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * @author quang huy
 * @created 20/11/2025 - 10:08 PM
 */
@Service
@Transactional
public class GHNServiceImpl implements GHNService {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  public static final String branchName = "GHN";
  @Value("${ghn.token}")
  private String tokenGHN;
  // @Value("${ghn.shopId}")
  // private String shopId;
  @Value("${ghn.product.weight}")
  private String defaultProductWeight;
  @Value("${ghn.product.length}")
  private String defaultProductLength;
  @Value("${ghn.product.width}")
  private String defaultProductWidth;
  @Value("${ghn.product.height}")
  private String defaultProductHeight;

  @Autowired
  private GHN ghn;

  @Override
  public Map<String, Object> getInfoShippingFee(Order order)
      throws IOException {
    GHNRequest request = new GHNRequest();
    request.setTo_name(order.getFullName());
    request.setTo_phone(order.getPhone() != null ? order.getPhone() : order.getEmail());
    request.setTo_address(Utils.getLocationStringFromLocationAndLine(order.getLocation(),
        order.getLine()) + ", Việt Nam");
    String provinceId = ghn.getProvinceId(order.getLocation().getProvince());
    String districtId = ghn.getDistrictId(provinceId, order.getLocation().getDistrict());
    String wardCode = ghn.getWardId(districtId, order.getLocation().getCommune());
    request.setTo_district_id(Integer.parseInt(districtId));
    request.setTo_ward_code(wardCode);
    request.setWeight(Integer.parseInt(defaultProductWeight));
    request.setLength(Integer.parseInt(defaultProductLength));
    request.setWidth(Integer.parseInt(defaultProductWidth));
    request.setHeight(Integer.parseInt(defaultProductHeight));
    int walkService = 2;
    // int flyService = 1;
    request.setService_type_id(walkService);
    request.setService_id(0);
    // int senderPayment = 1;
    int receiverPayment = 2;
    request.setPayment_type_id(receiverPayment);
    request.setRequired_note("KHONGCHOXEMHANG");
    GHNItem item = new GHNItem();
    item.setName("Áo Polo");
    item.setQuantity(1);
    request.setItems(new ArrayList<>(Collections.singletonList(item)));
    URL url = new URL("https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/preview");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Token", tokenGHN);
    // connection.setRequestProperty("shop_id", shopId);
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Accept", "application/json");
    connection.setUseCaches(false);
    connection.setDoInput(true);
    connection.setDoOutput(true);

    Gson gson = new Gson();
    // OutputStream outStream = connection.getOutputStream();
    // OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream,
    // StandardCharsets.UTF_8);
    String jsonInputString = gson.toJson(request);
    try (OutputStream os = connection.getOutputStream()) {
      byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
      os.write(input, 0, input.length);
    }

    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
      StringBuilder response = new StringBuilder();
      String responseLine;
      while ((responseLine = br.readLine()) != null) {
        response.append(responseLine.trim());
      }
      responseLine = response.toString();
      if (responseLine.contains("{\"code\":200,\"message\":\"Success\",\"data\":")) {
        responseLine = responseLine.substring("{\"code\":200,\"message\":\"Success\",\"data\":".length());
        responseLine = responseLine.substring(0, responseLine.length() - 1);
        System.out.println(responseLine);
        Type resultType = new TypeToken<Map<String, Object>>() {
        }.getType();
        return gson.fromJson(responseLine, resultType);
      }
    } catch (Exception e) {
      this.LOGGER.error(e.getMessage());
    }
    return null;
  }
}
