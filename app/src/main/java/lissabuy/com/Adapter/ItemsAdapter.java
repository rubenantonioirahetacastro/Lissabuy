package lissabuy.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import lissabuy.com.Model.CategoryModel;
import lissabuy.com.Model.ItemsModel;
import lissabuy.com.R;
import lissabuy.com.ui.ItemsFragment;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private Context mContext;
    private ItemClickListener clickListener;
    private List<ItemsModel> mData;
    private List<String> list_items;
    private DatabaseReference FBReference;

    public ItemsAdapter(Context context, List<ItemsModel> mData, ItemClickListener clickListener) {
        this.mContext = context;
        this.mData = mData;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.item_items, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.item_title.setText(mData.get(position).getTitle());
        Glide.with(mContext).load(mData.get(position).getImage()).into(holder.item_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DO CLICK
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img;
        TextView item_title, item_price,item_store;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_img = itemView.findViewById(R.id.item_img);
            item_title = itemView.findViewById(R.id.item_title);
            item_price = itemView.findViewById(R.id.item_price);
            item_store = itemView.findViewById(R.id.item_store);
        }
    }
    public  interface ItemClickListener{
        public  void onItemClick(ItemsModel dataModel);
    }
}
