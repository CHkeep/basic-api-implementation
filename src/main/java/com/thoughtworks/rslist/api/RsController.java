package com.thoughtworks.rslist.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  private List<String> rsList = Arrays.asList("routing", "data-binding", "validation","customize-response","error-handling");

  @GetMapping("/rs/list")
  public String getrsList(){
    return rsList.toString();
  }
}
