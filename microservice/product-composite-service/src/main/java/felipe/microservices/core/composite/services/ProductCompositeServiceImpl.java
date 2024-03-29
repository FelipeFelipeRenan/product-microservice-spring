package felipe.microservices.core.composite.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import felipe.api.exceptions.NotFoundException;
import felipe.util.http.ServiceUtil;

@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService {

  private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeServiceImpl.class);

  private final ServiceUtil serviceUtil;
  private ProductCompositeIntegration integration;

  @Autowired
  public ProductCompositeServiceImpl(
      ServiceUtil serviceUtil, ProductCompositeIntegration integration) {

    this.serviceUtil = serviceUtil;
    this.integration = integration;
  }

  @Override
  public ProductAggregate getProduct(int productId) {

    Product product = integration.getProduct(productId);
    if (product == null) {
      throw new NotFoundException("No product found for productId: " + productId);
    }

    List<Recommendation> recommendations = integration.getRecommendations(productId);

    List<Review> reviews = integration.getReviews(productId);

    return createProductAggregate(product, recommendations, reviews, serviceUtil.getServiceAddress());
  }

  @Override
  public void createProduct(ProductAggregate body) {
    try {
      Product product = new Product(body.getProductId(), body.getName(), body.getWeight(), null);
      integration.createProduct(product);

      if (body.getRecommendations() != null) {
        body.getRecommendations().forEach(r -> {
          Recommendation recommendation = new Recommendation(body.getProductId(), r.getRecommendationId(),
              r.getAuthor(), r.getRate(), r.getContent(), null);
          integration.createRecommendations(recommendation);
        });
      }

      if (body.getReviews() != null) {
        body.getReviews().forEach(r -> {
          Review review = new Review(body.getProductId(), r.getReviewId(), r.getAuthor(), r.getSubject(),
              r.getContent(), null);
          integration.createReview(review);

        });
      }

    } catch (RuntimeException re) {
      LOG.warn("createCompositeProduct failed", re);
      throw re;
    }
  }

  public void deleteProduct(int productId) {
    integration.deleteProduct(productId);
    integration.deleteRecommendations(productId);
    integration.deleteReviews(productId);
    
  }

  private ProductAggregate createProductAggregate(
      Product product,
      List<Recommendation> recommendations,
      List<Review> reviews,
      String serviceAddress) {

    // 1. Setup product info
    int productId = product.getProductId();
    String name = product.getName();
    int weight = product.getWeight();

    // 2. Copy summary recommendation info, if available
    List<RecommendationSummary> recommendationSummaries = (recommendations == null) ? null
        : recommendations.stream()
            .map(r -> new RecommendationSummary(r.getRecommendationId(), r.getAuthor(), r.getRate(), r.getContent()))
            .collect(Collectors.toList());

    // 3. Copy summary review info, if available
    List<ReviewSummary> reviewSummaries = (reviews == null) ? null
        : reviews.stream()
            .map(r -> new ReviewSummary(r.getReviewId(), r.getAuthor(), r.getSubject(), r.getContent()))
            .collect(Collectors.toList());

    // 4. Create info regarding the involved microservices addresses
    String productAddress = product.getServiceAddress();
    String reviewAddress = (reviews != null && reviews.size() > 0) ? reviews.get(0).getServiceAddress() : "";
    String recommendationAddress = (recommendations != null && recommendations.size() > 0)
        ? recommendations.get(0).getServiceAddress()
        : "";
    ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress, productAddress, reviewAddress,
        recommendationAddress);

    return new ProductAggregate(productId, name, weight, recommendationSummaries, reviewSummaries, serviceAddresses);
  }
}