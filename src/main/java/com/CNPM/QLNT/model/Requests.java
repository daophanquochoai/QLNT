package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "Requests")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class Requests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "requestsId", nullable = false)
    private int requestsId;

    @Column(name = "createdDatatime", nullable = false)
    private Date createdDatatime;

    @Column(name="status", nullable = false)
    private Boolean status;

    @Column(name = "message", nullable = false)
    private String message;
}
