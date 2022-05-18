package com.example.test1;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.databinding.ActivityToDoListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ToDoList extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityToDoListBinding binding;
    private ArrayList<String> items = new ArrayList<>();
    private ArrayAdapter<String> itemsAdapter;
    private ListView listView;
    private Button button;
    private EditText et2;
    String value;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public static int parseInt(String s){
        int i=Integer.parseInt(s);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityToDoListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDbRef = mDatabase.getReference().child("Employee").child(uid).child("value");
        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        DAOEmployee dao = new DAOEmployee();
        Bundle arguments = getIntent().getExtras();
        String mail = arguments.get("mail").toString();
        String password = arguments.get("password").toString();
        listView = findViewById(R.id.listView);
        button = findViewById(R.id.button);
        et2 = findViewById(R.id.editText2);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Employee");
        DatabaseReference forVal = database.getReference().child("Employee").child(uid);
        forVal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    value = dataSnapshot.getValue().toString();
                    System.out.println(value);
                    String kukish = value.replace("}", " jg");
                    kukish = kukish.replace("value=", "thji ");
                    Pattern p = Pattern.compile("(?<=\\bthji\\b).*?(?=\\bjg\\b)");
                    Matcher m = p.matcher(kukish);

                    while (m.find()) {
                        if (m.group() != null) {
                            items.add(m.group());
                        }
                    }
                } else {
                    System.out.println("Todo is clear");
                }
                listView = findViewById(R.id.listView);
                itemsAdapter = new ArrayAdapter<>(ToDoList.this, android.R.layout.simple_list_item_1, items);
                listView.setAdapter(itemsAdapter);
                setUpListViewListener();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        button.setOnClickListener(v->
        {
            String key = FirebaseAuth.getInstance().getCurrentUser().getUid();

            System.out.println("ref.getKey()===="+ uid);
            EditText input = findViewById(R.id.editText2);
            String itemText = input.getText().toString();
            String spam = et2.getText().toString();
            Employee emp = new Employee(mail,password,spam);

            emp.setKey(FirebaseAuth.getInstance().getCurrentUser().getUid());

                if (!(itemText.equals("")) && (itemText != null)) {
                    itemsAdapter.add(spam);
                    setUpListViewListener();
                    input.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter text..", Toast.LENGTH_SHORT).show();
                }

            Employee employee = new Employee(mail,password,spam);
            ref.child(uid).child(ref.push().getKey()).setValue(employee);

        });
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Item Removed", Toast.LENGTH_LONG).show();

                itemsAdapter.notifyDataSetChanged();
                System.out.println(listView.getItemAtPosition(i));

                DatabaseReference ruff = FirebaseDatabase.getInstance().getReference().child("Employee");
               Query deletedElement = ruff.child(uid).orderByChild("value").equalTo(listView.getItemAtPosition(i).toString());
                deletedElement.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            child.getRef().setValue(null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                items.remove(i);
                return true;
            }
        });
    }
}