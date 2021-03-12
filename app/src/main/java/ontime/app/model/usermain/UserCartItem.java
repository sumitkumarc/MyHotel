package ontime.app.model.usermain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserCartItem {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cart_id")
    @Expose
    private Integer cartId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("restaurant_id")
    @Expose
    private Integer restaurantId;
    @SerializedName("menu_id")
    @Expose
    private Integer menuId;
    @SerializedName("item_id")
    @Expose
    private Integer itemId;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("size_id")
    @Expose
    private UserCartItemSizeId sizeId;
    @SerializedName("removal_id")
    @Expose
    private UserCartItemRemovalId removalId;
    @SerializedName("addition_id")
    @Expose
    private UserCartItemAdditionId additionId;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("unit_price")
    @Expose
    private String unitPrice;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("item_detail")
    @Expose
    private UserCartItemDetail itemDetail;
    @SerializedName("note")
    @Expose
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public UserCartItemSizeId getSizeId() {
        return sizeId;
    }

    public void setSizeId(UserCartItemSizeId sizeId) {
        this.sizeId = sizeId;
    }

    public UserCartItemRemovalId getRemovalId() {
        return removalId;
    }

    public void setRemovalId(UserCartItemRemovalId removalId) {
        this.removalId = removalId;
    }

    public UserCartItemAdditionId getAdditionId() {
        return additionId;
    }

    public void setAdditionId(UserCartItemAdditionId additionId) {
        this.additionId = additionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public UserCartItemDetail getItemDetail() {
        return itemDetail;
    }

    public void setItemDetail(UserCartItemDetail itemDetail) {
        this.itemDetail = itemDetail;
    }
}
