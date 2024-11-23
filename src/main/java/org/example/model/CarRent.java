package org.example.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "carRents")
public class CarRent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private double mileage;
}
