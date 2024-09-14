package com.dev.invoice.service;

import com.dev.invoice.Entity.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoiceService {

    /**
     * Takes Invoice Object as Input and return PK generated
     */
    Long saveInvoice(Invoice inc);

    /**
     * Takes existing Invoice Data as input and update values
     */
    void updateInvoice(Invoice e);

    /**
     * Takes PK(ID) as input and delete Invoice Object Data
     */
    void deleteInvoice(Long id);

    /**
     * Takes ID as input and returns one row as one Object
     */
    Invoice getOneInvoice(Long id);

    Optional<Invoice> getSingleInvoice(Long id);

    /**
     * select all rows and provide result as a List<Invoice>
     */
    List<Invoice> getAllInvoice();

    /**
     * Takes ID as input, check if record exists returns true, then false
     */
    boolean isInvoiceExists(Long id);

    /**
     * Takes 2 fields as input , update invoice data as provided where clause
     * Like UPDATE Invoice SET number=:number WHERE id=:id
     */
    Integer updateInvoiceNumberById(String number, Long Id);
}
