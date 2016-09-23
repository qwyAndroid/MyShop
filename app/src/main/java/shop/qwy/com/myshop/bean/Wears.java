package shop.qwy.com.myshop.bean;

import java.io.Serializable;

/**
 * created by qwyAndroid on 2016/9/23
 */
public class Wears implements Serializable{
    private Long id;
    private String imgUrl;
    private String name;
    private Float price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
