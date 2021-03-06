package com.thoughtworks.rslist.api;

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

import static javax.swing.text.StyleConstants.Size;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRepository voteRepository;

    UserPO userPO;
    RsEventPO rsEventPO;

    @BeforeEach
    void setUp(){

        voteRepository.deleteAll();
        rsEventRepository.deleteAll();
        userRepository.deleteAll();


        userPO = UserPO.builder().userName("xiangyu").age(22).email("f@d.com")
                .phone("13333333333").gender("male").voteNum(10).build();
        userPO = userRepository.save(userPO);
        rsEventPO = RsEventPO.builder().eventName("yuwen").keyWords("xueke").voteNum(0).build();
        rsEventPO = rsEventRepository.save(rsEventPO);
    }

    @Test
    public void should_vote() throws Exception{
        VotePO votePO = VotePO.builder().user(userPO).localDateTime(LocalDateTime.now())
                .rsEvent(rsEventPO).num(5).build();
        voteRepository.save(votePO);

        mockMvc.perform(post("/rs/vote").param("reEventId",String.valueOf(rsEventPO.getId())));
        assertEquals(1, voteRepository.findAll().size());

    }

    @Test
    public void should_get_vote_rescord_by_user_id_and_rs_event_id() throws Exception{
        VotePO votePO = VotePO.builder().user(userPO).localDateTime(LocalDateTime.now())
                .rsEvent(rsEventPO).num(5).build();
        voteRepository.save(votePO);

        mockMvc.perform(get("/rs/vote").param("userId",String.valueOf(userPO.getId()))
                .param("reEventId",String.valueOf(rsEventPO.getId())));
        assertEquals(1, voteRepository.findAll().size());

    }


    @Test
    public void should_get_vote_rescord_by_time() throws Exception{
       LocalDateTime indexTime = LocalDateTime.parse("2020-10-01T16:59:20");
        for (int i = 0; i < 8; i++){
            VotePO votePO = VotePO.builder().user(userPO).localDateTime(indexTime.plusDays(i+1))
                    .rsEvent(rsEventPO).num(i+1).build();
            voteRepository.save(votePO);
        }

        mockMvc.perform(get("/rv/vote").param("startDate","2020-10-02T00:00:00").param("endDate","2020-10-05T00:00:00"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(status().isOk());
    }


    @Test
    public void should_get_vote_rescord_by_time_throw() throws Exception{
        LocalDateTime indexTime = LocalDateTime.parse("2020-10-01T16:59:20");
        for (int i = 0; i < 8; i++){
            VotePO votePO = VotePO.builder().user(userPO).localDateTime(indexTime.plusDays(i+1))
                    .rsEvent(rsEventPO).num(i+1).build();
            voteRepository.save(votePO);
        }

        mockMvc.perform(get("/rv/vote").param("startDate","2020-10-05T00:00:00").param("endDate","2020-10-01T00:00:00"))
                .andExpect(status().isBadRequest());
    }


}

