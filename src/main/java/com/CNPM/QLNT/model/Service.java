package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int serviceId;
    @Column(name = "serviceName", unique = true, columnDefinition = "nvarchar(55)")
    private String serviceName;
    @Column(columnDefinition = "MONEY")
    private Long price;
}
