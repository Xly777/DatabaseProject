package com.example.demo.DAO;

import com.example.demo.entity.Account;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;



public interface accountDao extends JpaRepository<Account, String> {

    Integer countAccountByName(String name);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into account(name, password,account_id) values (?1,?2,?3);", nativeQuery = true)
    int signUp(String name, String password, String account_id);

    Account findAccountByName(String name);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "update account set password=:newPassword where account.name=:name", nativeQuery = true)
    int updatePassword(@Param("newPassword") String newPassword, @Param("name") String name);

}
