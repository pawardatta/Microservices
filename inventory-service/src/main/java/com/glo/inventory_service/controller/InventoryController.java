package com.glo.inventory_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.glo.inventory_service.dto.InventoryResponse;
import com.glo.inventory_service.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
	
	private final InventoryService inventoryService;
	
	//Path Variable call
    // http://localhost:8082/api/inventory/iphone-13,iphone13-red  
	//Request param call
    // http://localhost:8082/api/inventory?skuCode=iphone-13&skuCode=iphone13-red
	
	 @GetMapping
	    @ResponseStatus(HttpStatus.OK)
	    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
	       // log.info("Received inventory check request for skuCode: {}", skuCode);
	        return inventoryService.isInStock(skuCode);
	    }

}
