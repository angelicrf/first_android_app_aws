package com.example.createloginapp_kim;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.amplify.generated.graphql.CreatePetMutation;
import com.amazonaws.amplify.generated.graphql.ListPetsQuery;
import com.amazonaws.amplify.generated.graphql.OnCreatePetSubscription;
import com.amazonaws.amplify.generated.graphql.UpdatePetMutation;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.AppSyncSubscriptionCall;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;

import okhttp3.OkHttpClient;
import type.CreatePetInput;
import type.UpdatePetInput;


public class MainActivity extends AppCompatActivity {

     EditText myUserName,myPassword;
     Button myButton;
     TextView myReminder;
     private int count = 3;
     boolean length_passwprd, length_userName;
     String userPassword, userId;
     private static final String BASE_URL = "https://bg3pwcmpbrg3hpbbjtcwlv3ezm.appsync-api.us-east-2.amazonaws.com/graphql";
     private AWSAppSyncClient awsAppSyncClient;
     private AppSyncSubscriptionCall subscriptionWatcher;
     private OkHttpClient okHttpClient;
     private ApolloClient apolloClient;
     private  static boolean isThere;
     private String t1,t2,t3,t4;
     NotificationCompat.Builder not;
     private  int notificationid = 4658;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        not = new NotificationCompat.Builder(this);
        not.setAutoCancel(true);

        okHttpClient = new OkHttpClient.Builder().build();
        apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build();

       AWSConfiguration awsConfig = new AWSConfiguration(getApplicationContext());
        awsAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(awsConfig)
                .build();


        myButton = findViewById  (R.id.BUT_1);
        myUserName = findViewById (R.id.edUser);
        myPassword =  findViewById (R.id.edPassword);
        myReminder = findViewById(R.id.edReminder);



        myButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
               /* HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(BASE_URL);
                //request.setURI(new URI("http://emapzoom.com/setting/device_login"));
                try {
                    HttpResponse response = client.execute(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                //validateUser(userId, userPassword);
                //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                //StrictMode.setThreadPolicy(policy);
                userId = myUserName.getText().toString();
                userPassword = myPassword.getText().toString();

                awsAppSyncClient.query(ListPetsQuery.builder().build())
                        .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK).
                        enqueue(queryCallback);
           }
        });
    }


    private GraphQLCall.Callback<ListPetsQuery.Data> queryCallback = new GraphQLCall.Callback<ListPetsQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListPetsQuery.Data> response) {

            Log.i("onResponse", response.data().listPets().items().toString());
            t1 = response.data().listPets().items().toString();

            for(int i =0; i< response.data().listPets().items().size(); i++) {

                isThere = response.data().listPets().items().get(i).name().equals(userId);
                if (isThere == true) {

                   MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myReminder.setText("The first name is : " + userId);

                            not.setSmallIcon(R.mipmap.ic_launcher_round).
                                    setContentTitle("something happened!").
                                    setContentText("You logged in!").
                                    setWhen(System.currentTimeMillis()).
                                    setAutoCancel(true);

                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            PendingIntent notify_me  = PendingIntent.getActivity(MainActivity.this, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            not.setContentIntent(notify_me);
                            NotificationManager mynotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            mynotifyManager.notify(notificationid, not.build());
                        }
                    });

                }else{
                   // mutation();
                  //  subscribe();
                }
            }
            }

        @Override
        public void onFailure(@Nonnull ApolloException e) {

            Log.e("Error", e.toString());
        }
    };
    public void mutation(){
        CreatePetInput input= CreatePetInput.builder().id("3251")
                .name(userId)
                .description("polish").build();

        awsAppSyncClient.mutate(CreatePetMutation.builder().input(input).build())
                .enqueue(mutateCallback);
    }

    private GraphQLCall.Callback<CreatePetMutation.Data> mutateCallback = new GraphQLCall.Callback<CreatePetMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<CreatePetMutation.Data> response) {
            Log.i("Results", response.data().createPet().name());
            t2 = response.data().createPet().name();
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myReminder.setText("The first name is : " + t2);

                }
            });

        }
        @Override
        public void onFailure(@Nonnull ApolloException e) {

            Log.e("ERROR", e.toString());
        }
    };

    public void updateItem(){
        UpdatePetInput input= UpdatePetInput.builder().id("6321")
                .name("hamster")
                .description("mountain").build();

        awsAppSyncClient.mutate(UpdatePetMutation.builder().input(input).build())
                .enqueue(updateCallback);
    }
    private GraphQLCall.Callback<UpdatePetMutation.Data> updateCallback = new GraphQLCall.Callback<UpdatePetMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<UpdatePetMutation.Data> response) {
            Log.i("Results", response.data().updatePet().name());
            t3 = response.data().updatePet().name();
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myReminder.setText("The first name is : " + t3);
                }
            });
        }
        @Override
        public void onFailure(@Nonnull ApolloException e) {

            Log.e("ERROR", e.toString());
        }
    };

    private void subscribe(){
        OnCreatePetSubscription subscription = OnCreatePetSubscription.builder().build();
        subscriptionWatcher = awsAppSyncClient.subscribe(subscription);
        subscriptionWatcher.execute(subCallback);
    }

    private AppSyncSubscriptionCall.Callback subCallback = new AppSyncSubscriptionCall.Callback<OnCreatePetSubscription.Data>() {
        @Override
        public void onResponse(@Nonnull Response<OnCreatePetSubscription.Data> response) {

            if (response == null  || response.data() == null
                    || response.data().onCreatePet()== null ) {
                Log.i("Response", "Delta was null!");
                return;
            }
            Log.i("Response", response.data().onCreatePet().toString());



            OnCreatePetSubscription.OnCreatePet data = response.data().onCreatePet();

            final ListPetsQuery.Item addedItem = new ListPetsQuery.Item(data.__typename(), data.id(), data.name(), data.description());

            MainActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //.add(addedItem);
                    //mAdapter.notifyItemInserted(mPets.size() - 1);
                    myReminder.setText("Your subscription is done. " +addedItem.name());
                }
            });
            MainActivity.this.notify();
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("Error", e.toString());
        }

        @Override
        public void onCompleted() {
            Log.i("Completed", "Subscription completed");


        }
    };
   private void validateUser(String userName, String passWord) {
       length_passwprd = myPassword.getText().length() > 0;
       length_userName = myUserName.getText().length() > 0;

       if (userName == "angelique" && passWord == "1234") {
           Intent intent = new Intent(MainActivity.this, StudentMenu.class);
           startActivity(intent);
       } else if ( length_passwprd == false ||length_userName ==false || userName != "angelique" && passWord != "1234" ) {
           count--;
           myReminder.setText("No Attempt Allowed" + String.valueOf(count));

           if (count == 0) {
               myButton.setEnabled(false);
           }
       }
   }

}
