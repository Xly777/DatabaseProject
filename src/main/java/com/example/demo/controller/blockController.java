package com.example.demo.controller;

import com.example.demo.entity.Block;
import com.example.demo.DAO.authorDao;
import com.example.demo.DAO.blockDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class blockController {
    @Autowired
    private blockDao blockDao;
    @Autowired
    private com.example.demo.DAO.accountDao accountDao;
    @Autowired
    private authorDao authorDao;

    @RequestMapping("/listBlock/{block}")
    List<Block> listBlock(@PathVariable("block") String block) {
        return blockDao.findAllByBlock(accountDao.findAccountByName(block));
    }

    @RequestMapping("/block/{block}/{blocked}")
    int block(@PathVariable("block") String block, @PathVariable("blocked") String blocked) {
        return blockDao.block(block, blocked);
    }

    @RequestMapping("/unblock/{block}/{blocked}")
    int unblock(@PathVariable("block") String block, @PathVariable("blocked") String blocked) {
        return blockDao.unblock(block,blocked);
    }
}
