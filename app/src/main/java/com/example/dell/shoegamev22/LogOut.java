package com.example.dell.shoegamev22;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dell.shoegamev22.viewmodels.LogInActivityViewModel;
import com.example.dell.shoegamev22.viewmodels.LogOutViewModel;
import com.example.dell.shoegamev22.viewmodels.SignUpActivityViewModel;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.novoda.merlin.MerlinsBeard;

public class LogOut extends AppCompatActivity {


    //VIEWS
    LottieAnimationView animationView;
    Button logOutConfirmButton;


    //view model
    LogOutViewModel logOutViewModel;


    //merlin
    MerlinsBeard merlinsBeard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);


        //initialize the views
        animationView = findViewById(R.id.activityLogOutLoadingView);
        logOutConfirmButton = findViewById(R.id.activityLogOutSubmitButton);

        //create merlin. library used to monitor internet connectivity
        merlinsBeard = MerlinsBeard.from(this);

        logOutViewModel = ViewModelProviders.of(this).get(LogOutViewModel.class);
        logOutViewModel.init();


        //Add observers
        logOutViewModel.getLogUserOutResult().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {


                if (aBoolean) {


                    new iOSDialogBuilder(LogOut.this)
                            .setTitle("Success!")
                            .setSubtitle("You have successfully logged out")
                            .setBoldPositiveLabel(true)
                            .setCancelable(false)
                            .setPositiveListener("okay", new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {


                                    dialog.dismiss();
                                    Intent i = new Intent(LogOut.this, MainActivity.class);
                                    // set the new task and clear flags
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);



                                }
                            })
                            .build().show();


                } else {


                    new iOSDialogBuilder(LogOut.this)
                            .setTitle("Error!")
                            .setSubtitle("There has been an error trying to log you out. Try again?")
                            .setBoldPositiveLabel(true)
                            .setCancelable(false)
                            .setPositiveListener("okay", new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {


                                    logOutViewModel.logUserOut();

                                    dialog.dismiss();


                                }
                            })
                            .build().show();


                }


            }
        });


        logOutConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (merlinsBeard.isConnected()) {


                    logOutViewModel.logUserOut();


                }


            }
        });


    }
}




/*Intent intent = new Intent(this, Activity.class);
intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
startActivity(intent);

Intent i = new Intent(OldActivity.this, NewActivity.class);
// set the new task and clear flags
i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
startActivity(i);





*/