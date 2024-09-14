package invoice.service;

import com.dev.invoice.Entity.Invoice;
import com.dev.invoice.dao.InvoiceRepository;
import com.dev.invoice.exception.InvoiceNotFoundException;
import com.dev.invoice.service.impl.InvoiceServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class InvoiceServiceImplTest {
    @Mock
    private InvoiceRepository repo;

    @Mock
    private InvoiceServiceImpl invoiceService;

    @InjectMocks
    private InvoiceServiceImpl invoiceServiceImpl;

    private static final Long INVOICE_ID = 1L;

    @Test
    void testDeleteInvoice_Success() {
        // Arrange
        Invoice invoice = new Invoice();  // Create a mock Invoice object
        when(invoiceService.getOneInvoice(INVOICE_ID)).thenReturn(invoice);

        // Act
        invoiceServiceImpl.deleteInvoice(INVOICE_ID);

        // Assert
        verify(invoiceService, times(1)).getOneInvoice(INVOICE_ID);  // Ensure invoice retrieval
        verify(repo, times(1)).delete(invoice);  // Ensure delete operation is called
        verifyNoMoreInteractions(repo);  // Ensure no other repository interactions occurred
    }

    @Test
    void testDeleteInvoice_InvoiceNotFound() {
        // Arrange
        when(invoiceService.getOneInvoice(INVOICE_ID)).thenThrow(new InvoiceNotFoundException("Invoice not found"));

        // Act & Assert
        InvoiceNotFoundException exception = assertThrows(InvoiceNotFoundException.class, () -> {
            invoiceServiceImpl.deleteInvoice(INVOICE_ID);
        });

        assertEquals("Invoice not found", exception.getMessage());
        verify(invoiceService, times(1)).getOneInvoice(INVOICE_ID);
        verify(repo, never()).delete(any(Invoice.class));  // Ensure delete is never called
    }



}
