package ec.edu.espe.gpr.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
  private static final int CONNECT_TIMEOUT = 5_000;
  private static final int READ_TIMEOUT = 5_000;

  /*@Bean
  public RestTemplate restTemplate() {
    return new RestTemplate(getClientHttpRequestFactory());
  }*/
  @Bean
public RestTemplate restTemplate(RestTemplateBuilder builder) {
   // Do any additional configuration here
   return builder.build();
}

  // private ClientHttpRequestFactory getClientHttpRequestFactory() {
  //   HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
  //       new HttpComponentsClientHttpRequestFactory();

  //   clientHttpRequestFactory.setConnectTimeout(CONNECT_TIMEOUT);
  //   clientHttpRequestFactory.setReadTimeout(READ_TIMEOUT);

  //   return clientHttpRequestFactory;
  // }
}
