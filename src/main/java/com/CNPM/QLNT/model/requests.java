package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "requests")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class requests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "requests_id")
    private int requests_id;

    @Column(name = "created_datatime")
    private Date created_datatime;

    @Column(name="status")
    private Boolean status;

    @Column(name = "message")
    private String message;
}
