package ontime.app.restaurant.model.readerOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReaderData {
    @SerializedName("orders")
    @Expose
    private ReaderOrders orders;

    public ReaderOrders getOrders() {
        return orders;
    }

    public void setOrders(ReaderOrders orders) {
        this.orders = orders;
    }
}
