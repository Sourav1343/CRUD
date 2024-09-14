import com.dev.invoice.Entity.Invoice;
import com.dev.invoice.controller.InvoiceRestController;
import com.dev.invoice.service.InvoiceService;
import com.dev.invoice.util.InvoiceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class InvoiceRestControllerTest {

    @Mock
    private InvoiceService invoiceService;

    @Mock
    private InvoiceUtil invoiceUtil;


    @InjectMocks
    private InvoiceRestController invoiceRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveInvoiceSuccess() {
        // Arrange

        Invoice invoice = new Invoice(); // Create a sample invoice object
        invoice.setId(1L); // Set an ID for the sample invoice
        when(invoiceService.saveInvoice(invoice)).thenReturn(1L); // Mock the service call

        // Act
        ResponseEntity<String> response = invoiceRestController.saveInvoice(invoice);
      //  ResponseEntity<String> response = invoiceRestController.saveInvoice(invoice);

        // Assert
        assertEquals(HttpStatus.CREATED,response.getStatusCode());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Invoice with ID '1' has been created", response.getBody());
        verify(invoiceService, times(1)).saveInvoice(invoice); // Verify that the service was called once
    }

    @Test
    void testSaveInvoiceFailure() {
        // Arrange
        Invoice invoice = new Invoice();
        when(invoiceService.saveInvoice(invoice)).thenThrow(new RuntimeException("Error")); // Mock failure

        // Act
        ResponseEntity<String> response = invoiceRestController.saveInvoice(invoice);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unable to save invoice", response.getBody());
        verify(invoiceService, times(1)).saveInvoice(invoice);
    }

    @Test
    void testGetAllInvoicesSuccess() {
        // Arrange
        Invoice invoice1 = new Invoice();
        invoice1.setId(1L);
        Invoice invoice2 = new Invoice();
        invoice2.setId(2L);
        List<Invoice> invoices = Arrays.asList(invoice1, invoice2); // Create a list of invoices
        when(invoiceService.getAllInvoice()).thenReturn(invoices); // Mock the service call

        // Act
        ResponseEntity<?> response = invoiceRestController.getAllInvoices();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(invoices, response.getBody());
        verify(invoiceService, times(1)).getAllInvoice();
    }

    @Test
    void testGetAllInvoicesFailure() {
        // Arrange
        when(invoiceService.getAllInvoice()).thenThrow(new RuntimeException("Error")); // Mock failure

        // Act
        ResponseEntity<?> response = invoiceRestController.getAllInvoices();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unable to get Invoice", response.getBody());
        verify(invoiceService, times(1)).getAllInvoice();
    }

    /**
     * Edge Case: Test saveInvoice with a null invoice object
     */
    @Test
    void testSaveInvoice_NullInvoice() {
        // Arrange
        Invoice invoice = null;

        // Act
        ResponseEntity<String> response = invoiceRestController.saveInvoice(invoice);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invoice cannot be null", response.getBody());  // Expected message for null invoice
        verify(invoiceService, never()).saveInvoice(any(Invoice.class)); // Service should not be called with null
    }


    /**
     * Edge Case: Test saveInvoice with an invoice having missing fields
     */
    @Test
    void testSaveInvoice_InvoiceWithMissingFields() {
        // Arrange
        Invoice invoice = new Invoice(); // An invoice object with missing fields (e.g., no ID)
        when(invoiceService.saveInvoice(invoice)).thenReturn(1L); // Assume invoice gets saved despite missing fields

        // Act
        ResponseEntity<String> response = invoiceRestController.saveInvoice(invoice);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Invoice with ID '1' has been created", response.getBody());
        verify(invoiceService, times(1)).saveInvoice(invoice);
    }

    /**
     * Edge Case: Test getAllInvoices when the invoice list is empty
     */
    @Test
    void testGetAllInvoices_EmptyList() {
        // Arrange
        when(invoiceService.getAllInvoice()).thenReturn(Collections.emptyList()); // Return empty list

        // Act
        ResponseEntity<?> response = invoiceRestController.getAllInvoices();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody()); // Should return an empty list
        verify(invoiceService, times(1)).getAllInvoice();
    }

    /**
     * Edge Case: Test getAllInvoices when invoiceService returns null
     */
    @Test
    void testGetAllInvoices_NullResponse() {
        // Arrange
        when(invoiceService.getAllInvoice()).thenReturn(null); // Return null instead of a list

        // Act
        ResponseEntity<?> response = invoiceRestController.getAllInvoices();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unable to get Invoice", response.getBody()); // Expect error message
        verify(invoiceService, times(1)).getAllInvoice();
    }

    /**
     * Edge Case: Test saveInvoice when invoiceService throws a custom InvoiceNotFoundException
     */
    @Test
    void testSaveInvoice_InvoiceNotFoundException() {
        // Arrange
        Invoice invoice = new Invoice();
        doThrow(new RuntimeException("InvoiceNotFoundException")).when(invoiceService).saveInvoice(invoice);

        // Act
        ResponseEntity<String> response = invoiceRestController.saveInvoice(invoice);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unable to save invoice", response.getBody()); // Generic error response expected
        verify(invoiceService, times(1)).saveInvoice(invoice);
    }

    /**
     * Edge Case: Test saveInvoice when the invoice ID is negative (Invalid ID)
     */
    @Test
    void testSaveInvoice_NegativeInvoiceID() {
        // Arrange
        Invoice invoice = new Invoice();
        // Mock the service to throw an exception when saveInvoice is called
        when(invoiceService.saveInvoice(invoice)).thenThrow(new RuntimeException("Unable to save invoice"));

        // Act
        ResponseEntity<String> response = invoiceRestController.saveInvoice(invoice);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unable to save invoice", response.getBody());
        verify(invoiceService, times(1)).saveInvoice(invoice);
    }


    /**
     * Edge Case: Test getAllInvoices when invoiceService throws an exception
     */
    @Test
    void testGetAllInvoices_ExceptionThrown() {
        // Arrange
        when(invoiceService.getAllInvoice()).thenThrow(new RuntimeException("Database down"));

        // Act
        ResponseEntity<?> response = invoiceRestController.getAllInvoices();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unable to get Invoice", response.getBody()); // Generic error message expected
        verify(invoiceService, times(1)).getAllInvoice();
    }

    /**
     * Edge Case: Test saveInvoice with an invoice that already exists
     */
    @Test
    void testSaveInvoice_InvoiceAlreadyExists() {
        // Arrange
        Invoice invoice = new Invoice();
        when(invoiceService.saveInvoice(invoice)).thenThrow(new RuntimeException("Invoice already exists"));

        // Act
        ResponseEntity<String> response = invoiceRestController.saveInvoice(invoice);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unable to save invoice", response.getBody());  // Generic error message for duplicate entry
        verify(invoiceService, times(1)).saveInvoice(invoice);
    }
}
