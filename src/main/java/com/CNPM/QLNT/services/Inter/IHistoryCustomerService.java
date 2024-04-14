package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.HistoryCustomer;
import com.CNPM.QLNT.response.History;

import java.util.List;

public interface IHistoryCustomerService {
    List<History> getAllHistoryCustomer();
}
