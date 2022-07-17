package felipe.microservices.core.composite.services;

import java.util.List;              
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import felipe.api.composite.product.ProductAggregate;
import felipe.api.composite.product.ProductCompositeService;
import felipe.api.composite.product.RecommendationSummary;
import felipe.api.composite.product.ReviewSummary;
import felipe.api.composite.product.ServiceAddresses;
import felipe.api.core.product.Product;
import felipe.api.core.recommendation.Recommendation;
import felipe.api.core.review.Review;
import felipe.util.http.ServiceUtil;

@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService{

    private final ServiceUtil serviceUtil;
    private ProductCompositeIntegration integration;
    
    @Autowired
    public ProductCompositeServiceImpl(ServiceUtil serviceUtil, ProductCompositeIntegration integration) {
        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }

    @Override
    public ProductAggregate getProduct(int productId){

        Product product = integration.getProduct(productId);
        List<Recommendation> recommendations = integration.getRecommendations(productId);
        List<Review> reviews = integration.getReview(productId);
        
        return createProductAggregate(product, recommendations,
        reviews, serviceUtil.getServiceAddress());

    }

    private ProductAggregate createProductAggregate(
        Product product,
        List<Recommendation> recommendations,
        List<Review> reviews,
        String serviceAddress
    ){

        int productId = product.getProductId();
        String name = product.getName();
        int weight = product.getWeight();

        List<RecommendationSummary> recommendationSummaries =
        (recommendations == null)? null : recommendations.stream()
            .map(r -> new RecommendationSummary(r.getRecommendationId(), r.getAuthor(), r.getRate()))
            .collect(Collectors.toList());

        List<ReviewSummary> reviewsSummaries = 
        (reviews == null)? null: reviews.stream()
            .map(r -> new ReviewSummary(r.getReviewId(), r.getAuthor(), r.getSubject()))
            .collect(Collectors.toList());
        
        String productAddress = product.getServiceAddress();
        String reviewAddress = (reviews != null && reviews.size() > 0)? reviews.get(0).getServiceAddress() : "";
        String recommendationAddress = (recommendations != null && recommendations.size() > 0)? recommendations.get(0).getServiceAddress() : "";

        ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress, productAddress, reviewAddress, recommendationAddress);

        return new ProductAggregate(productId, name, weight, recommendationSummaries, reviewsSummaries, serviceAddresses);


    }


    
    

    
}
