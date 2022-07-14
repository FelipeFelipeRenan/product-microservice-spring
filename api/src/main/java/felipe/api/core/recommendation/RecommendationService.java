package main.java.felipe.api.core.recommendation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface RecommendationService {
    @GetMapping(value = "/recommendation/{recommendationId}" , produces = "application/json")
    Recommendation getRecommendation(@PathVariable int recommendationId);
}
