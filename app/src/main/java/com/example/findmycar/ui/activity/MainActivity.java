package com.example.findmycar.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.findmycar.R;
import com.example.findmycar.adapters.HomePagerAdapter;
import com.example.findmycar.contract.MainContract;
import com.example.findmycar.locationProcessor.GeoLocationManager;
import com.example.findmycar.locationProcessor.IGeoLocationCallback;
import com.example.findmycar.ui.fragment.BaseFragment;
import com.example.findmycar.ui.fragment.ParkingHistoryFragment;
import com.example.findmycar.utils.Utils;
import com.google.android.gms.common.api.ResolvableApiException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.IActivityCommunicator,
        IGeoLocationCallback {

    private List<MainContract.IFragmentInteraction> mFragmentInteractionsList = new ArrayList<>();
    // lists for permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    private static final int REQUEST_CHECK_SETTINGS = 1000;
    private View mView;
    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager vpPager;
    private GeoLocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mView = view;
                for (MainContract.IFragmentInteraction interaction : mFragmentInteractionsList) {
                    if (interaction != null) {
                        interaction.onSaveNewClicked();
                    }
                }
            }
        });
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        mPagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), this);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(vpPager);
        // we add permissions we need to request location of the users
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(
                        new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            } else {
                vpPager.setAdapter(mPagerAdapter);
                locationManager = new GeoLocationManager(MainActivity.this, this);
            }
        } else {
            vpPager.setAdapter(mPagerAdapter);
            locationManager = new GeoLocationManager(MainActivity.this, this);
        }

    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(MainActivity.this).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setCancelable(false).
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.
                                                        toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create().show();

                            return;
                        }
                    }
                } else {
                    vpPager.setAdapter(mPagerAdapter);
                    locationManager = new GeoLocationManager(MainActivity.this, this);
                }

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (locationManager != null) {
            locationManager.startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.stopLocationUpdates();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    @Override
    public void showEnableGPSAlert() {
        Utils.showSnackBarAlert(mView, this.getString(R.string.enable_gps_alert));
    }

    @Override
    public void showAddressNotFoundAlert() {
        Utils.showSnackBarAlert(mView, this.getString(R.string.add_fetch_alert));
    }

    @Override
    public void addFragmentInterationListener(MainContract.IFragmentInteraction fragmentInteraction) {
        mFragmentInteractionsList.add(fragmentInteraction);
    }

    @Override
    public void setNewAddress(String addressOutput) {
        for (MainContract.IFragmentInteraction interaction : mFragmentInteractionsList) {
            if (interaction != null) {
                interaction.onSetNewAddress(addressOutput);
            }
        }
    }

    @Override
    public void onGeoLocationResult(Location location) {
        for (MainContract.IFragmentInteraction interaction : mFragmentInteractionsList) {
            if (interaction != null) {
                interaction.onLocationUpdate(location);
            }
        }
    }

    @Override
    public void onLocationFailure(Exception e) {
        try {
            // Show the dialog by calling startResolutionForResult(),
            // and check the result in onActivityResult().
            ResolvableApiException resolvable = (ResolvableApiException) e;
            resolvable.startResolutionForResult(MainActivity.this,
                    REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException sendEx) {
            // Ignore the error.
        }
    }
}
