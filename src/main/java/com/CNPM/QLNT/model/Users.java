package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name="Users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name = "username", length = 36, nullable = false, unique = true, columnDefinition = "varchar(55)")
    @Size( min = 2, max = 55, message = "Khong dung gioi han")
    private String username;

    @Column(name = "password", length = 128, nullable = false, columnDefinition = "varchar(255)")
    @Size( min = 7, max = 100, message = "Khong dung gioi han")
    private String password;

    @Column(name = "active", length = 1, nullable = false)
    private Boolean active;
}
