package org.example.DataGenerator;

import org.example.HibernateExample;
import org.example.model.CarRent;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Random;

import static org.example.HibernateExample.BATCH_SIZE;
import static org.example.HibernateExample.TOTAL_RECORDS;

public class CarRentDataGenerator implements DataGenerator {

    @Override
    public void generateData() {
        SessionFactory sessionFactory = HibernateExample.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();

            for (int i = 0; i < TOTAL_RECORDS; i++) {
                CarRent carRent = generateRandomCarRent();
                session.save(carRent);

                // Сохраняем пакет данных
                if (i % BATCH_SIZE == 0) {
                    session.flush();
                    session.clear();
                }
            }

            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    private static CarRent generateRandomCarRent() {
        Random random = new Random();
        CarRent carRent = new CarRent();

        // Генерация случайных данных
        carRent.setName(generateRandomName());
        carRent.setAge(random.nextInt(20) + 1); // Возраст от 1 до 20
        carRent.setPrice(random.nextInt(10000) + 500);
        carRent.setColor(generateRandomColor());
        carRent.setMileage(random.nextInt(100000) + 5000);

        return carRent;
    }

    private static String generateRandomName() {
        return carNames[new Random().nextInt(carNames.length)];
    }

    private static String generateRandomColor() {
        return faker.color().name();
    }
}
