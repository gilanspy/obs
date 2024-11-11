package com.gilan.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gilan.test.model.request.OrderRequest;
import com.gilan.test.model.response.OrderResponse;
import com.gilan.test.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@GetMapping
	public Page<OrderResponse> listOrders(Pageable pageable) {
		return orderService.listOrders(pageable);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {

		return ResponseEntity.ok(orderService.getOrderById(id));
	}

	@PostMapping
	public OrderResponse createOrder(@RequestBody OrderRequest order) {
		return orderService.saveOrder(order);
	}

	@PutMapping("/{id}")
	public OrderResponse updateOrder(@PathVariable Long id, @RequestBody OrderRequest orderDetails) {
		return orderService.updateOrder(id, orderDetails);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
		orderService.deleteOrder(id);
		return ResponseEntity.ok().build();
	}
}
