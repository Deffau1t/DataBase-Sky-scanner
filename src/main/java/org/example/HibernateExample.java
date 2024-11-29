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
            orderStatusDataGenerator.generateData();
            paymentInfoDataGenerator.generateData();
            hotelOrderDataGenerator.generateData();
            reviewDataGenerator.generateData();
            carRentDataGenerator.generateData();
            carRentOrderDataGenerator.generateData();
            sessionFactory.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
