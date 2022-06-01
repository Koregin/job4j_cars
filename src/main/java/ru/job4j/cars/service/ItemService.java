package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Item;
import ru.job4j.cars.repository.AdsRepository;

import java.util.List;

@Service
public class ItemService {
    private final AdsRepository store;

    public ItemService(AdsRepository store) {
        this.store = store;
    }

    public Item create(Item item) {
        return store.add(item);
    }

    public boolean update(Item item) {
        return store.replace(item);
    }

    public boolean delete(int id) {
        return store.delete(id);
    }

    public Item findById(int id) {
        return store.findById(id);
    }

    public List<Item> findAll() {
        return store.findAll();
    }

    public void setSoldStatus(int id) {
        store.setSoldStatus(id);
    }

    public List<Item> findLastDayItem() {
        return store.findLastDayItems();
    }

    public List<Item> findItemsWithPhoto() {
        return store.findItemsWithPhoto();
    }

    public List<Item> findItemsByBrand(int brand) {
        return store.findItemsByBrand(brand);
    }

    public List<Item> findItemsByBody(int id) {
        return store.findItemsByBody(id);
    }
}
