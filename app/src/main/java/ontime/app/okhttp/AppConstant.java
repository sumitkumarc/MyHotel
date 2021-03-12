package ontime.app.okhttp;

public class AppConstant {
    public static final String REQUEST_HEADER_TOKEN = "Authorization";
    public static final String CONNECTION_ERROR_MSG = "No connection found. Please connect & try again.";
    public static final String BASE_URL1 = "https://ontime-sa.com/";
    public static final String BASE_URL_READER = BASE_URL1 + "api/merchant/";

    public static final String BASE_URL_NEW =BASE_URL1 + "api/customer/";
    public static final String GET_LOGIN_NEW = BASE_URL_NEW +"login";
    public static final String GET_LOGIN_UPDATE_OTP = BASE_URL_NEW +"updateOtp";
    public static final String GET_RESTAURANT_LIST = BASE_URL_NEW +"getRestaurant";
    public static final String GET_RESTAURANT_PROFILE = BASE_URL_NEW +"restaurantProfile";
    public static final String GET_RESTAURANT_MENU_ITEM = BASE_URL_NEW +"menu_items";
    public static final String GET_RESTAURANT_MENU_ITEM_DETAIL = BASE_URL_NEW +"itemDetail";
    public static final String GET_RESTAURANT_MENU_ITEM_UPDATE_DETAIL = BASE_URL_NEW +"updateCartItem";

    public static final String GET_RESTAURANT_MY_CART = BASE_URL_NEW +"getCart";
    public static final String GET_RESTAURANT_ADD_TO_CART = BASE_URL_NEW +"addtoCart";
    public static final String GET_RESTAURANT_DELETE_CART = BASE_URL_NEW +"removeCartItem";
    public static final String GET_RESTAURANT_DELETE = BASE_URL_NEW +"deleteRestaurantCart";
    public static final String GET_USER_PROFILE = BASE_URL_NEW +"profile";
    public static final String GET_USER_LOGOUT = BASE_URL_NEW +"logout";
    public static final String GET_CONTECT_US = BASE_URL_NEW +"contactUs";
    public static final String GET_ABOUT_US = BASE_URL_NEW +"getContents";
    public static final String GET_ITEM_BY_CATEGORY = BASE_URL_NEW +"itemsByCategory";
    public static final String GET_CHNAGE_PASSWORD = BASE_URL_NEW +"changePassword";
    public static final String GET_USER_UPDATE_PROFILE = BASE_URL_NEW +"updateProfile";
    public static final String GET_USER_UPLOAD_IMAGE = BASE_URL_NEW +"uploadProfileImage";
    public static final String GET_CHECK_NUMBER_ISEXIST = BASE_URL_NEW +"checkNumberIsExist";
    public static final String GET_FORGOT_PASSWORD = BASE_URL_NEW +"forgotPassword";
    public static final String GET_USER_SIGNUP = BASE_URL_NEW +"signUp";
    public static final String GET_USER_ORDER_LIST = BASE_URL_NEW +"orderListing";
    public static final String GET_USER_DASHBOARD = BASE_URL_NEW +"dashboard";
    public static final String GET_ADVERTISEMENTS = BASE_URL_NEW +"getAdvertisements";
    public static final String GET_PLACE_ORDER = BASE_URL_NEW +"placeOrder";
    public static final String GET_DISCOUNT = BASE_URL_NEW +"getDiscount";
    public static final String GET_WALLET_BALANCE = BASE_URL_NEW +"addWalletBalance";
    public static final String GET_WALLET = BASE_URL_NEW +"getWallet";
    public static final String GET_USER_CANCEL_ORDER = BASE_URL_NEW +"cancelOrder";
    public static final String GET_USER_NOTIFICATION = BASE_URL_NEW +"getNotification";
    public static final String GET_UPDATE_DEVICE_TOKEN = BASE_URL_NEW +"updateDeviceToken";
    public static final String GET_READER_ORDERDETAIL = BASE_URL_READER +"getOrders";

    public static final String GET_READER_ORDER_ACCEPT = BASE_URL_READER +"acceptOrder";
    public static final String GET_READER_ORDER_REJECT = BASE_URL_READER +"rejectOrder";
    public static final String GET_READER_ORDER_COMPLETE = BASE_URL_READER +"completeOrder";
    public static final String GET_MURCHANT_NOTI = BASE_URL_READER +"getNotification";
	 public static final String GET_READER_LOGOUT = BASE_URL_READER +"logout";
	 public static final String GET_READER_DELETE_NOTIFICATION = BASE_URL_READER +"deleteNotification";
	 public static final String GET_USER_DELETE_NOTIFICATION = BASE_URL_NEW +"deleteNotification";
	 public static final String GET_USER_SERRCH_RESRAURANTS = BASE_URL_NEW +"searchRestaurants";

	 public static final String GET_USER_RESTAURNT_RATING = BASE_URL_NEW +"getRestaurantRating";
	 public static final String GET_USER_PAYMENT_FAIL = BASE_URL_NEW +"paymentFail";
	 public static final String GET_USER_PAYMENT_POST = BASE_URL_NEW +"postPayment";

    public static final String GET_USER_MSG = BASE_URL_NEW +"sendReport";
    public static final String GET_USER_RATE = BASE_URL_NEW +"rateReview";

    public static final String GET_ADD_TO_CART_SUPER = BASE_URL_NEW +"addCartSuper";
    public static final String GET_SUPER_MENU_ITEM_UPDATE_DETAIL = BASE_URL_NEW +"updateCartItemSp";
    public static final String GET_USER_ORDERDETAIL = BASE_URL_NEW +"orderDetail";



}
