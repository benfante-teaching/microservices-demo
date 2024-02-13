package com.example.demoweb.controller;

import com.example.demoweb.model.Person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@Controller
public class PeopleController {

    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CircuitBreakerFactory<?,?> circuitBreakerFactory;
    
    @GetMapping
    public String list(Model model) {
        CircuitBreaker cb = circuitBreakerFactory.create("peopleServiceCB");

        Person[] people = 
            cb.run(() -> {
                // InstanceInfo peopleService = eurekaClient.getNextServerFromEureka("DEMOPERSON", false);
                // String peopleServiceUrl = peopleService.getHomePageUrl();
                // model.addAttribute("peopleServiceUrl", peopleServiceUrl);
                // return restTemplate.getForObject(peopleServiceUrl+"/api/v1/people", Person[].class);

                return restTemplate.getForObject("http://DEMOPERSON/api/v1/people", Person[].class);
            },
            (throwable) -> {
                model.addAttribute("errorMessage", "Il servizio People Service non è disponibile");
                return new Person[]{};
            }
            );

        model.addAttribute("people", people);

        return "people/list";
    }
}