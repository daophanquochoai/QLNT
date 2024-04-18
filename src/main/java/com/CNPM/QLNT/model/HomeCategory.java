package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "HomeCategory")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class HomeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roomTypeId", nullable = false)
    private int roomTypeId;

    @Column(name = "roomTypeName", nullable = false, unique = true, columnDefinition = "nvarchar(55)")
    private String roomTypeName;
}
