package ontime.app.model.usermain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserCart {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("restaurant_id")
    @Expose
    private Integer restaurantId;
    @SerializedName("Restaurant")
    @Expose
    private Restaurant restaurant;
    @SerializedName("delivery_type")
    @Expose
    private String deliveryType;
    @SerializedName("items")
    @Expose
    private List<UserCartItem> items = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<UserCartItem> getItems() {
        return items;
    }

    public void setItems(List<UserCartItem> items) {
        this.items = items;
    }

}
