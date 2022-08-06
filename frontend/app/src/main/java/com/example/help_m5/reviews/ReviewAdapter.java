package com.example.help_m5.reviews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.help_m5.R;
import com.example.help_m5.ReportActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    private static final String TAG = "ReviewAdapter";
    private Context context;
    private Activity activity;
    private List<ReviewItem> reviewItems;

    public ReviewAdapter(Context context, Activity activity, List<ReviewItem> reviewItems) {
        this.context = context;
        this.activity = activity;
        this.reviewItems = reviewItems;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_adapter_layout, parent, false);
        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    /*
        Show data onto recyclerview
        Called after OnCreateViewHolder to bind the views
     */
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        ReviewItem reviewItem = reviewItems.get(position);

        int getUpVoteCount = reviewItem.getVoteCounts().get(0);
        int getDownVoteCount = reviewItem.getVoteCounts().get(1);
        holder.upVoteCountView.setText(String.valueOf(getUpVoteCount));
        holder.downVoteCountView.setText(String.valueOf(getDownVoteCount));

        String getTitle = reviewItem.getFacilityInformation().get(0);
        boolean checkedUp = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("upvote"+getTitle+String.valueOf(position), false);
        if (checkedUp) {
            holder.upVoteView.setChecked(checkedUp);
        }

        boolean checkedDown = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("downvote"+getTitle+String.valueOf(position), false);
        if (checkedDown) {
            holder.downVoteView.setChecked(checkedDown);
        }

        int getFacilityId = Integer.parseInt(reviewItem.getFacilityInformation().get(1));
        int getFacilityType = Integer.parseInt(reviewItem.getFacilityInformation().get(2));
        boolean isPost = (reviewItem.getFacilityInformation().get(3).equals("true"));

        holder.userNameView.setText(reviewItem.getUserName());
        holder.userDateView.setText(reviewItem.getUserDate());
        if (isPost) {
            holder.userRateView.setVisibility(View.GONE);
        } else {
            holder.userRateView.setVisibility(View.VISIBLE);
            holder.userRateView.setRating((float) reviewItem.getUserRate());
        }
        holder.userDescriptionView.setText(reviewItem.getUserDescription());


        holder.upVoteView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AdjustVote(context, String.valueOf(getFacilityType), String.valueOf(getFacilityId), reviewItem.getReportedUserEmail(), "up", "pend");
                holder.upVoteCountView.setText(String.valueOf(Integer.parseInt(holder.upVoteCountView.getText().toString()) + 1));
                PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putBoolean("upvote"+getTitle+String.valueOf(position), true).apply();
                if (holder.downVoteView.isChecked()) {
                    holder.downVoteView.setChecked(false);
                }
            } else {
                AdjustVote(context, String.valueOf(getFacilityType), String.valueOf(getFacilityId), reviewItem.getReportedUserEmail(), "up", "cancel");
                holder.upVoteCountView.setText(String.valueOf(Integer.parseInt(holder.upVoteCountView.getText().toString()) - 1));
                PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putBoolean("upvote"+getTitle+String.valueOf(position), false).apply();
            }
        });

        holder.downVoteView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AdjustVote(context, String.valueOf(getFacilityType), String.valueOf(getFacilityId), reviewItem.getReportedUserEmail(), "down", "pend");
                holder.downVoteCountView.setText(String.valueOf(Integer.parseInt(holder.downVoteCountView.getText().toString()) + 1));
                PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putBoolean("downvote"+getTitle+String.valueOf(position), true).apply();
                if (holder.upVoteView.isChecked()) {
                    holder.upVoteView.setChecked(false);
                }
            } else {
                AdjustVote(context, String.valueOf(getFacilityType), String.valueOf(getFacilityId), reviewItem.getReportedUserEmail(), "down", "cancel");
                holder.downVoteCountView.setText(String.valueOf(Integer.parseInt(holder.downVoteCountView.getText().toString()) - 1));
                PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putBoolean("downvote"+getTitle+String.valueOf(position), false).apply();
            }
        });

        holder.reportCommentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent reportIntent = new Intent(context, ReportActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", getTitle);
                bundle.putString("user_email", reviewItem.getUserEmail());
                bundle.putInt("facility_id", getFacilityId);
                bundle.putInt("facility_type", getFacilityType);
                bundle.putString("report_type", "5"); //5 means report comment
                bundle.putString("reportedUserId", reviewItem.getReportedUserEmail());
                reportIntent.putExtras(bundle);
                reportIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(reportIntent);
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userNameView;
        public TextView userDateView;
        public RatingBar userRateView;
        public TextView userDescriptionView;
        public CheckBox upVoteView;
        public CheckBox downVoteView;
        public TextView upVoteCountView;
        public TextView downVoteCountView;
        public Button reportCommentButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameView = (TextView) itemView.findViewById(R.id.userNameView);
            userDateView = (TextView) itemView.findViewById(R.id.userDateView);
            userRateView = (RatingBar) itemView.findViewById(R.id.userRateView);
            userDescriptionView = (TextView) itemView.findViewById(R.id.userDescriptionView);
            upVoteView = (CheckBox) itemView.findViewById(R.id.upVote);
            downVoteView = (CheckBox) itemView.findViewById(R.id.downVote);
            upVoteCountView = (TextView) itemView.findViewById(R.id.upVoteCount);
            downVoteCountView = (TextView) itemView.findViewById(R.id.downVoteCount);
            reportCommentButton = (Button) itemView.findViewById(R.id.reportCommentButton);
        }
    }

    private void AdjustVote(Context context, String facilityType, String facilityId, String userId, String vote, String isCancelled) {
        String url = "http://20.213.243.141:8080/Votes";
        RequestQueue queue = Volley.newRequestQueue(context);
        HashMap<String, String> params = new HashMap<String, String>();
        queue.start();
        params.put("facilityType", facilityType);
        params.put("facility_id", facilityId);
        params.put("user_id", userId);  // reported user id
        params.put("vote", vote);
        params.put("isCancelled", isCancelled);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG,"response is: "+response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse" + "Error: " + error.getMessage());
                    }
                });
        queue.add(request);
    }
}