package es.upm.miw.fem.fembitacora;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import es.upm.miw.fem.fembitacora.models.DeliveryItem;

import static es.upm.miw.fem.fembitacora.MainActivity.LOG_TAG;

public class DetailDeliveryActivity extends AppCompatActivity {
    DeliveryItem deliveryItem;
    String currentUserID;
    private DatabaseReference mDelivererReference = FirebaseDatabase.getInstance().getReference();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        LocalDateTime deliveryDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDeliveryDate = deliveryDate.format(formatter);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("bookTitle");
        String author = bundle.getString("bookAuthor");
        String publisher = bundle.getString("bookPublisher");
        String price = bundle.getString("bookPrice");
        deliveryItem = (DeliveryItem) getIntent().getSerializableExtra("deliveryItem");
        deliveryItem.setId(md5(deliveryItem.getTitle() + deliveryItem.getAuthor()));
        deliveryItem.setDeliveryDate(formatDeliveryDate);

        Log.i(LOG_TAG, " !!!DATA book md5: " + deliveryItem.getId());
        currentUserID = intent.getStringExtra("FIREBASE_AUTH_CURRENT_USER");
        Log.i(LOG_TAG, " !!!DATA user: " + currentUserID);
        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(title);

        TextView authorTextView = findViewById(R.id.authorTextView);
        authorTextView.setText(author);
        TextView publisherTextView = findViewById(R.id.publisherTextView);
        publisherTextView.setText(publisher);
        TextView priceTextView = findViewById(R.id.priceTextView);
        priceTextView.setText(price);
        TextView deliveryDateTextView = findViewById(R.id.deliveryDateTextView);
        deliveryDateTextView.setText(formatDeliveryDate);

        mDelivererReference = FirebaseDatabase.getInstance().getReference()
                .child("deliverers")
                .child(currentUserID)
                .child("delivery")
                .child(deliveryItem.getId())
                .child("location");


        mDelivererReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Read failed, log a message
                Log.w(LOG_TAG, "locationError:onCancelled", databaseError.toException());
            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> result = (HashMap<String, String>) dataSnapshot.getValue();
                if (null != dataSnapshot.getValue()) {
                    deliveryItem.setLocation(result.values().iterator().next());
                }
                TextView locationTextView = findViewById(R.id.locationTextView);
                locationTextView.setText(deliveryItem.getLocation());
            }
        });

        mDelivererReference = FirebaseDatabase.getInstance().getReference();
        mDelivererReference
                .child("deliverers")
                .child(currentUserID)
                .child("delivery")
                .child(deliveryItem.getId())
                .child("date").removeValue();
        mDelivererReference
                .child("deliverers")
                .child(currentUserID)
                .child("delivery")
                .child(deliveryItem.getId())
                .child("date")
                .push()
                .setValue(deliveryItem.getDeliveryDate());


        TextView locationTextView = findViewById(R.id.deliveryDateTextView);
        locationTextView.setText(deliveryItem.getDeliveryDate());

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

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
