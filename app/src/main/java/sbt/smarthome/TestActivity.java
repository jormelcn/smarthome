package sbt.smarthome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import sbt.smarthome.basequery.BaseHome;
import sbt.smarthome.rooms.core.RoomParent;
import sbt.smarthome.rooms.core.RoomParser;

public class TestActivity extends Activity{

    PaperView paperView;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseAuth firebaseAuth;


    private void paintHouse(InputStream houseMap){
        RoomParser parser = new RoomParser();
        paperView = (PaperView) findViewById(R.id.paper_view);
        RoomParent mainRoom = parser.parse(houseMap);
        paperView.setMainRoom(mainRoom);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        try {
            paintHouse(getAssets().open("house_test.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = databaseUser.child("usuarios").child(firebaseAuth.getCurrentUser().getUid());
        ref.child("casas").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(dataSnapshot.getValue(Boolean.class)) {
                    FirebaseDatabase.getInstance().getReference().child("Casas").child(dataSnapshot.getKey())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Log.d("===========",""+dataSnapshot.getValue(BaseHome.class).getNombre());
                            storageRef = storage.getReferenceFromUrl("gs://smarthome-8f6be.appspot.com").child(dataSnapshot.getValue(BaseHome.class).getNombre()+".xml");
                            try {
                                final File localFile = File.createTempFile(dataSnapshot.getValue(BaseHome.class).getNombre()+"","xml");
                                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        try {
                                            File initialFile = new File(localFile.getAbsolutePath());
                                            InputStream targetStream = new FileInputStream(initialFile);
                                            paintHouse(targetStream);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                            }catch (Exception e){
                                if(firebaseAuth.getCurrentUser() == null){
                                    finish();
                                    startActivity(new Intent(TestActivity.this, Login.class));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
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

    }

}
