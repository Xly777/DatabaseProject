package com.example.demo.DAO;


import com.example.demo.entity.Account;
import com.example.demo.entity.Author;
import com.example.demo.entity.Follower;
import com.example.demo.entity.FollowerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface followerDao extends JpaRepository<Follower, FollowerId> {
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into follower (author, name) values (?1,?2);", nativeQuery = true)
    int follow(String author, String name);

    int countFollowerByAuthorAndName(Author a, Account b);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "delete from follower where follower.author=?1 and follower.name=?2 ;", nativeQuery = true)
    int unfollow(String author, String name);

    List<Follower> findAllByName(Account account);
}
