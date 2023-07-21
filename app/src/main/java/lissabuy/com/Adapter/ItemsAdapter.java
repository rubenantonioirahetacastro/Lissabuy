package lissabuy.com.Adapter;

import android.annotation.SuppressLint;
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

import java.text.NumberFormat;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(mContext).load(mData.get(position).getImage()).into(holder.item_img);
        holder.item_title.setText(mData.get(position).getTitle());

        double imports =mData.get(position).getPrice();
        NumberFormat formatImport = NumberFormat.getCurrencyInstance();
        holder.item_price.setText(formatImport.format(imports));
        holder.item_count.setText(Float.toString(mData.get(position).getCount()));
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
        ImageView item_img;
        TextView item_title, item_price, item_count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_img = itemView.findViewById(R.id.item_img);
            item_title = itemView.findViewById(R.id.item_title);
            item_price = itemView.findViewById(R.id.item_price);
         //   item_seller = itemView.findViewById(R.id.item_seller);
            item_count = itemView.findViewById(R.id.item_count);
            item_img = itemView.findViewById(R.id.item_img);

        }
    }
    public  interface ItemClickListener{
        public  void onItemClick(ItemsModel dataModel);
    }
}
