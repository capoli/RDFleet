package com.realdolmen.rdfleet.repositories;

import com.realdolmen.rdfleet.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
