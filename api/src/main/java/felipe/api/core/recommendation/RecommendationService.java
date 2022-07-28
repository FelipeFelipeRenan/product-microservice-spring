package felipe.api.core.recommendation;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public interface RecommendationService {

  @GetMapping(value = "/recommendation", produces = "application/json")
  List<Recommendation> getRecommendations(
      @RequestParam(value = "productId", required = true) int productId);


  @PostMapping(
    value = "/recommendation",
    consumes = "application/json",
    produces = "application/json"
  )
  Recommendation createRecommendations(@RequestBody Recommendation body);

  @DeleteMapping(value = "/recommendation")
  void deleteRecommendations(@RequestParam(value = "productId", required = true) int productId);

}