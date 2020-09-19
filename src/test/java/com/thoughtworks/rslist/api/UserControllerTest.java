package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    ObjectMapper objectMapper;

    @BeforeEach
    void  setUp(){
        objectMapper = new ObjectMapper();
        //清理数据库
    }

    @Test
    @Order(1)
    public void should_register_user() throws Exception {
        User user = new User("xiaoli",  "male", 19,"a@b.com", "18888888888");
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void user_name_should_less_8() throws Exception {
        User user = new User("xiaolidddddd", "male", 19, "a@b.com", "18888888888");
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    @Order(3)
    public void age_should_between_18_and_100() throws Exception {
        User user = new User("xiaolid", "male", 15, "a@b.com", "18888888888");
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    @Order(4)
    public void email_should_suit_format() throws Exception {
        User user = new User("xiaohong", "male", 20, "ab.com", "18888888888");
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));

    }

    @Test
    @Order(5)
    public void should_register_user_fail() throws Exception {
        User user = new User("xiaoli",  "male", 19,"a@b.com", "18888888888");
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @Order(6)
    protected void should_get_user_by_id() throws Exception {
        mockMvc.perform(get("/user/1"))
                .andExpect(jsonPath("$.userName",is("xiaoli")))
                .andExpect(jsonPath("$.gender",is("male")))
                .andExpect(status().isOk());
    }

//    @Test
//    @Order(7)
//    protected void should_delete_user_by_id() throws Exception {
//        mockMvc.perform(delete("/user/5"))
//                .andExpect(status().isOk());
//    }

}