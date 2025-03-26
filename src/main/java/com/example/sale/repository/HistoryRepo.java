package com.example.sale.repository;

import com.example.sale.model.History;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository

public interface HistoryRepo extends JpaRepository<History , Integer> {

    List<History> findByProductIdOrderByDate(int productId);

    List<History> findByDate(LocalDate date);
}
