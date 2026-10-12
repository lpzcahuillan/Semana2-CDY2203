package com.duoc.backend.Invoice;

import com.duoc.backend.care.Care;
import com.duoc.backend.care.CareRepository;
import com.duoc.backend.medication.Medication;
import com.duoc.backend.medication.MedicationRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final MedicationRepository medicationRepository;
    private final CareRepository careRepository;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          MedicationRepository medicationRepository,
                          CareRepository careRepository) {
        this.invoiceRepository = invoiceRepository;
        this.medicationRepository = medicationRepository;
        this.careRepository = careRepository;
    }

    public Iterable<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getInvoiceById(Long id) {
        if (id == null) {
            return null;
        }
        return invoiceRepository.findById(id).orElse(null);
    }

    public Invoice saveInvoice(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("La factura no puede ser nula.");
        }

        List<Medication> medications = invoice.getMedications() != null
                ? invoice.getMedications()
                : Collections.emptyList();

        List<Care> cares = invoice.getCares() != null
                ? invoice.getCares()
                : Collections.emptyList();

        // Validar que los medicamentos existen
        List<Medication> validMedications = Collections.emptyList();
        if (!medications.isEmpty()) {
            List<Long> medicationIds = medications.stream()
                    .filter(Objects::nonNull)
                    .map(Medication::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            validMedications = StreamSupport.stream(
                    medicationRepository.findAllById(medicationIds).spliterator(), false
            ).collect(Collectors.toList());

            if (validMedications.size() != medications.size()) {
                throw new IllegalArgumentException("Algunos medicamentos no existen en la base de datos.");
            }
        }

        // Validar que los servicios existen
        List<Care> validCares = Collections.emptyList();
        if (!cares.isEmpty()) {
            List<Long> careIds = cares.stream()
                    .filter(Objects::nonNull)
                    .map(Care::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            validCares = StreamSupport.stream(
                    careRepository.findAllById(careIds).spliterator(), false
            ).collect(Collectors.toList());

            if (validCares.size() != cares.size()) {
                throw new IllegalArgumentException("Algunos servicios no existen en la base de datos.");
            }
        }

        // Calcular el costo total basado en los servicios y medicamentos asociados
        double totalCareCost = validCares.stream()
                .filter(Objects::nonNull)
                .mapToDouble(c -> c.getCost() != null ? c.getCost() : 0.0)
                .sum();

        double totalMedicationCost = validMedications.stream()
                .filter(Objects::nonNull)
                .mapToDouble(m -> m.getCost() != null ? m.getCost() : 0.0)
                .sum();

        invoice.setCares(validCares);
        invoice.setMedications(validMedications);
        invoice.setTotalCost(totalCareCost + totalMedicationCost);

        // Guardar la factura en el repositorio
        return invoiceRepository.save(invoice);
    }

    public void deleteInvoice(Long id) {
        if (id != null) {
            invoiceRepository.deleteById(id);
        }
    }
}