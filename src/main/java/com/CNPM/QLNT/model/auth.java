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
public class auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name = "role")
    private String role;
}
