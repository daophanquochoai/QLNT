package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.home_category;

import java.util.List;

public interface IHomeCategory {
    void addHomeCate(home_category homeCate);
    List<home_category> getAllHomeCate();
    home_category getHomeCategory(String home_name);
}
