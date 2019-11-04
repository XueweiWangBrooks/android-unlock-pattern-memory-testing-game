package xuw220.cse298.lehigh.edu.androidunlock;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder> {
    interface ClickChallengeBtnListener {
        void onClick(GameData gameData);
    }
    private ClickChallengeBtnListener mClickListener;
    void setClickChallengeBtnListener(ClickChallengeBtnListener c) { mClickListener = c;}

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameView;
        TextView scoreView;
        TextView dateView;
        Button challengeBtn;

        ViewHolder(View itemView) {
            super(itemView);
            this.userNameView = itemView.findViewById(R.id.userNameTextView);
            this.scoreView = itemView.findViewById(R.id.highScoreView);
            this.dateView = itemView.findViewById(R.id.dateTextView);
            this.challengeBtn = itemView.findViewById(R.id.challengeBtn);
        }
    }

    private ArrayList<GameData> mData;
    private LayoutInflater mLayoutInflater;

    GameListAdapter(Context context, ArrayList<GameData> data) {
        mData = data;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.game_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GameData gameData = mData.get(position);
        holder.userNameView.setText(gameData.getUserName());
        holder.scoreView.setText("score: " + gameData.getHighScore());
        String dateStr = "";
        if(gameData.getHighScoreDate() != null)
                dateStr = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(gameData.getHighScoreDate());
        holder.dateView.setText(dateStr);

        // Attach a click listener to the view we are configuring
        final View.OnClickListener challengeBtnListerner = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mClickListener.onClick(gameData);
            }
        };
        holder.challengeBtn.setOnClickListener(challengeBtnListerner);
    }
}