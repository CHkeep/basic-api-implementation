package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Valid
public class VoteController {

   @Autowired
   VoteService voteService;

    @PostMapping("/rs/vote")
    public ResponseEntity addVote(@RequestBody Vote vote, @RequestParam int rsEventId) {
        voteService.vote(vote,rsEventId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rs/vote")
    public ResponseEntity<List<Vote>> getVoteById(@RequestParam int userId, @RequestParam int rsEventId) {
        return voteService.getvotebyUserIdAndRsEventId(userId,rsEventId);
    }

    @GetMapping("/rv/vote")
    public ResponseEntity<List<Vote>> getVoteByTime(@RequestParam String startDate, @RequestParam String endDate) {
        return voteService.getbyVoteTime(startDate,endDate);
    }

}
