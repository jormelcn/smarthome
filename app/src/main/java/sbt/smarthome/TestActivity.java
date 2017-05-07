package sbt.smarthome;

import android.app.Activity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;

import sbt.smarthome.rooms.House;
import sbt.smarthome.rooms.core.HouseServer;
import sbt.smarthome.rooms.core.HouseView;
import sbt.smarthome.rooms.core.RoomParser;

public class TestActivity extends Activity{

    HouseView houseView;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseAuth firebaseAuth;


    private void paintHouse(InputStream houseMap){
        RoomParser parser = new RoomParser();
        houseView = (HouseView) findViewById(R.id.house_view);
        House house = (House) parser.parse(houseMap);
        HouseServer houseServer = new HouseServer();
        houseView.connectToServer(houseServer);
        houseView.paintHouse(house);
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
        /*
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
        });*/

    }

}
