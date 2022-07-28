package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private static final Sort SORT_DATE_TIME = Sort.by(Sort.Direction.DESC, "dateTime");

    private final CrudMealRepository crudMealRepository;
    private final CrudUserRepository crudUserRepository;

    public DataJpaMealRepository(CrudMealRepository crudMealRepository, CrudUserRepository crudUserRepository) {
        this.crudMealRepository = crudMealRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    @Transactional
    @Modifying
    public Meal save(Meal meal, int userId) {
        User user = crudUserRepository.getOne(userId);
        meal.setUser(user);
        if (meal.isNew()) {
            return crudMealRepository.save(meal);
        } else {
            Meal oldMeal = crudMealRepository.findById(meal.getId()).orElse(null);
            if (oldMeal != null && oldMeal.getUser().getId() != userId) {
                return null;
            }
            return crudMealRepository.save(meal);
        }
    }

    @Override
    @Transactional
    @Modifying
    public boolean delete(int id, int userId) {
        Meal meal = crudMealRepository.findById(id).orElse(null);
        if (meal == null || meal.getUser() == null || meal.getUser().getId() != userId) {
            return false;
        } else {
            crudMealRepository.deleteById(id);
            return true;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = crudMealRepository.findById(id).orElse(null);
        return meal == null || meal.getUser().getId() != userId ? null : meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudMealRepository.findAll(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudMealRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
    }
}
