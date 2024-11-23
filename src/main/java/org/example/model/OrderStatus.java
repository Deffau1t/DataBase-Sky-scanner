package org.example.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ordersStatus")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
