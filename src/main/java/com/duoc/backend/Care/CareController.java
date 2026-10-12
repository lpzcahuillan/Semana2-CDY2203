package com.duoc.backend.care;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/care")
public class CareController {

    private final CareService careService;

    public CareController(CareService careService) {
        this.careService = careService;
    }

    @GetMapping
    public List<Care> getAllCares() {
        return careService.getAllCares();
    }

    @GetMapping("/{id}")
    public Care getCareById(@PathVariable Long id) {
        return careService.getCareById(id);
    }

    @PostMapping
    public Care saveCare(@RequestBody CareDTO careDTO) {
        return careService.saveCare(careDTO.toEntity());
    }

    @DeleteMapping("/{id}")
    public void deleteCare(@PathVariable Long id) {
        careService.deleteCare(id);
    }
}