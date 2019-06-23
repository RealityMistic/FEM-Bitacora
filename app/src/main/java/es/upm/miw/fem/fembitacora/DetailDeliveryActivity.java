package es.upm.miw.fem.fembitacora;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.upm.miw.fem.fembitacora.models.DeliveryItem;

public class DetailDeliveryActivity extends Activity {
    DeliveryItem deliveryItem;
    String currentUserID;
    // private DatabaseReference mDeliveryDatabaseReference;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        deliveryItem = (DeliveryItem) getIntent().getSerializableExtra("deliveryItem");
       // mDeliveryDatabaseReference = FirebaseDatabase.getInstance().getReference();
        currentUserID = intent.getStringExtra("FIREBASE_AUTH_CURRENT_USER");

        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(deliveryItem.getTitle());

        TextView authorTextView = findViewById(R.id.authorTextView);
        titleTextView.setText(deliveryItem.getAuthor());
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.noteEvent:
                registerEvent();
                return true;
            case R.id.showEvents:
                showEvents();
                return true;
            case R.id.updateLocation:
                updateLocation();
                return true;
            case R.id.idExit:

        }
        return super.onOptionsItemSelected(item);
    }
    public void registerEvent() {
        DialogFragment registerEventDialogFragment = new RegisterEventDialogFragment();
        registerEventDialogFragment.show(getFragmentManager(), String.valueOf(R.string.register_event));
    }
    public void updateLocation() {
        Intent intent = new Intent(this, LocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DELIVERY", deliveryItem);
        intent.putExtras(bundle);
        intent.putExtra("FIREBASE_AUTH_CURRENT_USER", currentUserID);
        startActivity(intent);
    }
    public void showEvents() {
        Intent intent = new Intent(this, ShowEventsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DELIVERY", deliveryItem);
        intent.putExtras(bundle);
        intent.putExtra("FIREBASE_AUTH_CURRENT_USER", currentUserID);
        startActivity(intent);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

}
