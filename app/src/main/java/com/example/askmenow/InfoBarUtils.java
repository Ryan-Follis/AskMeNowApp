package com.example.askmenow;

import android.app.Activity;
import android.widget.TextView;

import org.w3c.dom.Text;

public class InfoBarUtils {

    Activity activity;

    public InfoBarUtils(Activity activity) {
        this.activity = activity;
    }

    public void updateName(String name) {
        TextView nameView = (TextView) activity.findViewById(R.id.user_name);
        nameView.post(()->nameView.setText(name));
    }

    public void updateAge(String age) {
        TextView ageView = (TextView) activity.findViewById(R.id.user_age);
        ageView.post(()->ageView.setText(age));
    }

    public void isNearBy(Boolean nearby) {
        TextView proximityView = (TextView) activity.findViewById(R.id.nearby);
        if (nearby)
            proximityView.setText("nearby");
        else
            proximityView.setText("");
    }
}
