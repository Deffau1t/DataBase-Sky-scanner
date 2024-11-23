package org.example;

import lombok.Getter;
import org.example.DataGenerator.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

public class HibernateExample {
    @Getter
    private static SessionFactory sessionFactory;
    public static final int BATCH_SIZE = 20; // Размер пакета для сохранения
    public static final int TOTAL_RECORDS = 500; // Общее количество записей для создания
    public static final int TOTAL_RECORDS_PRIMITIVES = 1000; // Общее количество записей для создания

    private static final AirlineTicketsDataGenerator airlineTicketsDataGenerator = new AirlineTicketsDataGenerator();
    private static final CarRentDataGenerator carRentDataGenerator = new CarRentDataGenerator();
    private static final CarRentOrderDataGenerator carRentOrderDataGenerator = new CarRentOrderDataGenerator();
    private static final HotelDataGenerator hotelDataGenerator = new HotelDataGenerator();
    private static final HotelOrderDataGenerator hotelOrderDataGenerator = new HotelOrderDataGenerator();
    private static final OrderDataGenerator orderDataGenerator = new OrderDataGenerator();
    private static final OrderStatusDataGenerator orderStatusDataGenerator = new OrderStatusDataGenerator();
    private static final PaymentInfoDataGenerator paymentInfoDataGenerator = new PaymentInfoDataGenerator();
    private static final ReviewDataGenerator reviewDataGenerator = new ReviewDataGenerator();
    private static final RoleDataGenerator roleDataGenerator = new RoleDataGenerator();
    private static final UserDataGenerator userDataGenerator = new UserDataGenerator();

    public static void main(String[] args) {

        try {
            // Перенаправление вывода в файл
            PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
            System.setOut(out);
            System.setErr(out);
            sessionFactory = new Configuration().configure().buildSessionFactory();
            userDataGenerator.generateData();
            roleDataGenerator.generateData();
            hotelDataGenerator.generateData();
            orderDataGenerator.generateData();
            airlineTicketsDataGenerator.generateData();
            paymentInfoDataGenerator.generateData();
            orderStatusDataGenerator.generateData();
            hotelOrderDataGenerator.generateData();
            reviewDataGenerator.generateData();
            carRentDataGenerator.generateData();
            carRentOrderDataGenerator.generateData();
            // Выполнение SQL-запроса
            executeSqlQuery();

            sessionFactory.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void executeSqlQuery() {
        try (Session session = sessionFactory.openSession()) {
            String sqlQuery = "SELECT " +
                    "ho.id AS hotel_order_id, " +
                    "ho.startDate, " +
                    "ho.endDate, " +
                    "h.id AS hotel_id, " +
                    "h.name AS hotel_name, " +
                    "o.id AS order_id, " +
                    "u.id AS user_id, " +
                    "u.firstName, " +
                    "u.secondName " +
                    "FROM hotel_order ho " + // Исправлено имя таблицы
                    "JOIN hotels h ON ho.hotel_id = h.id " + // Исправлено имя таблицы
                    "JOIN orders o ON ho.order_id = o.id " + // Исправлено имя таблицы
                    "JOIN users u ON o.user_id = u.id " + // Исправлено имя таблицы
                    "WHERE EXISTS (" +
                    "SELECT 1 " +
                    "FROM hotel_order ho2 " + // Исправлено имя таблицы
                    "WHERE ho2.hotel_id = ho.hotel_id " +
                    "AND ho2.id <> ho.id " +
                    "AND (" +
                    "(ho2.startDate BETWEEN ho.startDate AND ho.endDate) OR " +
                    "(ho2.endDate BETWEEN ho.startDate AND ho.endDate) OR " +
                    "(ho.startDate BETWEEN ho2.startDate AND ho2.endDate) OR " +
                    "(ho.endDate BETWEEN ho2.startDate AND ho2.endDate)" +
                    ")" +
                    ") " +
                    "ORDER BY h.id, ho.startDate";

            NativeQuery<Object[]> query = session.createNativeQuery(sqlQuery);
            List<Object[]> results = query.getResultList();

            if (results.isEmpty()) {
                System.out.println("No overlapping bookings found. Data generation is correct.");
            } else {
                System.out.println("Overlapping bookings found:");
                for (Object[] result : results) {
                    System.out.println("HotelOrder ID: " + result[0] +
                            ", Start Date: " + result[1] +
                            ", End Date: " + result[2] +
                            ", Hotel ID: " + result[3] +
                            ", Hotel Name: " + result[4] +
                            ", Order ID: " + result[5] +
                            ", User ID: " + result[6] +
                            ", First Name: " + result[7] +
                            ", Second Name: " + result[8]);
                }
            }
        }
    }
}
