package com.example.demo.Services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.FoodRecord;
import com.example.demo.Repositories.FoodRecordRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FoodRecordService {
    @Autowired
    private FoodRecordRepository foodRecordRepository;

    public FoodRecord create(FoodRecord foodRecord) {
        return foodRecordRepository.save(foodRecord);
    }

    public FoodRecord update(FoodRecord newFoodRecord, Long id) {
        return foodRecordRepository.findById(id)
                .map(foodRecord -> {
                    foodRecord.setUser(newFoodRecord.getUser());
                    foodRecord.setFood(newFoodRecord.getFood());
                    foodRecord.setRecordDate(newFoodRecord.getRecordDate());
                    return foodRecordRepository.save(foodRecord);
                })
                .orElseGet(() -> {
                    return foodRecordRepository.save(newFoodRecord);
                });
    }

    public FoodRecord findById(Long id) {
        Optional<FoodRecord> foodRecord = foodRecordRepository.findById(id);
        if (!foodRecord.isPresent()) {
            return null;
        }
        return foodRecord.get();
    }

    public Iterable<FoodRecord> findAll() {
        return foodRecordRepository.findAll();
    }

    public void removeById(Long id) {
        foodRecordRepository.deleteById(id);
    }

    public void removeAll() {
        foodRecordRepository.deleteAll();
    }

    public List<FoodRecord> getWeekData() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date oneWeekAgo = calendar.getTime();

        return foodRecordRepository.findAllWithinOneWeek(oneWeekAgo);
    }
}
