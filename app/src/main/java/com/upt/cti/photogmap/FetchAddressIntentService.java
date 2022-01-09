package com.upt.cti.photogmap;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FetchAddressIntentService extends IntentService {

    private ResultReceiver resultReceiver;

    public FetchAddressIntentService(){
        super("com.upt.cti.photogmap.FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null){
            String errorMessage = "";
            resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
            Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
            if (location == null){
                return;

            }

            Geocoder geocoder = new Geocoder(getApplicationContext(), getResources().getConfiguration().locale );
            List<Address> addressList = new ArrayList<>();
            try {
                addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
                System.out.println(addressList.get(0).getLocality());
                System.out.println(addressList.get(0).getCountryName());
                System.out.println(addressList.get(0).getAdminArea());
                System.out.println(location.getLatitude());
                System.out.println(geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5));
                System.out.println(addressList);
            } catch (Exception exception){
                errorMessage = exception.getMessage();
            }
            if (addressList == null || addressList.isEmpty()){
                delieverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
            }
            else {

                String county = null;
                String locality = null;
                String country = null;

                for (Address addressItem: addressList) {

                    if (addressItem.getAdminArea() != null && county == null) {
                        county = addressItem.getAdminArea();
                        county = county.replace("County", "");
                    }
                    if (addressItem.getSubAdminArea() != null && locality == null){
                        locality = addressItem.getSubAdminArea();
                    }
                    if (addressItem.getCountryName() != null && country == null){
                        country = addressItem.getCountryName();
                    }

                }

                if (county == null){
                    county = "Județ";
                }
                if (country == null){
                    country = "Țară";
                }
                if (locality == null){
                    locality = "Localitate";
                }


                Address address = addressList.get(0);

                ArrayList<String> addresListFragments = new ArrayList<>();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++){
                    System.out.println(address);
                    addresListFragments.add(locality +", " + county+", " + country);
                    System.out.println("fragmente:");
                    System.out.println(addresListFragments);
                }
                delieverResultToReceiver(Constants.SUCCESS_RESULT, TextUtils.join(Objects.requireNonNull(System.getProperty("line.separator")), addresListFragments));

            }
        }
    }

    private void delieverResultToReceiver (int resultCode, String addressMessage){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, addressMessage);
        resultReceiver.send(resultCode,bundle);
    }
}
