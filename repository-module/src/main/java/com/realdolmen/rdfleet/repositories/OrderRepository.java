package com.realdolmen.rdfleet.repositories;

import com.realdolmen.rdfleet.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by JSTAX29 on 28/10/2015.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
}
