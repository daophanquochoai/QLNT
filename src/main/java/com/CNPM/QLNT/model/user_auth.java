package com.CNPM.QLNT.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "id")
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "users_id")
    private users users_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "auth_id")
    private auth auth_id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private customer Customer;
}
