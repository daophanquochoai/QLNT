package com.CNPM.QLNT.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Room")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "homeCategoryId", referencedColumnName = "homeCategoryId", nullable = false)
    private home_category homeCategoryId;

    @Column(name = "limit", nullable = false)
    private int limit;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "price", nullable = false, columnDefinition = "CHECK (price>0)")
    private BigDecimal price;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roomId")
    @JsonIgnore
    private List<bill> Bill;

}
