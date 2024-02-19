package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "home_category")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class home_category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "home_category_id")
    private int home_category_id;

    @Column(name = "home_category_name")
    private String home_category_name;

    @Column(name = "price")
    private BigDecimal price;
}
