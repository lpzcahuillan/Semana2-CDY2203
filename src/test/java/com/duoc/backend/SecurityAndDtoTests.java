package com.duoc.backend;

import com.duoc.backend.appointment.Appointment;
import com.duoc.backend.appointment.AppointmentDTO;
import com.duoc.backend.care.Care;
import com.duoc.backend.care.CareDTO;
import com.duoc.backend.Invoice.Invoice;
import com.duoc.backend.Invoice.InvoiceDTO;
import com.duoc.backend.medication.Medication;
import com.duoc.backend.medication.MedicationDTO;
import com.duoc.backend.patient.Patient;
import com.duoc.backend.patient.PatientController;
import com.duoc.backend.patient.PatientDTO;
import com.duoc.backend.patient.PatientService;
import com.duoc.backend.user.LoginDTO;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class SecurityAndDtoTests {

    @Test
    void testConstantsJwtSecretAndSigningKey() {
        String secret = Constants.getJwtSecret();
        assertNotNull(secret);
        assertFalse(secret.isBlank());

        Key key = Constants.getSigningKey();
        assertNotNull(key);
        assertTrue(key.getAlgorithm().startsWith("HmacSHA"));
    }

    @Test
    void testSecuredControllerXssSanitization() {
        SecuredController controller = new SecuredController();
        String maliciousInput = "<script>alert('xss')</script>";
        String response = controller.greetings(maliciousInput);

        assertFalse(response.contains("<script>"));
        assertTrue(response.contains("&lt;script&gt;"));
    }

    @Test
    void testPatientControllerXssSanitization() {
        PatientController controller = new PatientController((PatientService) null);
        String maliciousInput = "<img src=x onerror=alert(1)>";
        String response = controller.greetings(maliciousInput);

        assertFalse(response.contains("<img"));
        assertTrue(response.contains("&lt;img"));
    }

    @Test
    void testPatientDtoConversion() {
        PatientDTO dto = new PatientDTO(1L, "Firulais", "Perro", "Pastor Aleman", 3, "Juan");
        Patient entity = dto.toEntity();

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getSpecies(), entity.getSpecies());
        assertEquals(dto.getBreed(), entity.getBreed());
        assertEquals(dto.getAge(), entity.getAge());
        assertEquals(dto.getOwner(), entity.getOwner());

        PatientDTO fromEntity = PatientDTO.fromEntity(entity);
        assertEquals(dto.getName(), fromEntity.getName());
    }

    @Test
    void testAppointmentDtoConversion() {
        LocalDate date = LocalDate.of(2026, 10, 12);
        LocalTime time = LocalTime.of(10, 30);
        AppointmentDTO dto = new AppointmentDTO(1L, date, time, "Vacunación", "Dr. Pérez");
        Appointment entity = dto.toEntity();

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getDate(), entity.getDate());
        assertEquals(dto.getTime(), entity.getTime());
        assertEquals(dto.getReason(), entity.getReason());
        assertEquals(dto.getVeterinarian(), entity.getVeterinarian());

        AppointmentDTO fromEntity = AppointmentDTO.fromEntity(entity);
        assertEquals(dto.getReason(), fromEntity.getReason());
    }

    @Test
    void testCareDtoConversion() {
        CareDTO dto = new CareDTO(1L, "Consulta general", 15000.0);
        Care entity = dto.toEntity();

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getCost(), entity.getCost());

        CareDTO fromEntity = CareDTO.fromEntity(entity);
        assertEquals(dto.getName(), fromEntity.getName());
    }

    @Test
    void testMedicationDtoConversion() {
        MedicationDTO dto = new MedicationDTO(1L, "Amoxicilina", 8500.0);
        Medication entity = dto.toEntity();

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getCost(), entity.getCost());

        MedicationDTO fromEntity = MedicationDTO.fromEntity(entity);
        assertEquals(dto.getName(), fromEntity.getName());
    }

    @Test
    void testInvoiceDtoConversion() {
        CareDTO careDTO = new CareDTO(1L, "Consulta", 15000.0);
        MedicationDTO medDTO = new MedicationDTO(2L, "Vacuna", 5000.0);
        InvoiceDTO dto = new InvoiceDTO(10L, "Firulais", LocalDate.now(), LocalTime.now(),
                Collections.singletonList(careDTO), Collections.singletonList(medDTO), 20000.0);

        Invoice entity = dto.toEntity();
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getPatientName(), entity.getPatientName());
        assertEquals(1, entity.getCares().size());
        assertEquals(1, entity.getMedications().size());

        InvoiceDTO fromEntity = InvoiceDTO.fromEntity(entity);
        assertEquals(dto.getPatientName(), fromEntity.getPatientName());
        assertEquals(1, fromEntity.getCares().size());
    }

    @Test
    void testLoginDto() {
        LoginDTO dto = new LoginDTO("admin", "secret123");
        assertEquals("admin", dto.getUsername());
        assertEquals("secret123", dto.getPassword());

        dto.setUsername("user2");
        dto.setPassword("pass2");
        assertEquals("user2", dto.getUsername());
        assertEquals("pass2", dto.getPassword());
    }

    @Test
    void testInvoiceServiceNullSafety() {
        com.duoc.backend.Invoice.InvoiceRepository invoiceRepo = new com.duoc.backend.Invoice.InvoiceRepository() {
            @Override public <S extends Invoice> S save(S entity) { return entity; }
            @Override public <S extends Invoice> Iterable<S> saveAll(Iterable<S> entities) { return null; }
            @Override public java.util.Optional<Invoice> findById(Long id) { return java.util.Optional.empty(); }
            @Override public boolean existsById(Long id) { return false; }
            @Override public Iterable<Invoice> findAll() { return Collections.emptyList(); }
            @Override public Iterable<Invoice> findAllById(Iterable<Long> ids) { return Collections.emptyList(); }
            @Override public long count() { return 0; }
            @Override public void deleteById(Long id) {}
            @Override public void delete(Invoice entity) {}
            @Override public void deleteAllById(Iterable<? extends Long> ids) {}
            @Override public void deleteAll(Iterable<? extends Invoice> entities) {}
            @Override public void deleteAll() {}
        };

        com.duoc.backend.medication.MedicationRepository medRepo = new com.duoc.backend.medication.MedicationRepository() {
            @Override public <S extends Medication> S save(S entity) { return null; }
            @Override public <S extends Medication> Iterable<S> saveAll(Iterable<S> entities) { return null; }
            @Override public java.util.Optional<Medication> findById(Long id) { return java.util.Optional.empty(); }
            @Override public boolean existsById(Long id) { return false; }
            @Override public Iterable<Medication> findAll() { return Collections.emptyList(); }
            @Override public Iterable<Medication> findAllById(Iterable<Long> ids) { return Collections.emptyList(); }
            @Override public long count() { return 0; }
            @Override public void deleteById(Long id) {}
            @Override public void delete(Medication entity) {}
            @Override public void deleteAllById(Iterable<? extends Long> ids) {}
            @Override public void deleteAll(Iterable<? extends Medication> entities) {}
            @Override public void deleteAll() {}
        };

        com.duoc.backend.care.CareRepository careRepo = new com.duoc.backend.care.CareRepository() {
            @Override public <S extends Care> S save(S entity) { return null; }
            @Override public <S extends Care> Iterable<S> saveAll(Iterable<S> entities) { return null; }
            @Override public java.util.Optional<Care> findById(Long id) { return java.util.Optional.empty(); }
            @Override public boolean existsById(Long id) { return false; }
            @Override public Iterable<Care> findAll() { return Collections.emptyList(); }
            @Override public Iterable<Care> findAllById(Iterable<Long> ids) { return Collections.emptyList(); }
            @Override public long count() { return 0; }
            @Override public void deleteById(Long id) {}
            @Override public void delete(Care entity) {}
            @Override public void deleteAllById(Iterable<? extends Long> ids) {}
            @Override public void deleteAll(Iterable<? extends Care> entities) {}
            @Override public void deleteAll() {}
        };

        com.duoc.backend.Invoice.InvoiceService service = new com.duoc.backend.Invoice.InvoiceService(invoiceRepo, medRepo, careRepo);

        Invoice invoiceWithNullLists = new Invoice();
        invoiceWithNullLists.setPatientName("Firulais");
        invoiceWithNullLists.setCares(null);
        invoiceWithNullLists.setMedications(null);

        // Before the fix, this threw NullPointerException at line 42 / 52
        assertDoesNotThrow(() -> {
            Invoice saved = service.saveInvoice(invoiceWithNullLists);
            assertNotNull(saved);
            assertEquals(0.0, saved.getTotalCost());
        });

        assertNull(service.getInvoiceById(null));
        assertDoesNotThrow(() -> service.deleteInvoice(null));
    }
}
