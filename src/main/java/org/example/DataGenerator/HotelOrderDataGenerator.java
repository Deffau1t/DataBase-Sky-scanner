package org.example.DataGenerator;

import org.example.HibernateExample;
import org.example.model.Hotel;
import org.example.model.HotelOrder;
import org.example.model.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static org.example.HibernateExample.BATCH_SIZE;
import static org.example.HibernateExample.TOTAL_RECORDS;

public class HotelOrderDataGenerator implements DataGenerator {
    @Override
    public void generateData() {
        SessionFactory sessionFactory = HibernateExample.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();

            // Загружаем существующие Hotel и Order для связывания
            List<Hotel> hotels = session.createQuery("FROM Hotel", Hotel.class).getResultList();
            List<Order> orders = session.createQuery("FROM Order", Order.class).getResultList();

            for (int i = 0; i < TOTAL_RECORDS; i++) {
                HotelOrder hotelOrder = generateRandomHotelOrder(hotels, orders, session);
                session.save(hotelOrder);

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

    private static HotelOrder generateRandomHotelOrder(List<Hotel> hotels, List<Order> orders, Session session) {
        Random random = new Random();
        HotelOrder hotelOrder = new HotelOrder();

        // Выбираем случайные Hotel и Order
        Hotel hotel = hotels.get(random.nextInt(hotels.size()));
        Order order = orders.get(random.nextInt(orders.size()));

        hotelOrder.setHotel(hotel);
        hotelOrder.setOrder(order);

        // Генерация случайных дат
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.plusDays(random.nextInt(30));
        LocalDate endDate = startDate.plusDays(random.nextInt(30) + 1);

        // Проверка доступности даты
        if (isDateAvailable(session, hotel, startDate, endDate)) {
            hotelOrder.setStartDate(startDate);
            hotelOrder.setEndDate(endDate);
        } else {
            // Если дата занята, генерируем новый заказ
            return generateRandomHotelOrder(hotels, orders, session);
        }

        return hotelOrder;
    }

    private static boolean isDateAvailable(Session session, Hotel hotel, LocalDate startDate, LocalDate endDate) {
        // Проверяем, есть ли заказы на эту дату для данного отеля
        List<HotelOrder> hotelOrders = session.createQuery(
                        "SELECT ho FROM HotelOrder ho WHERE ho.hotel = :hotel AND ho.startDate <= :endDate AND ho.endDate >= :startDate", HotelOrder.class)
                .setParameter("hotel", hotel)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
        return hotelOrders.isEmpty();
    }
}