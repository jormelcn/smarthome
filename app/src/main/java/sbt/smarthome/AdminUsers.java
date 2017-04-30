package sbt.smarthome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sbt.smarthome.basequery.BaseUsers;

public class AdminUsers extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private TextView textViewNameUser;
    private Switch switchActUser;
    private BaseUsers admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, Login.class));
        }

        textViewNameUser = (TextView) findViewById(R.id.textViewNameUser);
        switchActUser = (Switch) findViewById(R.id.switchActUser);

        final DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = databaseUser.child("usuarios").child(getIntent().getExtras().getString("parametro"));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textViewNameUser.setText(dataSnapshot.getValue(BaseUsers.class).getNombre());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DatabaseReference databaseUser2 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2 = databaseUser.child("usuarios").child(firebaseAuth.getCurrentUser().getUid());
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final BaseUsers admin = dataSnapshot.getValue(BaseUsers.class);
                FirebaseDatabase.getInstance().getReference().child("Casas").child(admin.getHome_admin()).
                child("usuarios").child(getIntent().getExtras().getString("parametro"))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        switchActUser.setChecked(dataSnapshot.getValue(boolean.class));
                        switchActUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                FirebaseDatabase.getInstance().getReference().child("Casas")
                                .child(admin.getHome_admin()).child("usuarios")
                                .child(getIntent().getExtras().getString("parametro")).setValue(isChecked);
                                if(!isChecked){
                                    FirebaseDatabase.getInstance().getReference().child("usuarios")
                                    .child(getIntent().getExtras().getString("parametro")).child("casas")
                                    .child(admin.getHome_admin()).setValue(false);
                                }

                            }
                        });
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



    }
}
