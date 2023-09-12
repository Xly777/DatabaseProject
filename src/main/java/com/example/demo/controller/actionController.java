package com.example.demo.controller;


import com.example.demo.entity.Action;
import com.example.demo.entity.ActionId;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class actionController {
    @Autowired
    private com.example.demo.DAO.actionDao actionDao;

    @RequestMapping("/clickPost/{post_id}/{name}/{type}")
    public @ResponseBody int clickPost(@PathVariable("post_id") Integer post_id, @PathVariable("name") String name, @PathVariable("type") String type) {
        return actionDao.makeAction(post_id, name, type);
    }

    @RequestMapping("/selectAction/{action}/{account_name}")
    public List<Action> selectAction(@PathVariable("action") String action, @PathVariable("account_name") String account_name) {
        return actionDao.selectAction(action, account_name);
    }

    @RequestMapping("/checkAction/{post_id}/{name}/{type}")
    boolean checkAction(@PathVariable("post_id") int id, @PathVariable("name") String name, @PathVariable("type") String type) {
        ActionId a = new ActionId();
        a.setName(name);
        a.setType(type);
        a.setPostId(id);
        return actionDao.countAllById(a) != 0;
    }
}
