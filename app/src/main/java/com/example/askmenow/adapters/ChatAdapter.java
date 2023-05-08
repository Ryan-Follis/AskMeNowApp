package com.example.askmenow.adapters;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.askmenow.databinding.ItemContainerReceivedAnswerBinding;
import com.example.askmenow.databinding.ItemContainerReceivedQuestionBinding;
import com.example.askmenow.databinding.ItemContainerSentAnswerBinding;
import com.example.askmenow.databinding.ItemContainerSentMessageBinding;
import com.example.askmenow.databinding.ItemContainerReceivedMessageBinding;
import com.example.askmenow.databinding.ItemContainerSentQuestionBinding;
import com.example.askmenow.models.ChatMessage;
import com.example.askmenow.utilities.Constants;
import com.example.askmenow.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> chatMessages;
    private Bitmap receiverProfileImage;
    private final String senderID;
    private Context applicationContext;

    public static final int VIEW_TYPE_SENT_MESSAGE = 1;
    public static final int VIEW_TYPE_RECEIVED_MESSAGE = 2;
    public static final int VIEW_TYPE_SENT_QUESTION = 3;
    public static final int VIEW_TYPE_RECEIVED_QUESTION = 4;
    public static final int VIEW_TYPE_SENT_ANSWER = 5;
    public static final int VIEW_TYPE_RECEIVED_ANSWER = 6;

    public void setReceiverProfileImage(Bitmap bitmap){
        receiverProfileImage = bitmap;
    }

    public ChatAdapter(List<ChatMessage> chatMessages, Bitmap receiverProfileImage, String senderID, Context applicationContext){
        this.chatMessages = chatMessages;
        this.receiverProfileImage = receiverProfileImage;
        this.senderID = senderID;
        // this.applicationContext = applicationContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.applicationContext = parent.getContext();
        if(viewType == VIEW_TYPE_SENT_MESSAGE){
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        } else if(viewType == VIEW_TYPE_RECEIVED_MESSAGE){
            return new ReceivedMessageViewHolder(
                    ItemContainerReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        } else if(viewType == VIEW_TYPE_SENT_QUESTION){
            return new SentQuestionViewHolder(
                    ItemContainerSentQuestionBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        } else if(viewType == VIEW_TYPE_RECEIVED_QUESTION){
            return new ReceivedQuestionViewHolder(
                    ItemContainerReceivedQuestionBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    ),
                    applicationContext
            );
        } else if(viewType == VIEW_TYPE_SENT_ANSWER){
            return new SentAnswerViewHolder(
                    ItemContainerSentAnswerBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        } else{
            return new ReceivedAnswerViewHolder(
                    ItemContainerReceivedAnswerBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_SENT_MESSAGE){
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        } else if(getItemViewType(position) == VIEW_TYPE_RECEIVED_MESSAGE){
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position), receiverProfileImage);
        } else if(getItemViewType(position) == VIEW_TYPE_SENT_QUESTION){
            ((SentQuestionViewHolder) holder).setData(chatMessages.get(position));
        } else if(getItemViewType(position) == VIEW_TYPE_RECEIVED_QUESTION){
            ((ReceivedQuestionViewHolder) holder).setData(chatMessages.get(position), receiverProfileImage);
        } else if(getItemViewType(position) == VIEW_TYPE_SENT_ANSWER){
            ((SentAnswerViewHolder) holder).setData(chatMessages.get(position));
        } else{
            ((ReceivedAnswerViewHolder) holder).setData(chatMessages.get(position), receiverProfileImage);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position){
        if(chatMessages.get(position).senderID.equals(senderID) &&
                chatMessages.get(position).isQuestion) {
            return VIEW_TYPE_SENT_QUESTION;
        } else if(chatMessages.get(position).senderID.equals(senderID) &&
                chatMessages.get(position).isAnswer){
            return VIEW_TYPE_SENT_ANSWER;
        } else if(chatMessages.get(position).senderID.equals(senderID)){
            return VIEW_TYPE_SENT_MESSAGE;
        } else if(chatMessages.get(position).receiverID.equals(senderID) &&
                chatMessages.get(position).isQuestion){
            return VIEW_TYPE_RECEIVED_QUESTION;
        } else if(chatMessages.get(position).receiverID.equals(senderID) &&
                chatMessages.get(position).isAnswer){
            return VIEW_TYPE_RECEIVED_ANSWER;
        } else{
            return VIEW_TYPE_RECEIVED_MESSAGE;
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

    static class SentMessageViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding){
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(ChatMessage chatMessage){
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainterReceivedMessageBinding){
            super(itemContainterReceivedMessageBinding.getRoot());
            binding = itemContainterReceivedMessageBinding;
        }

        void setData(ChatMessage chatMessage, Bitmap receiverProfileImage){
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
            if(receiverProfileImage != null){
                binding.profileImage.setImageBitmap(receiverProfileImage);
            }
        }
    }

    static class SentQuestionViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerSentQuestionBinding binding;

        SentQuestionViewHolder(ItemContainerSentQuestionBinding itemContainerSentQuestionBinding){
            super(itemContainerSentQuestionBinding.getRoot());
            binding = itemContainerSentQuestionBinding;
        }

        void setData(ChatMessage chatMessage){
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }

    static class ReceivedQuestionViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerReceivedQuestionBinding binding;
        private final Context receivedQuestionApplicationContext;

        ReceivedQuestionViewHolder(ItemContainerReceivedQuestionBinding itemContainerReceivedQuestionBinding, Context applicationContext){
            super(itemContainerReceivedQuestionBinding.getRoot());
            binding = itemContainerReceivedQuestionBinding;
            receivedQuestionApplicationContext = applicationContext;
        }

        void setData(ChatMessage chatMessage, Bitmap receiverProfileImage){
            binding.textMessage.setText(chatMessage.message);
            // On click listeners for received messages allowing for responses to be
            // sent by the user
            binding.textMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PreferenceManager preferenceManager = new PreferenceManager(receivedQuestionApplicationContext);
                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                    DocumentReference documentReference =
                            database.collection(Constants.KEY_COLLECTION_USERS).document(
                                    preferenceManager.getString(Constants.KEY_USER_ID)
                            );
                    documentReference.update(Constants.KEY_CURR_MSG_STATUS, Constants.ANSWERING_QUESTION);
                }
            });
            binding.textDateTime.setText(chatMessage.dateTime);
            if(receiverProfileImage != null){
                binding.profileImage.setImageBitmap(receiverProfileImage);
            }
        }
    }

    static class SentAnswerViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerSentAnswerBinding binding;

        SentAnswerViewHolder(ItemContainerSentAnswerBinding itemContainerSentAnswerBinding){
            super(itemContainerSentAnswerBinding.getRoot());
            binding = itemContainerSentAnswerBinding;
        }

        void setData(ChatMessage chatMessage){
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }

    static class ReceivedAnswerViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerReceivedAnswerBinding binding;

        ReceivedAnswerViewHolder(ItemContainerReceivedAnswerBinding itemContainterReceivedAnswerBinding){
            super(itemContainterReceivedAnswerBinding.getRoot());
            binding = itemContainterReceivedAnswerBinding;
        }

        void setData(ChatMessage chatMessage, Bitmap receiverProfileImage){
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
            if(receiverProfileImage != null){
                binding.profileImage.setImageBitmap(receiverProfileImage);
            }
        }
    }

}
