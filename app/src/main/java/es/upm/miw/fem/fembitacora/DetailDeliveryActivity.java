package es.upm.miw.fem.fembitacora;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;

import es.upm.miw.fem.fembitacora.models.DeliveryItem;

public class DetailDeliveryActivity extends AppCompatActivity {
    DeliveryItem deliveryItem;
    String currentUserID;
    // private DatabaseReference mDeliveryDatabaseReference;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("bookTitle");
        String author = bundle.getString("bookAuthor");
        String publisher = bundle.getString("bookPublisher");
        String price = bundle.getString("bookPrice");
        deliveryItem = (DeliveryItem) getIntent().getSerializableExtra("deliveryItem");
        deliveryItem.setId(LocalDateTime.now().toString());
       // mDeliveryDatabaseReference = FirebaseDatabase.getInstance().getReference();
        currentUserID = intent.getStringExtra("FIREBASE_AUTH_CURRENT_USER");

        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(title);

        TextView authorTextView = findViewById(R.id.authorTextView);
        authorTextView.setText(author);
        TextView publisherTextView = findViewById(R.id.publisherTextView);
        publisherTextView.setText(publisher);
        TextView priceTextView = findViewById(R.id.priceTextView);
        priceTextView.setText(price);

        TextView locationTextView = findViewById(R.id.locationTextView);
        locationTextView = findViewById(R.id.locationTextView);

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
        Intent intent = new Intent(this, EventActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("deliveryItem", deliveryItem);
        intent.putExtras(bundle);
        intent.putExtra("FIREBASE_AUTH_CURRENT_USER", currentUserID);
        startActivity(intent);
    }
    public void updateLocation() {
        Intent intent = new Intent(this, LocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("deliveryItem", deliveryItem);
        intent.putExtras(bundle);
        intent.putExtra("FIREBASE_AUTH_CURRENT_USER", currentUserID);
        startActivity(intent);
    }
    public void showEvents() {
        Intent intent = new Intent(this, ShowEventsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("deliveryItem", deliveryItem);
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
