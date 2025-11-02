package com.example.sweetshop.repository;

import com.example.sweetshop.entity.Sweet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;

public interface SweetRepository extends JpaRepository<Sweet, UUID> {
  List<Sweet> findByNameContainingIgnoreCase(String name);
  List<Sweet> findByCategory(String category);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Sweet> findWithLockingById(UUID id);
}
