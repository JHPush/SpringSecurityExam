package com.example.securityapp.service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.securityapp.domain.Item;
import com.example.securityapp.dto.ItemDto;
import com.example.securityapp.repository.ItemRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository ir;

    public List<ItemDto> retriveItems() {
        return ir.findAll().stream().map(item -> entityToDto(item)).collect(Collectors.toList());
    }

    private ItemDto entityToDto(Item item) {
        return ItemDto.builder().id(item.getId()).name(item.getName()).build();
    }
}
