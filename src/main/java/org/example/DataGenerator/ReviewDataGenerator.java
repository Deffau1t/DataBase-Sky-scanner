package org.example.DataGenerator;

import org.example.HibernateExample;
import org.example.model.Hotel;
import org.example.model.Review;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Random;

import static org.example.HibernateExample.BATCH_SIZE;
import static org.example.HibernateExample.TOTAL_RECORDS;

public class ReviewDataGenerator implements DataGenerator {
    @Override
    public void generateData() {
        SessionFactory sessionFactory = HibernateExample.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();

            // Загружаем существующие User и Hotel для связывания
            List<User> users = session.createQuery("FROM User", User.class).getResultList();
            List<Hotel> hotels = session.createQuery("FROM Hotel", Hotel.class).getResultList();

            for (int i = 0; i < TOTAL_RECORDS; i++) {
                Review review = generateRandomReview(users, hotels);
                session.save(review);

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

    private static Review generateRandomReview(List<User> users, List<Hotel> hotels) {
        Random random = new Random();
        Review review = new Review();

        // Выбираем случайные User и Hotel
        User user = users.get(random.nextInt(users.size()));
        Hotel hotel = hotels.get(random.nextInt(hotels.size()));

        review.setUser(user);
        review.setHotel(hotel);

        // Генерация случайного рейтинга и описания
        review.setRating(random.nextInt(5) + 1); // Рейтинг от 1 до 5
        review.setDescription(generateRandomDescription());

        return review;
    }

    private static String generateRandomDescription() {
        return hotelReviews[new Random().nextInt(hotelReviews.length)];
    }
}
