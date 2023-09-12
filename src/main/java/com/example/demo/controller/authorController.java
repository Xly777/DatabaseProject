package com.example.demo.controller;

import com.example.demo.DAO.accountDao;
import com.example.demo.DAO.authorDao;
import com.example.demo.entity.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class authorController {
    @Autowired
    private authorDao authorDao;
    @Autowired
    private accountDao accountDao;

    @RequestMapping("/updatePhone/{author}/{phone}")
    int updatePhone(@PathVariable("author") String author, @PathVariable("phone") String phone) {
        return authorDao.updatePhone(author, phone);
    }

    @RequestMapping("/checkAuthor/{name}")
    boolean checkAuthor(@PathVariable("name") String name) {
        return authorDao.countAuthorByAccount(accountDao.findAccountByName(name)) != 0;
    }

    @RequestMapping("/author/{name}")
    Author findAuthor(@PathVariable("name") String name) {
        return authorDao.findAuthorByAuthor(name);
    }
}
