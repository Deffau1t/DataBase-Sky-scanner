package org.example.DataGenerator;

import org.example.HibernateExample;
import org.example.model.Order;
import org.example.model.PaymentInfo;
import org.example.model.Role;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static org.example.HibernateExample.BATCH_SIZE;
import static org.example.HibernateExample.TOTAL_RECORDS;

public class PaymentInfoDataGenerator implements DataGenerator {
    @Override
    public void generateData() {
        SessionFactory sessionFactory = HibernateExample.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();

            // Загружаем существующие User и Order для связывания
            List<User> users = session.createQuery("FROM User", User.class).getResultList();
            List<Order> orders = session.createQuery("FROM Order", Order.class).getResultList();
            List<Role> roles = session.createQuery("FROM Role", Role.class).getResultList();

            for (int i = 0; i < TOTAL_RECORDS; i++) {
                PaymentInfo paymentInfo = generateRandomPaymentInfo(users, orders, roles);
                if (paymentInfo != null) {
                    session.save(paymentInfo);

                    // Сохраняем пакет данных
                    if (i % BATCH_SIZE == 0) {
                        session.flush();
                        session.clear();
                    }
                }
            }

            session.getTransaction().commit();
        } finally {
        }
    }

    private static PaymentInfo generateRandomPaymentInfo(List<User> users, List<Order> orders, List<Role> roles) {
        Random random = new Random();
        PaymentInfo paymentInfo = null;

        // Выбираем случайные User и Order
        User user = users.get(random.nextInt(users.size()));
        Order order = orders.get(random.nextInt(orders.size()));

        // Проверяем, что у пользователя нет роли "Guest"
        boolean isGuest = roles.stream()
                .filter(role -> role.getUser().equals(user))
                .anyMatch(role -> "Guest".equals(role.getType()));

        if (!isGuest) {
            paymentInfo = new PaymentInfo();
            paymentInfo.setUser(user);
            paymentInfo.setOrder(order);

            // Генерация случайных данных
            paymentInfo.setName(user.getFirstName() + " " + user.getSecondName());
            paymentInfo.setCardNumber(generateRandomCardNumber());
            paymentInfo.setCardExpirationDate(LocalDate.now().plusYears(random.nextInt(5)));
            paymentInfo.setCvv(random.nextInt(900) + 100); // CVV от 100 до 999
        }

        return paymentInfo;
    }

    private static String generateRandomCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            cardNumber.append(String.format("%04d", random.nextInt(10000)));
            if (i < 3) {
                cardNumber.append(" ");
            }
        }
        return cardNumber.toString();
    }
}