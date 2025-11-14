package com.msuser.service;

import com.msuser.dto.CitizenDTO;
import com.msuser.entity.Citizen;
import com.msuser.repository.CitizenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CitizenService {

    private final CitizenRepository citizenRepository;

    @Transactional
    public CitizenDTO create(CitizenDTO dto) {
        if (citizenRepository.existsByDocumentNumber(dto.getDocumentNumber())) {
            throw new RuntimeException("Document number already exists");
        }

        Citizen citizen = Citizen.builder()
                .authUserId(dto.getAuthUserId())
                .documentNumber(dto.getDocumentNumber())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .birthDate(dto.getBirthDate())
                .gender(dto.getGender())
                .address(dto.getAddress())
                .district(dto.getDistrict())
                .province(dto.getProvince())
                .department(dto.getDepartment())
                .observations(dto.getObservations())
                .active(true)
                .build();

        citizen = citizenRepository.save(citizen);
        return toDTO(citizen);
    }

    @Transactional
    public CitizenDTO update(Long id, CitizenDTO dto) {
        Citizen citizen = citizenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Citizen not found"));

        citizen.setFirstName(dto.getFirstName());
        citizen.setLastName(dto.getLastName());
        citizen.setEmail(dto.getEmail());
        citizen.setPhoneNumber(dto.getPhoneNumber());
        citizen.setBirthDate(dto.getBirthDate());
        citizen.setGender(dto.getGender());
        citizen.setAddress(dto.getAddress());
        citizen.setDistrict(dto.getDistrict());
        citizen.setProvince(dto.getProvince());
        citizen.setDepartment(dto.getDepartment());
        citizen.setObservations(dto.getObservations());
        citizen.setUpdatedAt(java.time.LocalDateTime.now());

        citizen = citizenRepository.save(citizen);
        return toDTO(citizen);
    }

    @Transactional(readOnly = true)
    public CitizenDTO getById(Long id) {
        return citizenRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Citizen not found"));
    }

    @Transactional(readOnly = true)
    public CitizenDTO getByAuthUserId(Long authUserId) {
        return citizenRepository.findByAuthUserId(authUserId)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Citizen not found"));
    }

    @Transactional(readOnly = true)
    public Page<CitizenDTO> getAll(Pageable pageable) {
        return citizenRepository.findByActiveTrue(pageable)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<CitizenDTO> search(String query, Pageable pageable) {
        return citizenRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                query, query, pageable).map(this::toDTO);
    }

    @Transactional
    public void delete(Long id) {
        Citizen citizen = citizenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Citizen not found"));
        citizen.setActive(false);
        citizenRepository.save(citizen);
    }

    private CitizenDTO toDTO(Citizen citizen) {
        return CitizenDTO.builder()
                .id(citizen.getId())
                .authUserId(citizen.getAuthUserId())
                .documentNumber(citizen.getDocumentNumber())
                .firstName(citizen.getFirstName())
                .lastName(citizen.getLastName())
                .email(citizen.getEmail())
                .phoneNumber(citizen.getPhoneNumber())
                .birthDate(citizen.getBirthDate())
                .gender(citizen.getGender())
                .address(citizen.getAddress())
                .district(citizen.getDistrict())
                .province(citizen.getProvince())
                .department(citizen.getDepartment())
                .observations(citizen.getObservations())
                .active(citizen.getActive())
                .createdAt(citizen.getCreatedAt())
                .build();
    }
}
