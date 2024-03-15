package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.HomeCategory;

import java.util.List;

public interface IHomeCategory {
    void addHomeCate(HomeCategory homeCate);
    List<HomeCategory> getAllHomeCate();
    HomeCategory getHomeCategory(String home_name);
}
