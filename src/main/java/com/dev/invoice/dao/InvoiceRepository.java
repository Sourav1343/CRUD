package com.dev.invoice.dao;

import com.dev.invoice.Entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>{

    // Update is Non-Select Operation, so @Modifying is used
    @Modifying
    @Query("UPDATE Invoice SET number=:number WHERE id=:id")
    Integer updateInvoiceNumberById(String number,Long id);
}
