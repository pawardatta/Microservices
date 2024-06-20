package com.glo.orderservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glo.orderservice.dto.OrderLineItemsDto;
import com.glo.orderservice.dto.OrderRequest;
import com.glo.orderservice.model.Order;
import com.glo.orderservice.model.OrderLineItems;
import com.glo.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	
	private final OrderRepository orderRepository;
	
	public void placeOrder(OrderRequest orderRequest)
	{
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		
		List<OrderLineItems>orderLineItems = orderRequest.getOrderLineItemsDtoList()
		.stream()
		.map(orderLineItemsDto -> mapToDto(orderLineItemsDto))
		.toList();
		
		order.setOrderLineItemsList(orderLineItems);
		orderRepository.save(order);
		
	}
	
	private OrderLineItems mapToDto (OrderLineItemsDto orderLineItemsDto)
	{
		OrderLineItems orderLineItems= new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
	}

}
