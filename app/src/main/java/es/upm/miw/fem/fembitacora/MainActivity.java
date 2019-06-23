package es.upm.miw.fem.fembitacora;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ToolbarWidgetWrapper;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import es.upm.miw.fem.fembitacora.models.BookResult;
import es.upm.miw.fem.fembitacora.models.Books;
import es.upm.miw.fem.fembitacora.models.DeliveryItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mBDBooksRef;
    private BookApiService apiService;
    private static final String API_BASE_URL = "https://api.nytimes.com/";
    final static String LOG_TAG = "MiW-FEM:";
    private static final int RC_SIGN_IN = 2019;
    ArrayAdapter<String> adapterBookList;
    ArrayList<String> arrayBooks = new ArrayList<>();
    ListView lvBookList;
    static String mySelection [][];
    String arrayTitles[];
    String arrayAuthor[];
    String arrayPublisher[];
    String arrayLanguage[];
    String arrayPages[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BookResult bookResult;
        mFirebaseAuth = FirebaseAuth.getInstance();
        lvBookList = findViewById(R.id.lvBookList);

        adapterBookList = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                arrayBooks
        );
        lvBookList.setAdapter(adapterBookList);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    CharSequence username = user.getDisplayName();
                    Toast.makeText(MainActivity.this, "Welcome "+user.getDisplayName(), Toast.LENGTH_LONG).show();

                } else {
                    startActivityForResult(
                            AuthUI.getInstance().
                                    createSignInIntentBuilder().
                                    setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()
                                    )).
                                    setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */).
                                    build(),
                            RC_SIGN_IN);
                }
            }
        };
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(BookApiService.class);

        lvBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailDeliveryActivity.class);
                String title=arrayTitles[position].toString();
                String author=arrayAuthor[position].toString();
                String publisher=arrayPublisher[position].toString();
                String language=arrayLanguage[position].toString();
                String pages=arrayPages[position].toString();
                intent.putExtra("bookTitle",title);
                intent.putExtra("bookAuthor",author);
                intent.putExtra("bookPublisher",author);
                intent.putExtra("bookLanguage",author);
                intent.putExtra("bookPages",author);
                DeliveryItem deliveryItem = new DeliveryItem(title, author);
                intent.putExtra("deliveryItem", deliveryItem);
                startActivity(intent);
            }
        });

    };

   @Override
   public void onClick(View v) {
       mFirebaseAuth.signOut();
   }

   public void showBooksApi(View v){

        Call<Books> call_async = apiService.getBooks();

       call_async.enqueue(new Callback<Books>() {
           @Override
           public void onResponse(@NonNull Call<Books> call, @NonNull Response<Books> response) {

               Books books = response.body();
               Log.i(LOG_TAG, "Response: size: " + books.getBooks().size());

               int count=0;
                int rows = books.getBooks().size();

                arrayTitles=new String[rows];
                arrayAuthor=new String[rows];
                mySelection = new String[rows][2];

                if(null != books){
                    for (BookResult bookResult : books.getBooks()){
                        adapterBookList.add("Title: "+bookResult.getTitle()+ " Author: "+ bookResult.getAuthor());
                        arrayTitles[count]= bookResult.getTitle();
                        arrayAuthor[count]= bookResult.getAuthor();
                        count++;
                        Log.i(LOG_TAG, "API: Title: " + bookResult.getTitle()+" Author: "+bookResult.getAuthor());
                    }

                } else
                    Toast.makeText(MainActivity.this, "Empty list!!!", Toast.LENGTH_LONG).show();
            }

           @Override
           public void onFailure(Call<Books> call, Throwable t) {
               Toast.makeText(getApplicationContext(),"API Connection error: " + t.getMessage(), Toast.LENGTH_LONG).show();
               Log.i(LOG_TAG, "Error ->"+ t.getMessage() + " toString: " + t.toString());
           }
        });

    }

    private void signOut(View v) {
        mFirebaseAuth.signOut();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idExit:
                AuthUI.getInstance().signOut(this);
                return true;
            case R.id.idAbout:
                startActivity(new Intent(this, About.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




}
