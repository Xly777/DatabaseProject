package com.example.demo.DAO;

import com.example.demo.entity.Account;
import com.example.demo.entity.Block;
import com.example.demo.entity.BlockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface blockDao extends JpaRepository<Block, BlockId> {
    List<Block> findAllByBlock(Account account);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into block (block,blocked) values(?1,?2)", nativeQuery = true)
    int block(String block, String blocked);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "delete from block where block.block=:block and block.blocked=:blocked", nativeQuery = true)
    int unblock(@Param("block") String block,@Param("blocked")String blocked);
}
