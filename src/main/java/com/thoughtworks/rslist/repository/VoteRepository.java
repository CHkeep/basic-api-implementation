package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.VotePO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepository extends CrudRepository<VotePO, Integer> {
    @Override
    List<VotePO> findAll();

    List<VotePO> findAllByUserIdAndRsEventId(int userId, int rsEventId);

}
