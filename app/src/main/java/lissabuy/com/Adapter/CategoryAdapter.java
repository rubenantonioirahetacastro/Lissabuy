package lissabuy.com.Adapter;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import lissabuy.com.Model.CategoryModel;
import lissabuy.com.R;
import lissabuy.com.databinding.FragmentAccountBinding;
import lissabuy.com.ui.ItemsFragment;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context mContext;
    private ItemClickListener clickListener;
    private List<CategoryModel> mData;
    private List<String> list_category;
    private DatabaseReference FBReference;

    public CategoryAdapter(Context context, List<CategoryModel> mData, ItemClickListener clickListener) {
        this.mContext = context;
        this.mData = mData;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.item_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.category_title.setText(mData.get(position).getCategory_title());
        Glide.with(mContext).load(mData.get(position).getCategory_img()).into(holder.category_img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clickListener.onItemClick(mData.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView category_title;
        ImageView category_img;
        CardView category_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category_title = (TextView) itemView.findViewById(R.id.category_title);
            category_img = (ImageView) itemView.findViewById(R.id.category_img);
            category_item = itemView.findViewById(R.id.category_item);
        }
    }

    public  interface ItemClickListener{
        public  void onItemClick(CategoryModel dataModel);
    }
}