package com.friedrichvoelkers.myapplication.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.friedrichvoelkers.myapplication.R;
import com.friedrichvoelkers.myapplication.data.GlobalState;
import com.friedrichvoelkers.myapplication.databinding.ActivityChatBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding activityChatBinding;

    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private Button sendButton;
    private EditText inputText;

    private DatabaseReference firebaseRealtimeDatabaseChat;
    private DatabaseReference firebaseRealtimeDatabaseChats;

    private final long TESTTIME = 1666882455;

    /**
     * {@inheritDoc}
     * <p>
     * Perform initialization of all fragments.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityChatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(activityChatBinding.getRoot());

        firebaseRealtimeDatabaseChat =  FirebaseDatabase.getInstance().getReference().child("games").child(String.valueOf(GlobalState.getGameId()));

        mMessageRecycler = findViewById(R.id.recycler_gchat);
        mMessageAdapter = new MessageListAdapter(this);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);

        mMessageRecycler.scrollToPosition(mMessageAdapter.getItemCount() - 1);

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

        // Send a Message
        sendButton = findViewById(R.id.button_chat_view_send);
        sendButton.setOnClickListener(view -> sendMessage());

        firebaseRealtimeDatabaseChats = firebaseRealtimeDatabaseChat.child("chat");
        firebaseRealtimeDatabaseChats.addChildEventListener(new ChildEventListener() {
            /**
             * This method is triggered when a new child is added to the location to which this listener was
             * added.
             *
             * @param snapshot          An immutable snapshot of the data at the new child location
             * @param previousChildName The key name of sibling location ordered before the new child. This
             */
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Message msg = snapshot.getValue(Message.class);
                    mMessageAdapter.add(msg);
                    mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);
                }
            }

            /**
             * This method is triggered when the data at a child location has changed.
             *
             * @param snapshot          An immutable snapshot of the data at the new data at the child location
             * @param previousChildName The key name of sibling location ordered before the child. This will
             */
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            /**
             * This method is triggered when a child is removed from the location to which this listener was
             * added.
             *
             * @param snapshot An immutable snapshot of the data at the child that was removed.
             */
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            /**
             * This method is triggered when a child location's priority changes. See {@link
             * DatabaseReference#setPriority(Object)} and <a
             * href="https://firebase.google.com/docs/database/android/retrieve-data#data_order"
             * target="_blank">Ordered Data</a> for more information on priorities and ordering data.
             *
             * @param snapshot          An immutable snapshot of the data at the location that moved.
             * @param previousChildName The key name of the sibling location ordered before the child
             */
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            /**
             * This method will be triggered in the event that this listener either failed at the server, or
             * is removed as a result of the security and Firebase rules. For more information on securing
             * your data, see: <a href="https://firebase.google.com/docs/database/security/quickstart"
             * target="_blank"> Security Quickstart</a>
             *
             * @param error A description of the error that occurred
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage() {
        String text = String.valueOf(inputText.getText()).trim();
        if (text.equals("")) {
            inputText.setText("");
            return;
        }
        firebaseRealtimeDatabaseChats.push().setValue(new Message(GlobalState.getNickname(), text, TESTTIME));
        inputText.setText("");
    }
}