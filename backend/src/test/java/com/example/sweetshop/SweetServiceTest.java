package test.java.com.example.sweetshop;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import main.java.com.example.sweetshop.entity.Sweet;
import main.java.com.example.sweetshop.exception.BadRequestException;
import main.java.com.example.sweetshop.repository.SweetRepository;
import main.java.com.example.sweetshop.service.SweetService;

@SpringBootTest
class SweetServiceTest {

  @Autowired SweetService sweetService;
  @Autowired SweetRepository sweetRepository;

  @BeforeEach
  void setup() { sweetRepository.deleteAll(); }

  @Test
  void purchase_decrementsQuantity() {
    Sweet s = new Sweet();
    s.setName("Ladoo"); s.setCategory("Indian"); s.setPrice(BigDecimal.valueOf(20)); s.setQuantity(2);
    s = sweetRepository.save(s);

    sweetService.purchase(s.getId());

    Sweet after = sweetRepository.findById(s.getId()).get();
    assertEquals(1, after.getQuantity());
  }

  @Test
  void purchase_whenZero_throws() {
    Sweet s = new Sweet();
    s.setName("Barfi"); s.setCategory("Indian"); s.setPrice(BigDecimal.valueOf(10)); s.setQuantity(0);
    s = sweetRepository.save(s);

    assertThrows(BadRequestException.class, () -> sweetService.purchase(s.getId()));
  }
}
