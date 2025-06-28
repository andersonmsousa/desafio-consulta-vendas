package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SaleDTO;
import com.devsuperior.dsmeta.dto.SellerSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT " +
            "NEW com.devsuperior.dsmeta.dto.SaleDTO(s.id, s.date, s.amount, s.seller.name) " +
            "FROM Sale s " +
            "WHERE s.date >= :minDate " +
            "AND s.date <= :maxDate AND " +
            "UPPER(s.seller.name) LIKE UPPER(CONCAT('%', :name, '%'))")
    Page<SaleDTO> findSalesReport(LocalDate minDate, LocalDate maxDate, String name, Pageable pageable);

    @Query("SELECT " +
            "NEW com.devsuperior.dsmeta.dto.SellerSummaryDTO(s.seller.name, SUM(s.amount)) " +
            "FROM Sale s " +
            "WHERE s.date >= :minDate " +
            "AND s.date <= :maxDate " +
            "GROUP BY s.seller.name")
    List<SellerSummaryDTO> findSellersSummary(LocalDate minDate, LocalDate maxDate);
}
