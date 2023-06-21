package lissabuy.com.Adapter;

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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context mContext;
    private List<CategoryModel> mData;
    private List<String> list_category;
    private DatabaseReference FBReference;

    public CategoryAdapter(Context context, List<CategoryModel> mData) {
        this.mContext = context;
        this.mData = mData;
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
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.category_title.setText(mData.get(position).getCategory_title());
        Glide.with(mContext).load(mData.get(position).getCategory_img()).into(holder.category_img);
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
}