package main.java.felipe.api.core.recommendation;

public interface RecommendationService {
    @GetMapping(value = "/recommendation/{recommendationId}" , produces = "application/json")
    Product getRecommendation(@PathVariable int recommendationId);
}
