package com.coolmanshaft.Vocabulary.api;

import com.coolmanshaft.Vocabulary.model.WordResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DictionaryApi {
    @GET("{word}")
    Call<List<WordResponse>> getWordDefinition(
            @Path("word") String word
    );
}
