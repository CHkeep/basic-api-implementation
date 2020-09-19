package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
public class VoteController {

    final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/rs/vote")
    public ResponseEntity addVote(@RequestBody Vote vote, @RequestParam int rsEventId) {
        voteService.vote(vote,rsEventId);
        return ResponseEntity.ok().build();
    }


}
