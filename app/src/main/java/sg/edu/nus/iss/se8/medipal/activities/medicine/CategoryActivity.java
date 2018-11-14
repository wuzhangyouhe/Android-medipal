package sg.edu.nus.iss.se8.medipal.activities.medicine;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.BaseActivity;
import sg.edu.nus.iss.se8.medipal.adapters.CategoryAdapter;
import sg.edu.nus.iss.se8.medipal.dao.CategoryDao;
import sg.edu.nus.iss.se8.medipal.models.Category;


public class CategoryActivity extends BaseActivity {
    private static final String TOOLBAR_TITLE = "Medicine Category";

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Category> categories;
    public static final String CATEGORY = "CATEGORY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        initToolbar(TOOLBAR_TITLE, true);
        initFab();
        initRecyclerView();
    }

    private void initFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddRecord);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, AddCategoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerViewCategory);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        populateListFromDatabase();
    }

    private void populateListFromDatabase() {

        categories = new ArrayList<>();
        List<Category> list = CategoryDao.getAll();

        for (Category c: list) {
            categories.add(c);
        }
        initAdapter();
        recyclerView.setAdapter(adapter);
    }
    public void initAdapter() {
        adapter = new CategoryAdapter(categories, new CategoryAdapter.MyAdapterListener() {
            @Override
            public void layoutOnClick(View v, int position) {
                Category category = ((CategoryAdapter) recyclerView.getAdapter()).getItem(position);

                Intent goToEditActivity = new Intent(CategoryActivity.this, EditCategoryActivity.class);
                goToEditActivity.putExtra("CATEGORY", category);
                startActivity(goToEditActivity);
            }

            @Override
            public boolean layoutOnLongClick(View v, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(recyclerView.getContext());
                builder.setMessage("Categories can only be edited, but not deleted.");
                builder.setPositiveButton("OK", null);
                builder.show();
                return true;
            }
        });
    }
}
