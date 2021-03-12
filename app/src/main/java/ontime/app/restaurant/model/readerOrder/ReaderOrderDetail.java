package ontime.app.restaurant.model.readerOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReaderOrderDetail {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
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
    @SerializedName("size_id")
    @Expose
    private ReaderSizeId sizeId;
    @SerializedName("removal_id")
    @Expose
    private ReaderRemovalId removalId;
    @SerializedName("addition_id")
    @Expose
    private ReaderAdditionId additionId;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public ReaderSizeId getSizeId() {
        return sizeId;
    }

    public void setSizeId(ReaderSizeId sizeId) {
        this.sizeId = sizeId;
    }

    public ReaderRemovalId getRemovalId() {
        return removalId;
    }

    public void setRemovalId(ReaderRemovalId removalId) {
        this.removalId = removalId;
    }

    public ReaderAdditionId getAdditionId() {
        return additionId;
    }

    public void setAdditionId(ReaderAdditionId additionId) {
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
}
