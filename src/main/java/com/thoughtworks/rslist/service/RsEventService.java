package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.exception.ReEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@Service
@Configuration
public class RsEventService {

    final RsEventRepository rsEventRepository;
    final UserRepository userRepository;


    public RsEventService(RsEventRepository rsEventRepository, UserRepository userRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
    }


    public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) {
        Optional<UserPO> userPO = userRepository.findById(rsEvent.getUserId());
        if(!userPO.isPresent()){
            return ResponseEntity.badRequest().build();
        }else {
            RsEventPO rsEventPO = RsEventPO.builder()
                    .eventName(rsEvent.getEventName())
                    .keyWords(rsEvent.getKeyWords())
                    .userPO(userPO.get())
                    .voteNum(0)
                    .build();
            rsEventRepository.save(rsEventPO);
            //返回201，并且返回的头部带上index字段
            String index = String.valueOf(rsEventPO.getId());
            return ResponseEntity.status(HttpStatus.CREATED).header("index", index).build();
        }
    }


    public ResponseEntity updateRsEvent(@Valid RsEvent rsEvent, int rsEventId) {
        RsEventPO rsEventPO = rsEventRepository.findById(rsEventId).get();
        if(rsEventPO.getUserPO().getId() != rsEvent.getUserId()) {
            throw new ReEventNotValidException("invalid param");
        }
        if (rsEvent.getEventName() != null) {
            rsEventPO.setEventName(rsEvent.getEventName());
        }
        if (rsEvent.getKeyWords() != null) {
            rsEventPO.setKeyWords(rsEvent.getKeyWords());
        }
        rsEventRepository.save(rsEventPO );
        return ResponseEntity.ok().build();
    }


    public ResponseEntity<List<RsEvent>> getSomeEvent(Integer start, Integer end) {
        List<RsEventPO> rsEventPOList = rsEventRepository.findAll().subList(start, end);
        List<RsEvent> rsEventList = rsEventPOList.stream().map(rsEventPO ->
                RsEvent.builder().keyWords(rsEventPO.getKeyWords())
               .eventName(rsEventPO.getEventName())
               .voteNum(rsEventPO.getVoteNum()).build())
               .collect(Collectors.toList());
        if (start == null || end == null) {
            return ResponseEntity.ok(rsEventList);
        }
        if(start > end || start <= 0 || end > rsEventList.size()){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(rsEventList.subList(start - 1, end));
    }

    public ResponseEntity<RsEvent> getOneEvent(int index) {
        if(index <= 0 || index > rsEventRepository.findAll().size()){
            throw new ReEventNotValidException("invalid index");
        }
        Optional<RsEventPO> rsEventPO = rsEventRepository.findById(index);
        RsEvent rsEvent = RsEvent.builder().keyWords(rsEventPO.get().getKeyWords())
                        .eventName(rsEventPO.get().getEventName())
                        .voteNum(rsEventPO.get().getVoteNum()).build();

        return ResponseEntity.ok(rsEvent);
    }

}
