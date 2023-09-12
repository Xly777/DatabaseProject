package com.example.demo.DAO;

import com.example.demo.entity.Label;
import com.example.demo.entity.LabelId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface labelDao extends JpaRepository<Label, LabelId> {
}
