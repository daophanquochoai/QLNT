package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "DonGia")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class donGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "electricityPrice", nullable = false, columnDefinition = "CHECK (electricityPrice>0)")
    private int electricityPrice;

    @Column(name = "waterPrice", nullable = false, columnDefinition = "CHECK (waterPrice > 0)")
    private int waterPrice;

    @Column(name = "timeChange", nullable = false)
    private Date timeChange;
}
