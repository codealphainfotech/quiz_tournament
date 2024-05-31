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
import com.example.quizapp.models.QuizLikeModel;
import com.example.quizapp.models.QuizPlayedModel;
import com.example.quizapp.models.TournamentModel;
import com.example.quizapp.models.UserModel;
import com.example.quizapp.utils.HelperUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizConroller {

    static  String TAG = "QuizConroller";
    private String quiz_path = "all_quiz";

    private  String played_quiz_path = "quizPlayedRecords";

    private  String quiz_like_path = "quizLikes";

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


    //function to save played quiz
    public void saveQuizPlayedRecord(QuizPlayedModel quizPlayedModel, OnSaveRecordListener listener) {
        if (quizPlayedModel == null || quizPlayedModel.getId() == null || quizPlayedModel.getTournamentModel() == null || quizPlayedModel.getUserModel() == null || quizPlayedModel.getPlayedDate() == null || quizPlayedModel.getScore() == 0) {
            listener.onSaveError("Missing required fields in QuizPlayedModel.");
            return;
        }

        db.collection(played_quiz_path)
                .document(quizPlayedModel.getId())
                .set(quizPlayedModel)
                .addOnSuccessListener(unused -> listener.onSaveSuccess("Quiz Played Record saved successfully!"))
                .addOnFailureListener(e -> listener.onSaveError("Error saving Quiz Played Record: " + e.getMessage()));
    }

    // get played quiz by user id and tournament id
    public void getQuizPlayedModelByUserIdAndTournamentId(String userId, String tournamentId, OnGetQuizPlayedModelListener listener) {

        db.collection(played_quiz_path)
                .whereEqualTo("userModel.userID", userId)
                .whereEqualTo("tournamentModel.id", tournamentId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        listener.onGetQuizPlayedModel(null);
                        return;
                    }

                    // Assuming there's only one document matching the criteria (handle multiple if needed)
                    QuizPlayedModel quizPlayedModel = queryDocumentSnapshots.getDocuments().get(0).toObject(QuizPlayedModel.class);
                    listener.onGetQuizPlayedModel(quizPlayedModel);
                })
                .addOnFailureListener(e -> listener.onGetError(e.getMessage()));
    }


    public void getPlayedQuizModelsByTournamentId( String tournamentId, OnGetPlayedQuizModelsListener listener) {
        db.collection("quizPlayedRecords")
                .whereEqualTo("tournamentModel.id", tournamentId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<QuizPlayedModel> playedQuizModels = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        QuizPlayedModel playedQuizModel = document.toObject(QuizPlayedModel.class);
                        if (playedQuizModel != null) {
                            playedQuizModels.add(playedQuizModel);
                        }
                    }
                    listener.onGetPlayedQuizModels(playedQuizModels);
                })
                .addOnFailureListener(e -> listener.onGetError(e.getMessage()));
    }

    //function to add like and dis like
    public void addLikeDislikeEntry(String tournamentId, String userId, String type, OnAddLikeDislikeListener listener) {
        // Generate a unique ID for the entry (optional)
        String id = HelperUtils.generateUniqueID();

        // Create a QuizLikeModel object
        QuizLikeModel likeDislikeEntry = new QuizLikeModel(id, tournamentId, userId, type, new Date());

        // Add the entry to Fires tore
        db.collection(quiz_like_path) // Replace "quizLikes" with your actual collection name
                .add(likeDislikeEntry)
                .addOnSuccessListener(documentReference -> listener.onAddLikeDislikeSuccess())
                .addOnFailureListener(e -> listener.onAddLikeDislikeError(e.getMessage()));
    }

    public void getQuizLikeDislikeByTournamentIdAndUserId( String tournamentId, String userId, OnGetLikeDislikeListListener listener) {
        db.collection(quiz_like_path) // Replace "quizLikes" with your actual collection name
                .whereEqualTo("tournamentId", tournamentId)
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<QuizLikeModel> likeDislikeList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        QuizLikeModel likeDislikeModel = document.toObject(QuizLikeModel.class);
                        if (likeDislikeModel != null) {
                            likeDislikeList.add(likeDislikeModel);
                        }
                    }
                    listener.onGetLikeDislikeList(likeDislikeList);
                })
                .addOnFailureListener(e -> listener.onGetError(e.getMessage()));
    }

    public void getQuizLikeByTournamentId( String tournamentId,OnGetLikeDislikeListListener listener) {
        db.collection(quiz_like_path) // Replace "quizLikes" with your actual collection name
                .whereEqualTo("tournamentId", tournamentId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<QuizLikeModel> likeDislikeList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        QuizLikeModel likeDislikeModel = document.toObject(QuizLikeModel.class);
                        if (likeDislikeModel != null) {
                            likeDislikeList.add(likeDislikeModel);
                        }
                    }
                    listener.onGetLikeDislikeList(likeDislikeList);
                })
                .addOnFailureListener(e -> listener.onGetError(e.getMessage()));
    }


    //function to get participated Quiz list
    public void getQuizPlayedModelsByUserId( String userId, OnGetPlayedQuizModelsListener listener) {
        db.collection(played_quiz_path)
                .whereEqualTo("userModel.userID", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<QuizPlayedModel> playedQuizModels = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        QuizPlayedModel playedQuizModel = document.toObject(QuizPlayedModel.class);
                        if (playedQuizModel != null) {
                            playedQuizModels.add(playedQuizModel);
                        }
                    }
                    listener.onGetPlayedQuizModels(playedQuizModels);
                })
                .addOnFailureListener(e -> listener.onGetError(e.getMessage()));
    }

}
