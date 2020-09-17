package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.ReEventNotValidException;
import com.thoughtworks.rslist.exception.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
public class RsController{

  private List<RsEvent> rsList = initReEventList();

  private List<RsEvent> initReEventList() {
    List<RsEvent> rsEventList = new ArrayList<>();
    User user = new User("xiaoli", 19, "male", "a@b.com", "18888888888");
    rsEventList.add(new RsEvent("第一条事件", "无标签",user));
    rsEventList.add(new RsEvent("第二条事件", "无标签",user));
    rsEventList.add(new RsEvent("第三条事件", "无标签",user));
    rsEventList.add(new RsEvent("第四条事件", "无标签",user));

    return rsEventList;
  }

  @GetMapping("/rs/{index}")
  public ResponseEntity getOneRsEvent(@PathVariable int index) {
    if(index <= 0 || index > rsList.size()){
      throw new ReEventNotValidException("invalid index");
    }
    return ResponseEntity.ok(rsList.get(index - 1));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsEvent(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
    if (start == null || end == null) {
      return ResponseEntity.ok(rsList);
    }
    if(start <= 0 || end > rsList.size() || start > end){
      throw new ReEventNotValidException("invalid request param");
    }

    return ResponseEntity.ok(rsList.subList(start - 1, end));
  }

  @PostMapping("/rs/event")
//  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) {
    UserController userController = new UserController();
    List<User> userList = userController.getUserList();
    if(!userList.contains(rsEvent.getUser())){
      userList.add(rsEvent.getUser());
    }
    rsList.add(rsEvent);
    //返回201，并且返回的头部带上index字段
     String index = String.valueOf(this.rsList.indexOf(rsEvent));
     return ResponseEntity.status(HttpStatus.CREATED).header("index",index).build();
  }


  @DeleteMapping("/rd/{id}")
  public ResponseEntity deleteRsEvent(@PathVariable int id) {
    rsList.remove(id - 1);
    return ResponseEntity.created(null).build();
  }

  @PutMapping("/re/put/{id}")
  public ResponseEntity updateRsEvent(@PathVariable int id,
                            @RequestBody @Valid RsEvent rsEvent) {
    if (rsEvent.getEventName().isEmpty()) {
      rsList.get(id - 1).setKeyWords(rsEvent.getKeyWords());
    }
    if (rsEvent.getKeyWords().isEmpty() ) {
      rsList.get(id - 1).setEventName(rsEvent.getEventName());
    }
    if (rsEvent.getEventName().length()!=0 && rsEvent.getKeyWords().length()!=0) {
      rsList.get(id - 1).setKeyWords(rsEvent.getKeyWords());
      rsList.get(id - 1).setEventName(rsEvent.getEventName());
    }

    return ResponseEntity.created(null).build();
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

