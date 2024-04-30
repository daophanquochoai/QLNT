package com.CNPM.QLNT.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InfoInvoice {
    private Integer electricNumberBegin;
    private Integer waterNumberBegin;
    private Integer electricPrice;
    private Integer waterPrice;
    private BigDecimal roomPrice;
    private List<InfoService> service;
}
