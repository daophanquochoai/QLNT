package com.CNPM.QLNT.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "donGia")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class donGia {
    @Column(name = "giaDien")
    private BigDecimal giaDien;

    @Column(name = "giaNuoc")
    private BigDecimal giaNuoc;

    @Id
    @Column(name = "timeChange")
    private Date timeChange;
}
