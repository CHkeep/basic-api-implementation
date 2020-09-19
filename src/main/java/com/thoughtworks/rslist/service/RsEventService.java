package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.ReEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;


import javax.validation.Valid;
import java.util.Optional;

@Service
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
            RsEventPO rsEventPO = RsEventPO.builder().eventName(rsEvent.getEventName())
                    .keyWords(rsEvent.getKeyWords())
                    .userPO(userPO.get()).build();
            rsEventRepository.save(rsEventPO);
            //返回201，并且返回的头部带上index字段
            String index = String.valueOf(rsEventPO.getId());
            return ResponseEntity.status(HttpStatus.CREATED).header("index", index).build();
        }
    }


    public ResponseEntity updateRsEvent(@Valid RsEvent rsEvent, int rsEventId) {
        RsEventPO rsEventPO = rsEventRepository.findById(rsEventId).get();
        if(rsEventPO.getUserPO().getId() != rsEvent.getUserId()) {
            return ResponseEntity.badRequest().build();
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

    @ExceptionHandler({ReEventNotValidException.class, MethodArgumentNotValidException.class})
    public ResponseEntity rsExceptionHandler(Exception e){
        String errorMessage;
        if(e instanceof MethodArgumentNotValidException){
            errorMessage = "invalid param";
        }else {
            errorMessage = e.getMessage();
        }
        Error error = new Error();
        error.setError(errorMessage);
        return ResponseEntity.badRequest().body(error);
    }


}
