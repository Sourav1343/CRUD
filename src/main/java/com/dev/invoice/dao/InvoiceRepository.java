package com.dev.invoice.dao;

import com.dev.invoice.Entity.Invoice;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Transactional
public interface InvoiceRepository extends JpaRepository<Invoice, Long>{

    @Modifying
    @Query("UPDATE Invoice i SET i.number = :number WHERE i.id = :id")
    int updateInvoiceNumberById(@Param("number") String number, @Param("id") Long id);

}
