package pl.marczak.tauronrealityhack.networking;

import com.estimote.sdk.repackaged.retrofit_v1_9_0.retrofit.http.Query;

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

    @GET("/SystemKurierski/results")
    void sendAnswers(@Query("sector") String sector, @Query("correct") int correct, Callback<List<QuizQuestion>> callback);
}
