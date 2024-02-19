package com.CNPM.QLNT.model;

import jakarta.persistence.*;

import java.util.Date;

public class contracts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contracts_id")
    private int contracts_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private int cus_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "onwer_id", referencedColumnName = "customer_id")
    private int own_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id", referencedColumnName = "room_id")
    private int room_id;

    @Column(name = "con_date")
    private Date con_date;

    @Column(name = "begin_date")
    private Date begin_date;

    @Column(name = "end_date")
    private Date end_date;

    @Column(name = "status")
    private Boolean status;
}
