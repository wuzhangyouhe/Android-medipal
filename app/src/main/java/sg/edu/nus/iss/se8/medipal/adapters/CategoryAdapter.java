package sg.edu.nus.iss.se8.medipal.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.models.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private ArrayList<Category> categories;

    public CategoryAdapter(ArrayList<Category> categories, MyAdapterListener listener){
        this.categories= categories;
        onClickListener = listener;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Category category = categories.get(position);

        viewHolder.textViewName.setText(category.getName());
        viewHolder.textViewCode.setText(category.getCode());
        viewHolder.textViewDescription.setText(category.getDescription());
        viewHolder.textViewCategoryReminder.setText(category.isReminderApplicable().toString());
    }

    @Override
    public int getItemCount(){
        return categories.size();
    }

    public Category getItem(int position) {
        return categories.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout leftlayout;
        private TextView textViewName;
        private TextView textViewCode;
        private TextView textViewDescription;
        private TextView textViewCategoryReminder;

        private ViewHolder(View view) {
            super(view);
            leftlayout = (RelativeLayout) view.findViewById(R.id.leftlayoutCategory);
            textViewName = (TextView) view.findViewById(R.id.textViewCategory);
            textViewCode = (TextView) view.findViewById(R.id.textViewCode);
            textViewDescription = (TextView) view.findViewById(R.id.textViewDescription);
            textViewCategoryReminder = (TextView) view.findViewById(R.id.textViewCategoryReminder);

            leftlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.layoutOnClick(v, getAdapterPosition());
                }
            });

            leftlayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onClickListener.layoutOnLongClick(v, getAdapterPosition());
                    return true;
                }
            });
        }
    }

    private MyAdapterListener onClickListener;

    public interface MyAdapterListener {
        void layoutOnClick (View v, int position);

        boolean layoutOnLongClick(View v, int position);
    }
}
