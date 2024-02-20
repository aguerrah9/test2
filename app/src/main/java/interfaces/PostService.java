package interfaces;

import com.indigitall.androidtechnicaltest.models.Character;
import com.indigitall.androidtechnicaltest.models.Characters;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PostService {

    String API_ROUTE = "api/character";

    @GET(API_ROUTE)
    Call<Characters> getPost();
}
