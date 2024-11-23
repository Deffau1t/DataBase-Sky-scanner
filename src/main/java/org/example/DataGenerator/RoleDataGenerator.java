package org.example.DataGenerator;

import org.example.HibernateExample;
import org.example.model.Role;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Random;

import static org.example.HibernateExample.BATCH_SIZE;
import static org.example.HibernateExample.TOTAL_RECORDS;

public class RoleDataGenerator implements DataGenerator {
    @Override
    public void generateData() {
        SessionFactory sessionFactory = HibernateExample.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();

            // Загружаем существующие User для связывания
            List<User> users = session.createQuery("FROM User", User.class).getResultList();

            for (int i = 0; i < TOTAL_RECORDS; i++) {
                User user = users.get(i);
                Role role = generateRandomRole(user);
                session.save(role);

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

    private static Role generateRandomRole(User user) {
        Role role = new Role();

        // Устанавливаем пользователя для роли
        role.setUser(user);

        // Генерация случайного имени и типа роли
        role.setType(generateRandomType());

        return role;
    }

    private static String generateRandomType() {
        String[] types = {"System", "Custom", "Guest", "Admin", "User"};
        return types[new Random().nextInt(types.length)];
    }
}
