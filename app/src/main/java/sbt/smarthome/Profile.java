package sbt.smarthome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sbt.smarthome.basequery.BaseUsers;

public class Profile extends AppCompatActivity implements View.OnClickListener{

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private Button buttonLogout, BtnUser, btnMaps ,btnInciar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, Login.class));
        }

        FirebaseUser email = firebaseAuth.getCurrentUser();


        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        BtnUser = (Button) findViewById(R.id.BtnUser);
        btnMaps = (Button) findViewById(R.id.btnMaps);
        btnInciar = (Button) findViewById(R.id.btnInciar);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = databaseUser.child("usuarios").child(email.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BaseUsers admin = dataSnapshot.getValue(BaseUsers.class);
                if(admin.getAdmin() == false){
                    BtnUser.setVisibility(View.GONE);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });


        buttonLogout.setOnClickListener(this);
        BtnUser.setOnClickListener(this);
        btnMaps.setOnClickListener(this);
        btnInciar.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view == buttonLogout){
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, Login.class));
        }

        if(view == BtnUser){
            //finish();
            startActivity(new Intent(this, Users.class));
        }

        if (view == btnMaps){
            startActivity(new Intent(this, Maps.class));
        }

        if(view == btnInciar){
            startActivity(new Intent(this, TestActivity.class));
        }

    }

}
