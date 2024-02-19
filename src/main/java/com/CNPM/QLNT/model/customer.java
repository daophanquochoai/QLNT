package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int customer_id;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "CCCD")
    private String CCCD;

    @Column(name = "date_of_birth")
    private Date date_of_birth;

    @Column(name = "sex")
    private Boolean sex;

    @Column(name = "info_address")
    private String info_address;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "email")
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_detail_id", referencedColumnName = "room_id")
    private room Room;

    @OneToOne(mappedBy = "Customer", cascade = CascadeType.ALL)
    private user_auth UA;
}
