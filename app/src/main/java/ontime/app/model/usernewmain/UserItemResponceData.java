package ontime.app.model.usernewmain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ontime.app.model.usermain.Cart;
import ontime.app.model.usermain.UserCart;
import ontime.app.model.usermain.UserCartItem;

public class UserItemResponceData {
    @SerializedName("items")
    @Expose
    private List<UserItem> itemsa = null;

    public List<UserItem> getUserItem() {
        return itemsa;
    }

    public void setUserItem(List<UserItem> items) {
        this.itemsa = items;
    }

    @SerializedName("cart")
    @Expose
    private UserCartItem cart;

    public UserCartItem getCart() {
        return cart;
    }

    public void setCart(UserCartItem cart) {
        this.cart = cart;
    }

}
