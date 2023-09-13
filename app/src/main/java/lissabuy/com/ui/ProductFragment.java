package lissabuy.com.ui;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import static com.google.android.gms.maps.model.JointType.ROUND;

import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;


import android.location.LocationListener;
import android.location.LocationManager;

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ScrollingView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import lissabuy.com.Core.CirclePageIndicator;
import lissabuy.com.Core.GoogleMaps.Common;
import lissabuy.com.Core.GoogleMaps.Remot.IGoogleApi;
import lissabuy.com.Core.ImagePagerAdapter;
import lissabuy.com.Model.ItemsModel;
import lissabuy.com.R;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ViewPager viewPager;
    private CirclePageIndicator pagerIndicator;
    private ImagePagerAdapter imagePagerAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Conf/Exceptions");
    private MapView mMapView;
    Location mLastLocation;
    private GoogleMap mMap;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private static final String MAPVIEW_BUNDLE_KEY = "AIzaSyCB925VhBE63mkKSyJl9XUKCBm5zuxHXJM";
    private IGoogleApi mService;
    private List<LatLng> polylineList = new ArrayList<>();
    private ValueAnimator polylineAnimator;
    final private int MY_PERMISSIONS_REQUEST_ACCES_FINE_LOCATION = 124;
    private TextView priceTextView, descriptionTextView;
    private PolylineOptions polylineOptions;
    private Polyline greyPolyLine, blackPolyline; //polyline2

    private ShimmerFrameLayout mShimmerViewContainer;
    private ScrollView scrollProduct;

    public ProductFragment() {
        setHasOptionsMenu(true);
    }

    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // You have permission, proceed with location updates
            requestLocationUpdates();
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start location updates
                requestLocationUpdates();
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

    private static final int REQUEST_CODE = 123;

    int doing = 0;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                mLastLocation = location;
            }
        }
    };

    private void requestLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000); // Update interval in milliseconds

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private ChipGroup chipGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

      //  priceTextView = view.findViewById(R.id.priceTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        pagerIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        scrollProduct = view.findViewById(R.id.scrollProduct);
        chipGroup = view.findViewById(R.id.chip_group);

        mMapView = view.findViewById(R.id.mapView);
        initGoogleMap(savedInstanceState);
        mMapView.getMapAsync(this);
        mService = Common.getGoogleApi();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(mParam2);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_up_arrow);
            }
            chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(ChipGroup group, int checkedId) {
                    // Obtener el chip seleccionado
                    Chip selectedChip = view.findViewById(checkedId);
                    if (selectedChip != null) {
                        // Aquí puedes acceder al chip seleccionado
                        String selectedText = selectedChip.getText().toString();
                        Toast.makeText(getContext(), selectedText, Toast.LENGTH_LONG).show();

                        // Realizar las acciones que necesites con el chip seleccionado
                    }
                }
            });


        } catch (Exception e) {
            myRef.child(Calendar.getInstance().getTime().toString()).child(myRef.push().getKey()).setValue(e.toString());
        }
    }

    private void getProduct() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Product");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot snapshot = dataSnapshot.child(mParam1);
                ItemsModel product = snapshot.getValue(ItemsModel.class);

                //priceTextView.setText((int) product.getPrice());
                descriptionTextView.setText(product.getDescription());

                DataSnapshot sizesSnapshot = snapshot.child("sizes");
                ArrayList<String> sizesList = new ArrayList<>();
                for (DataSnapshot sizeSnapshot : sizesSnapshot.getChildren()) {
                    String size = sizeSnapshot.child("size").getValue(String.class);
                    if (size != null) {
                        sizesList.add(size);
                    }
                }

                // Limpiar el ChipGroup antes de agregar los chips
                chipGroup.removeAllViews();

                // Agregar chips al ChipGroup
                for (String size : sizesList) {
                    Chip chip = new Chip(getContext());
                    chip.setText(size);
                    chip.setCheckable(true); // Enable selection behavior
                    chip.setChipBackgroundColorResource(R.color.chip_background_color); // Default color

                    chip.setCheckedIconVisible(false); // Hide the checkmark icon

                    // Set a listener to handle chip selection
                    chip.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                        if (isChecked) {
                            // Chip is selected
                            chip.setChipBackgroundColorResource(R.color.selected_chip_color);
                            chip.setTextColor(getResources().getColorStateList(R.color.white));
                        } else {
                            // Chip is unselected
                            chip.setChipBackgroundColorResource(R.color.unselected_chip_color);
                            chip.setTextColor(getResources().getColorStateList(R.color.black));
                        }
                    });
                    chipGroup.addView(chip);
                }

                imagePagerAdapter = new ImagePagerAdapter(getContext());
                viewPager.setAdapter(imagePagerAdapter);
                pagerIndicator.setViewPager(viewPager);

                DataSnapshot catalogueSnapshot = snapshot.child("catalogue");
                ArrayList<String> images = new ArrayList<>();
                for (DataSnapshot imageSnapshot : catalogueSnapshot.getChildren()) {
                    String image = imageSnapshot.child("img").getValue(String.class);
                    if (image != null) {
                        images.add(image); // Agrega la imagen solo si no es nula
                    }
                }

                imagePagerAdapter.setImages(images); // Configura las imágenes en el adaptador



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                myRef.child(Calendar.getInstance().getTime().toString()).child(myRef.push().getKey()).setValue(error.getMessage());
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.style_json));

            if (!success) {
                //  Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            //Log.e(TAG, "Can't find style. Error: ", e);
        }
        //  mGoogleApiClient.connect();
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // mMap.setMyLocationEnabled(true);
        // locationStart();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(googleMap.getCameraPosition().target)
                .zoom(17)
                .bearing(30)
                .tilt(0)
                .build()));
        mMap.setTrafficEnabled(false);
        mMap.setBuildingsEnabled(true);

        sydney1 = new LatLng(-33.904438, 151.249852);
        sydney2 = new LatLng(-33.905823, 151.258422);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney1, 16F));

        getProduct();

    }


    private LatLng sydney1;
    private LatLng sydney2;

    private void showCurvedPolyline(LatLng p1, LatLng p2, double k) {
        //Calculate distance and heading between two points
        double d = SphericalUtil.computeDistanceBetween(p1, p2);
        double h = SphericalUtil.computeHeading(p1, p2);

        //Midpoint position
        LatLng p = SphericalUtil.computeOffset(p1, d * 0.5, h);

        //Apply some mathematics to calculate position of the circle center
        double x = (1 - k * k) * d * 0.5 / (2 * k);
        double r = (1 + k * k) * d * 0.5 / (2 * k);

        LatLng c = SphericalUtil.computeOffset(p, x, h > 40 ? h + 90.0 : h - 90.0);

        //Polyline options
        PolylineOptions options = new PolylineOptions();

        //Calculate heading between circle center and two points
        double h1 = SphericalUtil.computeHeading(c, p1);
        double h2 = SphericalUtil.computeHeading(c, p2);

        //Calculate positions of points on circle border and add them to polyline options
        int numpoints = 100;
        double step = (h2 - h1) / numpoints;

        for (int i = 0; i < numpoints; i++) {
            LatLng pi = SphericalUtil.computeOffset(c, r, h1 + i * step);
            options.add(pi);
        }

        //Draw polyline
        mMap.addPolyline(options.width(10).color(Color.BLACK).geodesic(false));
// Crear un objeto LatLngBounds que englobe ambas ubicaciones
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(sydney1);
        builder.include(sydney2);
        LatLngBounds bounds = builder.build();

// Obtener el ancho y alto de la pantalla en píxeles
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

// Definir un margen en píxeles para que las ubicaciones no queden justo en el borde
        int padding = 100;

// Mover y animar la cámara para que muestre el área que abarca las dos ubicaciones
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
        // Deshabilitar otras interacciones de usuario
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);

    }

    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mMapView != null)
            mMapView.onStart();

        //  mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        //   mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }
}