package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

@Entity
@Table(name = "Customer")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customerId", nullable = false)
    private int customerId;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "cccd", nullable = false, unique = true)
    private String CCCD;

    @Column(name = "dateOfBirth", nullable = false)
    private Date date_of_birth;

    @Column(name = "sex", nullable = false)
    private Boolean sex;

    @Column(name = "infoAddress", nullable = false)
    private String infoAddress;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private room Room;

    @OneToOne( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "userAuthId")
    private user_auth userAuthId;
}
