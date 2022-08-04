package com.example.help_m5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.example.help_m5.chat.ChatAdapter;
import com.example.help_m5.chat.ChatItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<ChatItem> chatItems;

    private Button topButton;
    private Button midButton;
    private Button botButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);  // every item in recyclerView has fixed size
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chatItems = new ArrayList<>();  // contains all the item that needs to be displayed

        adapter = new ChatAdapter(chatItems);
        recyclerView.setAdapter(adapter);

        Date date = new Date();
        ChatItem chatItem = new ChatItem("", "", "Hi there! What can I help you with today?", date.toString().substring(0, 20));
        chatItems.add(chatItem);

        topButton = (Button) findViewById(R.id.topButton);
        topButton.setText((String) getString(R.string.account_settings));
        topButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long startTime = System.currentTimeMillis();
                Button b = (Button) v;
                String buttonText = b.getText().toString();
                setTopButton(buttonText, startTime);
            }
        });

        midButton = (Button) findViewById(R.id.midButton);
        midButton.setText((String) getString(R.string.app_settings));
        midButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long startTime = System.currentTimeMillis();
                Button b = (Button) v;
                String buttonText = b.getText().toString();
                setMidButton(buttonText, startTime);
            }
        });

        botButton = (Button) findViewById(R.id.botButton);
        botButton.setText((String) getString(R.string.app_functionalities));
        botButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long startTime = System.currentTimeMillis();
                Button b = (Button) v;
                String buttonText = b.getText().toString();
                setBotButton(buttonText, startTime);
            }
        });

        backButton = (Button) findViewById(R.id.chatBackButton);
        backButton.setEnabled(false);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topButton.getText().equals(getString(R.string.account_settings_Q1))) {
                    backButton.setEnabled(false);
                    topButton.setText(getString(R.string.account_settings));
                    midButton.setText(getString(R.string.app_settings));
                    botButton.setText(getString(R.string.app_functionalities));
                } else if (topButton.getText().equals(getString(R.string.app_settings_Q1))) {
                    backButton.setEnabled(false);
                    topButton.setText(getString(R.string.account_settings));
                    midButton.setText(getString(R.string.app_settings));
                    botButton.setText(getString(R.string.app_functionalities));
                } else if (topButton.getText().equals(getString(R.string.app_functionalities_1))) {
                    backButton.setEnabled(false);
                    topButton.setText(getString(R.string.account_settings));
                    midButton.setText(getString(R.string.app_settings));
                    botButton.setText(getString(R.string.app_functionalities));
                } else if (topButton.getText().equals(getString(R.string.app_functionalities_1_Q1))
                        || topButton.getText().equals(getString(R.string.app_functionalities_2_Q1))
                        || topButton.getText().equals(getString(R.string.app_functionalities_3_Q1))) {
                    backButton.setEnabled(true);
                    topButton.setText(getString(R.string.app_functionalities_1));
                    midButton.setText(getString(R.string.app_functionalities_2));
                    botButton.setText(getString(R.string.app_functionalities_3));
                } else {
                    Log.d(TAG, "Error going back in chatBot");
                }
            }
        });

        ImageView returnButton = (ImageView) findViewById(R.id.backBtn);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void conversation(String myMessage, String botMessage1, long startTime1) {
        final String botMessage = botMessage1;
        final long startTime = startTime1;
        Date date = new Date();
        ChatItem chatItem = new ChatItem(myMessage, date.toString().substring(0, 20), "", "");
        chatItems.add(chatItem);
        adapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        backButton.setEnabled(true);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Date date = new Date();
                ChatItem chatItem = new ChatItem("", "", botMessage, date.toString().substring(0, 20));
                chatItems.add(chatItem);
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);

                // For testing non functional requirements
                long endTime = System.currentTimeMillis();
                Log.d(TAG, "endTime is: "+endTime);
                backButton.setTag(String.valueOf((endTime - startTime) < 900));
            }
        }, 800);
    }

    private void setTopButton(String buttonText, long startTime) {
        String myMessage = "";
        String botMessage = "";
        long endTime = 1;
        Log.d(TAG, "endTime is: "+endTime);
        if (buttonText.equals((String) getString(R.string.account_settings))) {
            backButton.setEnabled(true);
            topButton.setText((String) getString(R.string.account_settings_Q1));
            midButton.setText((String) getString(R.string.account_settings_Q2));
            botButton.setText((String) getString(R.string.account_settings_Q3));
        } else if (buttonText.equals(getString(R.string.account_settings_Q1))) {
            myMessage = buttonText;
            botMessage = getString(R.string.account_settings_A1);
            conversation(myMessage, botMessage, startTime);
        } else if (buttonText.equals(getString(R.string.app_settings_Q1))) {
            myMessage = buttonText;
            botMessage = getString(R.string.app_settings_A1);
            conversation(myMessage, botMessage, startTime);
        } else if (buttonText.equals(getString(R.string.app_functionalities_1))) {
            topButton.setText(getString(R.string.app_functionalities_1_Q1));
            midButton.setText(getString(R.string.app_functionalities_1_Q2));
            botButton.setText(getString(R.string.app_functionalities_1_Q3));
        } else if (buttonText.equals(getString(R.string.app_functionalities_1_Q1))) {
            myMessage = buttonText;
            botMessage = getString(R.string.app_functionalities_1_A1);
            conversation(myMessage, botMessage, startTime);
        } else if (buttonText.equals(getString(R.string.app_functionalities_2_Q1))) {
            myMessage = buttonText;
            botMessage = getString(R.string.app_functionalities_2_A1);
            conversation(myMessage, botMessage, startTime);
        } else if (buttonText.equals(getString(R.string.app_functionalities_3_Q1))) {
            myMessage = buttonText;
            botMessage = getString(R.string.app_functionalities_3_A1);
            conversation(myMessage, botMessage, startTime);
        } else {
            Log.d(TAG, "Error selecting question");
        }
    }

    private void setMidButton(String buttonText, long startTime) {
        String myMessage = "";
        String botMessage = "";

        if (buttonText.equals(getString(R.string.app_settings))) {
            backButton.setEnabled(true);
            topButton.setText(getString(R.string.app_settings_Q1));
            midButton.setText(getString(R.string.app_settings_Q2));
            botButton.setText(getString(R.string.app_settings_Q3));
        } else if (buttonText.equals(getString(R.string.account_settings_Q2))) {
            myMessage = buttonText;
            botMessage = getString(R.string.account_settings_A2);
            conversation(myMessage, botMessage, startTime);
        } else if (buttonText.equals(getString(R.string.app_settings_Q2))) {
            myMessage = buttonText;
            botMessage = getString(R.string.app_settings_A2);
            conversation(myMessage, botMessage, startTime);
        } else if (buttonText.equals((String) getString(R.string.app_functionalities_2))) {
            topButton.setText(getString(R.string.app_functionalities_2_Q1));
            midButton.setText(getString(R.string.app_functionalities_2_Q2));
            botButton.setText(getString(R.string.app_functionalities_2_Q3));
        } else if (buttonText.equals((String) getString(R.string.app_functionalities_1_Q2))) {
            myMessage = buttonText;
            botMessage = getString(R.string.app_functionalities_1_A2);
            conversation(myMessage, botMessage, startTime);
        } else if (buttonText.equals((String) getString(R.string.app_functionalities_2_Q2))) {
            myMessage = buttonText;
            botMessage = getString(R.string.app_functionalities_2_A2);
            conversation(myMessage, botMessage, startTime);
        } else if (buttonText.equals((String) getString(R.string.app_functionalities_3_Q2))) {
            myMessage = buttonText;
            botMessage = getString(R.string.app_functionalities_3_A2);
            conversation(myMessage, botMessage, startTime);
        } else {
            Log.d(TAG, "Error selecting question");
        }
    }

    private void setBotButton(String buttonText, long startTime) {
        String myMessage = "";
        String botMessage = "";
        if (buttonText.equals((String) getString(R.string.app_functionalities))) {
            backButton.setEnabled(true);
            topButton.setText((String) getString(R.string.app_functionalities_1));
            midButton.setText((String) getString(R.string.app_functionalities_2));
            botButton.setText((String) getString(R.string.app_functionalities_3));
        } else if (buttonText.equals(getString(R.string.account_settings_Q3))) {
            myMessage = buttonText;
            botMessage = getString(R.string.account_settings_A3);
            conversation(myMessage, botMessage, startTime);
        } else if (buttonText.equals(getString(R.string.app_settings_Q3))) {
            myMessage = buttonText;
            botMessage = getString(R.string.app_settings_A3);
            conversation(myMessage, botMessage, startTime);
        } else if (buttonText.equals((String) getString(R.string.app_functionalities_3))) {
            topButton.setText(getString(R.string.app_functionalities_3_Q1));
            midButton.setText(getString(R.string.app_functionalities_3_Q2));
            botButton.setText(getString(R.string.app_functionalities_3_Q3));
        } else if (buttonText.equals((String) getString(R.string.app_functionalities_1_Q3))) {
            myMessage = buttonText;
            botMessage = getString(R.string.app_functionalities_1_A3);
            conversation(myMessage, botMessage, startTime);
        } else if (buttonText.equals((String) getString(R.string.app_functionalities_2_Q3))) {
            myMessage = buttonText;
            botMessage = getString(R.string.app_functionalities_2_A3);
            conversation(myMessage, botMessage, startTime);
        } else if (buttonText.equals((String) getString(R.string.app_functionalities_3_Q3))) {
            myMessage = buttonText;
            botMessage = getString(R.string.app_functionalities_3_A3);
            conversation(myMessage, botMessage, startTime);
        } else {
            Log.d(TAG, "Error selecting question");
        }
    }

}