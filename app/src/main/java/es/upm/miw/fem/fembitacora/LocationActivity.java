package es.upm.miw.fem.fembitacora;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.upm.miw.fem.fembitacora.models.DeliveryItem;


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
        return (DeliveryItem) bundle.getSerializable("DELIVERY");
    }

    public void onClickUpdateLocation(View view) {
        String newLoc = locationEditText.getText().toString();
        StringBuffer locationBf = new StringBuffer(deliveryItem.getLocation()).append(", ").append(newLoc);

        deliveryItem.setLocation(locationBf.toString());

        mDelivererReference.child("deliverer").child(currentUserID).child("delivery").child(deliveryItem.getTitle()).setValue(deliveryItem);

        Toast.makeText(this, "Location correctly updated",
                Toast.LENGTH_SHORT).show();

        finish();
    }
}
