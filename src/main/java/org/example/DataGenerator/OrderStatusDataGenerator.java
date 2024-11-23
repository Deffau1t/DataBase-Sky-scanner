package org.example.DataGenerator;

import org.example.HibernateExample;
import org.example.model.Order;
import org.example.model.OrderStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Random;

import static org.example.HibernateExample.BATCH_SIZE;
import static org.example.HibernateExample.TOTAL_RECORDS;

public class OrderStatusDataGenerator implements DataGenerator {
    @Override
    public void generateData() {
        SessionFactory sessionFactory = HibernateExample.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();

            // Загружаем существующие Order для связывания
            List<Order> orders = session.createQuery("FROM Order", Order.class).getResultList();

            for (int i = 0; i < TOTAL_RECORDS; i++) {
                OrderStatus orderStatus = generateRandomOrderStatus(orders);
                session.save(orderStatus);

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

    private static OrderStatus generateRandomOrderStatus(List<Order> orders) {
        Random random = new Random();
        OrderStatus orderStatus = new OrderStatus();

        // Выбираем случайный Order
        Order order = orders.get(random.nextInt(orders.size()));
        orderStatus.setOrder(order);

        // Генерация случайного статуса и описания
        String[] statuses = {"Pending", "Processing", "Completed", "Cancelled"};
        String[] descriptions = {
            "Order is awaiting processing",
            "Order is being processed",
            "Order has been completed",
            "Order has been cancelled"
        };

        int randomStatusIndex = random.nextInt(statuses.length);
        String status = statuses[randomStatusIndex];
        String description = descriptions[randomStatusIndex];

        orderStatus.setStatus(status);
        orderStatus.setDescription(description);

        return orderStatus;
    }
}
