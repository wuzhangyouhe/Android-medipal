package sg.edu.nus.iss.se8.medipal.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.dao.UsersDao;
import sg.edu.nus.iss.se8.medipal.models.User;

public class OnboardingHelper {
    private Context context;
    private User user;
    private SharedPreferences sharedPreferences;

    public OnboardingHelper(Context context) {
        this.context = context;
        user = UsersDao.getUser();
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    public boolean hasUserEnteredBiodata() {
        boolean result = true;

        if (user == null) return false;
        if (user.getId() == null) result = false;
        if (user.getName() == null) result = false;
        if (user.getBirthDate() == null) result = false;
        if (user.getIdentityNumber() == null) result = false;
        if (user.getAddress() == null) result = false;
        if (user.getPostalCode() == null) result = false;
        if (user.getHeight() == null) result = false;
        if (user.getBloodType() == null) result = false;

        return result;
    }

    public boolean hasUserCheckedDontShowMeAgain() {
        return sharedPreferences.getBoolean(context.getString(R.string.has_user_checked_dont_show_me_again), false);
    }

    public boolean shouldShowOnboardingWizard() {
        boolean shouldShow = true;

        if (hasUserCheckedDontShowMeAgain()) shouldShow = false;

        if (hasUserEnteredBiodata()) shouldShow = false;

        return shouldShow;
    }
}
