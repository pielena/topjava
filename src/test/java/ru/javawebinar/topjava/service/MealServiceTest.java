package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.MealTestData.meal1;
import static ru.javawebinar.topjava.MealTestData.meal2;
import static ru.javawebinar.topjava.MealTestData.meal3;
import static ru.javawebinar.topjava.MealTestData.meal4;
import static ru.javawebinar.topjava.MealTestData.meal5;
import static ru.javawebinar.topjava.MealTestData.meal6;
import static ru.javawebinar.topjava.MealTestData.meal7;
import static ru.javawebinar.topjava.MealTestData.userMeals;
import static ru.javawebinar.topjava.UserTestData.admin;
import static ru.javawebinar.topjava.UserTestData.user;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(meal1.getId(), user.getId());
        assertMatch(meal, meal1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(meal2.getId(), admin.getId());
    }

    @Test
    public void delete() {
        service.delete(meal2.getId(), user.getId());
        assertMatch(service.getAll(user.getId()),
                meal7,
                meal6,
                meal5,
                meal4,
                meal3,
                meal1);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        service.delete(meal2.getId(), admin.getId());
    }

    @Test
    public void getBetweenInclusive() {
        assertMatch(service.getBetweenInclusive(LocalDate.of(2020, Month.JANUARY, 30),
                        LocalDate.of(2020, Month.JANUARY, 30), user.getId()),
                meal1,
                meal2);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(user.getId()), userMeals);
    }

    @Test
    public void update() {
        Meal updated = new Meal(meal1);
        updated.setDescription("UpdatedMeal");
        updated.setCalories(updated.getCalories() + 50);
        service.update(updated, user.getId());
        assertMatch(service.get(updated.getId(), user.getId()), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception {
        Meal updated = new Meal(meal1);
        updated.setDescription("UpdatedMeal");
        updated.setCalories(updated.getCalories() + 50);
        service.update(updated, admin.getId());
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 11, 0), "Завтрак", 500);
        Meal created = service.create(newMeal, user.getId());
        newMeal.setId(created.getId());
        assertMatch(newMeal, created);
    }
}