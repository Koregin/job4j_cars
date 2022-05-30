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
import java.util.List;
import java.util.function.Function;

public class AdRepository {
    private final SessionFactory sf = sf();

    public SessionFactory sf() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public List<Item> findLastDayItems(User user) {
        return this.tx(session -> session.createQuery("select i from Item i where i.user = :fUser "
                        + "and DATE(i.created) = :fDate", Item.class)
                .setParameter("fUser", user)
                .setParameter("fDate", LocalDateTime.now().toLocalDate())
                .list());
    }

    public List<Item> findItemsWithPhoto(User user) {
        return this.tx(session -> session.createQuery("select i from Item i where i.user = :fUser and i.photo != :fPhoto", Item.class)
                .setParameter("fUser", user)
                .setParameter("fPhoto", new byte[0])
                .list());
    }

    public List<Item> findItemsByBrand(User user, Brand brand) {
        return this.tx(session -> session.createQuery("select i from Item i where i.user = :fUser and i.brand = :fBrand", Item.class)
                .setParameter("fUser", user)
                .setParameter("fBrand", brand).list());

    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
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
