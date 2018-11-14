package sg.edu.nus.iss.se8.medipal.activities.medicine;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.BaseActivity;
import sg.edu.nus.iss.se8.medipal.dao.CategoryDao;
import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.Category;

public class AddCategoryActivity extends BaseActivity {
    private static final String TOOLBAR_TITLE = "Add Medicine Category";

    private EditText editTextCategoryName;
    private EditText editTextCategoryDescription;
    private EditText editTextCategoryCode;
    private Spinner spinnerCategoryReminder;
    private Button buttonSaveCategory;

    private String name;
    private String description;
    private String code;
    private String categoryReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        initToolbar(TOOLBAR_TITLE, true);
        initUserInterface();
    }

    private void initUserInterface() {
        editTextCategoryName = (EditText) findViewById(R.id.editTextCategoryName);
        editTextCategoryDescription = (EditText) findViewById(R.id.editTextCategoryDescription);
        editTextCategoryCode = (EditText) findViewById(R.id.editTextCategoryID);
        buttonSaveCategory = (Button) findViewById(R.id.buttonSaveCategory);
        spinnerCategoryReminder = (Spinner) findViewById(R.id.spinnerCategoryReminder);

        initButtonSaveCategory();
        initSpinnerArrayAdapter();
    }

    private void initSpinnerArrayAdapter() {
        spinnerCategoryReminder.setAdapter(new ArrayAdapter<Category.ReminderApplicableOption>(this, android.R.layout.simple_spinner_dropdown_item, Category.ReminderApplicableOption.values()));
    }

    private void initButtonSaveCategory() {
        buttonSaveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveCategoryDetails(v);
                } catch (MedipalException me) {
                    me.printStackTrace();
                }
            }
        });
    }

    public void saveCategoryDetails(View v) throws MedipalException{
        getDatafromUserInterface();

        if (validateDate()) {
            int nextIndex = 0;

            if (getCurrentIndex() !=0) {
                nextIndex = getCurrentIndex() + 1;
            }

            Category category = new Category(null, name, code, Category.ReminderApplicableOption.valueOf(categoryReminder), description);
            try {
                CategoryDao.save(category);
            } catch (MedipalException me) {
                throw me;
            } catch (SQLException se) {
                throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
            } catch (Exception e) {
                throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("A new Medicine Category added!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    runIntent();
                }
            });
            builder.show();
        }
    }

    private boolean validateDate() {
        if (TextUtils.isEmpty(editTextCategoryName.getText())) {
            editTextCategoryName.setError("You need to enter a Category Name");
            return false;
        }
        else if (description.isEmpty()) {
            editTextCategoryDescription.setError("You need to enter a Category Description");
            return false;
        }
        else if (code.isEmpty()) {
            editTextCategoryCode.setError("You need to enter a Three-letter Code");
            return false;
        }
        else { return true;
        }
    }

    private void runIntent() {
        Intent returnToActivity = new Intent(this, CategoryActivity.class);
        returnToActivity.putExtra("", 3);
        startActivity(returnToActivity);
    }

    public void getDatafromUserInterface() {
        name = editTextCategoryName.getText().toString();
        code = editTextCategoryCode.getText().toString();
        description = editTextCategoryDescription.getText().toString();
        categoryReminder = ((Category.ReminderApplicableOption) spinnerCategoryReminder.getSelectedItem()).name();
    }

    public int getCurrentIndex() {
        ArrayList<Category> categories = new ArrayList<>();
        List<Category> list = CategoryDao.getAll();
        for (Category ec: list) {
            categories.add(ec);
        }

        if (categories.size()!= 0) {
            Category last = categories.get(categories.size() -1);
            return last.getId();
        }
        return 0;
    }
}
