package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.repository.HomeCategoryRepo;
import com.CNPM.QLNT.services.Inter.IHomeCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeCategory implements IHomeCategory {
    private final HomeCategoryRepo homeCaRepo;
    @Override
    public void addHomeCate(com.CNPM.QLNT.model.HomeCategory homeCate) {
        List<com.CNPM.QLNT.model.HomeCategory> homeCategory = homeCaRepo.findAll();

        // Kiểm tra xem homeCate đã tồn tại trong danh sách chưa
        boolean isDuplicate = homeCategory.stream()
                .anyMatch(h -> h.getHome_category_name().equals(homeCate.getHome_category_name()));

        if (!isDuplicate) {
            homeCaRepo.save(homeCate);
        } else {
            // Nếu homeCate đã tồn tại, ném RuntimeException
            throw new RuntimeException("Đã tồn tại");
        }
    }


    @Override
    public List<com.CNPM.QLNT.model.HomeCategory> getAllHomeCate() {
        return homeCaRepo.findAll();
    }

    @Override
    public com.CNPM.QLNT.model.HomeCategory getHomeCategory(String home_name) {
        return homeCaRepo.findAll().stream().filter(h->h.getHome_category_name().equals(home_name)).findFirst().get();
    }
}
