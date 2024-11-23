package org.example.DataGenerator;

import org.example.HibernateExample;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Random;

import static org.example.HibernateExample.BATCH_SIZE;
import static org.example.HibernateExample.TOTAL_RECORDS;

public class UserDataGenerator implements DataGenerator {
    @Override
    public void generateData() {
        SessionFactory sessionFactory = HibernateExample.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();

            for (int i = 0; i < TOTAL_RECORDS; i++) {
                User user = generateRandomUser();
                session.save(user);

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

    private static User generateRandomUser() {
        User user = new User();

        // Генерация случайных данных
        user.setFirstName(generateRandomFirstName());
        user.setSecondName(generateRandomSecondName());
        user.setSettings(generateRandomSettings());
        user.setMail(generateRandomMail(user.getFirstName(), user.getSecondName()));

        return user;
    }

    private static String generateRandomFirstName() {
        return faker.name().firstName();
    }

    private static String generateRandomSecondName() {
        return faker.name().lastName();
    }

    private static String generateRandomSettings() {
        String[] settings = {"Default", "Advanced", "Custom", "Basic", "Expert"};
        return settings[new Random().nextInt(settings.length)];
    }

    private static String generateRandomMail(String firstName, String secondName) {
        String[] domains = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "example.com"};
        return firstName.toLowerCase() + "." + secondName.toLowerCase() + "@" + domains[new Random().nextInt(domains.length)];
    }
}
