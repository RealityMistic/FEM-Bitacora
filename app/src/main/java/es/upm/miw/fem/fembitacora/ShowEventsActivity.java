package es.upm.miw.fem.fembitacora;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.upm.miw.fem.fembitacora.models.DeliveryItem;
import es.upm.miw.fem.fembitacora.models.Event;

import static es.upm.miw.fem.fembitacora.MainActivity.LOG_TAG;

public class ShowEventsActivity extends Activity {

    private ListView incidenciasListView;

    // IncidenciaAdapter adapter;

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
        incidenciasListView = findViewById(R.id.eventsListView);

        View headerView = getLayoutInflater().inflate(R.layout.event_entry, null);
        incidenciasListView.addHeaderView(headerView);

        mDelivererReference = FirebaseDatabase.getInstance().getReference()
                .child("deliverers")
                .child(currentUserID)
                .child("delivery")
                .child(deliveryItem.getId())
                .child("events");

        mDelivererReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                @SuppressWarnings("unchecked")
                Map<String, ?> events = (Map<String, ?>) dataSnapshot.getValue();
                if (events == null) {
                    /* log a warning, DataSnapshot.getValue may return null */
                    Log.w(LOG_TAG, "DataSnapshot-incidencias.getValue may return null");

                    Toast.makeText(
                            getApplicationContext(),
                            "No events. ",
                            Toast.LENGTH_LONG
                    ).show();

                    return;
                }
                ArrayList<Event> eventList = new ArrayList<>();

                for (Map.Entry<String, ?> eventSetEntry : events.entrySet()) {

                    Event event = new Event();

                    Map<String, ?> eventMap = (HashMap<String, ?>) eventSetEntry.getValue();
                    for (Map.Entry<String, ?> eventEntry : eventMap.entrySet()) {
                        if (eventSetEntry.getValue() instanceof String) {
                            event.setNotes((String) eventSetEntry.getValue());
                        }
                    }
                    eventList.add(event);

                }

                eventAdapter = new EventAdapter(
                        getApplicationContext(),
                        R.layout.event_list,
                        eventList);

                incidenciasListView.setAdapter(eventAdapter);
                eventAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(LOG_TAG, "loadIncidencia:onCancelled", databaseError.toException());
                // ...
            }
        });

    }
}
