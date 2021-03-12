package ontime.app.restaurant.model.readerOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReaderExample {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responceData")
    @Expose
    private ReaderData responceData;

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

    public ReaderData getResponceData() {
        return responceData;
    }

    public void setResponceData(ReaderData responceData) {
        this.responceData = responceData;
    }

}
