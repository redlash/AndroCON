package utils;
import android.app.Activity;
import android.widget.*;

/**
 * Created by em9736 on 16/09/2016.
 */
public class UIHelper {
    public static void displayText(Activity activity, int id, String text) {
        TextView tv = (TextView) activity.findViewById(id);
        tv.setText(text);
    }

    public static void appendText(Activity activity, int id, String text) {
        TextView tv = (TextView) activity.findViewById(id);
        tv.append(text);
        tv.append("\n");
    }

    public static String getText(Activity activity, int id) {
        EditText et = (EditText) activity.findViewById(id);
        return et.getText().toString();
    }

    public static boolean getCBChecked(Activity activity, int id) {
        CheckBox cb = (CheckBox) activity.findViewById(id);
        return cb.isChecked();
    }

    public static void setCBChecked(Activity activity, int id, boolean value) {
        CheckBox cb = (CheckBox) activity.findViewById(id);
        cb.setChecked(value);
    }
}
