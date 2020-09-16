package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
//  private List<RsEvent> rsList = initReEventList();
private List<String> rsList = Arrays.asList("aaaa","bbbb","cccc");

//  private List<RsEvent> initReEventList() {
//    List<RsEvent> rsEventList = new ArrayList<>();
//    rsEventList.add(new RsEvent("第一事件","无关键"));
//    rsEventList.add(new RsEvent("第二事件","无关键"));
//    rsEventList.add(new RsEvent("第三事件","无关键"));
//    rsEventList.add(new RsEvent("第四事件","无关键"));
//
//    return rsEventList;
//  }



//  @GetMapping("/rs/list")
//  public List<RsEvent> getrsList(){
//    return rsList;
//  }
//    @GetMapping("/rs/list")
//    public String getRsEvent(){
//      return rsList.toString();
//    }

    @GetMapping("/rs/{index}")
    public String getOneRsEvent(@PathVariable int index){
        return rsList.get(index-1);
    }

    @GetMapping("/rs/list")
    public String getRsEvent(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end){
        if(start == null || end == null){
            return rsList.toString();
        }
        return rsList.subList(start-1, end).toString();
    }



}
