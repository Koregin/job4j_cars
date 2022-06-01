package ru.job4j.cars.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.model.Item;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.CarService;
import ru.job4j.cars.service.ItemService;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class ItemController {
    private final ItemService itemService;
    private final CarService carService;

    public ItemController(ItemService itemService, CarService carService) {
        this.itemService = itemService;
        this.carService = carService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setUsername("Гость");
        }
        model.addAttribute("user", user);
        model.addAttribute("brands", carService.findAllBrands());
        model.addAttribute("bodies", carService.findAllBodies());
        model.addAttribute("items", itemService.findAll());
        return "index";
    }

    @GetMapping("/items/brand/{brandId}")
    public String itemsByBrand(Model model, @PathVariable("brandId") int id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setUsername("Гость");
        }
        model.addAttribute("user", user);
        model.addAttribute("brands", carService.findAllBrands());
        model.addAttribute("bodies", carService.findAllBodies());
        model.addAttribute("items", itemService.findItemsByBrand(id));
        model.addAttribute("filter", "brand");
        return "index";
    }

    @GetMapping("/items/body/{bodyId}")
    public String itemsByBody(Model model, @PathVariable("bodyId") int id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setUsername("Гость");
        }
        model.addAttribute("user", user);
        model.addAttribute("brands", carService.findAllBrands());
        model.addAttribute("bodies", carService.findAllBodies());
        model.addAttribute("items", itemService.findItemsByBody(id));
        model.addAttribute("filter", "body");
        return "index";
    }

    @GetMapping("/items/withPhoto")
    public String itemsWithPhoto(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setUsername("Гость");
        }
        model.addAttribute("user", user);
        model.addAttribute("brands", carService.findAllBrands());
        model.addAttribute("bodies", carService.findAllBodies());
        model.addAttribute("items", itemService.findItemsWithPhoto());
        model.addAttribute("filter", "photo");
        return "index";
    }

    @GetMapping("/fullCarInfo/{itemId}")
    public String fullInfoItem(Model model, @PathVariable("itemId") int id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setUsername("Гость");
        }
        model.addAttribute("user", user);
        Item item = itemService.findById(id);
        model.addAttribute("creator", item.getUser().getId() == user.getId());
        model.addAttribute("item", item);
        return "carInfo";
    }

    @GetMapping("/photoCar/{itemId}")
    public ResponseEntity<Resource> download(@PathVariable("itemId") Integer itemId) throws IOException {
        byte[] bImage = Files.readAllBytes(Paths.get("images/noPhoto.jpg"));
        Item item = itemService.findById(itemId);
        ResponseEntity<Resource> response = ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(bImage));
        if (item.getPhoto() != null) {
            response = ResponseEntity.ok()
                    .headers(new HttpHeaders())
                    .contentLength(item.getPhoto().length)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new ByteArrayResource(item.getPhoto()));
        }
        return response;
    }

    @GetMapping("/formAddItem")
    public String addItem(Model model) {
        model.addAttribute("brands", carService.findAllBrands());
        model.addAttribute("bodies", carService.findAllBodies());
        return "addItem";
    }

    @PostMapping("/createItem")
    public String createCandidate(@ModelAttribute Item item,
                                  @RequestParam("file") MultipartFile file,
                                  @RequestParam("brandId") int brandId,
                                  @RequestParam("bodyId") int bodyId,
                                  HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");
        item.setUser(user);
        item.setBody(carService.findBodyById(bodyId));
        item.setBrand(carService.findBrandById(brandId));
        if (file.getBytes().length > 0) {
            item.setPhoto(file.getBytes());
        }
        itemService.create(item);
        return "redirect:/index";
    }

    @GetMapping("/soldStatus/{itemId}")
    public String soldStatus(Model model, @PathVariable("itemId") int id, HttpSession session) {
        itemService.setSoldStatus(id);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setUsername("Гость");
        }
        model.addAttribute("user", user);
        Item item = itemService.findById(id);
        model.addAttribute("creator", item.getUser().getId() == user.getId());
        model.addAttribute("item", item);
        return "carInfo";
    }

    @GetMapping("/deleteItem/{itemId}")
    public String deleteItem(@PathVariable("itemId") int id) {
        itemService.delete(id);
        return "redirect:/index";
    }
}
