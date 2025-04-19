package com.example.demo.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.TestDemoApplication;

@SpringBootTest
@Import(TestDemoApplication.class)
@Transactional
@AutoConfigureMockMvc(addFilters = false)
public class PeopleControllerTest {
    private static final String BASE_PATH = "/api/v1/people";
    @Autowired
    private MockMvc mvc;

    @Test
    void testGetByUuid() throws Exception {
        String uuid = "00000000-0000-0000-0000-000000000003";
        mvc.perform(get(BASE_PATH + "/{uuid}", uuid)).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Carlo")))
                .andExpect(jsonPath("$.lastName", is("Neri")))
                .andExpect(jsonPath("$.uuid", is(uuid))).andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void testNotFound() throws Exception {
        String uuid = "00000000-0000-0000-0000-000000000999";
        mvc.perform(get(BASE_PATH + "/{uuid}", uuid)).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title", is("Not Found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void testListAllPerson() throws Exception {
        mvc.perform(get(BASE_PATH)).andExpect(status().isOk()).andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void testSearchForMario() throws Exception {
        mvc.perform(get(BASE_PATH).param("q", "Mario")).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("Mario")))
                .andExpect(jsonPath("$[1].firstName", is("Mario")))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void testAddPerson() throws Exception {
        mvc.perform(post(BASE_PATH).contentType(MediaType.APPLICATION_JSON).content("""
                {
                   "firstName": "New first name",
                   "lastName": "New last name"
                }
                """)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("New first name")))
                .andExpect(jsonPath("$.lastName", is("New last name")))
                .andExpect(jsonPath("$.uuid").isNotEmpty())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

}
