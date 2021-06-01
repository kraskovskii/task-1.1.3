package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static Connection connection;
    private static final String TABLE_NAME = "users";
    static int count;

    private final static String url = "jdbc:mysql://localhost:3306/mydbtest";
    private final static String login = "root";
    private final static String pas = "admin123";

    public UserDaoJDBCImpl() {
        connection = Util.connectDbJdbc();
    }

    public void createUsersTable() {
        String SQL = "CREATE TABLE users " +
                "(`id` INT NOT NULL AUTO_INCREMENT," +
                " `name` VARCHAR(45) NOT NULL," +
                " `lastName` VARCHAR(45) NOT NULL," +
                " `age` INT NOT NULL," +
                " PRIMARY KEY (`id`))";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(SQL);
            System.out.println("БД успешно создана");
        } catch (SQLException e) {
            System.err.println("Ошибка создания БД");
        }
    }

    public void dropUsersTable() {
        String SQL = "DROP TABLE " + TABLE_NAME;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(SQL);
            System.out.println("Таблица " + TABLE_NAME + " успешно удалена");
        } catch (SQLException e) {
            System.err.println("ошибка удаления таблицы " + TABLE_NAME);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String SQL = "INSERT INTO " + TABLE_NAME + " (name, lastName, age) Values(?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.executeUpdate();
            System.out.println(++count + " Юзер с именем " + name + " добавлен в БД");
        } catch (SQLException e) {
            System.err.println("Ошибка добавления Юзера");
        }
    }

    public void removeUserById(long id) {

        String SQL = "DELETE FROM users where id = " + id;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL)){
            preparedStatement.execute(SQL);
            System.out.println("Юзер с ID = " + id + " удален");
        } catch (SQLException e) {
            System.err.println("Ошибка удаления юзера по ID");
        }
    }

    public List<User> getAllUsers() {
        ResultSet resultSet;
        List<User> usersList = new ArrayList<>();
        String SQL = "SELECT * From " + TABLE_NAME;
        try (Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(SQL);
            System.out.println("Список Юзеров получен");
            while (resultSet.next()) {
                User user = new User(resultSet.getString("name"),
                        resultSet.getString("LastName"),
                        resultSet.getByte("age"));
                user.setId(resultSet.getLong("id"));
                usersList.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error get all users");
        }
        return usersList;
    }

    public void cleanUsersTable() {
        String SQL = "DELETE FROM " + TABLE_NAME;
        try (Statement statement = connection.createStatement()) {
            statement.execute(SQL);
            System.out.println("Таблица " + TABLE_NAME + " очищена");
        } catch (SQLException e) {
            System.err.println("Ошибка очистки таблицы " + TABLE_NAME);
        }
    }
}
