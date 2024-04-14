package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

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
    @ManyToOne
    @JoinColumn(name = "roomId")
    private Room roomId;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "serviceId")
    private Services serviceId;
    private int quantity;
    private LocalDate beginDate;
    private LocalDate endDate;
}