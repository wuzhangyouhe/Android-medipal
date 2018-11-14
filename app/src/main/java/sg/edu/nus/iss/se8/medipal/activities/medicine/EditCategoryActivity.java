package sg.edu.nus.iss.se8.medipal.activities.medicine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.Format;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.BaseActivity;
import sg.edu.nus.iss.se8.medipal.dao.CategoryDao;
import sg.edu.nus.iss.se8.medipal.models.Category.ReminderApplicableOption;
import sg.edu.nus.iss.se8.medipal.models.Category;

public class EditCategoryActivity extends BaseActivity{

    private static final String TOOLBAR_TITLE = "Category Details";
    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextCode;
    private Button buttonSaveCategoryDetails;
    private Spinner spinnerCategoryReminder;

    private String name;
    private String description;
    private String code;
    private Category.ReminderApplicableOption categoryReminder;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            category = (Category) getIntent().getSerializableExtra(CategoryActivity.CATEGORY);
        }
        initToolbar(TOOLBAR_TITLE, true);
        initUserInterface();
    }

    public void initUserInterface() {
        editTextName = (EditText) findViewById(R.id.editTextCategoryName);
        editTextDescription = (EditText) findViewById(R.id.editTextCategoryDescription);
        editTextCode = (EditText) findViewById(R.id.editTextCategoryID);
        spinnerCategoryReminder = (Spinner) findViewById(R.id.spinnerCategoryReminder);
        buttonSaveCategoryDetails = (Button) findViewById(R.id.buttonSaveCategory);

        setDataInUserInterface();
        initSpinnerArrayAdapter();
        initButtonSaveCategoryDetails();
    }

    private void initSpinnerArrayAdapter() {

        ArrayAdapter<Category.ReminderApplicableOption> adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Category.ReminderApplicableOption.values());
        spinnerCategoryReminder.setAdapter(adapter);

    }

    private void initButtonSaveCategoryDetails() {
        buttonSaveCategoryDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCategoryDetails(v);
            }
        });
    }

    public void setDataInUserInterface() {
        editTextName.setText(category.getName());
        editTextDescription.setText(category.getDescription());
        editTextCode.setText(category.getCode());
    }

    public void saveCategoryDetails(View v) {
        getDataFromUserInterface();

        if (validateData()) {
            updateCategoryDetails();

            CategoryDao.update(category);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Changes saved!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    runIntent();
                }
            });
            builder.show();
        }
    }

    public boolean validateData() {

        if (TextUtils.isEmpty(editTextName.getText())) {
            editTextName.setError("You need to enter a category name");
            return false;
        }
        else if (TextUtils.isEmpty(editTextDescription.getText())) {
            editTextDescription.setError("You need to enter a category description");
            return false;
        }
        else if (TextUtils.isEmpty(editTextCode.getText())) {
            editTextCode.setError("You need to enter a three-letter code");
            return false;
        }
        else {
            return true;
        }
    }

    public void runIntent() {
        Intent returnToCategory = new Intent(this, CategoryActivity.class);
        startActivity(returnToCategory);
    }

    public void getDataFromUserInterface() {
        name = editTextName.getText().toString();
        description = editTextDescription.getText().toString();
        code = editTextCode.getText().toString();
        categoryReminder = (Category.ReminderApplicableOption) spinnerCategoryReminder.getSelectedItem();
    }

    public void updateCategoryDetails() {
        category.setName(name);
        category.setDescription(description);
        category.setCode(code);
        category.setReminderApplicable(categoryReminder);
    }
}
