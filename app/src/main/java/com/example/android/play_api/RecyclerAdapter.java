package com.example.android.play_api;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.HomeRecycler>
{
    private List<item> itemList = new ArrayList<item>();

    private OnItemClickListener mListener;
    public RecyclerAdapter(List<item> items)
    {
        itemList = items;
    }


    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        mListener = onItemClickListener;
    }
    @Override
    public HomeRecycler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.list_item,viewGroup,false);
        return new HomeRecycler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecycler homeRecycler, int i) {

        //Do parsing here and set the texts over here...

        homeRecycler.month.setText(itemList.get(i).getMonth());
        homeRecycler.day.setText(itemList.get(i).getDay()+"");
        homeRecycler.title.setText(itemList.get(i).getTitle());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    class HomeRecycler extends RecyclerView.ViewHolder {

        public TextView month;
        public TextView day;
        public TextView title;
        public HomeRecycler(View itemView) {
            super(itemView);

            month = (TextView) itemView.findViewById(R.id.month);
            day = (TextView) itemView.findViewById(R.id.day);
            title = (TextView) itemView.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null)
                    {
                        int pos = getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION)
                        {
                            mListener.onItemClick(pos);
                        }
                    }
                }
            });

        }
    }
}
