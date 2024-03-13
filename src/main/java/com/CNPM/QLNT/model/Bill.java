package com.CNPM.QLNT.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "Bill")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "billId")
    private int billId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRoom")
    @JsonIgnore
    private Room roomId;

    @Column(name = "beginDate", nullable = false)
    private Date beginDate;

    @Column(name = "endDate", nullable = false)
    private Date endDate;

    @Column(name = "electricNumberBegin", nullable = false)
    private int electricNumberBegin;

    @Column(name = "electricNumberEnd", nullable = false)
    private int electricNumberEnd;

    @Column(name = "waterNumberBegin", nullable = false)
    private int waterNumberBegin;

    @Column(name = "waterNumberEnd", nullable = false)
    private int waterNumberEnd;

    @Column(name = "otherPrice", nullable = false)
    private int otherPrice;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "ghiChu")
    private String ghiChu;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "donGiaId")
    private PriceQuotation priceQuotationId;
}
