package sg.edu.nus.iss.se8.medipal.activities.report;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.BaseActivity;

public class HelpActivity extends BaseActivity {
    private static final String TOOLBAR_TITLE = "Help";


    private TextView textViewContent1;
    private TextView textViewContent2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_menu);

        initToolbar(TOOLBAR_TITLE, true);
        initUserInterface();
    }


    private void initUserInterface() {
        textViewContent1 = (TextView) findViewById(R.id.textView1);

        textViewContent1.setText(Html.fromHtml(
                "<h2>How to reorder my Emergency Contacts?</h2><br>" +
                        "<p style='text-align:justify'> Step 1: Click on the UpDown arrow <br>" +
                        "Step 2: Click and hold the contact until it become gray and drag it the desired position.</p>"
        ));
        textViewContent2 = (TextView) findViewById(R.id.textView2);

        textViewContent2.setText(Html.fromHtml(
                "<h2>How to view my Medicine Consumption history?</h2><br>" +
                        "<p style='text-align:justify'> Step 1: Go to the Medicine Prescription page <br>" +
                        "Step 2: Click on the icon next to the expiry date <br>" +
                        "</p>"
        ));



    }
}
