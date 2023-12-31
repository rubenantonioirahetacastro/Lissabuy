package lissabuy.com.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lissabuy.com.Adapter.CategoryAdapter;
import lissabuy.com.Adapter.ItemsAdapter;
import lissabuy.com.Model.CategoryModel;
import lissabuy.com.Model.ItemsModel;
import lissabuy.com.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemsFragment extends Fragment implements ItemsAdapter.ItemClickListener {
    private static final String TAG = "Errors";
    private ItemsAdapter _itemsAdapter;
    private Query mDatabaseRef;
    private List<ItemsModel> _itemsModel;
    private RecyclerView _rvItems;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private int mParam2;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Conf/Exceptions");

    public ItemsFragment() {
        setHasOptionsMenu(true);
    }

    public static ItemsFragment newInstance(String param1, int param2) {
        ItemsFragment fragment = new ItemsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_items, container, false);
        _rvItems= (RecyclerView) view.findViewById(R.id.rv_Items);
        _itemsModel = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(mParam1);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_up_arrow);
            }
            getItems();
        }
        catch (Exception e){
            myRef.child(Calendar.getInstance().getTime().toString()).child(myRef.push().getKey()).setValue(e.getMessage());
        }

    }
    private void getItems() {

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Product");
        Query query = databaseRef.orderByChild("category_id").equalTo(mParam2);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                _itemsModel.removeAll(_itemsModel);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemsModel img = snapshot.getValue(ItemsModel.class);
                    String key = snapshot.getKey();
                    img.key = key;
                    _itemsModel.add(img);
                }
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                _rvItems.setLayoutManager(layoutManager);
                _itemsAdapter = new ItemsAdapter(getActivity(), _itemsModel,ItemsFragment.this);
                _rvItems.setAdapter(_itemsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                myRef.child(Calendar.getInstance().getTime().toString()).child(myRef.push().getKey()).setValue(databaseError.getMessage());
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
    public void onItemClick(ItemsModel dataModel) {
        Fragment fragment = ProductFragment.newInstance(dataModel.getKey(), dataModel.getTitle());
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}