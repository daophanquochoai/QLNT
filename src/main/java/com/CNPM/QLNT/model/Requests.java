package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


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

    @Column(name = "createdDatatime", nullable = false, columnDefinition = "DATE")
    private LocalDateTime createdDatatime;

    @Column(name="status", nullable = false)
    private Boolean status;

    @Column(name = "message", nullable = false, columnDefinition = "nvarchar(255)")
    private String message;
}
