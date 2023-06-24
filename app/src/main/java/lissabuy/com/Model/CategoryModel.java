package lissabuy.com.Model;

public class CategoryModel {

    public  String category_id;
    public String category_title;

    public String category_img;

    public CategoryModel() {
    }

    public CategoryModel(String category_id, String category_title, String category_img) {
        this.category_id = category_id;
        this.category_title = category_title;
        this.category_img = category_img;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_title() {
        return category_title;
    }

    public void setCategory_title(String category_title) {
        this.category_title = category_title;
    }

    public String getCategory_img() {
        return category_img;
    }

    public void setCategory_img(String category_img) {
        this.category_img = category_img;
    }
}