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
        ResponseEntity<String> responseEntity = null;
        logger.info("Received request to save invoice: {}", invoice); // Log the received invoice

        try {
            logger.debug("Attempting to save the invoice...");
            Long id = invoiceService.saveInvoice(invoice); // Save invoice
            responseEntity = new ResponseEntity<>("Invoice with ID '" + id + "' has been created", HttpStatus.CREATED);
        } catch (Exception exception) {
            logger.error("Error occurred while saving invoice: {}", exception.getMessage()); // Log the error
            responseEntity = new ResponseEntity<>("Unable to save invoice", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.debug("Returning response: {}", responseEntity); // Log the final response
        return responseEntity;
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


}



