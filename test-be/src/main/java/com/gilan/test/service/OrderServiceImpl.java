package com.gilan.test.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gilan.test.exception.AppException;
import com.gilan.test.model.ErrorType;
import com.gilan.test.model.request.OrderRequest;
import com.gilan.test.model.response.OrderResponse;
import com.gilan.test.persistence.entity.Item;
import com.gilan.test.persistence.entity.Order;
import com.gilan.test.persistence.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private ItemService itemService;

	@Autowired
	private OrderRepository orderRepository;

	@Override
	public OrderResponse saveOrder(OrderRequest orderRequest) {
		Item item = itemService.getEntityById(orderRequest.getItemId());

		if (item.getStock() < orderRequest.getQuantity()) {
			throw new AppException("Stok tidak mencukupi untuk item: " + item.getName(), ErrorType.INSUFFICIENT_STOCK);
		}

		item.setStock(item.getStock() - orderRequest.getQuantity());
		itemService.updateStockItem(item);

		Order order = new Order();
		order.setItem(item);
		order.setQuantity(orderRequest.getQuantity());
		order.setPrice(orderRequest.getPrice());
		Order savedOrder = orderRepository.save(order);
		return convertToResponse(savedOrder);
	}

	@Override
	public OrderResponse getOrderById(Long id) {
		Order order = orderRepository.findById(id).orElseThrow(
				() -> new AppException("Order tidak ditemukan dengan ID: " + id, ErrorType.ORDER_NOT_FOUND));
		return convertToResponse(order);
	}

	@Override
	public Page<OrderResponse> listOrders(Pageable pageable) {
		Page<Order> orders = orderRepository.findAll(pageable);
		return new PageImpl<>(orders.getContent().stream().map(this::convertToResponse).collect(Collectors.toList()),
				pageable, orders.getTotalElements());
	}

	@Override
	public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {
	    Order existingOrder = orderRepository.findById(id)
	            .orElseThrow(() -> new AppException("Order tidak ditemukan", ErrorType.ORDER_NOT_FOUND));

	    Item item = existingOrder.getItem();

	    int qtyDif = orderRequest.getQuantity() - existingOrder.getQuantity();
	    if (qtyDif > 0) { 
	        if (item.getStock() < qtyDif) {
	            throw new AppException("Stok tidak mencukupi untuk item: " + item.getName(), ErrorType.INSUFFICIENT_STOCK);
	        }
	        item.setStock(item.getStock() - qtyDif); 
	    } else if (qtyDif < 0) { 
	        item.setStock(item.getStock() + Math.abs(qtyDif)); 
	    }

	    itemService.updateStockItem(item);

	    existingOrder.setQuantity(orderRequest.getQuantity());
	    existingOrder.setPrice(orderRequest.getPrice());
	    Order updatedOrder = orderRepository.save(existingOrder);

	    return convertToResponse(updatedOrder);
	}

	@Override
	public void deleteOrder(Long id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new AppException("Order tidak ditemukan", ErrorType.ORDER_NOT_FOUND));
		orderRepository.delete(order);
	}

	private OrderResponse convertToResponse(Order order) {
		OrderResponse response = new OrderResponse();
		response.setId(order.getId());
		response.setItemId(order.getItem().getId());
		response.setQuantity(order.getQuantity());
		response.setPrice(order.getPrice());
		return response;
	}
}