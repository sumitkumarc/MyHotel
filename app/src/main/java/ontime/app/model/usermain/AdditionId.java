package ontime.app.model.usermain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdditionId {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("adidtion_item")
    @Expose
    private String adidtionItem;
    @SerializedName("price")
    @Expose
    private String price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
