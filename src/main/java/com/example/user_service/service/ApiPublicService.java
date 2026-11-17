package com.example.user_service.service;

import com.example.user_service.dto.response.ProvinceItemResponse;
import com.example.user_service.dto.response.WardItemResponse;

import java.util.List;

public interface ApiPublicService {
    List<ProvinceItemResponse> getAllProvince();
    List<WardItemResponse> getAllWard(String provinceCode) ;
}
