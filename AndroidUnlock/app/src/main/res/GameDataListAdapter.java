package xuw220.cse216.lehigh.edu.phase0;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

class GameDataListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {
    interface ClickListener{
        void onClick(Datum d);
    }
    private ClickListener mClickListener;
    ClickListener getClickListener() {return mClickListener;}
    void setClickListener(ClickListener c) { mClickListener = c;}

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mIndex;
        TextView mText;

        ViewHolder(View itemView) {
            super(itemView);
            this.mIndex = itemView.findViewById(R.id.listItemIndex);
            this.mText = itemView.findViewById(R.id.listItemText);
        }
    }

    private ArrayList<Datum> mData;
    private LayoutInflater mLayoutInflater;

    GameDataListAdapter(Context context, ArrayList<Datum> data) {
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
        View view = mLayoutInflater.inflate(R.layout.list_item, parent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Datum d = mData.get(position);
        holder.mIndex.setText(Integer.toString(d.mIndex));
        holder.mText.setText(d.mText);

        // Attach a click listener to the view we are configuring
        final View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mClickListener.onClick(d);
            }
        };
        holder.mIndex.setOnClickListener(listener);
        holder.mText.setOnClickListener(listener);
    }
}