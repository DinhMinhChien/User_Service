package com.example.user_service.controller;

import com.example.user_service.common.BaseResponse;
import com.example.user_service.dto.response.ProvinceItemResponse;
import com.example.user_service.dto.response.WardItemResponse;
import com.example.user_service.service.ApiPublicService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/public")
public class ApiPublicController {
    private final ApiPublicService apiPublicService ;

    @GetMapping("/provinces")
    public ResponseEntity<BaseResponse<List<ProvinceItemResponse>>> getAllProvince() {
        List<ProvinceItemResponse> responses = apiPublicService.getAllProvince();
        return ResponseEntity.ok(BaseResponse.ofSuccess(responses,"Lấy danh sách tỉnh thành thành công"));
    }

    @GetMapping("/wards")
    public ResponseEntity<BaseResponse<List<WardItemResponse>>> getAllWard(@RequestParam(required = true)String provinceCode) {
        List<WardItemResponse> responses = apiPublicService.getAllWard(provinceCode) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess(responses,"Lấy danh sách xã/phường theo tỉnh thành thành công"));
    }
}
