package ontime.app.restaurant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ontime.app.model.restaurantlist.ResponceDatum;

public class NotificationExample {


    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responceData")
    @Expose
    private List<NotificationDataModel> responceData = null;

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

    public List<NotificationDataModel> getResponceData() {
        return responceData;
    }

    public void setResponceData(List<NotificationDataModel> responceData) {
        this.responceData = responceData;
    }

}
