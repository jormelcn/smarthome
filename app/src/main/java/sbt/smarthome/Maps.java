package sbt.smarthome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import sbt.smarthome.basequery.BaseHome;
import sbt.smarthome.basequery.BaseUsers;

public class Maps extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private List<String[]> mMapsname = new LinkedList<String[]>();
    private ListView mlisMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, Login.class));
        }

        mlisMaps = (ListView) findViewById(R.id.IDListMaps);

        final ArrayAdapter<String[]> arrayAdapter = new ArrayAdapter<String[]>(this,android.R.layout.simple_list_item_2,android.R.id.text1, mMapsname){
            public View getView(int position, View convertView, ViewGroup parent){

                View view = super.getView(position, convertView, parent);

                String[] entry = mMapsname.get(position);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(entry[0]);
                text2.setText(entry[1]);

                return view;
            }
        };

        mlisMaps.setAdapter(arrayAdapter);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                .child("usuarios").child(firebaseAuth.getCurrentUser().getUid());
        database.child("casas").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                FirebaseDatabase.getInstance().getReference().child("Casas").child(dataSnapshot.getKey())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshothome) {
                            Boolean dta = dataSnapshot.getValue(Boolean.class);
                            if(dataSnapshothome.getValue(BaseHome.class).getUsuarios().get(firebaseAuth.getCurrentUser().getUid())){
                                mMapsname.add(new String[]{dataSnapshothome.getValue(BaseHome.class).getNombre(), dta.toString() , dataSnapshot.getKey()});

                            }else{
                                for(int i= 0; i <mMapsname.size();i++) {
                                    if(mMapsname.get(i)[0].equals(dataSnapshothome.getValue(BaseHome.class).getNombre()) &&
                                    mMapsname.get(i)[1].equals(dta.toString()) && mMapsname.get(i)[2].equals(dataSnapshot.getKey())) {
                                        mMapsname.remove(i);
                                    }
                                }
                            }
                            arrayAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            }

            @Override
            public void onChildChanged(final DataSnapshot dataSnapshot, String s) {
                FirebaseDatabase.getInstance().getReference().child("Casas").child(dataSnapshot.getKey())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshothome) {
                                Boolean dta = dataSnapshot.getValue(Boolean.class);
                                for (int i = 0; i < mMapsname.size(); i++) {
                                    String[] entry = mMapsname.get(i);
                                    if (entry[2].equals(dataSnapshot.getKey())) {
                                        mMapsname.set(i, new String[]{dataSnapshothome.getValue(BaseHome.class).getNombre(), dta.toString(), dataSnapshot.getKey()});
                                    }
                                }
                                arrayAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mlisMaps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> AdapterView, View view, int position, long id) {
                if(!mMapsname.get(position)[1].equals("true")){
                    for (int j=0; j< mMapsname.size(); j++){
                        if(mMapsname.get(j)[1].equals("true")){
                            FirebaseDatabase.getInstance().getReference().child("usuarios").child(firebaseAuth.getCurrentUser().getUid())
                                    .child("casas").child(mMapsname.get(j)[2]).setValue(false);
                            break;
                        }
                    }
                    FirebaseDatabase.getInstance().getReference().child("usuarios").child(firebaseAuth.getCurrentUser().getUid())
                            .child("casas").child(mMapsname.get(position)[2]).setValue(true);
                }else {
                    Toast.makeText(Maps.this,"ESTA ACTIVADA",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
