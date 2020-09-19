package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.ReEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
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


    public void addRsEvent(@RequestBody @Valid RsEvent rsEvent) {
        Optional<UserPO> userPO = userRepository.findById(rsEvent.getUserId());
        if(!userPO.isPresent()){
            throw new ReEventNotValidException("Unregistered user");
        }else {
            RsEventPO rsEventPO = RsEventPO.builder().eventName(rsEvent.getEventName())
                    .keyWords(rsEvent.getKeyWords())
                    .userId(rsEvent.getUserId()).build();
            System.out.println(rsEventPO.getEventName());
            rsEventRepository.save(rsEventPO);
        }
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
