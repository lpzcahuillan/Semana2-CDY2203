package com.duoc.backend.care;

public class CareDTO {
    private Long id;
    private String name;
    private Double cost;

    public CareDTO() {
    }

    public CareDTO(Long id, String name, Double cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
    }

    public static CareDTO fromEntity(Care care) {
        if (care == null) {
            return null;
        }
        return new CareDTO(care.getId(), care.getName(), care.getCost());
    }

    public Care toEntity() {
        Care care = new Care();
        care.setId(this.id);
        care.setName(this.name);
        care.setCost(this.cost);
        return care;
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
