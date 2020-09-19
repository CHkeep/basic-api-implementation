package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class VoteService {
    final RsEventRepository rsEventRepository;
    final UserRepository userRepository;
    final VoteRepository voteRepository;

    public VoteService(RsEventRepository rsEventRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public void vote(Vote vote, int rsEventId){
       Optional<RsEventPO> rsEventPO = rsEventRepository.findById(rsEventId);
       Optional<UserPO> userPO = userRepository.findById(vote.getUserId());
       if(!rsEventPO.isPresent() || !userPO.isPresent()
               || !(vote.getVoteNum() <= userPO.get().getVoteNum())){
           throw new RuntimeException();
       }
        VotePO votePO = VotePO.builder().user(userPO.get()).rsEvent(rsEventPO.get())
                .num(vote.getVoteNum()).localDateTime(vote.getTime()).build();
       voteRepository.save(votePO);
       UserPO userPO1 = userPO.get();
       userPO1.setVoteNum(userPO1.getVoteNum() - vote.getVoteNum());
       userRepository.save(userPO1);
       RsEventPO rsEventPO1 = rsEventPO.get();
       rsEventPO1.setVoteNum(rsEventPO1.getVoteNum() - vote.getVoteNum());
       rsEventRepository.save(rsEventPO1);

    }
}
