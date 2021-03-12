package ontime.app.model.usermain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserRestaurantPro {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("parent_id")
    @Expose
    private Integer parentId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("licence")
    @Expose
    private String licence;
    @SerializedName("branch_name")
    @Expose
    private String branchName;
    @SerializedName("google_location_link")
    @Expose
    private String googleLocationLink;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("account_number")
    @Expose
    private String accountNumber;
    @SerializedName("ibn_number")
    @Expose
    private String ibnNumber;
    @SerializedName("ibn_card_img")
    @Expose
    private String ibnCardImg;
    @SerializedName("authorised_person_name")
    @Expose
    private String authorisedPersonName;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;


    @SerializedName("orgpw")
    @Expose
    private String orgpw;
    @SerializedName("resource")
    @Expose
    private Object resource;
    @SerializedName("created_at")
    @Expose
    private String createdAt;



    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }









    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    public String getOrgpw() {
        return orgpw;
    }

    public void setOrgpw(String orgpw) {
        this.orgpw = orgpw;
    }


    @SerializedName("token")
    @Expose
    private Object token;
    @SerializedName("email_verified_at")
    @Expose
    private Integer emailVerifiedAt;
    @SerializedName("avag_rating")
    @Expose
    private Integer avag_rating;
    public Integer getAvag_rating() {
        return avag_rating;
    }

    public void setAvag_rating(Integer avag_rating) {
        this.avag_rating = avag_rating;
    }


    @SerializedName("categories")
    @Expose
    private List<UserRestaurantProCategory> categories = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getGoogleLocationLink() {
        return googleLocationLink;
    }

    public void setGoogleLocationLink(String googleLocationLink) {
        this.googleLocationLink = googleLocationLink;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIbnNumber() {
        return ibnNumber;
    }

    public void setIbnNumber(String ibnNumber) {
        this.ibnNumber = ibnNumber;
    }

    public String getIbnCardImg() {
        return ibnCardImg;
    }

    public void setIbnCardImg(String ibnCardImg) {
        this.ibnCardImg = ibnCardImg;
    }

    public String getAuthorisedPersonName() {
        return authorisedPersonName;
    }

    public void setAuthorisedPersonName(String authorisedPersonName) {
        this.authorisedPersonName = authorisedPersonName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Object getToken() {
        return token;
    }

    public void setToken(Object token) {
        this.token = token;
    }

    public Integer getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(Integer emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<UserRestaurantProCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<UserRestaurantProCategory> categories) {
        this.categories = categories;
    }


    // Menu Details
    @SerializedName("menu_id")
    @Expose
    private Integer menuId;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("calories")
    @Expose
    private String calories;
    @SerializedName("other")
    @Expose
    private String other;
    @SerializedName("is_active")
    @Expose
    private Integer isActive;
    @SerializedName("size_detail")
    @Expose
    private List<RestMenuItemSizeDetail> sizeDetail = null;
    @SerializedName("addition_detail")
    @Expose
    private List<RestMenuItemAdditionDetail> additionDetail = null;
    @SerializedName("removal_detail")
    @Expose
    private List<RestMenuItemRemovalDetail> removalDetail = null;


    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }


    public List<RestMenuItemSizeDetail> getSizeDetail() {
        return sizeDetail;
    }

    public void setSizeDetail(List<RestMenuItemSizeDetail> sizeDetail) {
        this.sizeDetail = sizeDetail;
    }

    public List<RestMenuItemAdditionDetail> getAdditionDetail() {
        return additionDetail;
    }

    public void setAdditionDetail(List<RestMenuItemAdditionDetail> additionDetail) {
        this.additionDetail = additionDetail;
    }

    public List<RestMenuItemRemovalDetail> getRemovalDetail() {
        return removalDetail;
    }

    public void setRemovalDetail(List<RestMenuItemRemovalDetail> removalDetail) {
        this.removalDetail = removalDetail;
    }


    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("contract")
    @Expose
    private String contract;
    @SerializedName("full_address")
    @Expose
    private String fullAddress;
    @SerializedName("avg_rate")
    @Expose
    private String avgRate;
    @SerializedName("is_banned")
    @Expose
    private Integer isBanned;



    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(String avgRate) {
        this.avgRate = avgRate;
    }

    public Integer getIsBanned() {
        return isBanned;
    }

    public void setIsBanned(Integer isBanned) {
        this.isBanned = isBanned;
    }

    public Object getResource() {
        return resource;
    }

    public void setResource(Object resource) {
        this.resource = resource;
    }





}
