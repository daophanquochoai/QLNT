package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "managerId")
    private int managerId;

    @Column(name = "firstName", nullable = false, columnDefinition = "nvarchar(55)")
    private String firstName;

    @Column(name = "lastName", nullable = false, columnDefinition = "nvarchar(55)")
    private String lastName;

    @Column(name = "cccd", nullable = false, unique = true, columnDefinition = "varchar(12)")
    private String CCCD;

    @Column(name = "dateOfBirth", nullable = false, columnDefinition = "DATE")
    private LocalDate date_of_birth;

    @Column(name = "sex", nullable = false)
    private Boolean sex;

    @Column(name = "infoAddress", nullable = false, columnDefinition = "nvarchar(100)")
    private String infoAddress;

    @Column(name = "phoneNumber", unique = true, nullable = false, columnDefinition = "varchar(10)")
    @Size(min = 10, message = "SDT lon hon 10 so")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @OneToOne( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "userAuthId")
    private UserAuth userAuthId;
}
