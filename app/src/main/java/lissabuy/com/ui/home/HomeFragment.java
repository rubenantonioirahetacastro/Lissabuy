package lissabuy.com.ui.home;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lissabuy.com.Adapter.CategoryAdapter;
import lissabuy.com.Model.CategoryModel;
import lissabuy.com.R;
import lissabuy.com.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private static final String TAG = "Errors";
    private FragmentHomeBinding binding;
    private CategoryAdapter _categoryAdapter;
    private Query mDatabaseRef;
    private List<CategoryModel> _categoryModel;
    private RecyclerView _rvCategory, _rvBestSeller, _rvOffers;
    Toolbar toolbar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //View view = inflater.inflate(R.layout.fragment_home, container, false);
        _rvCategory = (RecyclerView) root.findViewById(R.id.rv_Category);

        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        //  ((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff00DDED));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7f72c5")));

        _categoryModel = new ArrayList<>();
        return root;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //switch (item.getItemId()) {
          //  case R.id.home_buscar:  {
                // Navigate to settings screen.
            //    return true;
           // }
           // default:
                return super.onOptionsItemSelected(item);
       // }

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            getCategories();
        }
        catch (Exception exception){
            //do method to try save errors
            Log.d(TAG, exception.toString());
        }
    }
    private void getCategories() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Conf/Categories").limitToFirst(4);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                _categoryModel.removeAll(_categoryModel);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CategoryModel img = snapshot.getValue(CategoryModel.class);
                    _categoryModel.add(img);
                }
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                _rvCategory.setLayoutManager(layoutManager);
                _categoryAdapter = new CategoryAdapter(getActivity(), _categoryModel);
                _rvCategory.setAdapter(_categoryAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.toString());
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}