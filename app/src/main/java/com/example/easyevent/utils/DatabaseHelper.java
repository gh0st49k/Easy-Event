package com.example.easyevent.utils;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.easyevent.models.User;
import com.example.easyevent.models.EventParticipant;

public class DatabaseHelper {
    private static DatabaseHelper instance;
    private final DatabaseReference database;

    public static final String USERS_PATH = "users";
    public static final String CODING_COMPETITION_PATH = "coding_competition";

    private DatabaseHelper() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public static synchronized DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    // Save User by UID
    public void saveUser(User user, DatabaseCallback callback) {
        if (user.getUid() == null || user.getUid().isEmpty()) {
            if (callback != null) callback.onFailure("User UID is null or empty");
            return;
        }

        database.child(USERS_PATH).child(user.getUid()).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    if (callback != null) callback.onSuccess("Participant saved successfully");

                })
                .addOnFailureListener(e -> {
                    if (callback != null) callback.onFailure("Failed to save participant");
                    
                });
    }

    // Save Event Participant (uses push() for unique ID)

    public void saveEventParticipant(String path, EventParticipant participant, DatabaseCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
        String participantId = ref.push().getKey();  // generate unique ID
        participant.setParticipantId(participantId); // set ID in object

        ref.child(participantId).setValue(participant)
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess("Participant saved successfully");
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage());
                });
    }


    public interface DatabaseCallback {
        void onSuccess(String message);
        void onFailure(String error); // Must be exactly this name and signature
    }

}
