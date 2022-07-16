package felipe.microservices.core.composite.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import felipe.api.composite.product.ProductAggregate;
import felipe.api.composite.product.ProductCompositeService;
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



    
    

    
}
