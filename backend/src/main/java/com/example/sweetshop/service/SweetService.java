package com.example.sweetshop.service;

import com.example.sweetshop.dto.SweetDto;
import com.example.sweetshop.entity.Sweet;
import com.example.sweetshop.repository.SweetRepository;
import com.example.sweetshop.exception.ResourceNotFoundException;
import com.example.sweetshop.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;

@Service
public class SweetService {
  private final SweetRepository sweetRepository;
  private final EntityManager em;

  public SweetService(SweetRepository sweetRepository, EntityManager em) {
    this.sweetRepository = sweetRepository;
    this.em = em;
  }

  public SweetDto create(String name, String category, BigDecimal price, Integer quantity) {
    Sweet s = new Sweet();
    s.setName(name); s.setCategory(category); s.setPrice(price); s.setQuantity(quantity);
    s = sweetRepository.save(s);
    return map(s);
  }

  public List<SweetDto> listAll() {
    return sweetRepository.findAll().stream().map(this::map).collect(Collectors.toList());
  }

  public List<SweetDto> search(String name, String category, BigDecimal minPrice, BigDecimal maxPrice) {
    // Basic search: name then filter by others
    var list = (name != null && !name.isBlank()) ? sweetRepository.findByNameContainingIgnoreCase(name) : sweetRepository.findAll();
    return list.stream()
      .filter(s -> category == null || s.getCategory().equalsIgnoreCase(category))
      .filter(s -> minPrice == null || s.getPrice().compareTo(minPrice) >= 0)
      .filter(s -> maxPrice == null || s.getPrice().compareTo(maxPrice) <= 0)
      .map(this::map).collect(Collectors.toList());
  }

  public SweetDto update(UUID id, String name, String category, BigDecimal price, Integer quantity) {
    Sweet s = sweetRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sweet not found"));
    if (name != null) s.setName(name);
    if (category != null) s.setCategory(category);
    if (price != null) s.setPrice(price);
    if (quantity != null) s.setQuantity(quantity);
    return map(sweetRepository.save(s));
  }

  public void delete(UUID id) {
    sweetRepository.deleteById(id);
  }

  @Transactional
  public SweetDto purchase(UUID id) {
    // pessimistic lock using EntityManager
    Sweet s = em.find(Sweet.class, id, LockModeType.PESSIMISTIC_WRITE);
    if (s == null) throw new ResourceNotFoundException("Sweet not found");
    if (s.getQuantity() <= 0) throw new BadRequestException("Out of stock");
    s.setQuantity(s.getQuantity() - 1);
    em.persist(s);
    return map(s);
  }

  @Transactional
  public SweetDto restock(UUID id, int amount) {
    Sweet s = em.find(Sweet.class, id, LockModeType.PESSIMISTIC_WRITE);
    if (s == null) throw new ResourceNotFoundException("Sweet not found");
    s.setQuantity(s.getQuantity() + amount);
    em.persist(s);
    return map(s);
  }

  private SweetDto map(Sweet s) {
    return new SweetDto(s.getId(), s.getName(), s.getCategory(), s.getPrice(), s.getQuantity());
  }
}
