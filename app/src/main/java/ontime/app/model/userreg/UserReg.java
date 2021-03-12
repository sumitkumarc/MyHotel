package ontime.app.model.userreg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserReg {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private UserRegData data;

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

    public UserRegData getData() {
        return data;
    }

    public void setData(UserRegData data) {
        this.data = data;
    }
}
