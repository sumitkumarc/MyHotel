package ontime.app.model.RestaurantMenu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RstMenuExample {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responceData")
    @Expose
    private ResponceMenuData responceData;

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

    public ResponceMenuData getResponceData() {
        return responceData;
    }

    public void setResponceData(ResponceMenuData responceData) {
        this.responceData = responceData;
    }

}
