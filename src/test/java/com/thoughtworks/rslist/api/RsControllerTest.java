package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.awt.*;
import java.util.logging.Handler;


import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RsControllerTest {

//    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(new RsController()).build();
    }

    @Test
//    @Order(1)
    public void should_get_one_rs_event_list() throws Exception {
        System.out.println(1);
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyWords", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyWords", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyWords", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/4"))
                .andExpect(jsonPath("$.eventName", is("第四条事件")))
                .andExpect(jsonPath("$.keyWords", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
//    @Order(2)
    public void should_get_some_rs_event_list() throws Exception {
        System.out.println(2);
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWords", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWords", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWords", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWords", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=3&end=4"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[0].keyWords", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第四条事件")))
                .andExpect(jsonPath("$[1].keyWords", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWords", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWords", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWords", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end=4"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWords", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWords", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第四条事件")))
                .andExpect(jsonPath("$[2].keyWords", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
//    @Order(3)
    public void should_add_rs_event() throws Exception {
        System.out.println(3);
        User user = new User("xiaochen", 18, "male", "a@b.com", "18888888888");
        RsEvent rsEvent = new RsEvent( "猪肉涨价了","经济", user);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("index","4"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWords", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWords", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWords", is("无标签")))
                .andExpect(jsonPath("$[3].eventName", is("第四条事件")))
                .andExpect(jsonPath("$[3].keyWords", is("无标签")))
                .andExpect(jsonPath("$[4].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[4].keyWords", is("经济")))
                .andExpect(status().isOk());

    }


    @Test
//    @Order(4)
    public void should_delete_rs_event() throws Exception {
        System.out.println(4);
        mockMvc.perform(delete("/rd/4"))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWords", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWords", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWords", is("无标签")))
                .andExpect(status().isOk());

    }

    @Test
//    @Order(5)
    public void should_put_event_name_rs_event() throws Exception {
        System.out.println(5);
        User user = new User("xiaoli", 19, "male", "a@b.com", "18888888888");
        RsEvent rsEvent = new RsEvent( "第二条新闻","", user);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/re/put/2").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWords", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条新闻")))
                .andExpect(jsonPath("$[1].keyWords", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWords", is("无标签")))
                .andExpect(jsonPath("$[3].eventName", is("第四条事件")))
                .andExpect(jsonPath("$[3].keyWords", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
//    @Order(6)
    public void should_put_key_words_rs_event() throws Exception {
        System.out.println(6);
        User user = new User("xiaoli", 19, "male", "a@b.com", "18888888888");
        RsEvent rsEvent = new RsEvent( "","价格", user);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/re/put/2").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWords", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWords", is("价格")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWords", is("无标签")))
                .andExpect(jsonPath("$[3].eventName", is("第四条事件")))
                .andExpect(jsonPath("$[3].keyWords", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
//    @Order(7)
    public void should_put_rs_event() throws Exception {
        System.out.println(7);
        User user = new User("xiaoli", 19, "male", "a@b.com", "18888888888");
        RsEvent rsEvent = new RsEvent( "股票跌了","金融", user);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/re/put/2").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWords", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("股票跌了")))
                .andExpect(jsonPath("$[1].keyWords", is("金融")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWords", is("无标签")))
                .andExpect(jsonPath("$[3].eventName", is("第四条事件")))
                .andExpect(jsonPath("$[3].keyWords", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_throw_rs_event_not_valid_exception() throws Exception {
        mockMvc.perform(get("/rs/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));

    }

    @Test
    public void should_throw_rs_event_not_valid_param_exception() throws Exception{
        mockMvc.perform(get("/rs/list?start=0&end=4"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));

    }


    @Test
    public void should_throw_rs_event_not_valid_rs_event() throws Exception {
        User user = new User("xiaozheng", 15, "male", "c@b.com", "16888888888");
        RsEvent rsEvent = new RsEvent( "股票跌了","经济", user);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));


    }

}