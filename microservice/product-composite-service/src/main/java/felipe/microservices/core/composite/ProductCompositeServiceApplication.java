package felipe.microservices.core.composite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import io.swagger.v3.oas.models.OpenAPI;


@SpringBootApplication
@ComponentScan("felipe")
public class ProductCompositeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductCompositeServiceApplication.class, args);
	}

	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Bean
	public OpenAPI getOpenApiDocumentation{
		return new OpenAPI();
	}
	
	



	
}
