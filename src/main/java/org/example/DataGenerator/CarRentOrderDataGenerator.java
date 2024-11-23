package org.example.DataGenerator;

import org.example.HibernateExample;
import org.example.model.CarRent;
import org.example.model.CarRentOrder;
import org.example.model.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static org.example.HibernateExample.BATCH_SIZE;
import static org.example.HibernateExample.TOTAL_RECORDS;

public class CarRentOrderDataGenerator implements DataGenerator {
    @Override
    public void generateData() {
        SessionFactory sessionFactory = HibernateExample.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();

            // Загружаем существующие CarRent и Order для связывания
            List<CarRent> carRents = session.createQuery("FROM CarRent", CarRent.class).getResultList();
            List<Order> orders = session.createQuery("FROM Order", Order.class).getResultList();

            for (int i = 0; i < TOTAL_RECORDS; i++) {
                CarRentOrder carRentOrder = generateRandomCarRentOrder(carRents, orders);
                session.save(carRentOrder);

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

    private static CarRentOrder generateRandomCarRentOrder(List<CarRent> carRents, List<Order> orders) {
        Random random = new Random();
        CarRentOrder carRentOrder = new CarRentOrder();

        // Выбираем случайные CarRent и Order
        CarRent carRent = carRents.get(random.nextInt(carRents.size()));
        Order order = orders.get(random.nextInt(orders.size()));

        carRentOrder.setCarRent(carRent);
        carRentOrder.setOrder(order);

        // Генерация случайных дат
        LocalDate now = LocalDate.now();
        carRentOrder.setStartDate(now.plusDays(random.nextInt(30)));
        carRentOrder.setEndDate(now.plusDays(random.nextInt(30) + 30));

        return carRentOrder;
    }
}
