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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lissabuy.com.Adapter.CategoryAdapter;
import lissabuy.com.Model.CategoryModel;
import lissabuy.com.R;
import lissabuy.com.databinding.FragmentHomeBinding;
import lissabuy.com.ui.ItemsFragment;

public class HomeFragment extends Fragment implements CategoryAdapter.ItemClickListener {
    private static final String TAG = "Errors";
    private FragmentHomeBinding binding;
    private CategoryAdapter _categoryAdapter;
    private Query mDatabaseRef;
    private List<CategoryModel> _categoryModel;
    private   RecyclerView _rvCategory;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        _rvCategory= (RecyclerView) root.findViewById(R.id.rv_Category);
        _categoryModel = new ArrayList<>();
        TextView txt_userhome = root.findViewById(R.id.txt_userhome);
        ImageView img_userhome = root.findViewById(R.id.img_userhome);

        txt_userhome.setText(currentUser.getDisplayName());
        Glide.with(this).load(currentUser.getPhotoUrl()).into(img_userhome);

        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        //  ((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff00DDED));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#a7d0d2")));

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            // Mostrar el botón de navegación "Up"
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("Home");
            // Establecer el ícono del botón de navegación "Up"
            actionBar.setHomeAsUpIndicator(R.drawable.ic_up_arrow);
        }
        return root;
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
    //private CategoryAdapter.ItemClickListener clickListener;

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

    private void getCategories() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Conf/Categories").limitToFirst(6);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                _categoryModel.removeAll(_categoryModel);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CategoryModel img = snapshot.getValue(CategoryModel.class);
                    _categoryModel.add(img);
                }
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
                _rvCategory.setLayoutManager(layoutManager);
                _categoryAdapter = new CategoryAdapter(getActivity(), _categoryModel,HomeFragment.this);
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

    @Override
    public void onItemClick(CategoryModel dataModel) {
        Fragment fragment = ItemsFragment.newInstance(dataModel.category_title, dataModel.getCategory_id());
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}