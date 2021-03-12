package ontime.app.model.usernewmain;

import ontime.app.model.usermain.ResponceData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExampleUserItem {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responceData")
    @Expose
    private UserItemResponceData responceData;

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

    public UserItemResponceData getResponceData() {
        return responceData;
    }

    public void setResponceData(UserItemResponceData responceData) {
        this.responceData = responceData;
    }
}
