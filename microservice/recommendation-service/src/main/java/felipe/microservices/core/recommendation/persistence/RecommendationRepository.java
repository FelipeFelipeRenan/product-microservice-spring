package felipe.microservices.core.recommendation.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;

public interface RecommendationRepository extends ReactiveCrudRepository<RecommendationEntity, String> {
  Mono<RecommendationEntity> findByProductId(int productId);
}