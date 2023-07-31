package lissabuy.com.ui;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lissabuy.com.Adapter.ItemsAdapter;
import lissabuy.com.Core.CirclePageIndicator;
import lissabuy.com.Core.ImagePagerAdapter;
import lissabuy.com.Model.ItemsModel;
import lissabuy.com.Model.ProductModel;
import lissabuy.com.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ViewPager viewPager;
    private CirclePageIndicator pagerIndicator;
    private ImagePagerAdapter imagePagerAdapter;
    private List<String> images;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Conf/Exceptions");
    private GoogleMap mMap;

    private  TextView titleTextView,priceTextView, descriptionTextView;
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_product, container, false);


        titleTextView = view.findViewById(R.id.titleTextView);
        priceTextView = view.findViewById(R.id.priceTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        pagerIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);

        imagePagerAdapter = new ImagePagerAdapter(this.getContext());

        viewPager.setAdapter(imagePagerAdapter);
        pagerIndicator.setViewPager(viewPager);



        images = new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
            getProduct();
        }
        catch (Exception e){
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

                titleTextView.setText(product.getTitle());
                //priceTextView.setText((int) product.getPrice());
                descriptionTextView.setText(product.getDescription());

                DataSnapshot catalogueSnapshot = snapshot.child("catalogue");
                if (catalogueSnapshot.exists()) {
                    for (DataSnapshot imageSnapshot : catalogueSnapshot.getChildren()) {
                        String image = imageSnapshot.child("img").getValue(String.class);
                        images.add(image);
                    }
                }
                imagePagerAdapter.setImages(images);
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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng( 13.591255, -89.289661);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}