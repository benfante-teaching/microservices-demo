package com.example.demoweb.controller;

import com.example.demoweb.model.Person;
import com.example.demoweb.model.Result;
import com.example.demoweb.service.PeopleService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@SuppressWarnings("unused")
@Controller
@RequestMapping("/people")
public class PeopleController {
    @Autowired
    private PeopleService peopleService;
    
    @GetMapping
    public String list(@RequestParam(name = "query", defaultValue = "") String query, Model model) {
        Result<Iterable<Person>> people = peopleService.findPeople(query);
        if (people.code() == PeopleService.CODE_SERVICE_NOT_AVAILABLE) {
            model.addAttribute("errorMessage", people.messages()[0]);
        }
        model.addAttribute("people", people.data());
        return "people/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable(name = "id") UUID id, Model model) {
        Result<Person> person = peopleService.findPerson(id);
        if (person.code() == PeopleService.CODE_SERVICE_NOT_AVAILABLE) {
            model.addAttribute("errorMessage", person.messages()[0]);
        }
        model.addAttribute("person", person.data());
        return "people/detail";
    }

    @GetMapping("/new")
    public String newTodo(Model model) {
        model.addAttribute("person", new Person());
        return "people/form";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") UUID id, Model model) {
        Result<Person> person = peopleService.findPerson(id);
        if (person.data() == null) {
            return "redirect:/people";
        }
        model.addAttribute("person", person.data());
        return "people/form";
    }

    @PostMapping()
    public String save(@ModelAttribute("person") Person person, RedirectAttributes redirectAttributes) {
        Result<Person> result = peopleService.save(person);
        if (result.code() != PeopleService.CODE_SUCCESS) {
            redirectAttributes.addFlashAttribute("errorMessage", result.messages()[0]);
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "Person saved successfully");
        }
        return "redirect:/people";
    }
    

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        Result<Person> result = peopleService.delete(id);
        if (result.code() != PeopleService.CODE_SUCCESS) {
            redirectAttributes.addFlashAttribute("errorMessage", result.messages()[0]);
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "Person deleted successfully");
        }
        return "redirect:/people";
    }

}