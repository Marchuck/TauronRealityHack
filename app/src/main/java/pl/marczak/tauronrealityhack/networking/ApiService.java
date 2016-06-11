package pl.marczak.tauronrealityhack.networking;

import java.util.List;

import pl.marczak.tauronrealityhack.model.QuizQuestion;
import pl.marczak.tauronrealityhack.model.SectorResponse;
import retrofit.Callback;
import retrofit.http.GET;

public interface ApiService {

    @GET("/SystemKurierski/sectors")
    void getSensor( Callback<List<SectorResponse>> callback);

    @GET("/SystemKurierski/quizes")
    void getQuestions( Callback<List<QuizQuestion>> callback);
}
