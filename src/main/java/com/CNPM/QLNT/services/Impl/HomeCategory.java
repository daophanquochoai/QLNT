package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.model.home_category;
import com.CNPM.QLNT.repository.homeCategoryRepo;
import com.CNPM.QLNT.services.Inter.IHomeCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeCategory implements IHomeCategory {
    private final homeCategoryRepo homeCaRepo;
    @Override
    public void addHomeCate(home_category homeCate) {
        List<home_category> homeCategory = homeCaRepo.findAll();

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
    public List<home_category> getAllHomeCate() {
        return homeCaRepo.findAll();
    }

    @Override
    public home_category getHomeCategory(String home_name) {
        return homeCaRepo.findAll().stream().filter(h->h.getHome_category_name().equals(home_name)).findFirst().get();
    }
}
