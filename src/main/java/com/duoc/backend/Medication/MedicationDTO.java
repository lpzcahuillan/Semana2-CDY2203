package com.duoc.backend.medication;

public class MedicationDTO {
    private Long id;
    private String name;
    private Double cost;

    public MedicationDTO() {
    }

    public MedicationDTO(Long id, String name, Double cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
    }

    public static MedicationDTO fromEntity(Medication medication) {
        if (medication == null) {
            return null;
        }
        return new MedicationDTO(medication.getId(), medication.getName(), medication.getCost());
    }

    public Medication toEntity() {
        Medication medication = new Medication();
        medication.setId(this.id);
        medication.setName(this.name);
        medication.setCost(this.cost);
        return medication;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}
