package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Body;
import ru.job4j.cars.model.Brand;

import java.util.List;
import java.util.function.Function;

@Repository
public class CarRepository {
    private final SessionFactory sf;

    public CarRepository(SessionFactory sf) {
        this.sf = sf;
    }

    public List<Brand> findAllBrands() {
        return this.tx(session -> session.createQuery("from Brand", Brand.class).list());
    }

    public List<Body> findAllBodies() {
        return this.tx(session -> session.createQuery("from Body", Body.class).list());
    }

    public Brand findBrandById(int id) {
        return this.tx(session -> session.createQuery("from Brand b where b.id = :fId ", Brand.class)
                .setParameter("fId", id)
                .uniqueResult());

    }

    public Body findBodyById(int id) {
        return this.tx(session -> session.createQuery("from Body b where b.id = :fId ", Body.class)
                .setParameter("fId", id)
                .uniqueResult());
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
