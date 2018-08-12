package com.example.thu.realtysocial;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListenOnline {
    FirebaseUser userNote;
    private FirebaseAuth firebaseAuthNote;
    private DatabaseReference userReferenceNote;

    public ListenOnline() {
    }
    public void listen()
    {
        firebaseAuthNote = FirebaseAuth.getInstance();
        userNote = firebaseAuthNote.getCurrentUser();
        String online_user_id=firebaseAuthNote.getCurrentUser().getUid();
        userReferenceNote= FirebaseDatabase.getInstance().getReference().child("users").child(online_user_id);
        userReferenceNote.child("online").setValue(true);
    }
}
