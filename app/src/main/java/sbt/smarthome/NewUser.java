package sbt.smarthome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sbt.smarthome.basequery.BaseHome;
import sbt.smarthome.basequery.BaseUsers;

public class NewUser extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextEmail,editTextPassword,editTextName,editTextNameHome;
    private Button buttonSignup;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private ArrayList<BaseHome> maps = new ArrayList<>();
    private ArrayList<String> UIDS = new ArrayList<>();
    private int position=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_new_user);

        progressDialog.setMessage("Loading");
        progressDialog.show();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Casas");
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                BaseHome post = dataSnapshot.getValue(BaseHome.class);
                UIDS.add(dataSnapshot.getKey());
                maps.add(post);
                progressDialog.dismiss();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){

            finish();

            startActivity(new Intent(getApplicationContext(), Profile.class));
        }

        editTextNameHome = (EditText) findViewById(R.id.editTextNameHome);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        buttonSignup = (Button) findViewById(R.id.buttonSignup);

        buttonSignup.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }

    private void registerUser(){
        final Map<String, Boolean> map = new HashMap<>();
        final Map<String, Boolean> maphome = new HashMap<>();
        String Home = editTextNameHome.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();


        if(TextUtils.isEmpty(Home)){
            Toast.makeText(this,"Please enter home",Toast.LENGTH_LONG).show();
            return;
        }else {
            if(!fansmaps(Home)){
                Toast.makeText(this,"There is no home",Toast.LENGTH_LONG).show();
                return;
            }
        }

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please enter name",Toast.LENGTH_LONG).show();
            return;
        }

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        map.put(Home,false);


        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference();
                    databaseUser.child("usuarios").child(firebaseAuth.getCurrentUser().getUid()).setValue(new BaseUsers(name, false, "false", map));

                    DatabaseReference databaseHome = FirebaseDatabase.getInstance().getReference();
                    databaseHome.child("Casas").child(UIDS.get(position).toString()).child("usuarios").child(firebaseAuth.getCurrentUser().getUid()).setValue(false);

                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                }else{
                    Toast.makeText(NewUser.this,"Registration Error",Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View view) {

        if(view == buttonSignup){
            registerUser();
        }

        if(view == textViewSignin){
            Log.d("========",maps.get(0).getNombre());
            startActivity(new Intent(this, Login.class));
        }

    }

    public boolean fansmaps(String name){
        Boolean rs=false;

        for(int i=0; i<maps.size();i++) {
            if(name.equals(maps.get(i).getNombre())){
                rs=true;
                position=i;
                break;
            }
        }

        return rs;
    }

}
