package ontime.app.customer.doneActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import ontime.app.R;
import ontime.app.customer.Adapter.RvRestaurantListAdapter;
import ontime.app.customer.Adapter.VpNivoSliderAdapter;
import ontime.app.databinding.ActivityRestaurantlistBinding;
import ontime.app.model.advertisements.AdvertisementDatum;
import ontime.app.model.advertisements.AdvertisementsExample;
import ontime.app.model.usermain.ExampleUser;
import ontime.app.model.usermain.UserRestaurantsData;
import ontime.app.model.usermain.Userdate;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.okhttp.SharedPreferenceManagerFile;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;
import ontime.app.utils.LanguageManager;
import ontime.app.utils.SessionManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.RequestBody;

import static ontime.app.utils.Common.DELIVER_TYPE;
import static ontime.app.utils.Common.MERCHANT_TYPE;

public class RestaurantlistActivity extends BaseActivity implements View.OnClickListener, APIcall.ApiCallListner {

    ActivityRestaurantlistBinding binding;
    ProgressDialog dialog;
    RvRestaurantListAdapter mAdapter;
    SessionManager sessionManager;
    Userdate userData;
    private SharedPreferenceManagerFile sharedPref;
    int CAT_TYPE = 1;
    ArrayList<AdvertisementDatum> advertisementData = new ArrayList<>();
    FusedLocationProviderClient mFusedLocationClient;
    String Latitude = "";
    String Langtitude = "";
    int PERMISSION_ID = 101;
    private int pageCount = 1;
    List<UserRestaurantsData> userRestaurantsData = new ArrayList<>();
    private boolean isLoading = false;
    Locale locale;
    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurantlist);
    }

    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            RestaurantlistActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (binding.vpBanner.getCurrentItem() < advertisementData.size() - 1) {
                        binding.vpBanner.setCurrentItem(binding.vpBanner.getCurrentItem() + 1);
                    } else {
                        binding.vpBanner.setCurrentItem(0);
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(RestaurantlistActivity.this);
        userData = sessionManager.getUserDetails();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (locale.getLanguage().equals(LanguageManager.LANGUAGE_KEY_ARABIC)) {
                    binding.closeNew.setVisibility(View.VISIBLE);
                    binding.close.setVisibility(View.GONE);
                } else {
                    binding.close.setVisibility(View.VISIBLE);
                    binding.closeNew.setVisibility(View.GONE);

                }
            }
        });
        sharedPref = new SharedPreferenceManagerFile(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        Animation bottomUp = AnimationUtils.loadAnimation(this, R.anim.top_to_up);
        binding.ivMenu.startAnimation(bottomUp);
        binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() != 0) {
                    if (s.toString().length() > 3)
                        filter(s.toString());
                } else {
                    GetAPICallRestaurantList(CAT_TYPE);
                }

            }
        });


        setUpUI();
    }




    @SuppressLint("UseCompatLoadingForDrawables")
    private void setUpUI() {

        if (MERCHANT_TYPE == 1) {
            Common.setSystemBarColor(this, R.color.colorAccent);
//            Common.setSystemBarLight(this);
            binding.llBack.setBackground(getResources().getDrawable(R.drawable.rest_circle_itemback));
            binding.txtTop.setBackground(getResources().getDrawable(R.drawable.btn_top));
            binding.edSearch.setBackground(getResources().getDrawable(R.drawable.btn_golden));
            binding.ivMenu.setColorFilter(getResources().getColor(R.color.colorAccent));
            binding.ivFilter.setColorFilter(getResources().getColor(R.color.colorAccent));
            binding.ivHome.setColorFilter(getResources().getColor(R.color.colorAccent));
            binding.ivCart.setColorFilter(getResources().getColor(R.color.colorAccent));
            binding.ivSearch.setColorFilter(getResources().getColor(R.color.colorAccent));
            binding.ivMyorder.setColorFilter(getResources().getColor(R.color.colorAccent));
            binding.ivChangPws.setColorFilter(getResources().getColor(R.color.colorAccent));
            binding.ivContactUs.setColorFilter(getResources().getColor(R.color.colorAccent));
            binding.ivPayment.setColorFilter(getResources().getColor(R.color.colorAccent));
            binding.ivNotification.setColorFilter(getResources().getColor(R.color.colorAccent));
            binding.ivLanguages.setColorFilter(getResources().getColor(R.color.colorAccent));
            binding.ivAboutUs.setColorFilter(getResources().getColor(R.color.colorAccent));
            binding.AuthorUserName.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtHome.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtCart.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtMyorder.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtChangPws.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtPayment.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtNearMe.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtNotification.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtAboutUs.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtContactUs.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtLanguages.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            Common.setSystemBarColor(this, R.color.super_mart);
//            Common.setSystemBarLight(this);
            binding.llBack.setBackground(getResources().getDrawable(R.drawable.super_circle_itemback));
            binding.txtTop.setBackground(getResources().getDrawable(R.drawable.btn_top_kesari));
            binding.ivMenu.setColorFilter(getResources().getColor(R.color.super_mart));
            binding.edSearch.setBackground(getResources().getDrawable(R.drawable.btn_kesari));
            binding.ivFilter.setColorFilter(getResources().getColor(R.color.super_mart));
            binding.ivHome.setColorFilter(getResources().getColor(R.color.super_mart));
            binding.ivSearch.setColorFilter(getResources().getColor(R.color.super_mart));
            binding.ivCart.setColorFilter(getResources().getColor(R.color.super_mart));
            binding.txtNearMe.setTextColor(getResources().getColor(R.color.super_mart));
            binding.ivChangPws.setColorFilter(getResources().getColor(R.color.super_mart));
            binding.ivMyorder.setColorFilter(getResources().getColor(R.color.super_mart));
            binding.ivPayment.setColorFilter(getResources().getColor(R.color.super_mart));
            binding.ivAboutUs.setColorFilter(getResources().getColor(R.color.super_mart));
            binding.ivLanguages.setColorFilter(getResources().getColor(R.color.super_mart));
            binding.ivNotification.setColorFilter(getResources().getColor(R.color.super_mart));
            binding.ivContactUs.setColorFilter(getResources().getColor(R.color.super_mart));
            binding.AuthorUserName.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtHome.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtCart.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtMyorder.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtChangPws.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtPayment.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtNotification.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtAboutUs.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtContactUs.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtLanguages.setTextColor(getResources().getColor(R.color.super_mart));
        }

        binding.llMain.setVisibility(View.GONE);
        CAT_TYPE = getIntent().getIntExtra("CAT_TYPE", 1);
        LinearLayoutManager mLayoutManager1as = new LinearLayoutManager(getContext());
        mLayoutManager1as.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvList.setLayoutManager(mLayoutManager1as);
        binding.AuthorUserName.setText(Common.isStrempty(userData.getFullName()));
        Glide.with(this).load(Common.isStrempty(userData.getImage())).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(binding.AuthorAvatar);
        GetAPICallBannerList();
        GetAPICallRestaurantList(MERCHANT_TYPE);
        binding.rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int visibleItemCount = mLayoutManager1as.getChildCount();
                    int totalItemCount = mLayoutManager1as.getItemCount();
                    int firstVisibleItemPosition = mLayoutManager1as.findFirstVisibleItemPosition();
                    if (isLoading) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            isLoading = false;
                            pageCount++;
                            recyclerView.scrollToPosition(userRestaurantsData.size() - 1);
                            GetAPICallRestaurantList(CAT_TYPE);
//                            ApiGetProductByCategoryList(getContext());
                        }
                    }
                }
            }
        });




    }

    @Override
    protected void setListener() {
        super.setListener();
        binding.ivMenu.setOnClickListener(this);
        binding.llBack.setOnClickListener(this);
        binding.ivFilter.setOnClickListener(this);
        binding.ivSearch.setOnClickListener(this);
        binding.close.setOnClickListener(this);
        binding.llhome.setOnClickListener(this);
        binding.llabout.setOnClickListener(this);
        binding.llangages.setOnClickListener(this);
        binding.llcart.setOnClickListener(this);
        binding.llmyorder.setOnClickListener(this);
        binding.llpaymet.setOnClickListener(this);
        binding.llprofile.setOnClickListener(this);
        binding.llcontact.setOnClickListener(this);
        binding.llnotifi.setOnClickListener(this);
        binding.llChangePws.setOnClickListener(this);
        binding.txtNearMe.setOnClickListener(this);
        binding.txtTop.setOnClickListener(this);
        binding.close.setOnClickListener(this);
        binding.closeNew.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
//                if (locale.getLanguage().equals(LanguageManager.LANGUAGE_KEY_ARABIC)) {
//
//                } else {
//                    binding.drawerLayout.openDrawer(Gravity.LEFT);
//                }
                binding.drawerLayout.openDrawer(Gravity.RIGHT);
                break;
            case R.id.close_new:
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.txt_top:
                showData(1);
                break;
            case R.id.txt_near_me:
                showData(2);
                break;
            case R.id.llhome:
                startActivity(new Intent(RestaurantlistActivity.this, UserDashboardActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.iv_filter:
                binding.ivFilter.setVisibility(View.GONE);
                binding.edSearch.setVisibility(View.GONE);
                binding.ivSearch.setVisibility(View.VISIBLE);
                binding.txtTop.setVisibility(View.VISIBLE);
                binding.txtNearMe.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_search:
                binding.ivFilter.setVisibility(View.VISIBLE);
                binding.edSearch.setVisibility(View.VISIBLE);
                binding.ivSearch.setVisibility(View.GONE);
                binding.txtTop.setVisibility(View.GONE);
                binding.txtNearMe.setVisibility(View.GONE);
                break;
            case R.id.ll_back:
                binding.drawerLayout.closeDrawers();
                onBackPressed();
                break;
            case R.id.close:
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.llprofile:
                startActivity(new Intent(RestaurantlistActivity.this, UserProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.ll_change_pws:
                startActivity(new Intent(RestaurantlistActivity.this, ChangePasswordActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.llcart:
                startActivity(new Intent(RestaurantlistActivity.this, RestCartItemActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.llmyorder:
                startActivity(new Intent(RestaurantlistActivity.this, MyOrdersListActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.llpaymet:
                startActivity(new Intent(RestaurantlistActivity.this, PaymentActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.llnotifi:
                startActivity(new Intent(RestaurantlistActivity.this, NotificationActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.llcontact:
                startActivity(new Intent(RestaurantlistActivity.this, ContactusActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.llabout:
                startActivity(new Intent(RestaurantlistActivity.this, AboutusActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.llangages:
                startActivity(new Intent(RestaurantlistActivity.this, LanguageActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                binding.drawerLayout.closeDrawers();
                break;
            default:
                break;
        }
    }

    private void showData(int i) {
        Common.FILTER_TYPE = i;
        if (i == 1) {
            if (MERCHANT_TYPE == 1) {
                binding.txtTop.setTextColor(getResources().getColor(R.color.white));
                binding.txtTop.setBackground(getResources().getDrawable(R.drawable.btn_top));
                binding.txtNearMe.setTextColor(getResources().getColor(R.color.colorAccent));
                binding.txtNearMe.setBackground(null);
            } else {
                binding.txtTop.setTextColor(getResources().getColor(R.color.white));
                binding.txtTop.setBackground(getResources().getDrawable(R.drawable.btn_top_kesari));
                binding.txtNearMe.setTextColor(getResources().getColor(R.color.super_mart));
                binding.txtNearMe.setBackground(null);
            }
        } else {
            if (MERCHANT_TYPE == 1) {
                binding.txtTop.setTextColor(getResources().getColor(R.color.colorAccent));
                binding.txtTop.setBackground(null);
                binding.txtNearMe.setTextColor(getResources().getColor(R.color.white));
                binding.txtNearMe.setBackground(getResources().getDrawable(R.drawable.btn_top));
            } else {
                binding.txtTop.setTextColor(getResources().getColor(R.color.super_mart));
                binding.txtTop.setBackground(null);
                binding.txtNearMe.setTextColor(getResources().getColor(R.color.white));
                binding.txtNearMe.setBackground(getResources().getDrawable(R.drawable.btn_top_kesari));
            }
        }
        GetAPICallRestaurantList(CAT_TYPE);

    }

    private void showDialog() {
        dialog = new ProgressDialog(RestaurantlistActivity.this);
        dialog.setMessage(getResources().getString(R.string.Please_wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        setUpUI();
//        DELIVER_TYPE = "";
        binding.AuthorUserName.setText(Common.isStrempty(userData.getFullName()));
        Glide.with(this).load(Common.isStrempty(userData.getImage())).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(binding.AuthorAvatar);
        if (checkPermissions()) {
            getLastLocation();
        }

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    getLocastionAddress(location);
                                }
                            }
                        }
                );
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
                final String message = getResources().getString(R.string.Turn_on_location);
                builder.setMessage(message)
                        .setPositiveButton(getResources().getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int id) {
                                        dialogInterface.cancel();
                                        startActivity(new Intent(action));
                                    }
                                })
                        .setNegativeButton(getResources().getString(R.string.Cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int id) {
                                        dialogInterface.cancel();
                                    }
                                });
                builder.create().show();
            }
        } else {
            requestPermissions();
        }
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            getLocastionAddress(mLastLocation);
        }
    };

    private void getLocastionAddress(Location location) {
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                String locality = addresses.get(0).getLocality();
//                String subLocality = addresses.get(0).getSubLocality();
//                String state = addresses.get(0).getAdminArea();
//                String country = addresses.get(0).getCountryName();
//                String postalCode = addresses.get(0).getPostalCode();
//                String knownName = addresses.get(0).getFeatureName();
                Latitude = String.valueOf(location.getLatitude());
                Langtitude = String.valueOf(location.getLongitude());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GetAPICallRestaurantList(int CatTpe) {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("marchent_type", MERCHANT_TYPE);
            jsonObject.put("page", pageCount);
            jsonObject.put("no_of_rows", "12");
            jsonObject.put("latitude", Latitude);
            jsonObject.put("longitude", Langtitude);
            jsonObject.put("filter_type", Common.FILTER_TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_RESTAURANT_LIST;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_RESTAURANT_LIST, this);
    }

    private void GetAPICallBannerList() {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
//        try {
////            jsonObject.put("marchent_type", CatTpe);
////            jsonObject.put("page", "1");
////            jsonObject.put("no_of_rows", "10");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_ADVERTISEMENTS;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_ADVERTISEMENTS, this);
    }

    private void filter(String text) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("marchent_type", MERCHANT_TYPE);
            jsonObject.put("page", 1);
            jsonObject.put("no_of_rows", 12);
            jsonObject.put("keyword", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_USER_SERRCH_RESRAURANTS;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_USER_SERRCH_RESRAURANTS, this);
    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_USER_SERRCH_RESRAURANTS) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_RESTAURANT_LIST) {
            showDialog();
        }

    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        hideDialog();
        if (operationCode == APIcall.OPERATION_ADVERTISEMENTS) {
            hideDialog();
            binding.llMain.setVisibility(View.VISIBLE);
            binding.txtWorning.setVisibility(View.GONE);
            Gson gson = new Gson();
            AdvertisementsExample exampleUser = gson.fromJson(response, AdvertisementsExample.class);
            if (exampleUser.getStatus() == 200) {
                advertisementData.clear();
                binding.vpBanner.setVisibility(View.VISIBLE);
                if(exampleUser.getResponceData().getAdvertisement().getData().size() != 0) {
                    for (int i = 0; i < exampleUser.getResponceData().getAdvertisement().getData().size(); i++) {
                        if (exampleUser.getResponceData().getAdvertisement().getData().get(i).getType() == MERCHANT_TYPE) {
                            advertisementData.add(exampleUser.getResponceData().getAdvertisement().getData().get(i));
                        }
                    }
                    VpNivoSliderAdapter vpNivoSliderAdapter = new VpNivoSliderAdapter(getContext(), advertisementData);
                    binding.vpBanner.setAdapter(vpNivoSliderAdapter);
                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
                }else {
                 //   binding.vpBanner.setVisibility(View.GONE);
                }
            } else if (exampleUser.getStatus() == 401) {
                binding.llMain.setVisibility(View.GONE);
                binding.txtWorning.setVisibility(View.VISIBLE);
                binding.txtWorning.setText(Common.isStrempty(exampleUser.getMessage()));
            } else {
                Toast.makeText(RestaurantlistActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (operationCode == APIcall.OPERATION_RESTAURANT_LIST) {
            hideDialog();
            binding.llMain.setVisibility(View.VISIBLE);
            binding.txtWorning.setVisibility(View.GONE);
            Gson gson = new Gson();
            ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
            if (exampleUser.getStatus() == 200) {
                userRestaurantsData = exampleUser.getResponceData().getRestaurants().getData();
                if (exampleUser.getResponceData().getRestaurants().getLastPage() != null) {
                    if (pageCount == exampleUser.getResponceData().getRestaurants().getLastPage()) {
                        isLoading = false;
                    } else {
                        isLoading = true;
                    }
                }

                if (userRestaurantsData.size() != 0) {
                    binding.rvList.setVisibility(View.VISIBLE);
                    binding.tvNodata.setVisibility(View.GONE);
                    mAdapter = new RvRestaurantListAdapter(getContext(), userRestaurantsData);
                    binding.rvList.setItemAnimator(new DefaultItemAnimator());
                    binding.rvList.setAdapter(mAdapter);
                } else {
                    binding.rvList.setVisibility(View.GONE);
                    binding.tvNodata.setVisibility(View.VISIBLE);
                }
            } else if (exampleUser.getStatus() == 401) {
                binding.llMain.setVisibility(View.GONE);
                binding.txtWorning.setVisibility(View.VISIBLE);
                binding.txtWorning.setText(Common.isStrempty(exampleUser.getMessage()));
            } else {
                Toast.makeText(RestaurantlistActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (operationCode == APIcall.OPERATION_USER_SERRCH_RESRAURANTS) {
            hideDialog();
            binding.llMain.setVisibility(View.VISIBLE);
            binding.txtWorning.setVisibility(View.GONE);
            Gson gson = new Gson();
            ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
            if (exampleUser.getStatus() == 200) {
                List<UserRestaurantsData> userRestaurantsData = exampleUser.getResponceData().getRestaurants().getData();
                if (userRestaurantsData.size() != 0) {
                    binding.rvList.setVisibility(View.VISIBLE);
                    binding.tvNodata.setVisibility(View.GONE);
                    mAdapter = new RvRestaurantListAdapter(getContext(), userRestaurantsData);
                    binding.rvList.setItemAnimator(new DefaultItemAnimator());
                    binding.rvList.setAdapter(mAdapter);
                } else {
                    binding.rvList.setVisibility(View.GONE);
                    binding.tvNodata.setVisibility(View.VISIBLE);
                }

            } else if (exampleUser.getStatus() == 401) {
                binding.llMain.setVisibility(View.GONE);
                binding.txtWorning.setVisibility(View.VISIBLE);
                binding.txtWorning.setText(Common.isStrempty(exampleUser.getMessage()));
                //    Toast.makeText(RestaurantlistActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RestaurantlistActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        hideDialog();
//        } catch (Exception e) {
//            hideDialog();
//        }
    }

    @Override
    public void onFail(int operationCode, String response) {
        hideDialog();
    }

    @Override
    public void onBackPressed() {
        if (this.binding.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            this.binding.drawerLayout.closeDrawers();
        } else {
            finish();
        }
    }
}