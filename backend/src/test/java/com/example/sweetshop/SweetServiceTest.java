package com.example.sweetshop;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.sweetshop.entity.Sweet;
import com.example.sweetshop.exception.BadRequestException;
import com.example.sweetshop.repository.SweetRepository;
import com.example.sweetshop.service.SweetService;

@SpringBootTest(properties = "spring.profiles.active=test")
class SweetServiceTest {

    @Autowired
    private SweetService sweetService;

    @Autowired
    private SweetRepository sweetRepository;

    @BeforeEach
    void setup() {
        sweetRepository.deleteAll();
    }

    @Test
    void purchase_decrementsQuantity() {
        Sweet s = new Sweet();
        s.setName("Ladoo");
        s.setCategory("Indian");
        s.setPrice(BigDecimal.valueOf(20));
        s.setQuantity(2);

        Sweet savedSweet = sweetRepository.save(s);

        sweetService.purchase(savedSweet.getId());

        Sweet after = sweetRepository.findById(savedSweet.getId()).orElseThrow();
        assertEquals(1, after.getQuantity());
    }

    @Test
    void purchase_whenZero_throws() {
        Sweet s = new Sweet();
        s.setName("Barfi");
        s.setCategory("Indian");
        s.setPrice(BigDecimal.valueOf(10));
        s.setQuantity(0);

        Sweet savedSweet = sweetRepository.save(s);

        assertThrows(BadRequestException.class,
                () -> sweetService.purchase(savedSweet.getId()));
    }
}
