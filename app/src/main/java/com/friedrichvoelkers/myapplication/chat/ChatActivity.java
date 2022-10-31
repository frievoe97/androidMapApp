package com.friedrichvoelkers.myapplication.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.CountDownTimer;

import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.friedrichvoelkers.myapplication.R;
import com.friedrichvoelkers.myapplication.databinding.ActivityChatBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding activityChatBinding;

    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private Button sendButton;
    private EditText inputText;

    private DatabaseReference firebaseRealtimeDatabase;
    private DatabaseReference firebaseRealtimeDatabaseChats;

    private final int GAME_ID = 5324956;

    private CountDownTimer countdownTimer;

    private String lastStringTime = "";
    private String lastName = "";

    private int itsMe = 1;

    private long testtime = 1666882455;

    List<Message> allMessages = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityChatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(activityChatBinding.getRoot());

        firebaseRealtimeDatabase =  FirebaseDatabase.getInstance().getReference().child("games").child(String.valueOf(GAME_ID));

        mMessageRecycler = findViewById(R.id.recycler_gchat);
        mMessageAdapter = new MessageListAdapter(this);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);

        allMessages.add(new Message("frievoe", "Hallo", testtime));
        allMessages.add(new Message("David", "Moin ;)", testtime + 30));
        allMessages.add(new Message("Malte", "Hallöle", testtime + 31));
        allMessages.add(new Message("frievoe", "Wo wollen wir suchen?", testtime + 32));
        allMessages.add(new Message("frievoe", "Soll ich richtung Frensehturm?", testtime + 101));
        allMessages.add(new Message("Gustav", "Ja, klingt gut!", testtime + 115));
        allMessages.add(new Message("Gustav", "Ja, klingt gut!", testtime + 130));
        allMessages.add(new Message("frievoe", "Wo wollen wir suchen?", testtime + 134));
        allMessages.add(new Message("frievoe", "Wo wollen wir suchen?", testtime + 135));
        allMessages.add(new Message("Gustav", "Ja, klingt gut!", testtime + 156));
        allMessages.add(new Message("Gustav", "Ja, klingt gut!", testtime + 173));
        allMessages.add(new Message("frievoe", "Wo wollen wir suchen?", testtime + 194));
        allMessages.add(new Message("Gustav", "Ja, klingt gut!", testtime + 204));
        allMessages.add(new Message("frievoe", "Letzte Nachricht", testtime + 230));
        allMessages.add(new Message("Manuel", "Okay, eine Nachricht kommt noch... Und dier ist sehr land. " +
                "Der folgende Satz wird ganz oft kopier. Okay? Der folgende Satz wird ganz oft kopier. Okay? Der folgende " +
                "Satz wird ganz oft kopier. Okay? Der folgende Satz wird ganz oft kopier. Okay? Der folgende Satz wird ganz " +
                "oft kopier. Okay?", testtime + 256));

        for (int i = 0; i < allMessages.size(); i++) {
            mMessageAdapter.add(allMessages.get(i));
        }

        mMessageRecycler.scrollToPosition(mMessageAdapter.getItemCount() - 1);

        countdownTimer = new CountDownTimer(1000 * 60, 2000) {
            @Override
            public void onTick(long l) {
                if (itsMe % 2 == 0) mMessageAdapter.add(new Message("frievoe", "Ja, Bist du da?", testtime));
                else mMessageAdapter.add(new Message("Daniel", "Ja, Bist du da?", testtime));
                mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);
                itsMe++;
            }
            @Override
            public void onFinish() {}
        };

        inputText = findViewById(R.id.edittext_chat_view_message);
        inputText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        inputText.setSingleLine(true);
        inputText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (keyEvent == null) return true;
            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                sendMessage();
                return true;
            }
            return false;
        });

        sendButton = findViewById(R.id.button_chat_view_send);
        sendButton.setOnClickListener(view -> {
            sendMessage();
        });
        //countdownTimer.start();

        firebaseRealtimeDatabaseChats = firebaseRealtimeDatabase.child("chat");
        firebaseRealtimeDatabaseChats.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Message msg = snapshot.getValue(Message.class);
                    mMessageAdapter.add(msg);
                    mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        for (int i = 0; i < allMessages.size(); i++) {
            firebaseRealtimeDatabaseChats.push().setValue(allMessages.get(i));
        }
    }

    private void sendMessage() {

        String text = String.valueOf(inputText.getText()).trim();

        if (text.equals("")) {
            inputText.setText("");
            return;
        }

        Message newMsg;

        if (itsMe % 2 == 0) newMsg = new Message("frievoe", text, testtime);
        else newMsg = new Message("Daniel", text, testtime);

        firebaseRealtimeDatabaseChats.push().setValue(newMsg);
        itsMe++;
        inputText.setText("");
    }
}