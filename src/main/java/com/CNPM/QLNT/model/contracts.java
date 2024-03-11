package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "Contracts")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class contracts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contractsId")
    private int contractsId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customerId", referencedColumnName = "customerId", nullable = false)
    private customer cusId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "onwer_id", referencedColumnName = "customerId", nullable = false)
    private customer ownId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "roomId", referencedColumnName = "id", nullable = false)
    private room Room;

    @Column(name = "conDate", nullable = false, columnDefinition = "default getdate()")
    private Date conDate;

    @Column(name = "beginDate", nullable = false)
    private Date beginDate;

    @Column(name = "endDate" ,nullable = false, columnDefinition = "CHECK (endDate>beginDate)")
    private Date endDate;

    @Column(name = "status", nullable = false, columnDefinition = "default false")
    private Boolean status;
}
