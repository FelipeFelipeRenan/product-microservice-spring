package felipe.microservices.core.composite.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import felipe.api.core.product.Product;
import felipe.api.core.product.ProductService;
import felipe.api.core.recommendation.Recommendation;
import felipe.api.core.recommendation.RecommendationService;
import felipe.api.core.review.Review;
import felipe.api.core.review.ReviewService;


@Component
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService{

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String productServiceUrl;
    private final String recommendationServiceUrl;
    private final String reviewServiceUrl;

    @Autowired
    public ProductCompositeIntegration(
        RestTemplate restTemplate,
        ObjectMapper mapper,

        @Value("${app.product-service.host}")
        String productServiceHost,

        @Value("${app.product-service.port}")
        int productServicePort,

        @Value("${app.recommendation-service.host}")
        String recommendationServiceHost,

        @Value("${app.recommendation-service.port}")
        int recommendationServicePort,

        @Value("${app.review-service.host}")
        String reviewServiceHost,

        @Value("${app.review-service.port}")
        int reviewServicePort
    ){

        this.restTemplate = restTemplate;
        this.mapper = mapper;

        this.productServiceUrl = "http://" + productServiceHost + ":" + 
        Integer.toString(productServicePort) + "/product/";

        this.recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + 
        Integer.toString(recommendationServicePort) + "/recommendation?productId=";

        this.reviewServiceUrl = "http://" + reviewServiceHost + ":" +
        Integer.toString(reviewServicePort) + "/review?productId=";

    }

    @Override
    public Product getProduct(int productId){
        
        String url = productServiceUrl + Integer.toString(productId);
        Product product = restTemplate.getForObject(url, Product.class);
        
        return product;

    
    }

    @Override
    public List<Recommendation> getRecommendations(int productId){

        String url = recommendationServiceUrl + Integer.toString(productId);

        List<Recommendation> recommendations = restTemplate.exchange(url, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<Recommendation>>() {}).getBody();

        return recommendations;
    }

    @Override
    public List<Review> getReview(int productId){
        
        String url = reviewServiceUrl + Integer.toString(productId);
        List<Review> reviews = restTemplate.exchange(url, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<Review>>(){}).getBody();

        return reviews;
    }

    
}