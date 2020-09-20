package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@Service
@Configuration
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

    public ResponseEntity<List<Vote>> getvotebyUserIdAndRsEventId(int userId, int rsEventId) {
        return ResponseEntity.ok(
                voteRepository.findAllByUserIdAndRsEventId(userId,rsEventId).stream()
                        .map(item->Vote.builder().rsEventId(item.getRsEvent().getId())
                                .userId(item.getUser().getId())
                                .time(item.getLocalDateTime())
                                .voteNum(item.getNum()).build())
                        .collect(Collectors.toList()));
    }


    public ResponseEntity<List<Vote>> getbyVoteTime(LocalDateTime time) {
        List<VotePO> votePOList = voteRepository.findAll();
        if(votePOList.get(votePOList.size()-1).getLocalDateTime().compareTo(time) < 0){
            return ResponseEntity.badRequest().build();
        }
    System.out.println(voteRepository.findAll().get(4).getLocalDateTime().compareTo(time) );
        return ResponseEntity.ok(votePOList.stream()
                .filter(i-> i.getLocalDateTime().compareTo(time) > 0)
                .map(item -> Vote.builder().userId(item.getUser().getId())
                        .rsEventId(item.getRsEvent().getId())
                        .time(item.getLocalDateTime())
                        .voteNum(item.getNum()).build())
                .collect(Collectors.toList()));
    }
}
