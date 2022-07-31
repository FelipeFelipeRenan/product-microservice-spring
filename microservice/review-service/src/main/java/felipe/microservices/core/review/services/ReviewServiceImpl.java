package felipe.microservices.core.review.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;

import felipe.api.core.review.Review;
import felipe.api.core.review.ReviewService;
import felipe.api.exceptions.InvalidInputException;
import felipe.microservices.core.review.persistence.ReviewEntity;
import felipe.microservices.core.review.persistence.ReviewRepository;
import felipe.util.http.ServiceUtil;

@RestController
public class ReviewServiceImpl implements ReviewService {

  private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);

  private final ServiceUtil serviceUtil;

  private final ReviewRepository repository;

  private final ReviewMapper mapper;

  @Autowired
  public ReviewServiceImpl(ReviewRepository repository, ServiceUtil serviceUtil, ReviewMapper mapper) {
    this.repository = repository;
    this.serviceUtil = serviceUtil;
    this.mapper = mapper;
  }

  @Override
  public List<Review> getReviews(int productId) {

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    List<ReviewEntity> entityList = repository.findByProductId(productId);
    List<Review> list = mapper.entityListToApiList(entityList);
    list.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

    return list;
  }

  @Override
  public Review createReview(Review body) {
    try {
      ReviewEntity entity = mapper.apiToEntity(body);
      ReviewEntity newEntity = repository.save(entity);

      LOG.debug("createReview: created a review entity: {}/{}", body.getProductId(), body.getReviewId());

      return mapper.entityToApi(newEntity);
    } catch (DataIntegrityViolationException dive) {

      throw new InvalidInputException(
          "Duplicate key, Product Id: " + body.getProductId() + ", Review Id:" + body.getReviewId());

    }

  }

  @Override
  public void deleteReviews(int productId) {
    LOG.debug("deleteReviews: tries to delete reviews for the product with productId: {}", productId);
    repository.deleteAll(repository.findByProductId(productId));
  }

}