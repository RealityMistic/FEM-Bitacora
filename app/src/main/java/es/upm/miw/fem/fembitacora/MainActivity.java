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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity implements View.OnClickListener{

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
   // private FirebaseDatabase mFirebaseDatabase;
   // private DatabaseReference mBDBooksRef;
    private BookApiService apiService;
    private static final String API_BASE_URL = "http://www.etnassoft.com/api/v1/";
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
                    Toast.makeText(MainActivity.this, "Bienvenido "+user.getDisplayName(), Toast.LENGTH_LONG).show();

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
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                String title=arrayTitles[position].toString();
                String author=arrayAuthor[position].toString();
                intent.putExtra("bookTitle",title);
                intent.putExtra("bookAuthor",author);
                startActivity(intent);
            }
        });

    };

   @Override
   public void onClick(View v) {
       mFirebaseAuth.signOut();
   }
    public void showBooksApi(View v){



        Call<List<Book>> call_async = apiService.getBooks();

        call_async.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(@NonNull Call<List<Book>> call, @NonNull Response<List<Book>> response) {

                List<Book> books = response.body();

                int count=0;
                int rows = books.size();

                arrayTitles=new String[rows];
                arrayAuthor=new String[rows];
                mySelection = new String[rows][2];

                if(null != books){
                    for (Book book : books){
                        //Se asigna los campos recuperados de la API al adaptador
                        adapterBookList.add("Title: "+book.getTitle()+ " Author: "+ book.getAuthor());
                        arrayTitles[count]= book.getTitle();
                        arrayAuthor[count]= book.getAuthor();
                        count++;
                        //Se visualizan los datos de la API en la consola
                        Log.i(LOG_TAG, "API: Nombre: " + book.getTitle()+" Modelo: "+book.getAuthor());
                    }

                } else
                    Toast.makeText(MainActivity.this, "Empty list!!!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(@NonNull Call<List<Book>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"API Connection error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i(LOG_TAG, "Error "+ t.getMessage());
            }
        });

    }

    private void signOut() {
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
