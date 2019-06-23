package es.upm.miw.fem.fembitacora;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.upm.miw.fem.fembitacora.models.DeliveryItem;

import static es.upm.miw.fem.fembitacora.MainActivity.LOG_TAG;


public class LocationActivity extends Activity {

    DeliveryItem deliveryItem;
    String currentUserID;

    EditText locationEditText;

    private DatabaseReference mDelivererReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        deliveryItem = getSelectedItem();
        currentUserID = getCurrentUser();

        mDelivererReference = FirebaseDatabase.getInstance().getReference();

        locationEditText = findViewById(R.id.locationEditText);
    }

    private String getCurrentUser() {
        Intent intent = getIntent();
        return intent.getStringExtra("FIREBASE_AUTH_CURRENT_USER");
    }

    private DeliveryItem getSelectedItem() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        return (DeliveryItem) bundle.getSerializable("deliveryItem");
    }

    public void onClickUpdateLocation(View view) {
        String newLocation = locationEditText.getText().toString();

        deliveryItem.setLocation(newLocation);
        Log.i(LOG_TAG, " ClickUpdateLocation before Firebase call");
        mDelivererReference
                .child("deliverers")
                .child(currentUserID)
                .child("delivery")
                .child(deliveryItem.getId())
                .child("location").removeValue();

        mDelivererReference
                .child("deliverers")
                .child(currentUserID)
                .child("delivery")
                .child(deliveryItem.getId())
                .child("location")
                .push()
                .setValue(deliveryItem.getLocation());

        Toast.makeText(this, "Location correctly updated",
                Toast.LENGTH_SHORT).show();

        finish();
    }
}
