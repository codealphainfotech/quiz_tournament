package com.example.quizapp.contoller;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.quizapp.adapters.TournamentListAdapter;
import com.example.quizapp.models.ApiResponse;
import com.example.quizapp.models.CategoryModel;
import com.example.quizapp.models.TournamentModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuizConroller {

    static  String TAG = "QuizConroller";
    private String quiz_path = "all_quiz";

    private TournamentListAdapter adapter;
    public QuizConroller() {
        this.db = FirebaseFirestore.getInstance();
        initCat();
        initDifficulty();
    }

    FirebaseFirestore db ;

    ArrayList<CategoryModel> catList = new ArrayList<CategoryModel>();

    //difficulty list
    ArrayList<String> difficultyList = new ArrayList();


    //tournament list
    List<TournamentModel> tournamentModelList = new ArrayList<>();

    private void initDifficulty(){
        difficultyList.add( "Any Difficulty");
        difficultyList.add(  "Easy");
        difficultyList.add( "Medium");
        difficultyList.add( "Hard");
    }
    private void initCat(){
        catList.add(new CategoryModel(
                9, "General Knowledge"
        ));
        catList.add(new CategoryModel(
                10, "Entertainment: Books"
        ));
        catList.add(new CategoryModel(
                11, "Entertainment: Film"
        ));
        catList.add(new CategoryModel(
                12, "Music"
        ));
        catList.add(new CategoryModel(
                14, "Television"
        ));
        catList.add(new CategoryModel(
                17, "Science & Nature"
        ));
    }

    public void initAdapter(TournamentListAdapter adapter){
        this.adapter = adapter;
    }

    public ArrayList<CategoryModel> getCategories(){
        return  catList;
    }

    public ArrayList<String> getDifficultyList(){
        return difficultyList;
    }

    //add data to firebase
    public  void createQuiz(TournamentModel quiz,OnSuccessListner successListner, OnFailed onFailed){
         try{
             db.collection(quiz_path).document(quiz.getId()).set(quiz).addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void aVoid) {

                             Log.e(TAG, "DocumentSnapshot successfully written!");
                            successListner.onSuccess();
                         }
                     })
                     .addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Log.e(TAG, "onFailure: " + e.getMessage() );
                             onFailed.onFailed();
                         }
                     });
         }catch (Exception e){
             Log.e(TAG, "createQuiz: error : " +e.getMessage(),e );
             onFailed.onFailed();
         }

    }


    //function to get all tournaments
    public void getTournamentFromDb(OnSuccessListner successListner) {

        CollectionReference tournamentsRef = db.collection(quiz_path);

        // Order by start date (ascending)
        tournamentsRef.orderBy("startDate", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        tournamentModelList.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            TournamentModel tournament = document.toObject(TournamentModel.class);
                            if (tournament != null) {
                                tournamentModelList.add(tournament);
                            }
                        }
                        successListner.onSuccess();
                        // Update recycler view
                    } else {
                        Log.e(TAG, "Error getting documents.", task.getException());
                        successListner.onSuccess();

                    }
                });
    }

    public List<TournamentModel> getTournamentList(){
        return  tournamentModelList;
    }

    public TournamentModel getTournamentFromID(String id){
        TournamentModel mode = new TournamentModel();
        for (int i = 0; i < tournamentModelList.size(); i++) {
            if (tournamentModelList.get(i).getId() == id){
                mode = tournamentModelList.get(i);
                Log.e(TAG, "getTournamentFromID: Found" );
            }
        }

        return  mode;
    }

    public TournamentModel decodeTournamentFromJson(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, TournamentModel.class);
    }

    //delete Tournament
    public void deleteTournament(String tournamentId, OnSuccessListner onSuccessListner, OnFailed onFailed ){
        db.collection(quiz_path).document(tournamentId)
                .delete()
                .addOnSuccessListener(unused -> {
                    Log.e(TAG, "deleteTournament: Success" );
                    // Update UI after deletion (remove from list)
                    tournamentModelList.removeIf(t -> t.getId().equals(tournamentId));
                    if (adapter != null){
                        adapter.notifyDataSetChanged();
                    }
                   onSuccessListner.onSuccess();
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error deleting tournament", e);
                        onFailed.onFailed();
                    }
                });
    }


    //get quiz from open db
    public void getQuizQuestions(Context context, String url, VolleyCallback callback) {

        Log.e(TAG, "getQuizQuestions: url:: "+url );

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Handle successful response
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int responseCode = jsonObject.getInt("response_code");
                        if (responseCode == 0) {
                            JSONArray resultsArray = jsonObject.getJSONArray("results");
                            List<ApiResponse.Result> results = new ArrayList<>();
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject resultObject = resultsArray.getJSONObject(i);
                                ApiResponse.Result result = new ApiResponse.Result(
                                        resultObject.getString("type"),
                                        resultObject.getString("difficulty"),
                                        resultObject.getString("category"),
                                        resultObject.getString("question"),
                                        resultObject.getString("correct_answer"),
                                        convertStringList(resultObject.getJSONArray("incorrect_answers"))
                                );
                                results.add(result);
                            }
                            callback.onSuccess(results);
                        } else {
                            callback.onError("API Error: " + responseCode);
                        }
                    } catch (JSONException e) {
                        callback.onError("Parsing Error: " + e.getMessage());
                    }
                },
                error -> {
                    // Handle API error
                    callback.onError("Volley Error: " + error.getMessage());
                });

        // Add the request to the request queue
        RequestQueue queue = Volley.newRequestQueue(context); // Replace 'context' with your context
        queue.add(request);
    }

    private List<String> convertStringList(JSONArray jsonArray) throws JSONException {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            stringList.add(jsonArray.getString(i));
        }
        return stringList;
    }

}
