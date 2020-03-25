package tech.bencloud.receiver.shared_preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    // Define the filename of our preferences file
    public static final String PREFS_FILENAME = "MyPrefsFile";

    // Define names for preferences
    public static final String COLOUR_PREF = "faveColour";
    public static final String NUMBER_PREF = "faveNumber";
    public static final String SILENT_PREF = "silentMode";

    // Class properties for preferences
    private String faveColour;
    private Integer faveNumber;
    private Boolean silentMode;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // When we resume the app we'll restore the preferences from file
        readPreferences();
    }

    // Method to save our preferences when the application is paused
    @Override
    protected void onPause() {
        super.onPause();
        // When we pause the app, we'll write the preferences to file
        writePreferences();
    }

    public void writePreferences() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE).edit();

        // Update our preference properties with the latest values the user has entered into the UI
        EditText tempET = findViewById(R.id.faveColourET);
        faveColour = tempET.getText().toString();
        tempET = findViewById(R.id.faveNumberET);
        faveNumber = Integer.valueOf(tempET.getText().toString());
        CheckBox cb = findViewById(R.id.silentModeCB);
        silentMode = cb.isChecked();

        // Now set the key/value pairs for our preferences file...
        editor.putString(COLOUR_PREF, faveColour);
        editor.putInt(NUMBER_PREF, faveNumber);
        editor.putBoolean(SILENT_PREF, silentMode);

        // ...and commit the edits!
        boolean commitSuccess = editor.commit();

        // Commit failed? Log an error!
        if (!commitSuccess) {
            Log.e(TAG, "Failed to write preferences to file!");
        } else { // Commit worked? Log a debug message!
            Log.d(TAG, "Wrote preferences successfully to file!");
        }
    }

    public void readPreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE);

        // If we wanted to, we can check if the preferences file contains values for a given key...
        Log.d(TAG, "Settings contains " + COLOUR_PREF + "?: " + settings.contains(COLOUR_PREF));
        Log.d(TAG, "Settings contains " + NUMBER_PREF + "?: " + settings.contains(NUMBER_PREF));
        Log.d(TAG, "Settings contains " + SILENT_PREF + "?: " + settings.contains(SILENT_PREF));

        // Retrieve preference values by key
        // Note: If the key does not exist, 2nd parameter is used as default
        try {
            faveColour = settings.getString(COLOUR_PREF, "No Value Set");
            faveNumber = settings.getInt(NUMBER_PREF, -1);
            silentMode = settings.getBoolean(SILENT_PREF, false);
        } catch (ClassCastException cce) // Runs if existing data is of wrong type!
        {
            Log.e(TAG, "Wrong data types in preferences file!");
            cce.printStackTrace();
            finish();
        }

        // Update the layout with our preference details
        EditText tempET = findViewById(R.id.faveColourET);
        tempET.setText(faveColour);
        tempET = findViewById(R.id.faveNumberET);
        tempET.setText(String.format("%s", faveNumber.toString()));
        CheckBox cb = findViewById(R.id.silentModeCB);
        cb.setChecked(silentMode);
    }
}