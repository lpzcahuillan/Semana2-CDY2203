package com.duoc.backend.patient;

public class PatientDTO {
    private Long id;
    private String name;
    private String species;
    private String breed;
    private int age;
    private String owner;

    public PatientDTO() {
    }

    public PatientDTO(Long id, String name, String species, String breed, int age, String owner) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.owner = owner;
    }

    public static PatientDTO fromEntity(Patient patient) {
        if (patient == null) {
            return null;
        }
        return new PatientDTO(
                patient.getId(),
                patient.getName(),
                patient.getSpecies(),
                patient.getBreed(),
                patient.getAge(),
                patient.getOwner()
        );
    }

    public Patient toEntity() {
        Patient patient = new Patient();
        patient.setId(this.id);
        patient.setName(this.name);
        patient.setSpecies(this.species);
        patient.setBreed(this.breed);
        patient.setAge(this.age);
        patient.setOwner(this.owner);
        return patient;
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

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
