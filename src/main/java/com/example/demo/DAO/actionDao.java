package com.example.demo.DAO;


import com.example.demo.entity.Account;
import com.example.demo.entity.Action;
import com.example.demo.entity.ActionId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface actionDao extends JpaRepository<Action, ActionId> {
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into Action(post_id, name, type) values (?1,?2,?3);", nativeQuery = true)
    int makeAction(Integer post_id, String name, String type);

    Action findActionByName(Account name);

    @Query(value = "select action.name,action.post_id,action.type from action join posts on action.post_id = posts.post_id where action.name=?1 and action.type=?2 ;", nativeQuery = true)
    List<Action> selectAction(String name, String type);

    int countAllById(ActionId a);
}
