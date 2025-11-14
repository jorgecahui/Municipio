package com.msarea.repository;

import com.msarea.entity.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AreaRepository extends JpaRepository<Area, Long> {
    Optional<Area> findByCode(String code);
    List<Area> findByActiveTrue();
    List<Area> findByParentAreaId(Long parentAreaId);
    List<Area> findByParentAreaIdIsNull(); // Root areas
    Page<Area> findByActiveTrue(Pageable pageable);
    boolean existsByCode(String code);
    boolean existsByName(String name);
}