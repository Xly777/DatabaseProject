package com.example.demo.controller;


import com.example.demo.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class accountController {
    @Autowired
    private com.example.demo.DAO.accountDao accountDao;

    @RequestMapping("/checkAccountExist/{name}")
    public @ResponseBody boolean checkAccountExist(@PathVariable("name") String name) {
        Integer x = accountDao.countAccountByName(name);
        return x != 0;
    }

    @RequestMapping("/signUp/{userName}/{password}/{account_id}")
    public @ResponseBody int signUp(@PathVariable("userName") String userName, @PathVariable("password")
    String password, @PathVariable("account_id") String account_id) {
        return accountDao.signUp(userName, password, account_id);
    }

    @RequestMapping("/checkPassword/{name}/{password}")
    public @ResponseBody boolean checkPassword(@PathVariable("name") String name, @PathVariable("password") String password) {
        return accountDao.findAccountByName(name).getPassword().equals(password);
    }

    @RequestMapping("/updatePassword/{name}/{newPassword}")
    int updatePassword(@PathVariable("name") String name, @PathVariable("newPassword") String newPassword) {
        return accountDao.updatePassword(newPassword, name);
    }

    @RequestMapping("/connect/{name}")
    Integer connect(@PathVariable("name") String name) {
        System.out.println(name + " login!");
        return 1;
    }

    @RequestMapping("/account/{name}")
    Account find(@PathVariable("name") String name) {
        return accountDao.findAccountByName(name);
    }
}
