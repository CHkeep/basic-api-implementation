package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    ObjectMapper objectMapper;

    @BeforeEach
    void  setUp(){
        //现场清理
        userRepository.deleteAll();
        rsEventRepository.deleteAll();

        objectMapper = new ObjectMapper();
        UserPO userPO= UserPO.builder().userName("xiaochen").age(18).gender("female").phone("10000000001").voteNum(10).build();
        userRepository.save(userPO);
    }

    @Test
    @Order(1)
    public void should_register_user() throws Exception {
        User user = new User("lizi",  "male", 19,"a@b.com", "10000000002",10);
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void user_name_should_less_8() throws Exception {
        User user = new User("xiaolidddddd", "male", 19, "a@b.com", "18888888888",10);
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    @Order(3)
    public void age_should_between_18_and_100() throws Exception {
        User user = new User("xiaolid", "male", 15, "a@b.com", "18888888888",10);
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    @Order(4)
    public void email_should_suit_format() throws Exception {
        User user = new User("xiaohong", "male", 20, "ab.com", "18888888888",10);
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));

    }

    @Test
    @Order(5)
    public void should_register_user_fail() throws Exception {
        User user = new User("xiaoli",  "male", 19,"a@b.com", "18888888888",10);
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @Order(6)
    protected void should_get_user_by_id() throws Exception {
        UserPO userPO= UserPO.builder().userName("yangyu").age(18).gender("female").phone("19966999999").voteNum(10).build();
        userRepository.save(userPO);
        RsEventPO rsEventPO = RsEventPO.builder().eventName("股票").keyWords("金融").userPO(userPO).build();
        rsEventRepository.save(rsEventPO);
        mockMvc.perform(get("/user/{id}",userPO.getId()))
                .andExpect(jsonPath("$.userName",is(userRepository.findById(userPO.getId()).get().getUserName())))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    protected void should_delete_user_by_id() throws Exception {
        UserPO userPO= UserPO.builder().userName("xiaoyu").age(18).gender("female").phone("19366999999").voteNum(10).build();
        userRepository.save(userPO);
        RsEventPO rsEventPO1 = RsEventPO.builder().eventName("xiao").keyWords("金融1").userPO(userPO).build();
        rsEventRepository.save(rsEventPO1);
        RsEventPO rsEventPO = RsEventPO.builder().eventName("xiao").keyWords("金融").userPO(userPO).build();
        rsEventRepository.save(rsEventPO);
        mockMvc.perform(delete("/user/{id}", userPO.getId()))
                .andExpect(status().isOk());
        assertEquals(Optional.empty(), userRepository.findById(userPO.getId()));
    }

}