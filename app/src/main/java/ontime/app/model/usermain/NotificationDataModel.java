package ontime.app.model.usermain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ontime.app.model.usermain.Restaurant;
import ontime.app.restaurant.model.MerchantUser;

public class NotificationDataModel {

    @SerializedName("id")
    @Expose
    private Integer id;
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
    @SerializedName("reciver_type")
    @Expose
    private Object reciverType;
    @SerializedName("noty_type")
    @Expose
    private Integer notyType;
    @SerializedName("resource")
    @Expose
    private Object resource;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("restaurant")
    @Expose
    private Restaurant restaurant;
    @SerializedName("user")
    @Expose
    private MerchantUser user;
    public MerchantUser getUser() {
        return user;
    }

    public void setUser(MerchantUser user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Object getReciverType() {
        return reciverType;
    }

    public void setReciverType(Object reciverType) {
        this.reciverType = reciverType;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

}
