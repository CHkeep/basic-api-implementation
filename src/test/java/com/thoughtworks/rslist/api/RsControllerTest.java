package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static java.lang.Integer.valueOf;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RsControllerTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventService rsEventService;
    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

//    @BeforeEach
//    public void init() {
//           mockMvc = MockMvcBuilders.standaloneSetup(new RsController()).build();
//
//    }
        @BeforeEach
        void  setUp(){
            objectMapper = new ObjectMapper();
            //清理数据库
            userRepository.deleteAll();
            userRepository.deleteAll();
        }

    @Test
    @Order(1)
    public void should_get_one_rs_event_list() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void should_get_some_rs_event_list() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=3&end=4"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end=4"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void should_add_rs_event_when_user_Id_exist() throws Exception {
        UserPO userPO= UserPO.builder().userName("xiaoyu").age(18).gender("female").phone("19366999999").voteNum(10).build();
        userRepository.save(userPO);
        RsEvent rsEvent = new RsEvent("zhurouzhangjiang","经济",userPO.getId());
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        System.out.println(jsonString);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(4)
    public void should_add_rs_event_when_user_Id_Not_exist() throws Exception {
        RsEvent rsEvent = new RsEvent("zhurouzhangjiang","经济",10000);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        System.out.println(jsonString);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    public void should_delete_rs_event() throws Exception {
        mockMvc.perform(delete("/rd/3"))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    public void should_patch_event_rs_event() throws Exception {
        UserPO userPO= UserPO.builder().userName("yangli").age(18).email("a@b.com").gender("female").phone("19966999999").voteNum(10).build();
        userRepository.save(userPO);
        RsEventPO rsEventPO = RsEventPO.builder().eventName("股票").keyWords("金融").userPO(userPO).build();
        rsEventRepository.save(rsEventPO);
        RsEvent rsEvent = RsEvent.builder().eventName("电影").keyWords("票").userId(userPO.getId()).build();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/{rsEventId}",rsEventPO.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("电影",rsEventRepository.findById(rsEventPO.getId()).get().getEventName());
        assertEquals("票",rsEventRepository.findById(rsEventPO.getId()).get().getKeyWords());
    }

    @Test
    @Order(7)
    public void should_patch_event_name_rs_event() throws Exception {
        UserPO userPO= UserPO.builder().userName("yangli").age(18).email("a@b.com").gender("female").phone("19966999999").voteNum(10).build();
        userRepository.save(userPO);
        RsEventPO rsEventPO = RsEventPO.builder().eventName("股票").keyWords("金融").userPO(userPO).build();
        rsEventRepository.save(rsEventPO);
        RsEvent rsEvent = RsEvent.builder().eventName("电影").userId(userPO.getId()).build();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/{rsEventId}",rsEventPO.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("电影",rsEventRepository.findById(rsEventPO.getId()).get().getEventName());
        assertEquals("金融",rsEventRepository.findById(rsEventPO.getId()).get().getKeyWords());

    }

    @Test
    @Order(8)
    public void should_patch_key_words_rs_event() throws Exception {
        UserPO userPO= UserPO.builder().userName("yangli").age(18).email("a@b.com").gender("female").phone("19966999999").voteNum(10).build();
        userRepository.save(userPO);
        RsEventPO rsEventPO = RsEventPO.builder().eventName("股票").keyWords("金融").userPO(userPO).build();
        rsEventRepository.save(rsEventPO);
        RsEvent rsEvent = RsEvent.builder().keyWords("文化").userId(userPO.getId()).build();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/{rsEventId}",rsEventPO.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("股票",rsEventRepository.findById(rsEventPO.getId()).get().getEventName());
        assertEquals("文化",rsEventRepository.findById(rsEventPO.getId()).get().getKeyWords());

    }

    @Test
    @Order(9)
    public void should_patch_error_user_id() throws Exception {
        UserPO userPO= UserPO.builder().userName("yangli").age(18).email("a@b.com").gender("female").phone("19966999999").voteNum(10).build();
        userRepository.save(userPO);
        RsEventPO rsEventPO = RsEventPO.builder().eventName("股票").keyWords("金融").userPO(userPO).build();
        rsEventRepository.save(rsEventPO);
        RsEvent rsEvent = RsEvent.builder().keyWords("文化").userId(userPO.getId()+1).build();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/{rsEventId}",rsEventPO.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertEquals("股票",rsEventRepository.findById(rsEventPO.getId()).get().getEventName());
        assertEquals("金融",rsEventRepository.findById(rsEventPO.getId()).get().getKeyWords());

    }

    @Test
    @Order(10)
    public void should_throw_rs_event_not_valid_exception() throws Exception {
        mockMvc.perform(get("/rs/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));

    }

    @Test
    @Order(11)
    public void should_throw_rs_event_not_valid_param_exception() throws Exception{
        mockMvc.perform(get("/rs/list?start=0&end=4"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));

    }


//    @Test
//    @Order(11)
//    public void should_throw_rs_event_not_valid_rs_event() throws Exception {
//        User user = new User("xiaozheng","male",  15, "c@b.com", "16888888888");
//        RsEvent rsEvent = new RsEvent( "股票跌了","经济", 1);
//        String jsonString = objectMapper.writeValueAsString(rsEvent);
//        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error", is("invalid param")));
//    }
}
