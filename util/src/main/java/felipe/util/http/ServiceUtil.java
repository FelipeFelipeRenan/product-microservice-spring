package felipe.util.http;

import org.springframework.stereotype.Component;

@Component
public class ServiceUtil {

    private final String port;
    public ServiceUtil(){
        this.port = "8080";
    }





    public String getServiceAddress(){
        return "Address: localhost";
    }
    
    public String getPort(){
        return this.port;
    }
}
