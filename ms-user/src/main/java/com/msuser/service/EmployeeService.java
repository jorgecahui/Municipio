package com.msuser.service;

import com.msuser.dto.EmployeeDTO;
import com.msuser.entity.Employee;
import com.msuser.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional
    public EmployeeDTO create(EmployeeDTO dto) {
        if (employeeRepository.existsByEmployeeCode(dto.getEmployeeCode())) {
            throw new RuntimeException("Employee code already exists");
        }
        if (employeeRepository.existsByDocumentNumber(dto.getDocumentNumber())) {
            throw new RuntimeException("Document number already exists");
        }

        Employee employee = Employee.builder()
                .authUserId(dto.getAuthUserId())
                .employeeCode(dto.getEmployeeCode())
                .documentNumber(dto.getDocumentNumber())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .position(dto.getPosition())
                .areaId(dto.getAreaId())
                .hireDate(dto.getHireDate())
                .active(true)
                .build();

        employee = employeeRepository.save(employee);
        return toDTO(employee);
    }

    @Transactional
    public EmployeeDTO update(Long id, EmployeeDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setPosition(dto.getPosition());
        employee.setAreaId(dto.getAreaId());
        employee.setUpdatedAt(java.time.LocalDateTime.now());

        employee = employeeRepository.save(employee);
        return toDTO(employee);
    }

    @Transactional(readOnly = true)
    public EmployeeDTO getById(Long id) {
        return employeeRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Transactional(readOnly = true)
    public EmployeeDTO getByAuthUserId(Long authUserId) {
        return employeeRepository.findByAuthUserId(authUserId)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Transactional(readOnly = true)
    public Page<EmployeeDTO> getAll(Pageable pageable) {
        return employeeRepository.findByActiveTrue(pageable)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDTO> getByArea(Long areaId) {
        return employeeRepository.findByAreaId(areaId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setActive(false);
        employeeRepository.save(employee);
    }

    private EmployeeDTO toDTO(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .authUserId(employee.getAuthUserId())
                .employeeCode(employee.getEmployeeCode())
                .documentNumber(employee.getDocumentNumber())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .position(employee.getPosition())
                .areaId(employee.getAreaId())
                .hireDate(employee.getHireDate())
                .active(employee.getActive())
                .createdAt(employee.getCreatedAt())
                .build();
    }
}
