package com.duoc.backend.Invoice;

import com.duoc.backend.care.CareDTO;
import com.duoc.backend.medication.MedicationDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceDTO {
    private Long id;
    private String patientName;
    private LocalDate date;
    private LocalTime time;
    private List<CareDTO> cares;
    private List<MedicationDTO> medications;
    private Double totalCost;

    public InvoiceDTO() {
    }

    public InvoiceDTO(Long id, String patientName, LocalDate date, LocalTime time,
                      List<CareDTO> cares, List<MedicationDTO> medications, Double totalCost) {
        this.id = id;
        this.patientName = patientName;
        this.date = date;
        this.time = time;
        this.cares = cares;
        this.medications = medications;
        this.totalCost = totalCost;
    }

    public static InvoiceDTO fromEntity(Invoice invoice) {
        if (invoice == null) {
            return null;
        }
        List<CareDTO> careDTOs = null;
        if (invoice.getCares() != null) {
            careDTOs = invoice.getCares().stream()
                    .map(CareDTO::fromEntity)
                    .collect(Collectors.toList());
        }

        List<MedicationDTO> medicationDTOs = null;
        if (invoice.getMedications() != null) {
            medicationDTOs = invoice.getMedications().stream()
                    .map(MedicationDTO::fromEntity)
                    .collect(Collectors.toList());
        }

        return new InvoiceDTO(
                invoice.getId(),
                invoice.getPatientName(),
                invoice.getDate(),
                invoice.getTime(),
                careDTOs,
                medicationDTOs,
                invoice.getTotalCost()
        );
    }

    public Invoice toEntity() {
        Invoice invoice = new Invoice();
        invoice.setId(this.id);
        invoice.setPatientName(this.patientName);
        invoice.setDate(this.date);
        invoice.setTime(this.time);
        invoice.setTotalCost(this.totalCost);

        if (this.cares != null) {
            invoice.setCares(this.cares.stream()
                    .map(CareDTO::toEntity)
                    .collect(Collectors.toList()));
        }

        if (this.medications != null) {
            invoice.setMedications(this.medications.stream()
                    .map(MedicationDTO::toEntity)
                    .collect(Collectors.toList()));
        }

        return invoice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public List<CareDTO> getCares() {
        return cares;
    }

    public void setCares(List<CareDTO> cares) {
        this.cares = cares;
    }

    public List<MedicationDTO> getMedications() {
        return medications;
    }

    public void setMedications(List<MedicationDTO> medications) {
        this.medications = medications;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }
}
