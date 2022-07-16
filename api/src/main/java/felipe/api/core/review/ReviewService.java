package felipe.api.core.review;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ReviewService {
    @GetMapping(value = "/review/{reviewId}" , produces = "application/json")
    List<Review> getReview(@PathVariable int reviewId);
}
