package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "HomeCategory")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class home_category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homeCategoryId", nullable = false)
    private int home_category_id;

    @Column(name = "homeCategoryName", nullable = false, columnDefinition = "nvarchar(55)")
    private String home_category_name;
}
