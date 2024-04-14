package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WaterPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column(name = "dataChanged", columnDefinition = "DATETIME")
    private LocalDateTime dataChanged;
    @Column(name = "price", columnDefinition = "money")
    private int price;
}
