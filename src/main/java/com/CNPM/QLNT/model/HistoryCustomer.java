package com.CNPM.QLNT.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @ManyToOne
    @JoinColumn(name = "customers")
    @JsonIgnore
    private Customers customers;
    @ManyToOne
    @JoinColumn(name = "roomOld", nullable = false)
    private Room roomOld;
    @ManyToOne
    @JoinColumn(name = "roomNew", nullable = true)
    private Room roomNew;
    @Column(columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime beginDate;
    @Column(columnDefinition = "DATETIME", nullable = true)
    private LocalDateTime endDate;
}