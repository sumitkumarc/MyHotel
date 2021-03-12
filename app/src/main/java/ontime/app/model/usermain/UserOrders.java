package ontime.app.model.usermain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserOrders {
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
}
