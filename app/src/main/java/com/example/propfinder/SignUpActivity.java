package com.example.propfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.propfinder.Firebase.FirebaseDB;
import com.example.propfinder.Firebase.FirebaseSignIn;
import com.example.propfinder.Firebase.FirebaseSignUp;
import com.example.propfinder.businessLogic.Agent;
import com.example.propfinder.businessLogic.Buyer;
import com.example.propfinder.businessLogic.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private String[] userTypes = {"Buyer", "Agent"};
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItems;
    private String userType;
    private Button signUp;
    private EditText name, email, phone, password, cPass, license, agencyName, commissionRate;
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.fullname_txt);
        email = findViewById(R.id.email_txt);
        phone = findViewById(R.id.phone_txt);
        password = findViewById(R.id.password_txt);
        cPass = findViewById(R.id.c_pass_txt);
        license = findViewById(R.id.license_txt);
        agencyName = findViewById(R.id.agency_txt);
        commissionRate = findViewById(R.id.commision_txt);
        signUp = findViewById(R.id.signUp_btn);

        userType = null;

        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, userTypes);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userType = parent.getItemAtPosition(position).toString();
                onAgentSelection();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_txt = email.getText().toString().trim();
                String name_txt = name.getText().toString().trim();
                String phone_txt = phone.getText().toString().trim();
                String password_txt = password.getText().toString().trim();
                String cPass_txt = cPass.getText().toString().trim();
                String license_txt = license.getText().toString().trim();
                String agency_txt = agencyName.getText().toString().trim();
                String commission_txt = commissionRate.getText().toString().trim();


                if (!email_txt.isEmpty() && !name_txt.isEmpty() &&
                        !phone_txt.isEmpty() && !password_txt.isEmpty()) {
                    if (!passwordVerification(password_txt, cPass_txt)) {
                        Toast.makeText(getApplicationContext(), "Confirm Password not Correct! ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (userType != null) {
                            if (userType.equals("Buyer")) {
                                Buyer buyer = createBuyer(name_txt, email_txt, phone_txt);
                                userSignUp(buyer, password_txt);
                                FirebaseSignIn.signIn(FirebaseSignIn.getAuth(), buyer.getE_mail(), password_txt);
                                user = buyer;
                            } else if (userType.equals("Agent")) {
                                if (!license_txt.isEmpty() && !agency_txt.isEmpty() && !commission_txt.isEmpty()) {
                                    Agent agent = createAgent(name_txt, email_txt, phone_txt, license_txt, agency_txt, Double.parseDouble(commission_txt));
                                    userSignUp(agent, password_txt);
                                    FirebaseSignIn.signIn(FirebaseSignIn.getAuth(), agent.getE_mail(), password_txt);
                                    user = agent;
                                } else {
                                    Toast.makeText(getApplicationContext(), "Enter all Credentials for Agent! ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Select User Type! ", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter All Credentials! ", Toast.LENGTH_SHORT).show();
                }
                if (user != null){
                    FirebaseDB.addUserToDB(FirebaseSignUp.getAuth().getCurrentUser(), user);
                    Toast.makeText(getApplicationContext(), "User Signed Up Successfully!!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Error Signing up!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void onAgentSelection(){
        if (userType.equals("Agent")){
            license.setVisibility(View.VISIBLE);
            agencyName.setVisibility(View.VISIBLE);
            commissionRate.setVisibility(View.VISIBLE);
        } else {
            license.setVisibility(View.GONE);
            agencyName.setVisibility(View.GONE);
            commissionRate.setVisibility(View.GONE);
        }
    }
    private Buyer createBuyer(String name, String email, String phone){
        return new Buyer(name, email, phone);
    }
    private Agent createAgent(String fullName, String e_mail, String phoneNumber, String licenseNumber, String agencyName, double commissionRate){
        return new Agent(fullName, e_mail, phoneNumber, licenseNumber, agencyName, commissionRate);
    }
    private void userSignUp(User user, String password){
        FirebaseSignUp.createUser(user.getE_mail(), password);
    }
    private boolean passwordVerification(String pass, String cPass){
        return pass.equals(cPass);
    }
}