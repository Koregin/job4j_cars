package ru.job4j.cars.model;

public class HbmRun {
    public static void main(String[] args) {
        /*
        AdsRepository store = new AdsRepository();
        User user = new User();
        user.setId(1);
        Brand brand = new Brand();
        brand.setId(1);
        List<Item> lastItems = store.findLastDayItems(user);
        List<Item> lastItems2 = store.findItemsWithPhoto(user);
        List<Item> lastItems3 = store.findItemsByBrand(user, brand);

        System.out.println("Поиск объявлений за последний день");
        lastItems.forEach(System.out::println);
        System.out.println("Поиск объявлений с фото");
        lastItems2.forEach(System.out::println);
        System.out.println("Поиск объявлений по определенной марке");
        lastItems3.forEach(System.out::println);

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();
            Car car = new Car();
            Engine engine = new Engine();
            session.save(engine);
            Driver driver = new Driver();
            session.save(driver);
            car.setEngine(engine);
            car.getDrivers().add(driver);
            session.save(car);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        */
    }
}
