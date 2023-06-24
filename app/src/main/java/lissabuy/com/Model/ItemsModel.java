package lissabuy.com.Model;

public class ItemsModel {

    public String description;
    public Double price;
    public String title;
    public String image;

    public ItemsModel() {
    }

    public ItemsModel(String description, Double price, String title, String image) {
        this.description = description;
        this.price = price;
        this.title = title;
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
