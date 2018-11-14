package sg.edu.nus.iss.se8.medipal.activities.medicine;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
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
import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.Category;
import sg.edu.nus.iss.se8.medipal.models.Medicine;

public class AddMedicineActivity extends BaseActivity {

    private EditText editTextMedicineName;
    private EditText editTextMedicineDescription;
    private Spinner spinnerAddMedicineCode;

    private String medicineName;
    private String medicineDescription;
    private Category category;
    private String description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        spinnerAddMedicineCode = (Spinner) findViewById(R.id.spinnerAddMedicineCode);

        List<Category> categories = CategoryDao.getAll();
        ArrayAdapter<Category> dataAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAddMedicineCode.setAdapter(dataAdapter);
        initToolbar();
        initCatButton();
        initUserInterface();
        initSaveButton();
    }

    private void initCatButton() {
        Button catButton = (Button) findViewById(R.id.buttonEditCategories);

        catButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCat = new Intent(AddMedicineActivity.this, CategoryActivity.class);
                startActivity(intentCat);
            }
        });
    }

    private void initUserInterface() {
        editTextMedicineName = (EditText) findViewById(R.id.editTextAddMedicineName);
        editTextMedicineDescription = (EditText) findViewById(R.id.editTextAddMedicineDescription);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Medicine");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initSaveButton() {

        Button buttonAdd = (Button) findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showNotification(v);
                } catch (MedipalException me) {
                    me.printStackTrace();
                }
            }
        });
    }

    public void showNotification(View v) throws MedipalException {
        getDataFromUserInterface();

        if (medicineName.isEmpty()) {
            editTextMedicineName.setError("You need to enter a medicine name");
        } else if (medicineDescription.isEmpty()) {
            editTextMedicineDescription.setError("You need to enter a medicine descritpion");
        } else {
            Medicine medicine = new Medicine(null, medicineName, medicineDescription, category);
            try {
                MedicineDao.save(medicine);
            } catch (MedipalException me) {
                throw me;
            } catch (SQLException se) {
                throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
            } catch (Exception e) {
                throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("A new Medicine added!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    runIntent();
                }
            });
            builder.show();
        }
    }

    public void runIntent() {
        Intent returnToMedicine = new Intent(this, MedicineActivity.class);
        startActivity(returnToMedicine);
        finish();
    }

    public void getDataFromUserInterface() {

        medicineName = editTextMedicineName.getText().toString();
        medicineDescription = editTextMedicineDescription.getText().toString();
        category = (Category) spinnerAddMedicineCode.getSelectedItem();
        description = "todo: hard coded value, changeit";
    }
}
