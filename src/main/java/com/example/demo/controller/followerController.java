package com.example.demo.controller;

import com.example.demo.DAO.accountDao;
import com.example.demo.DAO.authorDao;
import com.example.demo.DAO.followerDao;
import com.example.demo.entity.Account;
import com.example.demo.entity.Author;
import com.example.demo.entity.Follower;
import com.example.demo.entity.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class followerController {
    @Autowired
    private followerDao followerDao;
    @Autowired
    private authorDao authorDao;
    @Autowired
    private accountDao accountDao;

    @RequestMapping("/follow/{author}/{name}")
    int follow(@PathVariable("author") String author, @PathVariable("name") String name) {
        return followerDao.follow(author, name);
    }

    @RequestMapping("/isFollow/{author}/{name}")
    boolean isfollow(@PathVariable("author") String author, @PathVariable("name") String name) {
        Author a = authorDao.findAuthorByAuthor(author);
        Account b = accountDao.findAccountByName(name);
        return followerDao.countFollowerByAuthorAndName(a, b) != 0;
    }

    @RequestMapping("/unfollow/{author}/{name}")
    int unfollow(@PathVariable("author") String author, @PathVariable("name") String name) {
        return followerDao.unfollow(author, name);
    }

    @RequestMapping("/selectFollowList/{name}")
    List<Follower> selectFollowList(@PathVariable("name") String name) {
        return followerDao.findAllByName(accountDao.findAccountByName(name));
    }


}
