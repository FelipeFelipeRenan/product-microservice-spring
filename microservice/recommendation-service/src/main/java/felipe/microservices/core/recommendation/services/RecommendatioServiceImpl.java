package felipe.microservices.core.recommendation.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;

import felipe.api.core.recommendation.Recommendation;
import felipe.api.core.recommendation.RecommendationService;
import felipe.api.exceptions.InvalidInputException;
import felipe.microservices.core.recommendation.persistence.RecommendationEntity;
import felipe.microservices.core.recommendation.persistence.RecommendationRepository;
import felipe.util.http.ServiceUtil;

@RestController
public class RecommendatioServiceImpl implements RecommendationService {

  private static final Logger LOG = LoggerFactory.getLogger(RecommendationService.class);

  private final RecommendationRepository repository;

  private final RecommendationMapper mapper;

  private final ServiceUtil serviceUtil;

  @Autowired
  public RecommendatioServiceImpl(RecommendationRepository repository, ServiceUtil serviceUtil,
      RecommendationMapper mapper) {
    this.repository = repository;
    this.serviceUtil = serviceUtil;
    this.mapper = mapper;
  }

  @Override
  public List<Recommendation> getRecommendations(int productId) {

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    List<RecommendationEntity> entityList = repository.findByProductId(productId);
    List<Recommendation> list = mapper.entityListToApiList(entityList);
    list.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

    return list;
  }

  @Override
  public Recommendation createRecommendations(Recommendation body) {
    try {
      RecommendationEntity entity = mapper.apiToEntity(body);
      RecommendationEntity newEntity = repository.save(entity);

      LOG.debug("createRecommendation: created a recommendation entity: {}/{}", body.getProductId(),
          body.getRecommendationId());

      return mapper.entityToApi(newEntity);
    } catch (DuplicateKeyException dke) {
      throw new InvalidInputException(
          "Duplicate key, Product Id: " + body.getProductId() + ", Recommendation Id:" + body.getRecommendationId());
    }
  }

  @Override
  public void deleteRecommendations(int productId) {
    LOG.debug("deleteRecommendations: tries to delete recommendations for the product with productId: {}", productId);
    repository.deleteAll(repository.findByProductId(productId));
  }

}
