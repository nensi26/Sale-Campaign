package com.example.sale.repository;

import com.example.sale.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository

public interface CampaignRepo extends JpaRepository<Campaign , Integer> {

    @Query("SELECT c FROM Campaign c WHERE c.startDate <= :currentDate AND c.endDate >= :currentDate")
    List<Campaign> findActiveCampaigns(@Param("currentDate") LocalDate currentDate);


    @Query("SELECT c FROM Campaign c WHERE c.endDate = :currentDate")
    List<Campaign> findCampaignsEndingToday(@Param("currentDate") LocalDate currentDate);


}
