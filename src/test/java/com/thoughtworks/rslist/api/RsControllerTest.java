package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RsControllerTest {


    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRepository voteRepository;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    UserPO userPO;
    RsEventPO rsEventPO;

        @BeforeEach
        void  setUp(){
            objectMapper = new ObjectMapper();
            //清理数据库
            voteRepository.deleteAll();;
            rsEventRepository.deleteAll();
            userRepository.deleteAll();

            userPO = UserPO.builder().userName("li").age(20).email("f@d.com")
                    .phone("11111111111").gender("male").voteNum(10).build();
            userPO = userRepository.save(userPO);
            rsEventPO = RsEventPO.builder().eventName("yuwen").keyWords("xueke").voteNum(0).build();
            rsEventRepository.save(rsEventPO);
        }

    @Test
    @Order(1)
    public void should_get_one_rs_event_list() throws Exception {
        userPO = UserPO.builder().userName("li").age(20).email("f@d.com")
                .phone("11111111111").gender("male").voteNum(10).build();
        userPO = userRepository.save(userPO);
        rsEventPO = RsEventPO.builder().eventName("yuwen").keyWords("xueke").voteNum(0).build();
        rsEventRepository.save(rsEventPO);

        mockMvc.perform(get("/rs/{id}",rsEventPO.getId()));

        assertEquals(true,rsEventRepository.existsById(rsEventPO.getId()));
    }

    @Test
    @Order(2)
    public void should_get_some_rs_event_list() throws Exception {
        userPO = UserPO.builder().userName("liu").age(22).email("f@d.com")
                .phone("12222222222").gender("male").voteNum(10).build();
        userRepository.save(userPO);
        rsEventPO = RsEventPO.builder().eventName("shuxue").keyWords("xueke").voteNum(0).build();
        rsEventRepository.save(rsEventPO);
        mockMvc.perform(get("/rs/list").param("start", "0")
                .param("end", String.valueOf(rsEventPO.getId())));

        assertEquals(2,rsEventRepository.findAll().size());
    }

    @Test
    @Order(3)
    public void should_add_rs_event_when_user_Id_exist() throws Exception {
        int  size = rsEventRepository.findAll().size() + 1;
        RsEvent rsEvent = new RsEvent("猪肉涨价了","经济",userPO.getId(),0);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        System.out.println(jsonString);
        mockMvc.perform(post("/rs/event").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(size,rsEventRepository.findAll().size());
    }

    @Test
    @Order(4)
    public void should_add_rs_event_when_user_Id_Not_exist() throws Exception {
        int  size = rsEventRepository.findAll().size();
        RsEvent rsEvent = new RsEvent("zhurouzhangjiang","经济",10000,0);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertEquals(size,rsEventRepository.findAll().size());
    }

    @Test
    @Order(5)
    public void should_delete_rs_event() throws Exception {
        mockMvc.perform(delete("/rd/{id}", rsEventPO.getId()))
                .andExpect(status().isCreated());
        assertEquals(Optional.empty(), rsEventRepository.findById(rsEventPO.getId()));
    }

    @Test
    @Order(6)
    public void should_patch_event_rs_event() throws Exception {
        UserPO userPO= UserPO.builder().userName("yangli").age(18).email("a@b.com").gender("female").phone("13333333333").voteNum(10).build();
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
        UserPO userPO= UserPO.builder().userName("yangli").age(18).email("a@b.com").gender("female").phone("13333333333").voteNum(10).build();
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
        UserPO userPO= UserPO.builder().userName("yangli").age(18).email("a@b.com").gender("female").phone("13333333333").voteNum(10).build();
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
        UserPO userPO= UserPO.builder().userName("yangqi").age(22).email("a@b.com").gender("male").phone("144444444444").voteNum(10).build();
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


    @Test
    @Order(11)
    public void should_throw_rs_event_not_valid_rs_event() throws Exception {
        User user = new User("xiaozheng","male",  15, "c@b.com", "1555555555",0);
        RsEvent rsEvent = new RsEvent( "股票跌了","经济", 1,0);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
//                .andExpect(jsonPath("$.error", is("invalid param")));
    }
}
