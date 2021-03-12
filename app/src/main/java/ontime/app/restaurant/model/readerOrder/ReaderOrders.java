package ontime.app.restaurant.model.readerOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReaderOrders {
    @SerializedName("new_order")
    @Expose
    private List<ReaderNewOrder> newOrder = null;
    @SerializedName("proccessing")
    @Expose
    private List<ReaderProccessing> proccessing = null;
    @SerializedName("cancel")
    @Expose
    private List<ReaderCancel> cancel = null;
    @SerializedName("completed")
    @Expose
    private List<ReaderCompleted> completed = null;

    public List<ReaderNewOrder> getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(List<ReaderNewOrder> newOrder) {
        this.newOrder = newOrder;
    }

    public List<ReaderProccessing> getProccessing() {
        return proccessing;
    }

    public void setProccessing(List<ReaderProccessing> proccessing) {
        this.proccessing = proccessing;
    }

    public List<ReaderCancel> getCancel() {
        return cancel;
    }

    public void setCancel(List<ReaderCancel> cancel) {
        this.cancel = cancel;
    }

    public List<ReaderCompleted> getCompleted() {
        return completed;
    }

    public void setCompleted(List<ReaderCompleted> completed) {
        this.completed = completed;
    }
}
