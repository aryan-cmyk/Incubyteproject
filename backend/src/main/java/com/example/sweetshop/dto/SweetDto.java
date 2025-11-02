package com.example.sweetshop.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record SweetDto(UUID id, String name, String category, BigDecimal price, Integer quantity) {}
