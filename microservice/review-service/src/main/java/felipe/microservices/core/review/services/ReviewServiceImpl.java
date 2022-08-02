package felipe.microservices.core.review.services;

import static java.util.logging.Level.FINE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;

import felipe.api.core.review.Review;
import felipe.api.core.review.ReviewService;
import felipe.api.exceptions.InvalidInputException;
import felipe.microservices.core.review.persistence.ReviewEntity;
import felipe.microservices.core.review.persistence.ReviewRepository;
import felipe.util.http.ServiceUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@RestController
public class ReviewServiceImpl implements ReviewService {

  private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);

  private final ServiceUtil serviceUtil;

  private final ReviewRepository repository;

  private final ReviewMapper mapper;

  private final Scheduler jdbcScheduler;

  @Autowired
  public ReviewServiceImpl(
      @Qualifier("jdbcScheduler") Scheduler jdbcScheduler,
      ReviewRepository repository, ServiceUtil serviceUtil, ReviewMapper mapper) {
    this.jdbcScheduler = jdbcScheduler;
    this.repository = repository;
    this.serviceUtil = serviceUtil;
    this.mapper = mapper;
  }

  @Override
  public Flux<Review> getReviews(int productId) {

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }
    LOG.info("Will get reviews for product with id={}", productId);

    return Mono.fromCallable(() -> internalGetReviews(productId))
        .flatMapMany(Flux::fromIterable)
        .log(LOG.getName(), FINE)
        .subscribeOn(jdbcScheduler);
  }

  @Override
  public Mono<Review> createReview(Review body) {
    if (body.getProductId() < 1) {
      throw new InvalidInputException("Invalid productId: " + body.getProductId());
    }
    return Mono.fromCallable(() -> internalCreateReview(body))
        .subscribeOn(jdbcScheduler);
  }

  @Override
  public Mono<Void> deleteReviews(int productId) {
    if(productId < 1){
      throw new InvalidInputException("Invalid productId: " + productId);

    }

    return Mono.fromRunnable(() -> internalDeleteReviews(productId)).subscribeOn(jdbcScheduler).then();
  }

  private List<Review> internalGetReviews(int productId){
    List<ReviewEntity> entityList = repository.findByProductId(productId);
    List<Review> list = mapper.entityListToApiList(entityList);
    list.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

    LOG.debug("Response size: {}", list.size());

    return list;
 
  }

  private Review internalCreateReview(Review body) {
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

  private void internalDeleteReviews(int productId){
    LOG.debug("deleteReviews: tries to delete reviews for the product with productId: {}", productId);
    repository.deleteAll(repository.findByProductId(productId));
  }

}