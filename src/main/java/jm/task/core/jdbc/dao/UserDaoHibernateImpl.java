package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private Transaction transaction = null;

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        String SQL = "CREATE TABLE IF NOT  EXISTS users" +
                "(`id` INT NOT NULL AUTO_INCREMENT," +
                " `name` VARCHAR(45) NOT NULL," +
                " `lastName` VARCHAR(45) NOT NULL," +
                " `age` INT NOT NULL," +
                " PRIMARY KEY (`id`))";
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Query query = session.createSQLQuery(SQL);
            query.executeUpdate();
            transaction.commit();
            session.close();
        } catch (Exception e) {
            System.err.println("Ошибка создания таблицы");
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Query query = session.createSQLQuery("DROP TABLE IF EXISTS users");
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            System.err.println("Ошибка удаления таблицы");
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }

    }


    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User user = new User(name, lastName, age);
            user.setId((Long) session.save(user));
            transaction.commit();
            System.out.println("User" + name + "добавлен в БД");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Ошибка добаления Юзера");
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Query query = session.createQuery("DELETE FROM User WHERE id = :userId");
            query.setParameter("userId", id);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Ошибка удаления Юзера");
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Query query = session.createQuery("FROM User");
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Ошибка получения всех users");
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
           transaction = session.beginTransaction();

            Query query = session.createQuery("DELETE User");
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            System.err.println("Ошибка очистки таблицы");
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}