package gt.electronic.ecommerce;

import gt.electronic.ecommerce.config.AppProperties;
import gt.electronic.ecommerce.utils.InitData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@EnableScheduling
// @Import(EmbeddedTomcatConfiguration.class)
public class GtBackendApplication implements CommandLineRunner {
  private final Logger LOGGER = LoggerFactory.getLogger(GtBackendApplication.class);
  private InitData initData;

  @Autowired public void InitData(InitData initData) {
    this.initData = initData;
  }

  @Value("${spring.jpa.hibernate.ddl-auto}")
  private String hibernate_ddl;

  public static void main(String[] args) {
    SpringApplication.run(GtBackendApplication.class, args);
  }

  @Override
  public void run(String... args) {
//    initData.Init();
  }
}