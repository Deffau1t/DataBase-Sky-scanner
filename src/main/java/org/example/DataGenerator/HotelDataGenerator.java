package org.example.DataGenerator;

import org.example.HibernateExample;
import org.example.model.Hotel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.util.Random;

import static org.example.HibernateExample.BATCH_SIZE;
import static org.example.HibernateExample.TOTAL_RECORDS;

public class HotelDataGenerator implements DataGenerator {
    @Override
    public void generateData() {
        SessionFactory sessionFactory = HibernateExample.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            for (int i = 0; i < TOTAL_RECORDS; i++) {
                Hotel hotel = generateRandomHotel();
                session.save(hotel);

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

    private static Hotel generateRandomHotel() {
        Hotel hotel = new Hotel();

        // Генерация случайных данных
        hotel.setName(generateRandomName());
        hotel.setDescription(generateRandomDescription());

        return hotel;
    }

    private static String generateRandomName() {
        return faker.company().name() + " Hotel";
    }

    private static String generateRandomDescription() {
        return hotelDescriptions[new Random().nextInt(hotelDescriptions.length)];
    }
}