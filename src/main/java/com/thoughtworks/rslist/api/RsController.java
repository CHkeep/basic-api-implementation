package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.ReEventNotValidException;
import com.thoughtworks.rslist.exception.Error;

import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
public class RsController{

//  @Autowired
  final RsEventService rsEventService;
  @Autowired
  UserService userService;

  private List<RsEvent> rsList = initReEventList();

  public RsController(RsEventService rsEventService) {
    this.rsEventService = rsEventService;
  }

  private List<RsEvent> initReEventList() {
    List<RsEvent> rsEventList = new ArrayList<>();
    User user = new User("xiaoli", "male", 19, "a@b.com", "18888888888");
    rsEventList.add(new RsEvent("第一条事件", "无标签",1));
    rsEventList.add(new RsEvent("第二条事件", "无标签",1));
    rsEventList.add(new RsEvent("第三条事件", "无标签",1));
    rsEventList.add(new RsEvent("第四条事件", "无标签",1));

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
  public void addRsEvent(@RequestBody @Valid RsEvent rsEvent) {
    rsEventService.addRsEvent(rsEvent);
//    rsEventService.
//    rsEventRepository.save(RsEventPO.builder().eventName("股价").keyWords("经济").userId(1).build());
  }

      //返回201，并且返回的头部带上index字段
//      String index = String.valueOf(this.rsList.indexOf(rsEvent));
//      return ResponseEntity.status(HttpStatus.CREATED).header("index", index).build();




  @DeleteMapping("/rd/{id}")
  public ResponseEntity deleteRsEvent(@PathVariable int id) {
//    rsEventRepository.deleteById(id);
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


}

