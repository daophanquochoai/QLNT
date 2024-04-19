package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;

@Entity
@Table(name = "Contract")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contractId")
    private int contractId;

    @OneToOne( fetch = FetchType.EAGER)
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @JoinColumn(name = "customerId", referencedColumnName = "customerId", nullable = false)
    private Customer cusId;

    @OneToOne
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @JoinColumn(name = "roomId", referencedColumnName = "roomId", nullable = false, unique = true)
    private com.CNPM.QLNT.model.Room roomId;

    @Column(name = "createdDate", nullable = false, columnDefinition = "DATE")
    private LocalDate createdDate;

    @Column(name = "beginDate", nullable = false, columnDefinition = "DATE")
    private LocalDate beginDate;

    @Column(name = "endDate" ,nullable = false, columnDefinition = "DATE")
    private LocalDate endDate;

    @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean status;
}