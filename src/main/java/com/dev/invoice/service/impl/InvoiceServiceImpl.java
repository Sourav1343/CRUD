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
   private  InvoiceRepository repo;

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
        logger.info("Initiating deletion process for invoice with ID: {}", id);

        try {
            Invoice inv = getOneInvoice(id);
            logger.info("Successfully retrieved invoice with ID: {} for deletion", id);

            repo.delete(inv);
            logger.info("Successfully deleted invoice with ID: {}", id);
        } catch (InvoiceNotFoundException ex) {
            logger.warn("Invoice with ID {} not found, deletion aborted: {}", id, ex.getMessage());
            throw ex;  // Rethrow for controller to handle
        } catch (Exception ex) {
            logger.error("Error occurred while deleting invoice with ID: {}: {}", id, ex.getMessage());
            throw new RuntimeException("Unexpected error occurred during invoice deletion", ex);
        }
    }



    @Override
    public Invoice getOneInvoice(Long id) {
        logger.info("Fetching invoice with ID: {}", id);

        try {
            Invoice inv = repo.findById(id)
                    .orElseThrow(() -> new InvoiceNotFoundException(
                            "Invoice with ID " + id + " does not exist"));

            logger.info("Successfully retrieved invoice with ID: {}", id);
            return inv;
        } catch (InvoiceNotFoundException ex) {
            logger.warn("Invoice not found with ID: {}: {}", id, ex.getMessage());
            throw ex; // Rethrow the exception for the controller to handle
        } catch (Exception ex) {
            logger.error("Error occurred while fetching invoice with ID: {}: {}", id, ex.getMessage());
            throw new RuntimeException("Unexpected error occurred while retrieving invoice", ex);
        }
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
        // Check if the invoice exists
        if (!repo.existsById(id)) {
            throw new InvoiceNotFoundException("Invoice with id '" + id + "' does not exist.");
        }

        // Perform the update and check the result
        int updatedRows = repo.updateInvoiceNumberById(number, id);

        if (updatedRows == 0) {
            throw new InvoiceNotFoundException("Failed to update the invoice with id '" + id + "'.");
        }

        return updatedRows;
    }

}
