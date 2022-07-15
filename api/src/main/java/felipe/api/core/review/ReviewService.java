package felipe.api.core.review;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ReviewService {
    @GetMapping(value = "/review/{reviewId}" , produces = "application/json")
    Review getReview(@PathVariable int reviewId);
}
