package algonquin.cst2335.xu000247;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatRoom extends AppCompatActivity{


    ArrayList<ChatMessage> messages = new ArrayList<>();
    MyChatAdapter adt = new MyChatAdapter();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlayout);
        RecyclerView chatList;
        chatList = findViewById(R.id.myrecycler);
        //chatList.setAdapter(new MyChatAdapter());
        chatList.setAdapter(adt);
        chatList.setLayoutManager(new LinearLayoutManager(this));

        Button sendButton = findViewById(R.id.sendbutton);
        Button receiveButton = findViewById(R.id.receivebutton);
        EditText input = findViewById(R.id.inputmessage);

        MyOpenHelper opener = new MyOpenHelper(this);


        sendButton.setOnClickListener(  clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyy hh-mm-ss a", Locale.getDefault());
            String currentDateAndTime = sdf.format(new Date());

            ChatMessage thisMessage = new ChatMessage(input.getText().toString(), 1, currentDateAndTime);
            messages.add(thisMessage);
            input.setText("");
            adt.notifyItemInserted(messages.size() - 1);
        } );
        receiveButton.setOnClickListener(  clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyy hh-mm-ss a", Locale.getDefault());
            String currentDateAndTime = sdf.format(new Date());

            ChatMessage thisMessage = new ChatMessage(input.getText().toString(), 2, currentDateAndTime);
            messages.add(thisMessage);
            input.setText("");
            adt.notifyItemInserted(messages.size() - 1);
        } );
    }

    private class MyRowView extends RecyclerView.ViewHolder{

        TextView messageText;
        TextView timeText;
        int position = -1;

        public MyRowView(View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setMessage("Do you want to delete the message: " + messageText.getText())
                .setTitle("Question:")
                .setNegativeButton("No", (dialog, cl) -> {})
                .setPositiveButton("Yes", (dialog, cl) -> {
                    position = getAbsoluteAdapterPosition();
                    ChatMessage removedMessage = messages.get(position);
                    messages.remove(position);
                    adt.notifyItemRemoved(position);

                    Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                            .setAction("Undo", click -> {
                                messages.add(position, removedMessage);
                                adt.notifyItemInserted(position);
                            })
                            .show();
                })
                .create().show();
            });

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }

        public void setPosition(int p) { position = p;}
    }

    private class MyChatAdapter extends RecyclerView.Adapter<MyRowView> {
        @Override
        public MyRowView onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            int layoutID;
            if(viewType == 1){
                layoutID = R.layout.sent_message;
            }else{
                layoutID = R.layout.receive_message;
            }
            View loadedRow = inflater.inflate(layoutID, parent, false);
            return new MyRowView(loadedRow);
        }

        @Override
        public void onBindViewHolder(MyRowView holder, int position) {
            holder.messageText.setText(messages.get(position).getMessage());
            holder.timeText.setText(messages.get(position).getTimeSent());
            holder.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        @Override
        public int getItemViewType(int position){
            ChatMessage thisRow = messages.get(position);
            int viewType = thisRow.getSendOrReceive();
            return viewType;
        }
    }

     private class ChatMessage{
        String message;
        int sendOrReceive;
        String timeSent;

         public ChatMessage(String message, int sendOrReceive, String timeSent) {
             this.message = message;
             this.sendOrReceive = sendOrReceive;
             this.timeSent = timeSent;
         }

         public String getMessage() {
             return message;
         }

         public int getSendOrReceive() {
             return sendOrReceive;
         }

         public String getTimeSent() {
             return timeSent;
         }
     }

}
