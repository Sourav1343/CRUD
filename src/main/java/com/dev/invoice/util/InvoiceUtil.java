package com.dev.invoice.util;



import com.dev.invoice.Entity.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InvoiceUtil {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceUtil.class);

    public void CalculateFinalAmountIncludingGST(Invoice inv) {
        logger.info("Calculating final amount including GST for invoice: {}", inv); // Log the invoice before calculation

        var amount = inv.getAmount();
        var gst = 0.1;
        var finalAmount = amount + (amount * gst);
        inv.setFinalAmount(finalAmount);

        logger.debug("Calculated final amount: {}", finalAmount); // Log the calculated final amount
    }

    public void copyNonNullValues(Invoice req, Invoice db) {
        logger.info("Copying non-null values from request invoice: {} to database invoice: {}", req, db);

        if (req.getName() != null) {
            db.setName(req.getName());
            logger.debug("Copied name: {}", req.getName()); // Log the copied field
        }
        if (req.getAmount() != null) {
            db.setAmount(req.getAmount());
            logger.debug("Copied amount: {}", req.getAmount()); // Log the copied field
        }
        if (req.getNumber() != null) {
            db.setNumber(req.getNumber());
            logger.debug("Copied number: {}", req.getNumber()); // Log the copied field
        }
        if (req.getReceivedDate() != null) {
            db.setReceivedDate(req.getReceivedDate());
            logger.debug("Copied received date: {}", req.getReceivedDate()); // Log the copied field
        }
        if (req.getType() != null) {
            db.setType(req.getType());
            logger.debug("Copied type: {}", req.getType()); // Log the copied field
        }
        if (req.getVendor() != null) {
            db.setVendor(req.getVendor());
            logger.debug("Copied vendor: {}", req.getVendor()); // Log the copied field
        }
        if (req.getComments() != null) {
            db.setComments(req.getComments());
            logger.debug("Copied comments: {}", req.getComments()); // Log the copied field
        }
    }
}
