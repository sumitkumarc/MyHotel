package ontime.app.model.usermain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRestaurantsData {
    @SerializedName("id")
    @Expose
    private Integer id;
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
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("password")
    @Expose
    private String password;
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
    @SerializedName("contract")
    @Expose
    private String contract;
    @SerializedName("full_address")
    @Expose
    private Object fullAddress;
    @SerializedName("latitude")
    @Expose
    private Object latitude;
    @SerializedName("longitude")
    @Expose
    private Object longitude;
    @SerializedName("rate_sum")
    @Expose
    private String rateSum;
    @SerializedName("avg_rate")
    @Expose
    private String avgRate;
    @SerializedName("rate_users")
    @Expose
    private String rateUsers;
    @SerializedName("is_banned")
    @Expose
    private Integer isBanned;
    @SerializedName("remember_token")
    @Expose
    private Object rememberToken;
    @SerializedName("device_type")
    @Expose
    private Object deviceType;
    @SerializedName("device_token")
    @Expose
    private Object deviceToken;
    @SerializedName("email_verified_at")
    @Expose
    private Integer emailVerifiedAt;
    @SerializedName("resource")
    @Expose
    private Object resource;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("distance")
    @Expose
    private Object distance;

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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Object getLatitude() {
        return latitude;
    }

    public void setLatitude(Object latitude) {
        this.latitude = latitude;
    }

    public Object getLongitude() {
        return longitude;
    }

    public void setLongitude(Object longitude) {
        this.longitude = longitude;
    }

    public String getRateSum() {
        return rateSum;
    }

    public void setRateSum(String rateSum) {
        this.rateSum = rateSum;
    }

    public String getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(String avgRate) {
        this.avgRate = avgRate;
    }

    public String getRateUsers() {
        return rateUsers;
    }

    public void setRateUsers(String rateUsers) {
        this.rateUsers = rateUsers;
    }

    public Integer getIsBanned() {
        return isBanned;
    }

    public void setIsBanned(Integer isBanned) {
        this.isBanned = isBanned;
    }

    public Object getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(Object rememberToken) {
        this.rememberToken = rememberToken;
    }

    public Object getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Object deviceType) {
        this.deviceType = deviceType;
    }

    public Object getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(Object deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Integer getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(Integer emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public Object getResource() {
        return resource;
    }

    public void setResource(Object resource) {
        this.resource = resource;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Object getDistance() {
        return distance;
    }

    public void setDistance(Object distance) {
        this.distance = distance;
    }
}
