package com.CNPM.QLNT.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "user_auth")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class user_auth {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usersId", nullable = false)
    private users usersId;

    @ManyToOne
    @Cascade({org.hibernate.annotations.CascadeType.DETACH,
    org.hibernate.annotations.CascadeType.MERGE,
    org.hibernate.annotations.CascadeType.REFRESH})
    @JoinColumn(name = "authId", nullable = false)
    private auth authId;
}
