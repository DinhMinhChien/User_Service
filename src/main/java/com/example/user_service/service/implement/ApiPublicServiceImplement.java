package com.example.user_service.service.implement;

import com.example.user_service.dto.response.ProvinceItemResponse;
import com.example.user_service.dto.response.WardItemResponse;
import com.example.user_service.entity.MdProvince;
import com.example.user_service.entity.MdWard;
import com.example.user_service.repository.MdProvinceRepository;
import com.example.user_service.repository.MdWardRepository;
import com.example.user_service.service.ApiPublicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiPublicServiceImplement implements ApiPublicService {
    private final MdProvinceRepository mdProvinceRepository ;
    private final MdWardRepository mdWardRepository ;

    @Override
    public List<ProvinceItemResponse> getAllProvince() {
        List<MdProvince> mdProvinces = mdProvinceRepository.findAll() ;
        List<ProvinceItemResponse> responses = (mdProvinces == null)
                ? Collections.emptyList()
                : mdProvinces.stream()
                .map(province -> new ProvinceItemResponse(province.getCode(), province.getName()))
                .toList();
        return responses;
    }

    @Override
    public List<WardItemResponse> getAllWard(String provinceCode) {
        if (provinceCode == null) return Collections.emptyList() ;
        List<MdWard> mdWards = mdWardRepository.findAllByProvinceCode(provinceCode) ;
        List<WardItemResponse> responses = (mdWards == null)
                ? Collections.emptyList()
                : mdWards.stream()
                .map(mdWard -> new WardItemResponse(mdWard.getCode(), mdWard.getName()))
                .toList();
        return responses ;
    }
}
