package com.msuser.controller;

import com.msuser.dto.CitizenDTO;
import com.msuser.service.CitizenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/citizens")
@RequiredArgsConstructor
public class CitizenController {

    private final CitizenService citizenService;

    @PostMapping
    public ResponseEntity<CitizenDTO> create(@Valid @RequestBody CitizenDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citizenService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitizenDTO> update(@PathVariable Long id, @Valid @RequestBody CitizenDTO dto) {
        return ResponseEntity.ok(citizenService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitizenDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(citizenService.getById(id));
    }

    @GetMapping("/auth-user/{authUserId}")
    public ResponseEntity<CitizenDTO> getByAuthUserId(@PathVariable Long authUserId) {
        return ResponseEntity.ok(citizenService.getByAuthUserId(authUserId));
    }

    @GetMapping
    public ResponseEntity<Page<CitizenDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(citizenService.getAll(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CitizenDTO>> search(@RequestParam String query, Pageable pageable) {
        return ResponseEntity.ok(citizenService.search(query, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        citizenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
