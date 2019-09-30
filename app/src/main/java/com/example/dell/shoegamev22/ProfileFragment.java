package com.example.dell.shoegamev22;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.backendless.BackendlessUser;
import com.example.dell.shoegamev22.viewmodels.ProfileActivityViewModel;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.novoda.merlin.MerlinsBeard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import br.vince.easysave.EasySave;
import br.vince.easysave.LoadAsyncCallback;


public class ProfileFragment extends Fragment {

    private View view;


    private TextView helloUserTextView;
    // private TextView signUpTextView, logInTextView, cartTV, faqTV, trackingTV, ordersTv, savedTv, editProfileTv, supportTv, aboutTv;
    private ConstraintLayout signUpLayout, logInLayout, cartLayout, faqLayout, trackingLayout, ordersLayout, savedLayout, editProfileLayout, supportLayout, aboutLayout;
    private ConstraintLayout logOutLayout, deleteAccountLayout, paymentMethodsLayout;
    private TextView helpHeaderTV, accountHeaderTV, personalHeaderTV;
    private LottieAnimationView loadingView;


    //view models
    ProfileActivityViewModel mProfileActivityViewModel;


    //Current User
    private BackendlessUser currentGlobalUser = new BackendlessUser();

    //merlin
    private MerlinsBeard merlinsBeard;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);


        //initialize the views
        loadingView = view.findViewById(R.id.profileFragmentAnimationView);
        personalHeaderTV = view.findViewById(R.id.profileFragmentPersonalHeaderTV);
        helpHeaderTV = view.findViewById(R.id.profileFragmentHelpHeaderTV);
        accountHeaderTV = view.findViewById(R.id.profileFragmentAccountHeaderTV);
        helloUserTextView = view.findViewById(R.id.profileFragmentHelloUserTV);
        cartLayout = view.findViewById(R.id.profileFragmentCartLayout);
        trackingLayout = view.findViewById(R.id.profileFragmentTrackingLayout);
        ordersLayout = view.findViewById(R.id.profileFragmentOrdersLayout);
        savedLayout = view.findViewById(R.id.profileFragmentSavedLayout);
        editProfileLayout = view.findViewById(R.id.profileFragmentEditProfileLayout);
        paymentMethodsLayout = view.findViewById(R.id.profileFragmentPaymentMethodsLayout);
        supportLayout = view.findViewById(R.id.profileFragmentSupportLayout);
        aboutLayout = view.findViewById(R.id.profileFragmentAboutAppLayout);
        faqLayout = view.findViewById(R.id.profileFragmentFaqLayout);
        signUpLayout = view.findViewById(R.id.profileFragmentSignUpLayout);
        logInLayout = view.findViewById(R.id.profileFragmentLogInLayout);
        logOutLayout = view.findViewById(R.id.profileFragmentLogOutLayout);
        deleteAccountLayout = view.findViewById(R.id.profileFragmentDeleteAccountLayout);


        //create merlin. library used to monitor internet connectivity
        merlinsBeard = MerlinsBeard.from(getActivity());

        //initialize view models
        mProfileActivityViewModel = ViewModelProviders.of(this).get(ProfileActivityViewModel.class);
        mProfileActivityViewModel.init();


        logInLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LogIn.class);
                startActivity(intent);
            }
        });
        signUpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignUp.class);
                startActivity(intent);
            }
        });


        logOutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logOutButtonClicked();

            }
        });


        if (merlinsBeard.isConnected()){


            checkLoginValidity();




        }else{


            displayViews(false,null);


        }









        return view;
    }




    //todo where you stopped
    private void logUserOutonChanged(){


        mProfileActivityViewModel.getLogUserOutResult().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){

                    //todo successful logout

                    new iOSDialogBuilder(getActivity())
                            .setTitle("Success!")
                            .setSubtitle("You have successfully logged out")
                            .setBoldPositiveLabel(true)
                            .setCancelable(false)
                            .setPositiveListener("okay", new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {

                                }
                            })
                            .build().show();





                }else{


                    //todo unsaceesfull logout


                }


            }
        });







        /*Intent i = new Intent(MainActivity.this, MainActivity.class);
finish();
overridePendingTransition(0, 0);
startActivity(i);
overridePendingTransition(0, 0);*/







    }




    private void logOutButtonClicked(){

        Intent intent = new Intent(getActivity(), LogOut.class);
        startActivity(intent);

    }


    private void checkLoginValidity() {
        mProfileActivityViewModel.checkIfUserLoginIsValid();

        mProfileActivityViewModel.getIsValidLoginCheckResult().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {


                if (aBoolean) {

                    mProfileActivityViewModel.getIsValidLoginCheckResponse().observe(getActivity(), new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {

                            if (aBoolean) {

                                //todo login session is valid
                                retrieveUserFromCache(true);

                            } else {

                                retrieveUserFromCache(false);
                                //todo login session is not valid

                            }

                        }
                    });


                } else {

                    //todo check has returned with an error

                }


            }
        });


    }


    private void retrieveUserFromCache(final Boolean isLoginValid) {



        new EasySave(getActivity()).retrieveModelAsync("current signed in user", BackendlessUser.class, new LoadAsyncCallback<BackendlessUser>() {
            @Override
            public void onComplete(BackendlessUser user) {

                if (user != null) {


                    displayViews(isLoginValid,user);
                    Log.d("MyLogsProfFrag", "User retrieved successfully from Cache. Response :" + user.toString());


                } else {

                    retrieveUserFromServer(isLoginValid);
                    Log.d("MyLogsProfFrag", "User retrieved successfully from Cache. response may be null . Response : NULL");


                }


            }

            @Override
            public void onError(String s) {

                retrieveUserFromServer(isLoginValid);
                Log.d("MyLogsProfFrag", "User NOT retrieved from Cache. Response :" + s);


            }
        });


    }


    private void displayViews(Boolean isValidSession, BackendlessUser user) {


        loadingView.cancelAnimation();
        loadingView.setVisibility(View.GONE);



        if(isValidSession && user!=null){

            String name = "Hello "+ (String) user.getProperty("last_name");

            helloUserTextView.setText(name);
            helloUserTextView.setVisibility(View.VISIBLE);

            personalHeaderTV.setVisibility(View.VISIBLE);
            accountHeaderTV.setVisibility(View.VISIBLE);
            helpHeaderTV.setVisibility(View.VISIBLE);
            aboutLayout.setVisibility(View.VISIBLE);
            supportLayout.setVisibility(View.VISIBLE);
            paymentMethodsLayout.setVisibility(View.VISIBLE);
            editProfileLayout.setVisibility(View.VISIBLE);
            savedLayout.setVisibility(View.VISIBLE);
            ordersLayout.setVisibility(View.VISIBLE);
            trackingLayout.setVisibility(View.VISIBLE);
            cartLayout .setVisibility(View.VISIBLE);
            deleteAccountLayout .setVisibility(View.VISIBLE);
            faqLayout.setVisibility(View.VISIBLE);
            logOutLayout.setVisibility(View.VISIBLE);
            deleteAccountLayout.setVisibility(View.VISIBLE);


            logInLayout.setVisibility(View.GONE);
            signUpLayout.setVisibility(View.GONE);






        }else if(isValidSession){

            helloUserTextView.setText("Hello");
            helloUserTextView.setVisibility(View.VISIBLE);
            accountHeaderTV.setVisibility(View.VISIBLE);
            helpHeaderTV.setVisibility(View.VISIBLE);
            aboutLayout.setVisibility(View.VISIBLE);
            supportLayout.setVisibility(View.VISIBLE);
            faqLayout.setVisibility(View.VISIBLE);
            logInLayout.setVisibility(View.VISIBLE);
            signUpLayout.setVisibility(View.VISIBLE);

            personalHeaderTV.setVisibility(View.GONE);
            paymentMethodsLayout.setVisibility(View.GONE);
            editProfileLayout.setVisibility(View.GONE);
            savedLayout.setVisibility(View.GONE);
            ordersLayout.setVisibility(View.GONE);
            trackingLayout.setVisibility(View.GONE);
            cartLayout .setVisibility(View.GONE);
            deleteAccountLayout .setVisibility(View.GONE);
            logOutLayout.setVisibility(View.GONE);
            deleteAccountLayout.setVisibility(View.GONE);




        }else {


            helloUserTextView.setText("Hello");
            helloUserTextView.setVisibility(View.VISIBLE);
            accountHeaderTV.setVisibility(View.VISIBLE);
            helpHeaderTV.setVisibility(View.VISIBLE);
            aboutLayout.setVisibility(View.VISIBLE);
            supportLayout.setVisibility(View.VISIBLE);
            faqLayout.setVisibility(View.VISIBLE);
            logInLayout.setVisibility(View.VISIBLE);
            signUpLayout.setVisibility(View.VISIBLE);

            personalHeaderTV.setVisibility(View.GONE);
            paymentMethodsLayout.setVisibility(View.GONE);
            editProfileLayout.setVisibility(View.GONE);
            savedLayout.setVisibility(View.GONE);
            ordersLayout.setVisibility(View.GONE);
            trackingLayout.setVisibility(View.GONE);
            cartLayout .setVisibility(View.GONE);
            deleteAccountLayout .setVisibility(View.GONE);
            logOutLayout.setVisibility(View.GONE);
            deleteAccountLayout.setVisibility(View.GONE);




        }






    }


    private void retrieveUserFromServer(final Boolean isValidSession){


        mProfileActivityViewModel.retrieveCurrentUserFromTheInternet();
        mProfileActivityViewModel.getRetrieveCurrentUserFromTheInternetResult().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){

                    //todo user retieval success

                    mProfileActivityViewModel.getRetrieveCurrentUserFromTheInternetResponse().observe(getActivity(), new Observer<BackendlessUser>() {
                        @Override
                        public void onChanged(BackendlessUser user) {


                            if (user!= null){

                                Log.d("MyLogsProfFrag", "User retrieved successfully from Internet. Response :" + user.toString());

                                displayViews(isValidSession,user);

                            }else{


                                Log.d("MyLogsProfFrag", "User retrieved successfully from Internet. response may be null . Response : NULL");

                                displayViews(false,null);


                            }


                        }
                    });


                }else {


                    //todo user retrieval failed. do something


                    displayViews(false,null);
                    Log.d("MyLogsProfFrag", "User NOT retrieved from Internet.");




                }



            }
        });








    }


}
