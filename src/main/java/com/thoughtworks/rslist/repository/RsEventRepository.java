package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.RsEventPO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RsEventRepository extends CrudRepository<RsEventPO, Integer> {
    @Override
    List<RsEventPO> findAll();

}