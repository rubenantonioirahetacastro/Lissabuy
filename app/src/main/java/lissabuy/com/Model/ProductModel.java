package lissabuy.com.Model;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class ProductModel {
    public String key;
    public int category_id;
    public int count;
    public String description;
    public String image;
    public float price;
    public String seller;
    public int state;
    public int stock;
    public String title;
    public String uid_user;
    public List<String> catalogue;

    public ProductModel(String key, int category_id, int count, String description, String image, float price, String seller, int state, int stock, String title, String uid_user, List<String> catalogue) {
        this.key = key;
        this.category_id = category_id;
        this.count = count;
        this.description = description;
        this.image = image;
        this.price = price;
        this.seller = seller;
        this.state = state;
        this.stock = stock;
        this.title = title;
        this.uid_user = uid_user;
        this.catalogue = catalogue;
    }

    public ProductModel() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
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

    public List<String> getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(List<String> catalogue) {
        this.catalogue = catalogue;
    }
}
