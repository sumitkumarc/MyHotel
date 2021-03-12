package ontime.app.model.restaurantlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ontime.app.model.usermain.Restaurant;
import ontime.app.restaurant.model.readerOrder.OrderDetail;

public class ResponceDatum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("image")
    @Expose
    private Object image;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("Licence")
    @Expose
    private String licence;
    @SerializedName("branch_name")
    @Expose
    private String branchName;
    @SerializedName("location_link")
    @Expose
    private String locationLink;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("account_number")
    @Expose
    private String accountNumber;
    @SerializedName("authorised_person_name")
    @Expose
    private String authorisedPersonName;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("email_verified_at")
    @Expose
    private Integer emailVerifiedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getLocationLink() {
        return locationLink;
    }

    public void setLocationLink(String locationLink) {
        this.locationLink = locationLink;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAuthorisedPersonName() {
        return authorisedPersonName;
    }

    public void setAuthorisedPersonName(String authorisedPersonName) {
        this.authorisedPersonName = authorisedPersonName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(Integer emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }



    ///  Menu list
    @SerializedName("menu_id")
    @Expose
    private Integer menuId;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("is_active")
    @Expose
    private Integer isActive;




    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }



    @SerializedName("sender_id")
    @Expose
    private Integer senderId;
    @SerializedName("reciver_id")
    @Expose
    private Integer reciverId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("read_status")
    @Expose
    private Integer readStatus;
    @SerializedName("sender_type")
    @Expose
    private String senderType;
    @SerializedName("noty_type")
    @Expose
    private Integer notyType;
    @SerializedName("resource")
    @Expose
    private Object resource;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("restaurant")
    @Expose
    private Restaurant restaurant;



    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReciverId() {
        return reciverId;
    }

    public void setReciverId(Integer reciverId) {
        this.reciverId = reciverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public Integer getNotyType() {
        return notyType;
    }

    public void setNotyType(Integer notyType) {
        this.notyType = notyType;
    }

    public Object getResource() {
        return resource;
    }

    public void setResource(Object resource) {
        this.resource = resource;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }


    @SerializedName("order_number")
    @Expose
    private String orderNumber;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("restaurant_id")
    @Expose
    private Integer restaurantId;
    @SerializedName("total_quantity")
    @Expose
    private String totalQuantity;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("delivery_type")
    @Expose
    private String deliveryType;
    @SerializedName("service_fee")
    @Expose
    private String serviceFee;
    @SerializedName("service_fee_tax")
    @Expose
    private String serviceFeeTax;
    @SerializedName("total_tax")
    @Expose
    private String totalTax;
    @SerializedName("origanal_item_price")
    @Expose
    private String origanalItemPrice;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("order_status")
    @Expose
    private Integer orderStatus;
    @SerializedName("delivery_status")
    @Expose
    private Integer deliveryStatus;
    @SerializedName("reported_status")
    @Expose
    private Integer reportedStatus;
    @SerializedName("delivery_time")
    @Expose
    private String deliveryTime;
    @SerializedName("delivered_time")
    @Expose
    private String deliveredTime;
    @SerializedName("countdown_time")
    @Expose
    private String countdownTime;
    @SerializedName("is_on_time")
    @Expose
    private Integer isOnTime;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("coupon_code")
    @Expose
    private Object couponCode;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("review")
    @Expose
    private Object review;
    @SerializedName("order_detail")
    @Expose
    private List<OrderDetail> orderDetail = null;


    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
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

    public String getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(String totalTax) {
        this.totalTax = totalTax;
    }

    public String getOriganalItemPrice() {
        return origanalItemPrice;
    }

    public void setOriganalItemPrice(String origanalItemPrice) {
        this.origanalItemPrice = origanalItemPrice;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(Integer deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Integer getReportedStatus() {
        return reportedStatus;
    }

    public void setReportedStatus(Integer reportedStatus) {
        this.reportedStatus = reportedStatus;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveredTime() {
        return deliveredTime;
    }

    public void setDeliveredTime(String deliveredTime) {
        this.deliveredTime = deliveredTime;
    }

    public String getCountdownTime() {
        return countdownTime;
    }

    public void setCountdownTime(String countdownTime) {
        this.countdownTime = countdownTime;
    }

    public Integer getIsOnTime() {
        return isOnTime;
    }

    public void setIsOnTime(Integer isOnTime) {
        this.isOnTime = isOnTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Object getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(Object couponCode) {
        this.couponCode = couponCode;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Object getReview() {
        return review;
    }

    public void setReview(Object review) {
        this.review = review;
    }

    public List<OrderDetail> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<OrderDetail> orderDetail) {
        this.orderDetail = orderDetail;
    }




}
