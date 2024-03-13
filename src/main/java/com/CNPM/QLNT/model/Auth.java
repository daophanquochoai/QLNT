package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "auth")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id" , nullable = false)
    private int id;

    @Column(name = "role", length = 30, nullable = false, unique = true)
    private String role;
}
