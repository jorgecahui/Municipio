package com.msarea.controller;

import com.msarea.dto.AreaDTO;
import com.msarea.service.AreaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/areas")
@RequiredArgsConstructor
public class AreaController {

    private final AreaService areaService;

    @PostMapping
    public ResponseEntity<AreaDTO> create(@Valid @RequestBody AreaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(areaService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody AreaDTO dto) {
        return ResponseEntity.ok(areaService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(areaService.getById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<AreaDTO> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(areaService.getByCode(code));
    }

    @GetMapping
    public ResponseEntity<List<AreaDTO>> getAll() {
        return ResponseEntity.ok(areaService.getAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<AreaDTO>> getAllPaged(Pageable pageable) {
        return ResponseEntity.ok(areaService.getAllPaged(pageable));
    }

    @GetMapping("/roots")
    public ResponseEntity<List<AreaDTO>> getRootAreas() {
        return ResponseEntity.ok(areaService.getRootAreas());
    }

    @GetMapping("/{parentId}/sub-areas")
    public ResponseEntity<List<AreaDTO>> getSubAreas(@PathVariable Long parentId) {
        return ResponseEntity.ok(areaService.getSubAreas(parentId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        areaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
