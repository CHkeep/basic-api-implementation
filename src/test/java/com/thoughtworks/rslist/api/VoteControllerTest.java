package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VoteControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRepository voteRepository;

    ObjectMapper objectMapper;
    UserPO userPO;
    RsEventPO rsEventPO;

    @BeforeEach
    void setUp(){

        objectMapper = new ObjectMapper();

        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        voteRepository.deleteAll();


        userPO = UserPO.builder().userName("xiangyu").age(22).email("f@d.com")
                .phone("13333333333").gender("male").voteNum(10).build();
        userPO = userRepository.save(userPO);
        rsEventPO = RsEventPO.builder().eventName("yuwen").keyWords("xueke").voteNum(0).build();
        rsEventPO = rsEventRepository.save(rsEventPO);
    }

    @Test
//    @Order(1)
    public void should_vote() throws Exception{
        VotePO votePO = VotePO.builder().user(userPO).localDateTime(LocalDateTime.now())
                .rsEvent(rsEventPO).num(5).build();
        voteRepository.save(votePO);

        mockMvc.perform(post("/rs/vote").param("reEventId",String.valueOf(rsEventPO.getId())));
        assertEquals(1, voteRepository.findAll().size());

    }

    @Test
//    @Order(2)
    public void should_get_vote_rescord_by_user_id_and_rs_event_id() throws Exception{
        VotePO votePO = VotePO.builder().user(userPO).localDateTime(LocalDateTime.now())
                .rsEvent(rsEventPO).num(5).build();
        voteRepository.save(votePO);

        mockMvc.perform(get("/rs/vote").param("userId",String.valueOf(userPO.getId()))
                .param("reEventId",String.valueOf(rsEventPO.getId())));
        assertEquals(1, voteRepository.findAll().size());

    }


    @Test
//    @Order(3)
    public void should_get_vote_rescord_by_time() throws Exception{
        LocalDateTime indexTime = LocalDateTime.of(2020,8,20,10,10,10);
        for (int i = 0; i < 8; i++){
            VotePO votePO = VotePO.builder().user(userPO).localDateTime(LocalDateTime.now())
                    .rsEvent(rsEventPO).num(1).build();
            voteRepository.save(votePO);
        }

        mockMvc.perform(get("/rs/vote").param("localDateTime",String.valueOf(indexTime)))
                .andExpect(jsonPath("$", hasSize(8)));


    }

    @Test
    public void should_get_some_vote_rescord_by_time() throws Exception{
        LocalDateTime indexTime1 = LocalDateTime.of(2019,7,20,10,10,10);
        VotePO votePO1 = VotePO.builder().user(userPO).localDateTime(indexTime1)
                .rsEvent(rsEventPO).num(1).build();
        voteRepository.save(votePO1);

        LocalDateTime indexTime = LocalDateTime.of(2020,8,20,10,10,10);

        for (int i = 0; i < 7; i++){
            VotePO votePO = VotePO.builder().user(userPO).localDateTime(LocalDateTime.now())
                    .rsEvent(rsEventPO).num(1).build();
            voteRepository.save(votePO);
        }

        mockMvc.perform(get("/rs/vote").param("localDateTime",String.valueOf(indexTime)))
                .andExpect(jsonPath("$", hasSize(7)))
                .andExpect(status().isOk());

    }

    @Test
    public void should_throw_get_vote_rescord_by_time() throws Exception{
        LocalDateTime indexTime = LocalDateTime.of(2090,8,20,10,10,10);
//        LocalDateTime time = LocalDateTime.parse(indexTime,)
//        String jsonStringTime = objectMapper.writeValueAsString(indexTime);
        for (int i = 0; i < 8; i++){
            VotePO votePO = VotePO.builder().user(userPO).localDateTime(LocalDateTime.now())
                    .rsEvent(rsEventPO).num(1).build();
            voteRepository.save(votePO);
        }

        mockMvc.perform(get("/rs/vote").param("localDateTime", String.valueOf(indexTime)))
                     .andExpect(jsonPath("$", hasSize(0)));
//                     .andExpect(status().isBadRequest());

    }

}
