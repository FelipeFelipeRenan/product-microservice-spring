package felipe.api.core.recommendation;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface RecommendationService {
    @GetMapping(value = "/recommendation/{recommendationId}" , produces = "application/json")
    List<Recommendation> getRecommendations(@PathVariable int recommendationId);
}
