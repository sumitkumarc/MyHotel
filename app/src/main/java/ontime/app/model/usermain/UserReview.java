package ontime.app.model.usermain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserReview {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("merchant_id")
    @Expose
    private Integer merchantId;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("resource")
    @Expose
    private Object resource;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    private Boolean asReview =true;

    public Boolean getAsReview() {
        return asReview;
    }

    public void setAsReview(Boolean asReview) {
        this.asReview = asReview;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
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

}
