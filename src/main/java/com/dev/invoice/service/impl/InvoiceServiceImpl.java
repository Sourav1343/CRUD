package com.dev.invoice.service.impl;

import com.dev.invoice.Entity.Invoice;
import com.dev.invoice.controller.InvoiceRestController;
import com.dev.invoice.exception.InvoiceNotFoundException;
import com.dev.invoice.dao.InvoiceRepository;
import com.dev.invoice.util.InvoiceUtil;
import com.dev.invoice.service.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {

   @Autowired
   private InvoiceRepository repo;

   @Autowired
   private InvoiceUtil util;

    private static final Logger logger = LoggerFactory.getLogger(InvoiceRestController.class);

    @Override
    public Long saveInvoice(Invoice inv) {
        logger.info("Save invoice: {}", inv); // Log the received invoice

        try {
            logger.debug("Calculating final amount including GST...");
            util.CalculateFinalAmountIncludingGST(inv); // Calculate final amount including GST
            logger.debug("Final amount calculated: {}", inv.getFinalAmount()); // Log the calculated amount

            logger.debug("Saving invoice to repository...");
            Long id = repo.save(inv).getId(); // Save invoice
            logger.info("Invoice saved successfully with ID: {}", id); // Log the saved invoice ID

            return id;
        } catch (Exception e) {
            logger.error("Error occurred while saving invoice: {}", e.getMessage()); // Log any error
            throw e; // Re-throw the exception to be handled by the controller
        }
    }

    @Override
    public void updateInvoice(Invoice inv) {
        util.CalculateFinalAmountIncludingGST(inv);
        repo.save(inv);
    }

    @Override
    public void deleteInvoice(Long id) {
    Invoice inv = getOneInvoice(id);
    repo.delete(inv);
    }

    @Override
    public Optional<Invoice> getSingleInvoice(Long id) {
        return repo.findById(id);
    }

    @Override
    public Invoice getOneInvoice(Long id){
        Invoice inv = repo.findById(id)
                .orElseThrow(()->new InvoiceNotFoundException(
                        new StringBuffer().append("Product  '")
                                .append(id)
                                .append("' not exist")
                                .toString())
                );
        return inv;
    }

    @Override
    public List<Invoice> getAllInvoice() {
        logger.info("Fetching all invoices");

        List<Invoice> list;
        try {
            list = repo.findAll(); // Retrieve all invoices
            logger.info("Retrieved {} invoices", list.size()); // Log the number of invoices retrieved
        } catch (Exception e) {
            logger.error("Error occurred while fetching invoices: {}", e.getMessage()); // Log any error
            throw e; // Re-throw the exception to be handled by the controller
        }

        return list;
    }

    @Override
    public boolean isInvoiceExists(Long id) {
        return false;
    }

    @Override
    public Integer updateInvoiceNumberById(String number, Long id) {
        if(!repo.existsById(id)) {
            throw new InvoiceNotFoundException(
                    new StringBuffer()
                            .append("Invoice '")
                            .append(id)
                            .append("' not exist")
                            .toString());
        }
        return repo.updateInvoiceNumberById(number, id);
    }
}
