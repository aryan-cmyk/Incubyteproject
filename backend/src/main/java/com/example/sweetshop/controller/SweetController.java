package com.example.sweetshop.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import main.java.com.example.sweetshop.dto.CreateSweetRequest;
import main.java.com.example.sweetshop.dto.SweetDto;
import main.java.com.example.sweetshop.dto.UpdateSweetRequest;
import main.java.com.example.sweetshop.service.SweetService;

@RestController
@RequestMapping("/api/sweets")
public class SweetController {
  private final SweetService sweetService;

  public SweetController(SweetService sweetService) { this.sweetService = sweetService; }

  @GetMapping
  public List<SweetDto> listAll() { return sweetService.listAll(); }

  @GetMapping("/search")
  public List<SweetDto> search(@RequestParam(required = false) String name,
                               @RequestParam(required = false) String category,
                               @RequestParam(required = false) BigDecimal minPrice,
                               @RequestParam(required = false) BigDecimal maxPrice) {
    return sweetService.search(name, category, minPrice, maxPrice);
  }

  @PostMapping
  public ResponseEntity<SweetDto> create(@RequestBody CreateSweetRequest req) {
    SweetDto dto = sweetService.create(req.getName(), req.getCategory(), req.getPrice(), req.getQuantity());
    return ResponseEntity.status(201).body(dto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<SweetDto> update(@PathVariable UUID id, @RequestBody UpdateSweetRequest req) {
    SweetDto dto = sweetService.update(id, req.getName(), req.getCategory(), req.getPrice(), req.getQuantity());
    return ResponseEntity.ok(dto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    sweetService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/purchase")
  public ResponseEntity<SweetDto> purchase(@PathVariable UUID id) {
    SweetDto dto = sweetService.purchase(id);
    return ResponseEntity.ok(dto);
  }

  @PostMapping("/{id}/restock")
  public ResponseEntity<SweetDto> restock(@PathVariable UUID id, @RequestParam int amount) {
    SweetDto dto = sweetService.restock(id, amount);
    return ResponseEntity.ok(dto);
  }
}
