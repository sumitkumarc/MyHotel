package ontime.app.model.RestaurantMenu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponceMenuRestaurant {
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
    private Object licence;
    @SerializedName("branch_name")
    @Expose
    private String branchName;
    @SerializedName("location_link")
    @Expose
    private Object locationLink;
    @SerializedName("bank_name")
    @Expose
    private Object bankName;
    @SerializedName("account_number")
    @Expose
    private Object accountNumber;
    @SerializedName("authorised_person_name")
    @Expose
    private Object authorisedPersonName;
    @SerializedName("phone_number")
    @Expose
    private Object phoneNumber;
    @SerializedName("token")
    @Expose
    private Object token;
    @SerializedName("email_verified_at")
    @Expose
    private Integer emailVerifiedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
//    @SerializedName("categories")
//    @Expose
//    private List<Category> categories = null;

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

    public Object getLicence() {
        return licence;
    }

    public void setLicence(Object licence) {
        this.licence = licence;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Object getLocationLink() {
        return locationLink;
    }

    public void setLocationLink(Object locationLink) {
        this.locationLink = locationLink;
    }

    public Object getBankName() {
        return bankName;
    }

    public void setBankName(Object bankName) {
        this.bankName = bankName;
    }

    public Object getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Object accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Object getAuthorisedPersonName() {
        return authorisedPersonName;
    }

    public void setAuthorisedPersonName(Object authorisedPersonName) {
        this.authorisedPersonName = authorisedPersonName;
    }

    public Object getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Object phoneNumber) {
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

//    public List<Category> getCategories() {
//        return categories;
//    }
//
//    public void setCategories(List<Category> categories) {
//        this.categories = categories;
//    }

}
