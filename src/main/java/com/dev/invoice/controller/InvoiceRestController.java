package com.dev.invoice.controller;

import com.dev.invoice.Entity.Invoice;
import com.dev.invoice.exception.InvoiceNotFoundException;
import com.dev.invoice.service.InvoiceService;
import com.dev.invoice.util.InvoiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InvoiceRestController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceUtil invoiceUtil;

   // ResponseEntity<String> responseEntity = null;
    private static final Logger logger = LoggerFactory.getLogger(InvoiceRestController.class);
    @PostMapping("/invoices")
    public ResponseEntity<String> saveInvoice(@RequestBody Invoice invoice) {
        logger.info("Received request to save invoice: {}", invoice); // Log the received invoice

        // Check if the invoice is null
        if (invoice == null) {
            logger.error("Received null invoice");
            return new ResponseEntity<>("Invoice cannot be null", HttpStatus.BAD_REQUEST);
        }

        try {
            logger.debug("Attempting to save the invoice...");
            Long id = invoiceService.saveInvoice(invoice); // Save invoice
            logger.info("Invoice saved with ID '{}'", id);
            return new ResponseEntity<>("Invoice with ID '" + id + "' has been created", HttpStatus.CREATED);
        } catch (Exception exception) {
            logger.error("Error occurred while saving invoice: {}", exception.getMessage()); // Log the error
            return new ResponseEntity<>("Unable to save invoice", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * To retrieve all Invoices, returns data retrieval Status as ResponseEntity<?>
     */
    @GetMapping("/invoices")
    public ResponseEntity<?> getAllInvoices() {
        logger.info("Received request to get all invoices");

        ResponseEntity<?> responseEntity;
        try {
            List<Invoice> invoiceList = invoiceService.getAllInvoice();
            logger.info("Successfully retrieved {} invoices", invoiceList.size()); // Log the number of invoices retrieved
            responseEntity = new ResponseEntity<>(invoiceList, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Error occurred while retrieving invoices: {}", exception.getMessage()); // Log the exception
            responseEntity = new ResponseEntity<>("Unable to get Invoice", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    /**
     * To retrieve one Invoice by providing id, returns Invoice object & Status as ResponseEntity<?>
     */
    @GetMapping("/invoices/{id}")
    public ResponseEntity<?> getOneInvoice(@PathVariable Long id) {
        logger.info("Received request to get invoice with ID: {}", id);

        ResponseEntity<?> responseEntity;
        try {
            Invoice invoice = invoiceService.getOneInvoice(id);
            if (invoice != null) {
                logger.info("Successfully retrieved invoice with ID: {}", id);
                responseEntity = new ResponseEntity<>(invoice, HttpStatus.OK);
            } else {
                logger.warn("No invoice found with ID: {}", id);
                responseEntity = new ResponseEntity<>("Invoice not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            logger.error("Error occurred while retrieving invoice with ID: {}: {}", id, exception.getMessage());
            responseEntity = new ResponseEntity<>("Unable to find Invoice", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;

    }

    /**
     * To Delete One Invoice by providing id, returns Status as ResponseEntity<String>
     */

    @DeleteMapping("invoices/{id}")
    public ResponseEntity<String> deleteInvoice(@PathVariable Long id) {
        logger.info("Received request to delete invoice with ID: {}", id);

        ResponseEntity<String> responseEntity;
        try {
            invoiceService.deleteInvoice(id);
            logger.info("Successfully deleted invoice with ID: {}", id);
            responseEntity = new ResponseEntity<>("Invoice with ID " + id + " deleted", HttpStatus.OK);
        } catch (InvoiceNotFoundException ex) {
            logger.warn("Invoice with ID {} not found: {}", id, ex.getMessage());
            responseEntity = new ResponseEntity<>("Invoice not found", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.error("Error occurred while deleting invoice with ID: {}: {}", id, ex.getMessage());
            responseEntity = new ResponseEntity<>("Unable to delete invoice", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }
}



