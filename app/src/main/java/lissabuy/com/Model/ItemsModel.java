package lissabuy.com.Model;

import java.util.HashMap;
import java.util.List;

public class ItemsModel {

    public int count;
    public String description;
    public int id_category;
    public String image;
    public float price;
    public String seller;
    public int state;
    public int stock;
    public String title;
    public String uid_user;

    public ItemsModel() {
    }

    public ItemsModel(int count, String description, int id_category, String image, float price, String seller, int state, int stock, String title, String uid_user) {
        this.count = count;
        this.description = description;
        this.id_category = id_category;
        this.image = image;
        this.price = price;
        this.seller = seller;
        this.state = state;
        this.stock = stock;
        this.title = title;
        this.uid_user = uid_user;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid_user() {
        return uid_user;
    }

    public void setUid_user(String uid_user) {
        this.uid_user = uid_user;
    }
}