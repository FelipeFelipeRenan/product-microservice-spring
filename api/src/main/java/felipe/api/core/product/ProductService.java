package main.java.felipe.api.core.product;

public interface ProductService {
    @GetMapping(value = "/product/{productId}" , produces = "application/json")
    Product getProduct(@PathVariable int productId);
}
