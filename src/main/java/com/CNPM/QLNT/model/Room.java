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
public class Room {

    @Id
    @Column(name = "id")
    private int id;
    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "homeCategoryId", referencedColumnName = "homeCategoryId", nullable = false)
    private HomeCategory homeCategoryId;

    @Column(name = "limit", nullable = false)
    private int limit;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "price", nullable = false, columnDefinition = "money")
    private BigDecimal price;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roomId",  fetch = FetchType.EAGER)
    @JsonIgnore
    private List<com.CNPM.QLNT.model.Bill> Bill;

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Customers> customers;

}
