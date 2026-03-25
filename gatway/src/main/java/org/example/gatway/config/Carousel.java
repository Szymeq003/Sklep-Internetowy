package org.example.gatway.config;


import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class Carousel {

    private final EurekaClient eurekaClient;
    List<InstanceInfo> instances = new ArrayList<>();
    int currentIndex = 0;

    public Carousel(EurekaClient eurekaClient){
        this.eurekaClient = eurekaClient;
        initAuthCarousel();
        events();
    }

    public String getUriAuth(){
        if (instances.isEmpty()) {
            initAuthCarousel();
            if (instances.isEmpty()) {
                throw new IllegalStateException("Brak połączonych instancji AUTH-SERVICE w Eureka!");
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        InstanceInfo instance = instances.get(currentIndex);
        stringBuilder.append(instance.getIPAddr()).append(":").append(instance.getPort());
        if (instances.size()-1 == currentIndex){
            currentIndex = 0;
        }else {
            currentIndex++;
        }
        return stringBuilder.toString();
    }

    private void events(){
        eurekaClient.registerEventListener(eurekaEvent -> {
            initAuthCarousel();
        });
        eurekaClient.unregisterEventListener(eurekaEvent -> {
            initAuthCarousel();
        });
    }

    private void initAuthCarousel() {
        com.netflix.discovery.shared.Application app = eurekaClient.getApplication("AUTH-SERVICE");
        if (app != null) {
            instances = app.getInstances();
        } else {
            instances = new ArrayList<>();
        }
    }
}
