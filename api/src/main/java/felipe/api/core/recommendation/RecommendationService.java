package felipe.api.core.recommendation;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecommendationService {


  Mono<Recommendation> createRecommendation(@RequestBody Recommendation body);

  /**
   * Sample usage: "curl $HOST:$PORT/recommendation?productId=1".
   *
   * @param productId Id of the product
   * @return the recommendations of the product
   */
  @GetMapping(
    value = "/recommendation",
    produces = "application/json")
  Flux<Recommendation> getRecommendations(
    @RequestParam(value = "productId", required = true) int productId);

  /**
   * Sample usage: "curl -X DELETE $HOST:$PORT/recommendation?productId=1".
   *
   * @param productId Id of the product
   */

  Mono<Void> deleteRecommendations(int productId);
}