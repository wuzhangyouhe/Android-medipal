package sg.edu.nus.iss.se8.medipal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.biodata.BiodataActivity;

public class OnboardingActivity extends BaseActivity {
    private static final String TOOLBAR_TITLE = "Onboarding Wizard";
    private Button buttonOnboardingStartUsingMedipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        initToolbar(TOOLBAR_TITLE, false);
        initHandles();
        initClickHandlers();
    }

    private void initHandles() {
        buttonOnboardingStartUsingMedipal = (Button) findViewById(R.id.buttonOnboardingStartUsingMedipal);
    }

    private void initClickHandlers() {
        buttonOnboardingStartUsingMedipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BiodataActivity.class);
                startActivity(intent);
            }
        });
    }
}
