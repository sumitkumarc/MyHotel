package ontime.app.model.userorder;

import ontime.app.model.usermain.OrderCancelled;
import ontime.app.model.usermain.OrderFinished;
import ontime.app.model.usermain.OrderProccessing;
import ontime.app.model.usermain.Restaurant;
import ontime.app.restaurant.model.readerOrder.OrderDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserOrderListData {

    @SerializedName("proccessing")
    @Expose
    private ArrayList<OrderProccessing> proccessing = null;
    @SerializedName("finished")
    @Expose
    private List<OrderFinished> finished = null;
    @SerializedName("cancelled")
    @Expose
    private List<OrderCancelled> ocancelled = null;

    public List<OrderCancelled> getOCancelled() {
        return ocancelled;
    }

    public void setOCancelled(List<OrderCancelled> ocancelled) {
        this.ocancelled = ocancelled;
    }

    public List<OrderFinished> getFinished() {
        return finished;
    }

    public void setFinished(List<OrderFinished> finished) {
        this.finished = finished;
    }
    public ArrayList<OrderProccessing> getProccessing() {
        return proccessing;
    }

    public void setProccessing(ArrayList<OrderProccessing> proccessing) {
        this.proccessing = proccessing;
    }

    @SerializedName("id")
    @Expose
    private Integer id;
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
    @SerializedName("app_commission")
    @Expose
    private Object appCommission;
    @SerializedName("site_commission_tax")
    @Expose
    private Object siteCommissionTax;
    @SerializedName("total_tax")
    @Expose
    private Object totalTax;
    @SerializedName("grand_total")
    @Expose
    private Object grandTotal;
    @SerializedName("payment_status")
    @Expose
    private Object paymentStatus;
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
    private Object deliveryTime;
    @SerializedName("countdown_time")
    @Expose
    private Object countdownTime;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("resource")
    @Expose
    private Object resource;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("review")
    @Expose
    private Object review;
    @SerializedName("order_detail")
    @Expose
    private List<OrderDetail> orderDetail = null;
    @SerializedName("restaurant")
    @Expose
    private Restaurant restaurant;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Object getAppCommission() {
        return appCommission;
    }

    public void setAppCommission(Object appCommission) {
        this.appCommission = appCommission;
    }

    public Object getSiteCommissionTax() {
        return siteCommissionTax;
    }

    public void setSiteCommissionTax(Object siteCommissionTax) {
        this.siteCommissionTax = siteCommissionTax;
    }

    public Object getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(Object totalTax) {
        this.totalTax = totalTax;
    }

    public Object getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Object grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Object getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Object paymentStatus) {
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

    public Object getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Object deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Object getCountdownTime() {
        return countdownTime;
    }

    public void setCountdownTime(Object countdownTime) {
        this.countdownTime = countdownTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Object getResource() {
        return resource;
    }

    public void setResource(Object resource) {
        this.resource = resource;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }


}
