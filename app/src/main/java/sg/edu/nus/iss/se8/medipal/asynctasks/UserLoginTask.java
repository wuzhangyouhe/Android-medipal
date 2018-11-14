package sg.edu.nus.iss.se8.medipal.asynctasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import sg.edu.nus.iss.se8.medipal.activities.LoginActivity;
import sg.edu.nus.iss.se8.medipal.activities.MainActivity;

public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    private final String email;
    private final String password;
    private LoginActivity loginActivity;

    public UserLoginTask(LoginActivity loginActivity, String email, String password) {
        this.loginActivity = loginActivity;
        this.email = email;
        this.password = password;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            return false;
        }

        for (String credential : DUMMY_CREDENTIALS) {
            String[] pieces = credential.split(":");
            if (pieces[0].equals(email)) {
                return pieces[1].equals(password);
            }
        }

        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        loginActivity.showProgress(false);

        if (success) {
            Toast.makeText(loginActivity, "logged in successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(loginActivity, MainActivity.class);
            loginActivity.startActivity(intent);

            loginActivity.finish();
        } else {
            Toast.makeText(loginActivity, "failed to log in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCancelled() {
        loginActivity.showProgress(false);
    }
}
