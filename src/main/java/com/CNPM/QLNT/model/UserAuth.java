package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "UserAuth")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class UserAuth {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usersId", nullable = false)
    private Users usersId;

    @ManyToOne
    @Cascade({org.hibernate.annotations.CascadeType.DETACH,
    org.hibernate.annotations.CascadeType.MERGE,
    org.hibernate.annotations.CascadeType.REFRESH})
    @JoinColumn(name = "authId", nullable = false)
    private Auth authId;
}
