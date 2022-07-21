package felipe.util.http;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceUtil {

    private final String port;
    private String serviceAddress = null;

    @Autowired
    public ServiceUtil(@Value("${server.port}") String port){
        this.port = port;
    }
    public String getServiceAddress(){

        if(this.serviceAddress == null){
            serviceAddress = findMyHostname() + "/" + findMyIpAddress() + ":" + port;
        }
        return serviceAddress;
    }
    
    public String findMyHostname(){
        try{
            return InetAddress.getLocalHost().getHostName();
        } catch(UnknownHostException e){
            return "Unknown host name";
        }
    } 

    public String findMyIpAddress(){
        try{
            return InetAddress.getLocalHost().getHostAddress();
        } catch(UnknownHostException e){
            return "Unknown IP address";
        }
    }
    
    public String getPort(){
        return this.port;
    }
}
