package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.RsEvent;

import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
public class RsController{

  @Autowired
  UserService userService;
  @Autowired
  RsEventRepository rsEventRepository;

  final RsEventService rsEventService;
  public RsController(RsEventService rsEventService) {
    this.rsEventService = rsEventService;
  }


  @GetMapping("/rs/{index}")
  public ResponseEntity<RsEvent> getOneRsEvent(@PathVariable int index) {
    return  rsEventService.getOneEvent(index);

  }

  @GetMapping("/rs/list")
  public ResponseEntity<List<RsEvent>> getRsEvent(@RequestParam(required = false) Integer start,
                                                  @RequestParam(required = false) Integer end) {
    return  rsEventService.getSomeEvent(start, end);
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) {

      return rsEventService.addRsEvent(rsEvent);
  }

  @DeleteMapping("/rd/{id}")
  public ResponseEntity deleteRsEvent(@PathVariable int id) {
    rsEventRepository.deleteById(id);
    return ResponseEntity.created(null).build();
  }

  @PatchMapping("/rs/{rsEventId}")
  public ResponseEntity pathRsEvent (@PathVariable int rsEventId,
                                     @RequestBody @Valid RsEvent rsEvent){
    return rsEventService.updateRsEvent(rsEvent,rsEventId);
  }
}




