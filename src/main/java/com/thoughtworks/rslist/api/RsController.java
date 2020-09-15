package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
//  private List<String> rsList = Arrays.asList("第一条事件", "第二条事件", "第三条事件");
  private List<RsEvent> rsList = initReEventList();

  private List<RsEvent> initReEventList() {
    List<RsEvent> rsEventList = new ArrayList<>();
    rsEventList.add(new RsEvent("第一条事件","无标签"));
    rsEventList.add(new RsEvent("第二条事件","无标签"));
    rsEventList.add(new RsEvent("第三条事件","无标签"));
    rsEventList.add(new RsEvent("第四条事件","无标签"));

    return rsEventList;
  }

  @GetMapping("/rs/{index}")
  public RsEvent getOneRsEvent(@PathVariable int index){
    return rsList.get(index-1);
  }

  @GetMapping("/rs/list")
  public List<RsEvent> getRsEvent(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end){
    if(start == null || end == null){
      return rsList;
    }
    return rsList.subList(start-1, end);
  }

  @PostMapping("/re/event")
  public  void addRsEvent(@RequestBody RsEvent rsEvent){
    rsList.add(rsEvent);
  }


}
