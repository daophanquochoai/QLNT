package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;

@Entity
@Table(name = "Contracts")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contracts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contractsId")
    private int contractsId;

    @OneToOne( fetch = FetchType.EAGER)
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @JoinColumn(name = "customerId", referencedColumnName = "customerId", nullable = false)
    private Customers cusId;

    @OneToOne
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @JoinColumn(name = "roomId", referencedColumnName = "id", nullable = false, unique = true)
    private com.CNPM.QLNT.model.Room Room;

    @Column(name = "conDate", nullable = false, columnDefinition = "DATE")
    private LocalDate conDate;

    @Column(name = "beginDate", nullable = false, columnDefinition = "DATE")
    private LocalDate beginDate;

    @Column(name = "endDate" ,nullable = false, columnDefinition = "DATE")
    private LocalDate endDate;

    @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean status;
}
