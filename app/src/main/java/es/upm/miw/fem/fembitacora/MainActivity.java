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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity implements View.OnClickListener{

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mBDBooksRef;
    private BookApiService apiService;
    private static final String API_BASE_URL = "http://www.etnassoft.com/api/v1/get/";
    final static String LOG_TAG = "MiW - Bitacora: ";
    private EditText mEmailField;
    private EditText mPasswordField;
    private static final int RC_SIGN_IN = 2019;
    ArrayAdapter<String> adapterBookList;
    ArrayList<String> arrayBooks = new ArrayList<>();
    ListView lvBookList;
    private String firebaseUser;
    int imagesBook [] = {R.drawable.book_icon};
    static String mySelection [][];
    String arrayTitles[];
    String arrayAuthor[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Book book;
        mFirebaseAuth = FirebaseAuth.getInstance();
        // Fields
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);

        // mFirebaseDatabase = FirebaseDatabase.getInstance();
        // mBDBooksRef = mFirebaseDatabase.getReference().child("books");
        // Click listeners
        findViewById(R.id.buttonSignIn).setOnClickListener(this);
        findViewById(R.id.buttonAnonymousSignOut).setOnClickListener(this);
        findViewById(R.id.statusSwitch).setClickable(false);
        findViewById(R.id.buttonAnonymousSignOut).setOnClickListener(this);
        lvBookList = findViewById(R.id.lvBookList);

        adapterBookList = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                arrayBooks
        );
        lvBookList.setAdapter(adapterBookList);

        /*
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(BookApiService.class);

        lvBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                String title=arrayTitles[position].toString();
                String author=arrayAuthor[position].toString();
                intent.putExtra("bookTitle",title);
                intent.putExtra("bookAuthor",author);
                startActivity(intent);
            }
        });
        */
    };

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.buttonSignIn) {
            signInWithCredentials();
        } else if (i == R.id.buttonAnonymousSignOut) {
            signOut();
        }
    }
    private void signOut() {
        mFirebaseAuth.signOut();
    }

    private void signInWithCredentials() {

        // Get email and password from form
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        // Create EmailAuthCredential with email and password
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

              mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user is signed in
                    CharSequence username = user.getEmail();
                    Toast.makeText(MainActivity.this, getString(R.string.firebase_user_fmt, username), Toast.LENGTH_LONG).show();
                    Log.i(LOG_TAG, "onAuthStateChanged() " + getString(R.string.firebase_user_fmt, username));
                    ((TextView) findViewById(R.id.titleAnonymous)).setText(getString(R.string.firebase_user_fmt, username));

                    firebaseUser = user.getUid();

                } else {
                    // user is signed out
                    startActivityForResult(
                            // Get an instance of AuthUI based on the default app
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

/*
    public void showBookApi (){

            Call<Book> call_async = apiService.getBooks();

            call_async.enqueue(new Callback<Book>() {
                @Override
                public void onResponse(@NonNull Call<Book> call, @NonNull Response<Book> response) {

                    Book books = response.body();

                    int count=0;
                    int rows = books.getResults().size();

                    arrayTitles=new String[rows];
                    arrayAuthor=new String[rows];
                    mySelection = new String[rows][2];

                    if(null != books){
                        for (Result Book : books.getResults()){
                            //Se asigna los campos recuperados de la API al adaptador
                            adapterBookList.add("Title: "+books.getTitle()+ " Author: "+ books.getAuthor());
                            arrayTitles[count]= books.getTitle();
                            arrayAuthor[count]= books.getAuthor();
                            count++;
                            //Se visualizan los datos de la API en la consola
                            Log.i(LOG_TAG, "API: Nombre: " + books.getTitle()+" Modelo: "+books.getAuthor());
                        }

                    } else
                        Toast.makeText(MainActivity.this, "Empty list!!!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(@NonNull Call<Book> call, @NonNull Throwable t) {
                    Toast.makeText(getApplicationContext(),"API Connection error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i(LOG_TAG, "Error "+ t.getMessage());
                }
            });

    }
    */
}
