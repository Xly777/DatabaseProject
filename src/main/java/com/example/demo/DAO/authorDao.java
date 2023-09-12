package com.example.demo.DAO;

import com.example.demo.entity.Account;
import com.example.demo.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface authorDao extends JpaRepository<Author, String> {
    Author findAuthorByAuthor(String author);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "update author set phone=:phone where author.author=:author", nativeQuery = true)
    int updatePhone(String author, String phone);

    int countAuthorByAccount(Account a);
}
