package ontime.app.model.usermain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExampleUser {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responceData")
    @Expose
    private ResponceData responceData;

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

    public ResponceData getResponceData() {
        return responceData;
    }

    public void setResponceData(ResponceData responceData) {
        this.responceData = responceData;
    }
}
