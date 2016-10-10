package com.lewtds.metropolia;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LunchMenu extends AppCompatActivity {
    public static String numberToDayOfWeek(int number) {
        assert number >= 0;
        assert number < 7;
        String[] days = {"sun", "mon", "tue", "wed", "thu", "fri", "sat"};
        return days[number];
    }

    public static List<Dish> getTodayMenu(Map<String, List<Dish>> weekMenu, int dayOfWeek) {
        // there is no menu for saturday and sunday
        if (dayOfWeek == 0 || dayOfWeek == 6) {
            return weekMenu.get("fri");
        }
        return weekMenu.get(numberToDayOfWeek(dayOfWeek - 1));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_menu);
        String venue = "vanhamaantie";
        final Activity currentActivity = this;

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(String.format("food/lunch_menu/%s/current_week", venue));
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, List<Dish>>> type = new GenericTypeIndicator<Map<String, List<Dish>>>() {};
                Map<String, List<Dish>> weekMenu = dataSnapshot.getValue(type);
                TextView textView = (TextView) findViewById(R.id.haha);

                List<Dish> menu = getTodayMenu(weekMenu, Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
                String dayName = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date());
                StringBuilder builder = new StringBuilder(dayName + "'s lunch menu for Vanhamaantie is:\n\n");

                List<String> dishes = new ArrayList<>();
                for (Dish d : menu) {
                    dishes.add(d.descriptionEn);
                }
                textView.setText(builder.toString());


                ArrayAdapter<String> adapter = new ArrayAdapter<>(currentActivity, android.R.layout.simple_list_item_1, dishes);
                ListView listView = (ListView) findViewById(R.id.dishes);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
