package com.example.user_service.client;

import com.example.user_service.common.BaseResponse;
import com.example.user_service.client.dto.request.ShopCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "shop-service")
public interface ShopClient {
    @PostMapping("/v1/internal/shops")
    BaseResponse<String> createShop(@RequestBody ShopCreateRequest request) ;
}
