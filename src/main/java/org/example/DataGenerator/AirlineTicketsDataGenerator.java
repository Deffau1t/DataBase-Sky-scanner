package org.example.DataGenerator;

import org.example.HibernateExample;
import org.example.model.AirlineTicket;
import org.example.model.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static org.example.HibernateExample.BATCH_SIZE;
import static org.example.HibernateExample.TOTAL_RECORDS;

public class AirlineTicketsDataGenerator implements DataGenerator {
    @Override
    public void generateData() {
        SessionFactory sessionFactory = HibernateExample.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();

            // Загружаем существующие Order для связывания
            List<Order> orders = session.createQuery("FROM Order", Order.class).getResultList();

            for (int i = 0; i < TOTAL_RECORDS; i++) {
                AirlineTicket ticket = generateRandomAirlineTicket(orders);
                session.save(ticket);

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

    private static AirlineTicket generateRandomAirlineTicket(List<Order> orders) {
        Random random = new Random();
        AirlineTicket ticket = new AirlineTicket();

        // Выбираем случайный Order
        Order order = orders.get(random.nextInt(orders.size()));
        ticket.setOrder(order);

        // Генерация случайных дат
        LocalDate now = LocalDate.now();
        ticket.setTimeArrive(now.plusDays(random.nextInt(30)));
        ticket.setTimeDeparture(now.plusDays(random.nextInt(30)));

        // Генерация случайных городов
        String cityDeparture = generateRandomCity();
        String cityArrive;
        do {
            cityArrive = generateRandomCity();
        } while (cityDeparture.equals(cityArrive));

        ticket.setCityDeparture(cityDeparture);
        ticket.setCityArrive(cityArrive);

        // Генерация случайных цен и вместимости
        ticket.setPrice(random.nextInt(10000) + 500);
        ticket.setPlaneCapacity(random.nextInt(200) + 50);

        return ticket;
    }

    private static String generateRandomCity() {
        return faker.address().city();
    }
}


