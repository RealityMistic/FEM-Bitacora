package es.upm.miw.fem.fembitacora;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import es.upm.miw.fem.fembitacora.models.DeliveryItem;
import es.upm.miw.fem.fembitacora.models.Event;

import static es.upm.miw.fem.fembitacora.MainActivity.LOG_TAG;

public class EventActivity extends Activity {

    DeliveryItem deliveryItem;
    String currentUserID;

    EditText notesEditText;

    private DatabaseReference mDeliverersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        deliveryItem = (DeliveryItem) bundle.getSerializable("deliveryItem");

        currentUserID = bundle.getString("FIREBASE_AUTH_CURRENT_USER");

        mDeliverersReference = FirebaseDatabase.getInstance().getReference();

        notesEditText = findViewById(R.id.notesEditText);
        Log.i(LOG_TAG, "Event activity created");

        Button setEvent=(Button) findViewById(R.id.setEvent);
        setEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = notesEditText.getText().toString();
                Log.i(LOG_TAG, "note for event taken.");
                mDeliverersReference
                        .child("deliverers")
                        .child(currentUserID)
                        .child("delivery")
                        .child(deliveryItem.getId())
                        .child("event")
                        .push().setValue(note);

                Toast.makeText(getApplicationContext(), "Event created", Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, "Event created");
            }
        });


       // finish();
    }


}

