package algonquin.cst2335.xu000247;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MessageListFragment extends Fragment {

    ArrayList<ChatMessage> messages = new ArrayList<>();
    RecyclerView chatList;
    MyChatAdapter adt;
    SQLiteDatabase db;
    Button send;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View chatLayout = inflater.inflate(R.layout.chatlayout, container, false);
        send = chatLayout.findViewById(R.id.sendbutton);

        //database
        MyOpenHelper opener = new MyOpenHelper(getContext());
        db = opener.getWritableDatabase();
        Cursor results = db.rawQuery("Select * from " + MyOpenHelper.TABLE_NAME + ";", null);
        int _idCol = results.getColumnIndex("_id");
        int messageCol = results.getColumnIndex(MyOpenHelper.col_message);
        int sendCol = results.getColumnIndex(MyOpenHelper.col_send_receive);
        int timeCol = results.getColumnIndex(MyOpenHelper.col_time_sent);
        while(results.moveToNext()) {
            long id = results.getInt(_idCol);
            String message = results.getString(messageCol);
            int sendOrReceive = results.getInt(sendCol);
            String time = results.getString(timeCol);
            messages.add(new ChatMessage(message, sendOrReceive, time, id));
        }

        //chatlist, button, edittext
        chatList = chatLayout.findViewById(R.id.myrecycler);
        Button sendButton = chatLayout.findViewById(R.id.sendbutton);
        Button receiveButton = chatLayout.findViewById(R.id.receivebutton);
        EditText input = chatLayout.findViewById(R.id.inputmessage);

        //adapter
        adt = new MyChatAdapter();
        chatList.setAdapter(adt);
        chatList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        //send click listener to add message
        sendButton.setOnClickListener(  clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyy hh-mm-ss a", Locale.getDefault());
            String currentDateAndTime = sdf.format(new Date());

            ChatMessage thisMessage = new ChatMessage(input.getText().toString(), 1, currentDateAndTime);

            //databse code
            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, thisMessage.getMessage());
            newRow.put(MyOpenHelper.col_send_receive, thisMessage.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent, thisMessage.getTimeSent());
            long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);
            thisMessage.setId(newId);

            messages.add(thisMessage);
            adt.notifyItemInserted(messages.size() - 1);
            input.setText("");
        } );

        //receive click listener to add message
        receiveButton.setOnClickListener(  clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyy hh-mm-ss a", Locale.getDefault());
            String currentDateAndTime = sdf.format(new Date());

            ChatMessage thisMessage = new ChatMessage(input.getText().toString(), 2, currentDateAndTime);

            //database code
            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, thisMessage.getMessage());
            newRow.put(MyOpenHelper.col_send_receive, thisMessage.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent, thisMessage.getTimeSent());
            long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);
            thisMessage.setId(newId);

            messages.add(thisMessage);
            adt.notifyItemInserted(messages.size() - 1);
            input.setText("");
        } );


        return chatLayout;
    }

    public void notifyMessageDelete(ChatMessage chosenMessage, int chosenPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you want to delete the message: " + chosenMessage.getMessage())
                .setTitle("Question:")
                .setNegativeButton("No", (dialog, cl) -> {})
                .setPositiveButton("Yes", (dialog, cl) -> {
                    //position = getAbsoluteAdapterPosition();
                    ChatMessage removedMessage = messages.get(chosenPosition);
                    messages.remove(chosenPosition);
                    adt.notifyItemRemoved(chosenPosition);

                    //databse code
                    db.delete(MyOpenHelper.TABLE_NAME, "_id=?", new String[]{Long.toString(removedMessage.getId())});


                    Snackbar.make(send, "You deleted message #" + chosenPosition, Snackbar.LENGTH_LONG)
                            .setAction("Undo", click -> {
                                //week7 code
                                db.execSQL("Insert into " + MyOpenHelper.TABLE_NAME + " values('" + removedMessage.getId() +
                                        "','" + removedMessage.getMessage() +
                                        "','" + removedMessage.getSendOrReceive() +
                                        "','" + removedMessage.getTimeSent() + "');");

                                messages.add(chosenPosition, removedMessage);
                                adt.notifyItemInserted(chosenPosition);
                            })
                            .show();
                })
                .create().show();
    }

    private class MyRowView extends RecyclerView.ViewHolder{

        TextView messageText;
        TextView timeText;
        int position = -1;

        public MyRowView(View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                ChatRoom parentActivity = (ChatRoom)getContext();
                int position = getAbsoluteAdapterPosition();
                parentActivity.userClickedMessage(messages.get(position), position);
                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you want to delete the message: " + messageText.getText())
                        .setTitle("Question:")
                        .setNegativeButton("No", (dialog, cl) -> {})
                        .setPositiveButton("Yes", (dialog, cl) -> {
                            //position = getAbsoluteAdapterPosition();
                            ChatMessage removedMessage = messages.get(position);
                            messages.remove(position);
                            adt.notifyItemRemoved(position);

                            //databse code
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
                        .create().show();*/
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

        class ChatMessage{
            String message;
            int sendOrReceive;
            String timeSent;

            //database code
            long id;


            public ChatMessage(String message, int sendOrReceive, String timeSent) {
                this.message = message;
                this.sendOrReceive = sendOrReceive;
                this.timeSent = timeSent;
            }

            //database code
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

            //database code
            public void setId( long l){ id = l; }
            public long getId(){ return id; }
        }
}
