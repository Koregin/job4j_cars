package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.model.Item;
import ru.job4j.cars.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class AdRepository {
    private final SessionFactory sf = sf();

    public SessionFactory sf() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public List<Item> findLastDayItems(User user) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        Date currentDate = Date.from(currentLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date minusDay = Date.from(currentLocalDateTime.minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        try {
            List<Item> rsl = session.createQuery("select i from Item i where i.user = :fUser "
                            + "and i.created between :dStart and :dEnd", Item.class)
                    .setParameter("fUser", user)
                    .setParameter("dStart", minusDay)
                    .setParameter("dEnd", currentDate).list();
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public List<Item> findItemsWithPhoto(User user) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            List<Item> rsl = session.createSQLQuery("select * from items where user_id = :fUserId and photo is not null")
                    .setParameter("fUserId", user.getId())
                    .addEntity(Item.class)
                    .list();
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public List<Item> findItemsByBrand(User user, Brand brand) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            List<Item> rsl = session.createQuery("select i from Item i where i.user = :fUser and i.brand = :fBrand", Item.class)
                    .setParameter("fUser", user)
                    .setParameter("fBrand", brand).list();
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
