package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Customer")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Customers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customerId", nullable = false)
    private int customerId;

    @Column(name = "firstName", nullable = false, columnDefinition = "nvarchar(55)")
    private String firstName;

    @Column(name = "lastName", nullable = false, columnDefinition = "nvarchar(55)")
    private String lastName;

    @Column(name = "cccd", nullable = false, unique = true, columnDefinition = "varchar(55)")
    private String CCCD;

    @Column(name = "dateOfBirth", nullable = false, columnDefinition = "DATE")
    private LocalDate date_of_birth;

    @Column(name = "sex", nullable = false)
    private Boolean sex;

    @Column(name = "infoAddress", nullable = false, columnDefinition = "nvarchar(100)")
    private String infoAddress;

    @Column(name = "phoneNumber", unique = true, nullable = false)
    @Size(min = 10, message = "SDT lon hon 10 so")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idRoom")
    private Room room;

    @OneToOne( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "userAuthId")
    private UserAuth userAuthId;

    @Override
    public String toString() {
        return "Customers{" +
                "customerId=" + customerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", CCCD='" + CCCD + '\'' +
                ", date_of_birth=" + date_of_birth +
                ", sex=" + sex +
                ", infoAddress='" + infoAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
//                ", room=" + ( room == null ? "" : room.getId()) +
                ", userAuthId=" + userAuthId +
                '}';
    }
}
