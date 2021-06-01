package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private final static String url = "jdbc:mysql://localhost:3306/mydbtest?serverTimezone=Europe/Moscow&useSSL=false";
    private final static String login = "root";
    private final static String pas = "admin123";
    private static Connection connection;

    public static Connection connectDbJdbc() {
        try {
            if (connection == null) {
                new com.mysql.cj.jdbc.Driver();
                connection = DriverManager.getConnection(url, login, pas);
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

}



