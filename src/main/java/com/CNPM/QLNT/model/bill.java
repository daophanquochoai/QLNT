package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "bill")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private int bill_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private room Room;

    @Column(name = "begin_date")
    private Date begin_date;

    @Column(name = "end_date")
    private Date end_date;

    @Column(name = "electric_number_begin")
    private int electric_number_begin;

    @Column(name = "electric_number_end")
    private int electric_number_end;

    @Column(name = "water_number_begin")
    private int water_number_begin;

    @Column(name = "water_number_end")
    private int water_number_end;

    @Column(name = "other_price")
    private int other_price;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "ghiChu")
    private String ghiChu;
}
