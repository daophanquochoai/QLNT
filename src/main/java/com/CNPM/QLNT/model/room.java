package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "room")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private int room_id;

    @OneToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "home_category_id", referencedColumnName = "home_category_id")
    private home_category home_category_id;

    @Column(name = "limit")
    private int limit;

    @Column(name = "status")
    private Boolean status;

}
