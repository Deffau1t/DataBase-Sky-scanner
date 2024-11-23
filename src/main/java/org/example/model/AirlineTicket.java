package org.example.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "airlineTickets")
public class AirlineTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate timeArrive;

    @Column(nullable = false)
    private LocalDate timeDeparture;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int planeCapacity;

    @Column(nullable = false)
    private String cityArrive;

    @Column(nullable = false)
    private String cityDeparture;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;
}