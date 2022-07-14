package main.java.felipe.api.core.review;

public interface ReviewService {
    @GetMapping(value = "/review/{reviewId}" , produces = "application/json")
    Product getReview(@PathVariable int reviewId);
}
