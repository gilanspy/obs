package com.gilan.test.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gilan.test.model.request.OrderRequest;
import com.gilan.test.model.response.OrderResponse;

import jakarta.validation.Valid;

public interface OrderService {
    
    OrderResponse saveOrder(@Valid OrderRequest orderRequest);
    OrderResponse getOrderById(Long id);
    Page<OrderResponse> listOrders(Pageable pageable);
    OrderResponse updateOrder(Long id, OrderRequest orderRequest);
    void deleteOrder(Long id);
}
