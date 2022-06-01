package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Repository
public class AdsRepository {
    private final SessionFactory sf;

    public AdsRepository(SessionFactory sf) {
        this.sf = sf;
    }

    public Item add(Item item) {
        Integer id = (Integer) this.tx(session -> session.save(item));
        item.setId(id);
        return item;
    }

    public boolean replace(Item item) {
        Integer rsl = this.tx(session -> session.createQuery("update Item i set i.description = :newDesc,"
                        + "i.brand = :fBrand, "
                        + "i.body = :fBody  where i.id = :fId", Item.class)
                .setParameter("newDesc", item.getDescription())
                .setParameter("fBrand", item.getBrand())
                .setParameter("fBody", item.getBody())
                .setParameter("fId", item.getId())
                .executeUpdate());
        return rsl == 1;
    }

    public boolean delete(int id) {
        Integer rsl = this.tx(session -> session.createQuery("delete from Item i where i.id = :fId")
                .setParameter("fId", id)
                .executeUpdate());
        return rsl == 1;
    }

    public Item findById(int id) {
        return (Item) this.tx(session -> session.createQuery("select i from Item i where i.id = :fId", Item.class)
                .setParameter("fId", id)
                .uniqueResult());
    }

    public boolean setSoldStatus(int id) {
        Integer rsl = this.tx(session -> session.createQuery("update Item i set i.soldStatus = :newStatus where i.id = :fId")
                .setParameter("newStatus", 1)
                .setParameter("fId", id)
                .executeUpdate());
        return rsl == 1;
    }

    public List<Item> findAll() {
        return this.tx(session -> session.createQuery("from Item").list());
    }

    public List<Item> findLastDayItems() {
        return this.tx(session -> session.createQuery("select i from Item i where DATE(i.created) = :fDate", Item.class)
                .setParameter("fDate", LocalDateTime.now().toLocalDate())
                .list());
    }

    public List<Item> findItemsWithPhoto() {
        return this.tx(session -> session.createQuery("select i from Item i where i.photo != :fPhoto", Item.class)
                .setParameter("fPhoto", new byte[0])
                .list());
    }

    public List<Item> findItemsByBrand(int brand) {
        return this.tx(session -> session.createQuery("select i from Item i where i.brand.id = :fBrand", Item.class)
                .setParameter("fBrand", brand).list());

    }

    public List<Item> findItemsByBody(int id) {
        return this.tx(session -> session.createQuery("select i from Item i where i.body.id = :fBody", Item.class)
                .setParameter("fBody", id).list());

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
