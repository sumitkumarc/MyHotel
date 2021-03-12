package ontime.app.model.usermain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponceData {
    @SerializedName("user")
    @Expose
    private Userdate user;

    public Userdate getUser() {
        return user;
    }

    public void setUser(Userdate user) {
        this.user = user;
    }

    @SerializedName("orders")
    @Expose
    private UserOrders orders;

    public UserOrders getOrders() {
        return orders;
    }

    public void setOrders(UserOrders orders) {
        this.orders = orders;
    }
    @SerializedName("restaurants")
    @Expose
    private UserRestaurants restaurants;

    public UserRestaurants getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(UserRestaurants restaurants) {
        this.restaurants = restaurants;
    }

    @SerializedName("items")
    @Expose
    private UserRestItems items;

    public UserRestItems getItems() {
        return items;
    }

    public void setItems(UserRestItems items) {
        this.items = items;
    }

    @SerializedName("restaurant")
    @Expose
    private UserRestaurantPro restaurant;

    public UserRestaurantPro getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(UserRestaurantPro restaurant) {
        this.restaurant = restaurant;
    }

    @SerializedName("cart")
    @Expose
    private List<UserCart> cart = null;

    public List<UserCart> getCart() {
        return cart;
    }

    public void setCart(List<UserCart> cart) {
        this.cart = cart;
    }

    @SerializedName("contact")
    @Expose
    private UserContact contact;

    public UserContact getContact() {
        return contact;
    }

    public void setContact(UserContact contact) {
        this.contact = contact;
    }


    @SerializedName("content")
    @Expose
    private UserContent content;

    public UserContent getContent() {
        return content;
    }

    public void setContent(UserContent content) {
        this.content = content;
    }


    @SerializedName("total_order")
    @Expose
    private Integer totalOrder;
    @SerializedName("total_spend")
    @Expose
    private String totalSpend;
    @SerializedName("cancelled")
    @Expose
    private Integer cancelled;

    public Integer getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(Integer totalOrder) {
        this.totalOrder = totalOrder;
    }

    public String getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(String totalSpend) {
        this.totalSpend = totalSpend;
    }

    public Integer getCancelled() {
        return cancelled;
    }

    public void setCancelled(Integer cancelled) {
        this.cancelled = cancelled;
    }

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("percentage")
    @Expose
    private Integer percentage;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("start_date_range")
    @Expose
    private String startDateRange;
    @SerializedName("end_date_range")
    @Expose
    private String endDateRange;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("discounted_amount")
    @Expose
    private String discountedAmount;
    @SerializedName("additional")
    @Expose
    private String additional;
	@SerializedName("tax")
	@Expose
	private String tax;
	@SerializedName("service_fee")
	@Expose
	private String serviceFee;
	@SerializedName("service_fee_tax")
	@Expose
	private String serviceFeeTax;
	@SerializedName("grand_total")
	@Expose
	private String grandTotal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getStartDateRange() {
        return startDateRange;
    }

    public void setStartDateRange(String startDateRange) {
        this.startDateRange = startDateRange;
    }

    public String getEndDateRange() {
        return endDateRange;
    }

    public void setEndDateRange(String endDateRange) {
        this.endDateRange = endDateRange;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountedAmount() {
        return discountedAmount;
    }

    public void setDiscountedAmount(String discountedAmount) {
        this.discountedAmount = discountedAmount;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }
	public String getTax() {
	return tax;
	}

	public void setTax(String tax) {
	this.tax = tax;
	}

	public String getServiceFee() {
	return serviceFee;
	}

	public void setServiceFee(String serviceFee) {
	this.serviceFee = serviceFee;
	}

	public String getServiceFeeTax() {
	return serviceFeeTax;
	}

	public void setServiceFeeTax(String serviceFeeTax) {
	this.serviceFeeTax = serviceFeeTax;
	}

	public String getGrandTotal() {
	return grandTotal;
	}

	public void setGrandTotal(String grandTotal) {
	this.grandTotal = grandTotal;
	}


    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("status")
    @Expose
    private Integer status;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @SerializedName("review")
    @Expose
    private Rest_Review review;

    public Rest_Review getReview() {
        return review;
    }

    public void setReview(Rest_Review review) {
        this.review = review;
    }



}
