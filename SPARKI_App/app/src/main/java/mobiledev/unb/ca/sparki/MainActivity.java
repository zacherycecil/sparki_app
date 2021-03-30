package mobiledev.unb.ca.sparki;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import mobiledev.unb.ca.sparki.model.ProfileInfo;
import mobiledev.unb.ca.sparki.util.CSVUtil;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference to the RecyclerView and configure it
        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        LoadDataTask loadDataTask = new LoadDataTask();
        loadDataTask.execute();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<ProfileInfo> dataset;

        public MyAdapter(ArrayList<ProfileInfo> myDataset) {
            dataset = myDataset;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView mTextView;

            public ViewHolder(LinearLayout v) {
                super(v);
                mTextView = v.findViewById(R.id.item_textview);
            }
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_layout, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.setIsRecyclable(false);

            final ProfileInfo profileInfo = dataset.get(position);

            holder.mTextView.setText(profileInfo.getProfileName());

            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(view.getContext(), DetailActivity.class);
                    myIntent.putExtra("name", profileInfo.getProfileName());
                    myIntent.putExtra("mode", profileInfo.getProfileMode());
                    myIntent.putExtra("e1", profileInfo.getElectrodeString(0));
                    myIntent.putExtra("e2", profileInfo.getElectrodeString(1));
                    myIntent.putExtra("e3", profileInfo.getElectrodeString(2));
                    myIntent.putExtra("e4", profileInfo.getElectrodeString(3));
                    MainActivity.this.startActivity(myIntent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataset.size();
        }
    }

    private class LoadDataTask extends AsyncTask<Void, Void, ArrayList<ProfileInfo>> {

        protected ArrayList<ProfileInfo> doInBackground(Void... params) {
            ProfileInfo[] profileData = CSVUtil.getProfileData(getApplicationContext());
            ArrayList<ProfileInfo> profileDataList = new ArrayList<>();
            for(int i=0; i < profileData.length; i++)
            {
                if (profileData[i] != null)
                    profileDataList.add(profileData[i]);
            }
            Log.v("hhhhhhhhhhhhh", profileDataList.toString());
            return profileDataList;
        }

        protected void onPostExecute(ArrayList<ProfileInfo> result) {
            MyAdapter adapter = new MyAdapter(result);
            recyclerView.setAdapter(adapter);
        }
    }
}
