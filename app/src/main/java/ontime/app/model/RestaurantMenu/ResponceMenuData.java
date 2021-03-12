package ontime.app.model.RestaurantMenu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponceMenuData    {
    @SerializedName("restaurant")
    @Expose
    private ResponceMenuRestaurant restaurant;

    public ResponceMenuRestaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(ResponceMenuRestaurant restaurant) {
        this.restaurant = restaurant;
    }
}
