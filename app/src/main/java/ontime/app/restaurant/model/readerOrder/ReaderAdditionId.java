package ontime.app.restaurant.model.readerOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReaderAdditionId {
    @SerializedName("adidtion_item")
    @Expose
    private String adidtionItem;
    @SerializedName("price")
    @Expose
    private String price;

    public String getAdidtionItem() {
        return adidtionItem;
    }

    public void setAdidtionItem(String adidtionItem) {
        this.adidtionItem = adidtionItem;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
