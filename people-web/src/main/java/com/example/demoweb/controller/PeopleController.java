package com.example.demoweb.controller;

import com.example.demoweb.model.Person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@SuppressWarnings("unused")
@Controller
@RequestMapping("/people")
public class PeopleController {

    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CircuitBreakerFactory<?,?> circuitBreakerFactory;
    
    @GetMapping
    public String list(@RequestParam(name = "query", defaultValue = "") String query, Model model) {
        CircuitBreaker cb = circuitBreakerFactory.create("peopleServiceCB");

        Person[] people = 
            cb.run(() -> {
                /* *** Uncomment this block to directly use Eureka client to get the service URL *** */
                // InstanceInfo peopleService = eurekaClient.getNextServerFromEureka("DEMOPERSON", false);
                // String peopleServiceUrl = peopleService.getHomePageUrl();
                // model.addAttribute("peopleServiceUrl", peopleServiceUrl);
                // return restTemplate.getForObject(peopleServiceUrl+"/api/v1/people", Person[].class);

                /* *** Comment this block if you want to directly use Eureka client to get the service URL *** */
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://DEMOPERSON/api/v1/people")
                    .queryParam("q", query);
                return restTemplate.getForObject(builder.toUriString(), Person[].class);
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