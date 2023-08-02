package com.example.pagenation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pagenation.model.Pager;

public interface PageRepository extends JpaRepository <Pager, Long> {
    
}
