package ontime.app.model.advertisements;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdvertisementsExample {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responceData")
    @Expose
    private AdvertisementsResponceData responceData;

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

    public AdvertisementsResponceData getResponceData() {
        return responceData;
    }

    public void setResponceData(AdvertisementsResponceData responceData) {
        this.responceData = responceData;
    }
}
