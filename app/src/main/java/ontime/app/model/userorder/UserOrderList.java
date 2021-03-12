package ontime.app.model.userorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserOrderList {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responceData")
    @Expose
    private UserOrderListData responceData;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserOrderListData getResponceData() {
        return responceData;
    }

    public void setResponceData(UserOrderListData responceData) {
        this.responceData = responceData;
    }
}
