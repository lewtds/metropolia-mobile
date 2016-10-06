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

import java.util.List;

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
                System.out.println(weekMenu);
                TextView textView = (TextView) findViewById(R.id.haha);

                StringBuilder builder = new StringBuilder("Today's lunch menu for Vanhamaantie is:\n\n");

                for (Dish d : weekMenu.get(0)) {
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
