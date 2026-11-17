package com.example.user_service.repository;

import com.example.user_service.entity.MdWard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MdWardRepository extends JpaRepository<MdWard,String> {
    List<MdWard> findAllByProvinceCode(String provinceCode);
}
