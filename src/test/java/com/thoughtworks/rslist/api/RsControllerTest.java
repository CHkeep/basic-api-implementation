package com.thoughtworks.rslist.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTest {

    @Autowired
    MockMvc mockMvc;
//
//    @Test
//    public void should_return_asList() throws Exception {
//        mockMvc.perform(get("/rs/list"))
//                .andExpect((ResultMatcher) jsonPath("$",hasSize(4)))
//                .andExpect(status().isOk());
//    }
    @Test
    public void should_get_rs_event_list() throws  Exception{
        mockMvc.perform(get("/rs/list"))
                .andExpect(content().string("[aaaa, bbbb, cccc]"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_one_rs_event_list() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(content().string("aaaa"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_some_rs_event_list() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(content().string("[aaaa, bbbb]"))
                .andExpect(status().isOk());
    }

}