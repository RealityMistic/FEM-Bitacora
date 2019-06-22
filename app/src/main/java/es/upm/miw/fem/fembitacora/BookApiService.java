package es.upm.miw.fem.fembitacora;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BookApiService {

    @GET("http://www.etnassoft.com/api/v1/get/?category=libros_programacion&criteria=most_viewed")
    Call<List<Book>> getBooks();
}
