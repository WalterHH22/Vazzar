package com.app.vaazar.billing.infrastructure.web;

import com.app.vaazar.billing.infrastructure.persistence.BillingCustomerJpaEntity;
import com.app.vaazar.billing.infrastructure.persistence.InvoiceItemJpaEntity;
import com.app.vaazar.billing.infrastructure.persistence.InvoiceJpaEntity;
import com.app.vaazar.billing.infrastructure.persistence.SpringDataBillingCustomerRepository;
import com.app.vaazar.billing.infrastructure.persistence.SpringDataInvoiceItemRepository;
import com.app.vaazar.billing.infrastructure.persistence.SpringDataInvoiceRepository;
import com.app.vaazar.billing.infrastructure.web.dto.CheckoutItemRequest;
import com.app.vaazar.billing.infrastructure.web.dto.CheckoutRequest;
import com.app.vaazar.billing.infrastructure.web.dto.InvoiceItemResponse;
import com.app.vaazar.billing.infrastructure.web.dto.InvoiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin(origins = "*")
public class BillingController {

    private final SpringDataBillingCustomerRepository customerRepository;
    private final SpringDataInvoiceRepository invoiceRepository;
    private final SpringDataInvoiceItemRepository invoiceItemRepository;

    public BillingController(SpringDataBillingCustomerRepository customerRepository,
                             SpringDataInvoiceRepository invoiceRepository,
                             SpringDataInvoiceItemRepository invoiceItemRepository) {
        this.customerRepository = customerRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoiceItemRepository = invoiceItemRepository;
    }

    @PostMapping("/checkout")
    @Transactional
    public ResponseEntity<?> processCheckout(@RequestBody CheckoutRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body("El pedido debe contener al menos un producto.");
        }

        String customerName = request.getCustomerName() != null && !request.getCustomerName().isBlank()
                ? request.getCustomerName().trim()
                : "Cliente Vazzar";

        String taxId = request.getTaxId() != null && !request.getTaxId().isBlank()
                ? request.getTaxId().trim()
                : "CF - Consumidor Final";

        UUID parsedUserId;
        try {
            parsedUserId = (request.getUserId() != null && !request.getUserId().isBlank())
                    ? UUID.fromString(request.getUserId().trim())
                    : UUID.randomUUID();
        } catch (Exception e) {
            parsedUserId = UUID.randomUUID();
        }
        final UUID userId = parsedUserId;

        // 1. Obtener o crear BillingCustomer
        BillingCustomerJpaEntity customer = customerRepository.findFirstByUserId(userId)
                .orElseGet(() -> {
                    BillingCustomerJpaEntity newCust = new BillingCustomerJpaEntity(
                            UUID.randomUUID(),
                            userId,
                            customerName,
                            taxId
                    );
                    return customerRepository.save(newCust);
                });

        // Actualizar taxId si viene especificado
        if (request.getTaxId() != null && !request.getTaxId().isBlank() && !request.getTaxId().equals(customer.getTaxId())) {
            customer.setTaxId(request.getTaxId().trim());
            customer.setName(customerName);
            customer = customerRepository.save(customer);
        }

        // 2. Calcular total de la factura
        BigDecimal total = BigDecimal.ZERO;
        for (CheckoutItemRequest item : request.getItems()) {
            BigDecimal price = item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO;
            int qty = item.getQuantity() != null && item.getQuantity() > 0 ? item.getQuantity() : 1;
            total = total.add(price.multiply(BigDecimal.valueOf(qty)));
        }

        // 3. Crear Factura
        UUID invoiceId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        String sourceChannel = request.getSourceChannel() != null && !request.getSourceChannel().isBlank()
                ? request.getSourceChannel().toLowerCase()
                : "ecommerce";

        InvoiceJpaEntity invoice = new InvoiceJpaEntity(
                invoiceId,
                orderId,
                sourceChannel,
                customer,
                total,
                "issued"
        );

        // 4. Crear Items de Factura
        for (CheckoutItemRequest item : request.getItems()) {
            UUID prodId;
            try {
                prodId = (item.getProductId() != null && !item.getProductId().isBlank())
                        ? UUID.fromString(item.getProductId().trim())
                        : UUID.randomUUID();
            } catch (Exception e) {
                prodId = UUID.randomUUID();
            }
            String prodName = item.getProductName() != null && !item.getProductName().isBlank()
                    ? item.getProductName().trim()
                    : "Producto";
            BigDecimal unitPrice = item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO;
            int qty = item.getQuantity() != null && item.getQuantity() > 0 ? item.getQuantity() : 1;

            InvoiceItemJpaEntity itemEntity = new InvoiceItemJpaEntity(
                    UUID.randomUUID(),
                    invoice,
                    prodId,
                    prodName,
                    qty,
                    unitPrice
            );
            invoice.addItem(itemEntity);
        }

        InvoiceJpaEntity savedInvoice = invoiceRepository.save(invoice);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(savedInvoice, request.getPaymentMethod()));
    }

    @GetMapping("/invoices")
    public ResponseEntity<List<InvoiceResponse>> listInvoices() {
        List<InvoiceJpaEntity> invoices = invoiceRepository.findAllByOrderByIssuedAtDesc();
        List<InvoiceResponse> responses = invoices.stream()
                .map(inv -> mapToResponse(inv, "credit_card"))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/invoices/{id}")
    public ResponseEntity<InvoiceResponse> getInvoice(@PathVariable("id") UUID id) {
        Optional<InvoiceJpaEntity> opt = invoiceRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapToResponse(opt.get(), "credit_card"));
    }

    private InvoiceResponse mapToResponse(InvoiceJpaEntity invoice, String paymentMethod) {
        String num = "FAC-" + Year.now().getValue() + "-" + invoice.getId().toString().substring(0, 8).toUpperCase();

        List<InvoiceItemResponse> itemResponses = new ArrayList<>();
        if (invoice.getItems() != null) {
            for (InvoiceItemJpaEntity item : invoice.getItems()) {
                BigDecimal sub = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                itemResponses.add(new InvoiceItemResponse(
                        item.getId(),
                        item.getProductId(),
                        item.getProductName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        sub
                ));
            }
        }

        return new InvoiceResponse(
                invoice.getId(),
                invoice.getOrderId(),
                num,
                invoice.getSourceChannel(),
                invoice.getCustomer().getId(),
                invoice.getCustomer().getName(),
                invoice.getCustomer().getTaxId(),
                invoice.getTotal(),
                invoice.getStatus(),
                invoice.getIssuedAt(),
                paymentMethod != null ? paymentMethod : "Tarjeta de Crédito / Débito",
                itemResponses
        );
    }
}
