package com.glo.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import com.glo.orderservice.dto.InventoryResponse;
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
	
	private final WebClient.Builder webClientBuilder;
	
	public void placeOrder(OrderRequest orderRequest)
	{
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		
		List<OrderLineItems>orderLineItems = orderRequest.getOrderLineItemsDtoList()
		.stream()
		.map(orderLineItemsDto -> mapToDto(orderLineItemsDto))
		.toList();
		
		order.setOrderLineItemsList(orderLineItems);
	 	
		List<String>skuCodes = order.getOrderLineItemsList().stream()
		.map(orderLineItem -> orderLineItem.getSkuCode())
		.toList();
		
		InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get().uri("http://inventory-service/api/inventory",
				uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
		.retrieve()
		.bodyToMono(InventoryResponse[].class)
		.block();
		
		boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
		.allMatch(InventoryResponse -> InventoryResponse.isInStock());
		
		if(allProductsInStock) {
			orderRepository.save(order);
		}
		else
		{
			throw new IllegalArgumentException("Product is not in stock. Please try again later");
		}
		
		
		
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
