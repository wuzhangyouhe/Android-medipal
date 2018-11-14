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

import java.util.List;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.BaseActivity;
import sg.edu.nus.iss.se8.medipal.dao.CategoryDao;
import sg.edu.nus.iss.se8.medipal.dao.MedicineDao;
import sg.edu.nus.iss.se8.medipal.models.Category;
import sg.edu.nus.iss.se8.medipal.models.Medicine;


public class EditMedicineActivity extends BaseActivity{
    private static final String TOOLBAR_TITLE = "Medicine Details";
    private EditText editName;
    private EditText editDescription;
    private Spinner spinnerEditCategory;
    private Button buttonSaveMedicineDetails;

    private String mediName;
    private String mediDescription;
    private Category editCatID;
    private Medicine medicine;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_add_medicine);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            medicine = (Medicine) getIntent().getSerializableExtra(MedicineActivity.MEDICINE);
        }
        initToolbar(TOOLBAR_TITLE, true);
        initUserInterface();
        initCatButton();
    }

    private void initCatButton() {
        Button catButton = (Button) findViewById(R.id.buttonEditCategories);

        catButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCat = new Intent(EditMedicineActivity.this, CategoryActivity.class);
                startActivity(intentCat);
            }
        });
    }

    public void initUserInterface() {
        editName = (EditText) findViewById(R.id.editTextAddMedicineName);
        editDescription = (EditText) findViewById(R.id.editTextAddMedicineDescription);
        spinnerEditCategory = (Spinner) findViewById(R.id.spinnerAddMedicineCode);
        List<Category> categories = CategoryDao.getAll();
        ArrayAdapter<Category> dataAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEditCategory.setAdapter(dataAdapter);
        buttonSaveMedicineDetails = (Button) findViewById(R.id.buttonAdd);

        setDataInUserInterface();
        initSpinnerArrayAdapter();
        initButtonSaveMedicineDetails();
    }

    public void initSpinnerArrayAdapter() {
        List<Category> categories = CategoryDao.getAll();
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEditCategory.setAdapter(adapter);
    }

    private void initButtonSaveMedicineDetails() {
        buttonSaveMedicineDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMedicineDetails(v);
            }
        });
    }

    public void setDataInUserInterface() {
        editName.setText(medicine.getName());
        editDescription.setText(medicine.getDescription());
        editCatID = medicine.getCategory();

        for (int i = 0; i < spinnerEditCategory.getCount(); i++) {
            if (spinnerEditCategory.getItemAtPosition(i).equals(editCatID)) {
                spinnerEditCategory.setSelection(i);
            }
        }
    }

    public void saveMedicineDetails(View v) {
        getDataFromUserInterface();

        if (validateDate()) {
            updateMedicineDetails();

            MedicineDao.update(medicine);

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

    public boolean validateDate() {

        if (TextUtils.isEmpty(editName.getText())) {
            editName.setError("You need to enter a medicine name");
            return false;
        }
        else if (TextUtils.isEmpty(editDescription.getText())) {
            editDescription.setError("You need to enter a medicine description");
            return false;
        }
        else {
            return true;
        }
    }

    public void runIntent() {
        Intent returnToMedicine = new Intent(this, MedicineActivity.class);
        startActivity(returnToMedicine);
    }

    public void getDataFromUserInterface() {
        mediName = editName.getText().toString();
        mediDescription = editDescription.getText().toString();
        editCatID = (Category) spinnerEditCategory.getSelectedItem();

    }

    public void updateMedicineDetails(){
        medicine.setName(mediName);
        medicine.setDescription(mediDescription);
        medicine.setCategory(editCatID);
    }
}
