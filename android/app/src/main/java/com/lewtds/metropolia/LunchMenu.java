package com.lewtds.metropolia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LunchMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_menu);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("food/lunch_menu/vanhamaantie/current_week");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<List<Dish>>> type = new GenericTypeIndicator<List<List<Dish>>>() {};
                List<List<Dish>> weekMenu = dataSnapshot.getValue(type);
                TextView textView = (TextView) findViewById(R.id.haha);

                int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                String dayName = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date());
                StringBuilder builder = new StringBuilder(dayName + "'s lunch menu for Vanhamaantie is:\n\n");

                for (Dish d : weekMenu.get(dayOfWeek - 1)) {
                    builder.append(d.descriptionEn);
                    builder.append("\n");
                }
                textView.setText(builder.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
