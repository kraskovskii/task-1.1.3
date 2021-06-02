package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private final static String URL =
            "jdbc:mysql://localhost:3306/mydbtest?serverTimezone=Europe/Moscow&useSSL=false&useSSL=false";
    private final static String USER = "root";
    private final static String PASS = "admin123";
    private static Connection connection;
    private static SessionFactory sessionFactory = null;
    private static StandardServiceRegistry registry;

    public static Connection connectDbJdbc() {
        try {
            if (connection == null) {
                new com.mysql.cj.jdbc.Driver();
                connection = DriverManager.getConnection(URL, USER, PASS);
                if (!connection.isClosed()) {
                    System.out.println("Соединение с БД установленно");
                }
            } else {
                System.err.println("Повторное открытие БД");
            }
        } catch (SQLException throwables) {
            System.err.println("Не удалось загрузить класс драйвер");
        }
        return connection;
    }

    public static void disconnectDbJdbc() {
        try {
            if ((connection != null) && (!connection.isClosed())) {
                connection.close();
                System.out.println("Соединение с БД закрыто");
            } else {
                System.err.println("БД уже была закрыта");
            }
        } catch (SQLException throwables) {
            System.err.println("Ошибка закрытия БД");
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

                Properties settings = new Properties();
                settings.put(Environment.URL, URL);
                settings.put(Environment.USER, USER);
                settings.put(Environment.PASS, PASS);

                registryBuilder.applySettings(settings);
                registry = registryBuilder.build();

                MetadataSources sources = new MetadataSources(registry).addAnnotatedClass(User.class);

                sessionFactory = sources.buildMetadata().buildSessionFactory();

            } catch (Exception e) {
                System.err.println("Ошибка соединения с БД (getSessionFactory) ");
            }
        }
        return sessionFactory;
    }
}



