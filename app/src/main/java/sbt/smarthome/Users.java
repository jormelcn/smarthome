package sbt.smarthome;

import android.app.ProgressDialog;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.LinkedList;
import java.util.List;


import sbt.smarthome.basequery.BaseUsers;

public class Users extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private List<String[]> mUsername = new LinkedList<String[]>();
    private ListView mlistusers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, Profile.class));
        }

        mlistusers = (ListView) findViewById(R.id.IDListUser);

        final ArrayAdapter<String[]> arrayAdapter = new ArrayAdapter<String[]>(this,android.R.layout.simple_list_item_2,android.R.id.text1, mUsername){
            public View getView(int position, View convertView, ViewGroup parent){

                View view = super.getView(position, convertView, parent);

                String[] entry = mUsername.get(position);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(entry[0]);
                text2.setText(entry[1]);

                return view;
            }
        };

        mlistusers.setAdapter(arrayAdapter);

        final DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = databaseUser.child("usuarios").child(firebaseAuth.getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BaseUsers admin = dataSnapshot.getValue(BaseUsers.class);

                FirebaseDatabase.getInstance().getReference().child("Casas").child(admin.getHome_admin()).
                        child("usuarios").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                        FirebaseDatabase.getInstance().getReference().child("usuarios").child(dataSnapshot.getKey())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot2) {
                                Boolean dta = dataSnapshot.getValue(Boolean.class);
                                if (!firebaseAuth.getCurrentUser().getUid().equals(dataSnapshot.getKey())){
                                    mUsername.add(new String[]{dataSnapshot2.getValue(BaseUsers.class).getNombre(), dta.toString() , dataSnapshot.getKey()});
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onChildChanged(final DataSnapshot dataSnapshot, String s) {
                        FirebaseDatabase.getInstance().getReference().child("usuarios").child(dataSnapshot.getKey())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot2) {
                                Boolean dta = dataSnapshot.getValue(Boolean.class);
                                for(int i=0;i< mUsername.size();i++) {
                                    String[] entry = mUsername.get(i);
                                    if(entry[2].equals(dataSnapshot.getKey())){
                                        mUsername.set(i,new String[]{dataSnapshot2.getValue(BaseUsers.class).getNombre(), dta.toString() , dataSnapshot.getKey()});
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mlistusers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> AdapterView, View view, int position, long id) {
                Intent intent = new Intent(Users.this, AdminUsers.class);
                intent.putExtra("parametro",""+mUsername.get(position)[2]);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

}
