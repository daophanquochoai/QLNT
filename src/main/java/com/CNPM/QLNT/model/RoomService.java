package com.CNPM.QLNT.model;

import jakarta.persistence.*;
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
@Table(name = "RoomService")
public class RoomService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roomServiceId")
    private int roomServiceId;
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "roomId")
    private Room room;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "serviceId")
    private Service service;
    private int quantity;
    private LocalDate beginDate;
    private LocalDate endDate;
}
