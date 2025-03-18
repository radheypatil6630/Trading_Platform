package com.stock.treading.modal;

import com.stock.treading.domain.OrderStatus;
import com.stock.treading.domain.OrderType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private OrderType orderType ;

    @Column(nullable = false)
    private BigDecimal price;

    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(nullable = false)
    private OrderStatus orderStatus;

    @OneToOne(mappedBy = "order",cascade =  CascadeType.ALL)
    private OrderItem orderItem;

}
