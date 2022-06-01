package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Body;
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.repository.CarRepository;

import java.util.List;

@Service
public class CarService {
    private final CarRepository store;

    public CarService(CarRepository store) {
        this.store = store;
    }

    public List<Brand> findAllBrands() {
        return store.findAllBrands();
    }

    public List<Body> findAllBodies() {
        return store.findAllBodies();
    }

    public Brand findBrandById(int id) {
        return store.findBrandById(id);
    }

    public Body findBodyById(int id) {
        return store.findBodyById(id);
    }
}
