package algonquin.cst2335.xu000247;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    RecyclerView chatList;
    MyChatAdapter adt;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //week7
        MyOpenHelper opener = new MyOpenHelper(this);
        db = opener.getWritableDatabase();

        //week7 code
        Cursor results = db.rawQuery("Select * from " + MyOpenHelper.TABLE_NAME + ";", null);
        int _idCol = results.getColumnIndex("_id");
        int messageCol = results.getColumnIndex(MyOpenHelper.col_message);
        int sendCol = results.getColumnIndex(MyOpenHelper.col_send_receive);
        int timeCol = results.getColumnIndex(MyOpenHelper.col_time_sent);
        //week7 code
        while(results.moveToNext()) {
            long id = results.getInt(_idCol);
            String message = results.getString(messageCol);
            int sendOrReceive = results.getInt(sendCol);
            String time = results.getString(timeCol);
            messages.add(new ChatMessage(message, sendOrReceive, time, id));
        }


        setContentView(R.layout.chatlayout);
        chatList = findViewById(R.id.myrecycler);
        //chatList.setAdapter(new MyChatAdapter());

        adt = new MyChatAdapter();
        chatList.setAdapter(adt);
        chatList.setLayoutManager(new LinearLayoutManager(this));

        Button sendButton = findViewById(R.id.sendbutton);
        Button receiveButton = findViewById(R.id.receivebutton);
        EditText input = findViewById(R.id.inputmessage);




        sendButton.setOnClickListener(  clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyy hh-mm-ss a", Locale.getDefault());
            String currentDateAndTime = sdf.format(new Date());



            ChatMessage thisMessage = new ChatMessage(input.getText().toString(), 1, currentDateAndTime);

            //week7 code
            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, thisMessage.getMessage());
            newRow.put(MyOpenHelper.col_send_receive, thisMessage.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent, thisMessage.getTimeSent());
            long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);

            //week7 code
            //long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);
            thisMessage.setId(newId);

            messages.add(thisMessage);
            input.setText("");
            adt.notifyItemInserted(messages.size() - 1);
        } );
        receiveButton.setOnClickListener(  clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyy hh-mm-ss a", Locale.getDefault());
            String currentDateAndTime = sdf.format(new Date());

            ChatMessage thisMessage = new ChatMessage(input.getText().toString(), 2, currentDateAndTime);

            //week7 code
            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, thisMessage.getMessage());
            newRow.put(MyOpenHelper.col_send_receive, thisMessage.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent, thisMessage.getTimeSent());
            long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);

            //week7 code
            //long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);
            thisMessage.setId(newId);


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
                    //position = getAbsoluteAdapterPosition();
                    ChatMessage removedMessage = messages.get(position);
                    messages.remove(position);
                    adt.notifyItemRemoved(position);

                    //week7 code
                    db.delete(MyOpenHelper.TABLE_NAME, "_id=?", new String[]{Long.toString(removedMessage.getId())});


                    Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                            .setAction("Undo", click -> {
                                //week7 code
                                db.execSQL("Insert into " + MyOpenHelper.TABLE_NAME + " values('" + removedMessage.getId() +
                                        "','" + removedMessage.getMessage() +
                                        "','" + removedMessage.getSendOrReceive() +
                                        "','" + removedMessage.getTimeSent() + "');");

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

        //week7 code
         long id;


         public ChatMessage(String message, int sendOrReceive, String timeSent) {
             this.message = message;
             this.sendOrReceive = sendOrReceive;
             this.timeSent = timeSent;
         }

         //week7 code
         public  ChatMessage(String message, int sendOrReceive, String timeSent, long id){
             this.message = message;
             this.sendOrReceive = sendOrReceive;
             this.timeSent = timeSent;
             setId(id);
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

         //week7 code
         public void setId( long l){ id = l; }
         public long getId(){ return id; }
     }



}
