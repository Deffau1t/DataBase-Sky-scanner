package org.example.DataGenerator;

import org.example.HibernateExample;
import org.example.model.Hotel;
import org.example.model.Order;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static org.example.HibernateExample.BATCH_SIZE;
import static org.example.HibernateExample.TOTAL_RECORDS;

public class OrderDataGenerator implements DataGenerator {
    @Override
    public void generateData() {
        SessionFactory sessionFactory = HibernateExample.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();

            // Загружаем существующие User и Hotel для связывания
            List<User> users = session.createQuery("FROM User", User.class).getResultList();
            for (int i = 0; i < TOTAL_RECORDS; i++) {
                Order order = generateRandomOrder(users);
                session.save(order);

                // Сохраняем пакет данных
                if (i % BATCH_SIZE == 0) {
                    session.flush();
                    session.clear();
                }
            }

            session.getTransaction().commit();
        } finally {
        }
    }

    private static Order generateRandomOrder(List<User> users) {
        Random random = new Random();
        Order order = new Order();

        // Выбираем случайные User и Hotel
        User user = users.get(random.nextInt(users.size()));
        order.setUser(user);

        // Генерация случайного типа заказа
        String[] types = {"Hotel", "CarRent", "AirlineTicket"};
        order.setType(types[random.nextInt(types.length)]);

        return order;
    }
}
