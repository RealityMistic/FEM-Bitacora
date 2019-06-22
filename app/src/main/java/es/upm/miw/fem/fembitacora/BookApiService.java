package es.upm.miw.fem.fembitacora;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BookApiService {

    @GET("?category=libros_programacion&criteria=most_viewed")
    Call<Book> getBooks();
}
