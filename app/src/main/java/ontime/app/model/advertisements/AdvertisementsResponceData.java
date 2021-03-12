package ontime.app.model.advertisements;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdvertisementsResponceData {

    @SerializedName("advertisement")
    @Expose
    private Advertisement advertisement;

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }

}
