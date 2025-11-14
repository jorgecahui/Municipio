package com.msarea.service;

import com.msarea.dto.AreaDTO;
import com.msarea.entity.Area;
import com.msarea.repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AreaService {

    private final AreaRepository areaRepository;

    @Transactional
    public AreaDTO create(AreaDTO dto) {
        if (areaRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Area code already exists");
        }
        if (areaRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Area name already exists");
        }

        Area area = Area.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .description(dto.getDescription())
                .parentAreaId(dto.getParentAreaId())
                .supervisorId(dto.getSupervisorId())
                .location(dto.getLocation())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .active(true)
                .build();

        area = areaRepository.save(area);
        return toDTO(area);
    }

    @Transactional
    public AreaDTO update(Long id, AreaDTO dto) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Area not found"));

        area.setName(dto.getName());
        area.setDescription(dto.getDescription());
        area.setParentAreaId(dto.getParentAreaId());
        area.setSupervisorId(dto.getSupervisorId());
        area.setLocation(dto.getLocation());
        area.setPhone(dto.getPhone());
        area.setEmail(dto.getEmail());
        area.setUpdatedAt(LocalDateTime.now());

        area = areaRepository.save(area);
        return toDTO(area);
    }

    @Transactional(readOnly = true)
    public AreaDTO getById(Long id) {
        return areaRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Area not found"));
    }

    @Transactional(readOnly = true)
    public AreaDTO getByCode(String code) {
        return areaRepository.findByCode(code)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Area not found"));
    }

    @Transactional(readOnly = true)
    public List<AreaDTO> getAll() {
        return areaRepository.findByActiveTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<AreaDTO> getAllPaged(Pageable pageable) {
        return areaRepository.findByActiveTrue(pageable)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public List<AreaDTO> getRootAreas() {
        return areaRepository.findByParentAreaIdIsNull().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AreaDTO> getSubAreas(Long parentId) {
        return areaRepository.findByParentAreaId(parentId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Area not found"));

        // Check if area has sub-areas
        List<Area> subAreas = areaRepository.findByParentAreaId(id);
        if (!subAreas.isEmpty()) {
            throw new RuntimeException("Cannot delete area with sub-areas");
        }

        area.setActive(false);
        area.setUpdatedAt(LocalDateTime.now());
        areaRepository.save(area);
    }

    private AreaDTO toDTO(Area area) {
        return AreaDTO.builder()
                .id(area.getId())
                .name(area.getName())
                .code(area.getCode())
                .description(area.getDescription())
                .parentAreaId(area.getParentAreaId())
                .supervisorId(area.getSupervisorId())
                .location(area.getLocation())
                .phone(area.getPhone())
                .email(area.getEmail())
                .active(area.getActive())
                .createdAt(area.getCreatedAt())
                .updatedAt(area.getUpdatedAt())
                .build();
    }
}
