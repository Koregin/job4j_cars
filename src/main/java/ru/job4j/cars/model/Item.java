package ru.job4j.cars.model;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private Date created;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "body_id")
    private Body body;

    @Column(name = "sold_status", nullable = false)
    private int soldStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private byte[] photo;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public boolean isSoldStatus() {
        return soldStatus == 1;
    }

    public void setSoldStatus(int soldStatus) {
        this.soldStatus = soldStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Item{"
                + "id=" + id
                + ", description='" + description + '\''
                + ", created=" + created
                + ", brand=" + brand
                + ", body=" + body
                + ", soldStatus=" + soldStatus
                + ", user=" + user
                + ", photo=" + Arrays.toString(photo)
                + '}';
    }
}
