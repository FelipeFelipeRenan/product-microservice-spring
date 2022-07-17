package felipe.microservices.core.product.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import felipe.api.core.product.ProductService;
import felipe.api.exceptions.InvalidInputException;
import felipe.api.exceptions.NotFoundException;
import felipe.util.http.ServiceUtil;
import felipe.api.core.product.Product;

@RestController
public class ProductServiceImpl implements ProductService{
    private final ServiceUtil serviceUtil;

    @Autowired    
    public ProductServiceImpl(ServiceUtil serviceUtil){
        this.serviceUtil = serviceUtil;
    }


    @Override
    public Product getProduct(int productId) {

        if(productId < 1){
            throw new InvalidInputException("Invalid productId: " + productId);

        }

        if(productId == 12){
            throw new NotFoundException("No product found for productId: " + productId);
        }

        return new Product(productId, "name-" + productId, 123, serviceUtil.getServiceAddress());
    }
    
}