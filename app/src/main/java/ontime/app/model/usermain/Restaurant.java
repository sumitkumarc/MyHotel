package ontime.app.model.usermain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Restaurant {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("Licence")
    @Expose
    private String licence;
    @SerializedName("branch_name")
    @Expose
    private String branchName;
    @SerializedName("location_link")
    @Expose
    private String locationLink;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("account_number")
    @Expose
    private String accountNumber;
    @SerializedName("authorised_person_name")
    @Expose
    private String authorisedPersonName;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("email_verified_at")
    @Expose
    private Integer emailVerifiedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

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

    public String getLocationLink() {
        return locationLink;
    }

    public void setLocationLink(String locationLink) {
        this.locationLink = locationLink;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
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



    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;



    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }



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
    @SerializedName("is_active")
    @Expose
    private Integer isActive;
    @SerializedName("size_detail")
    @Expose
    private List<SizeDetail> sizeDetail = null;
    @SerializedName("addition_detail")
    @Expose
    private List<AdditionDetail> additionDetail = null;
    @SerializedName("removal_detail")
    @Expose
    private List<RemovalDetail> removalDetail = null;


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

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }


    public List<SizeDetail> getSizeDetail() {
        return sizeDetail;
    }

    public void setSizeDetail(List<SizeDetail> sizeDetail) {
        this.sizeDetail = sizeDetail;
    }

    public List<AdditionDetail> getAdditionDetail() {
        return additionDetail;
    }

    public void setAdditionDetail(List<AdditionDetail> additionDetail) {
        this.additionDetail = additionDetail;
    }

    public List<RemovalDetail> getRemovalDetail() {
        return removalDetail;
    }

    public void setRemovalDetail(List<RemovalDetail> removalDetail) {
        this.removalDetail = removalDetail;
    }


    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("google_location_link")
    @Expose
    private String googleLocationLink;
    @SerializedName("ibn_number")
    @Expose
    private String ibnNumber;
    @SerializedName("ibn_card_img")
    @Expose
    private String ibnCardImg;
    @SerializedName("contract")
    @Expose
    private String contract;
    @SerializedName("full_address")
    @Expose
    private Object fullAddress;
    @SerializedName("avg_rate")
    @Expose
    private String avgRate;
    @SerializedName("is_banned")
    @Expose
    private Integer isBanned;
    @SerializedName("resource")
    @Expose
    private Object resource;


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }



    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }


    public String getGoogleLocationLink() {
        return googleLocationLink;
    }

    public void setGoogleLocationLink(String googleLocationLink) {
        this.googleLocationLink = googleLocationLink;
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



    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public Object getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(Object fullAddress) {
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
