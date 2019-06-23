package es.upm.miw.fem.fembitacora;

import es.upm.miw.fem.fembitacora.models.Books;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface BookApiService {
    @Headers("Content-Type: application/json")
    @GET("svc/books/v3/lists/best-sellers/history.json?api-key=QwxtSoJpUlZ1kAA4V2GOXtURVket0q9e")
    Call<Books> getBooks();
}
