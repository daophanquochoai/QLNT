package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.customer;
import com.CNPM.QLNT.model.requests;

public interface ICommuService {
    void addMessage(requests mess_id, customer sender_id, customer receiver_id );
}
