package com.dev.invoice.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Invoice {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Invoice name cannot be blank")
    @Size(min = 3, max = 100, message = "Invoice name must be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private Double amount;

    @NotNull(message = "Final amount cannot be null")
    @DecimalMin(value = "0.01", message = "Final amount must be greater than 0")
    private Double finalAmount;

    @NotBlank(message = "Invoice number cannot be blank")
    @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "Invoice number can only contain alphanumeric characters and hyphens")
    private String number;

    @NotBlank(message = "Received date cannot be blank")
    // You can add a more specific pattern for dates if necessary, e.g., YYYY-MM-DD format
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Received date must be in the format YYYY-MM-DD")
    private String receivedDate;

    @NotBlank(message = "Type cannot be blank")
    private String type;

    @NotBlank(message = "Vendor cannot be blank")
    private String vendor;

    @Size(max = 255, message = "Comments must not exceed 255 characters")
    private String comments;
}
