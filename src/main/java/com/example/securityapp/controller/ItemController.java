package com.example.securityapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.securityapp.domain.Item;
import com.example.securityapp.dto.ItemDto;
import com.example.securityapp.service.ItemService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ItemController {
    @Autowired
    ItemService itemService;

    @GetMapping("/items")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<List<ItemDto>> getAllItems() {
        log.info("items in Controller {}", itemService);
        
        List<ItemDto> items = itemService.retriveItems();
        log.info("items: {}", items.size());
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
    
    

}
