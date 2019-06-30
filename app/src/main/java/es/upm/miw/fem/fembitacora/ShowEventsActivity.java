package es.upm.miw.fem.fembitacora;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import es.upm.miw.fem.fembitacora.models.DeliveryItem;

import static es.upm.miw.fem.fembitacora.MainActivity.LOG_TAG;

public class ShowEventsActivity extends Activity {

    private ListView incidenciasListView;

    private DatabaseReference mDelivererReference;

    String currentUserID;

    String itemTitle;

    EventAdapter eventAdapter;

    DeliveryItem deliveryItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Intent intent = getIntent();
        currentUserID = intent.getStringExtra("FIREBASE_AUTH_CURRENT_USER");
        // itemTitle = intent.getStringExtra("ITEM_TITLE");
        deliveryItem = (DeliveryItem) intent.getSerializableExtra("deliveryItem");

        View entryView = getLayoutInflater().inflate(R.layout.event_entry, null);

        mDelivererReference = FirebaseDatabase.getInstance().getReference()
                .child("deliverers")
                .child(currentUserID)
                .child("delivery")
                .child(deliveryItem.getId())
                .child("event");

        mDelivererReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(LOG_TAG, "loadIncidencia:onCancelled", databaseError.toException());
                // ...
            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String eventNote = "";
                HashMap<String,String> result = (HashMap<String,String>) dataSnapshot.getValue();
                if (null != dataSnapshot.getValue()){
                    for(HashMap.Entry<String, String> entry: result.entrySet()) {
                        eventNote += entry.getValue() + "\n";
                    }
                }
              //  String eventNote = (String) result.values().iterator().next;
                /*
                HashMap<String, ?> eventMap = dataSnapshot.getValue(HashMap.class);
                String eventNote = "";
                for (Map.Entry<String, ?> incidenciaEntry : eventMap.entrySet()) {
                    if (incidenciaEntry.getValue() instanceof String) {
                        eventNote += (String) incidenciaEntry.getValue();
                        eventNote += "\n";
                    }
                 */
                if (eventNote == null) {
                    /* log a warning, DataSnapshot.getValue may return null */
                    Log.w(LOG_TAG, "DataSnapshot-eventNote.getValue may return null");

                    Toast.makeText(
                            getApplicationContext(),
                            "No events. ",
                            Toast.LENGTH_LONG
                    ).show();

                    return;
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Event notes read. ",
                            Toast.LENGTH_LONG
                    ).show();
                }

                TextView eventNoteTextView = findViewById(R.id.eventNoteTextView);
                eventNoteTextView.setText(eventNote);


            }
        });
    }
}
