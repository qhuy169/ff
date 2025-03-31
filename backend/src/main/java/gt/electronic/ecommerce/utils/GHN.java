package gt.electronic.ecommerce.utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author quang huy
 * @created 11/10/2025 - 7:21 PM
 */
@Component
public class GHN {
  private final Logger LOGGER = LoggerFactory.getLogger(GHN.class);
  @Value("${ghn.token}")
  private String tokenGHN;

  public String getProvinceId(String provinceString) throws IOException {
    URL url = new URL("https://online-gateway.ghn.vn/shiip/public-api/master-data/province");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    connection.setRequestProperty("Token", tokenGHN);
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Accept", "application/json");
    connection.setUseCaches(false);
    connection.setDoInput(true);
    connection.setDoOutput(true);
    // OutputStream outStream = connection.getOutputStream();
    // OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream,
    // "UTF-8");
    // String jsonInputString =
    // "{\"from_district_id\": 1454,\"service_id\": 53320,\"service_type_id\":
    // null,\"to_district_id\": 1452,\"to_ward_code\": \"21012\",\"height\":
    // 50,\"length\": 20,\"weight\": 200,\"width\": 20,\"insurance_value\":
    // 10000,\"coupon\": null}";
    // try (OutputStream os = connection.getOutputStream()) {
    // byte[] input = jsonInputString.getBytes("utf-8");
    // os.write(input, 0, input.length);
    // }
    /// Get Response
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
        Gson gson = new Gson();
        Type resultType = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        List<Map<String, Object>> provinceList = gson.fromJson(responseLine, resultType);
        provinceString = getSimpleAddress(provinceString);
        for (Map<String, Object> province : provinceList) {
          if (province.get("ProvinceName") != null) {
            String provinceName = (String) province.get("ProvinceName");
            if (provinceName.toLowerCase().contains(provinceString)) {
              return getStringFromObject(province.get("ProvinceID"));
            }
          }
        }
      }
    }
    return "";
  }

  public String getDistrictId(String provinceId, String districtString) throws IOException {
    if (!Objects.equals(provinceId, "")) {
      URL url = new URL("https://online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=" +
          provinceId);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("Token", tokenGHN);
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("Accept", "application/json");
      connection.setUseCaches(false);
      connection.setDoInput(true);
      connection.setDoOutput(true);
      // OutputStream outStream = connection.getOutputStream();
      // OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream,
      // "UTF-8");
      // String jsonInputString =
      // "{\"from_district_id\": 1454,\"service_id\": 53320,\"service_type_id\":
      // null,\"to_district_id\": 1452,\"to_ward_code\": \"21012\",\"height\":
      // 50,\"length\": 20,\"weight\": 200,\"width\": 20,\"insurance_value\":
      // 10000,\"coupon\": null}";
      // try (OutputStream os = connection.getOutputStream()) {
      // byte[] input = jsonInputString.getBytes("utf-8");
      // os.write(input, 0, input.length);
      // }
      /// Get Response
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
          Gson gson = new Gson();
          Type resultType = new TypeToken<List<Map<String, Object>>>() {
          }.getType();
          List<Map<String, Object>> districtList = gson.fromJson(responseLine, resultType);
          districtString = getSimpleAddress(districtString);
          for (Map<String, Object> district : districtList) {
            if (district.get("DistrictName") != null) {
              String districtName = (String) district.get("DistrictName");
              if (districtName.toLowerCase().contains(districtString)) {
                return getStringFromObject(district.get("DistrictID"));
              }
            }
          }
        }
      }
    }
    return "";
  }

  public String getWardId(String districtId, String wardString) throws IOException {
    if (!Objects.equals(districtId, "")) {
      URL url = new URL("https://online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=" +
          districtId);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("Token", tokenGHN);
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("Accept", "application/json");
      connection.setUseCaches(false);
      connection.setDoInput(true);
      connection.setDoOutput(true);
      // OutputStream outStream = connection.getOutputStream();
      // OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream,
      // "UTF-8");
      // String jsonInputString =
      // "{\"from_district_id\": 1454,\"service_id\": 53320,\"service_type_id\":
      // null,\"to_district_id\": 1452,\"to_ward_code\": \"21012\",\"height\":
      // 50,\"length\": 20,\"weight\": 200,\"width\": 20,\"insurance_value\":
      // 10000,\"coupon\": null}";
      // try (OutputStream os = connection.getOutputStream()) {
      // byte[] input = jsonInputString.getBytes("utf-8");
      // os.write(input, 0, input.length);
      // }
      /// Get Response
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
          Gson gson = new Gson();
          Type resultType = new TypeToken<List<Map<String, Object>>>() {
          }.getType();
          List<Map<String, Object>> wardList = gson.fromJson(responseLine, resultType);
          wardString = getSimpleAddress(wardString);
          for (Map<String, Object> ward : wardList) {
            if (ward.get("WardName") != null) {
              String wardName = (String) ward.get("WardName");
              if (wardName.toLowerCase().contains(wardString)) {
                return getStringFromObject(ward.get("WardCode"));
              }
            }
          }
        }
      }
    }
    return "";
  }

  public String getSimpleAddress(String address) {
    if (address != null) {
      address = address.replaceAll(
          "(?i)(TP[.]|T[.]|thành phố|H[.]|quần đảo|huyện đảo|đảo|huyện|thị trấn|quận|P[.]|X[.]|thị xã|xã|phường)",
          "");
      address = address.replaceAll("\\s", " ");
      return address.trim().toLowerCase();
    }
    return "";
  }

  public String getStringFromObject(Object obj) {
    if (obj != null) {
      if (obj instanceof Double) {
        String number = Double.toString((Double) obj);
        int index = number.indexOf(".");
        if (index != -1) {
          number = number.substring(0, index);
        }
        return number;
      }
      return (String) obj;
    }
    return "";
  }
}
