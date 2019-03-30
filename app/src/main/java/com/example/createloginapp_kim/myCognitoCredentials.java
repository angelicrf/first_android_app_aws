package com.example.createloginapp_kim;
import android.content.Context;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

public class myCognitoCredentials {

    CognitoCachingCredentialsProvider credentialsProvider = null;
    CognitoSyncManager syncManager = null;
    AmazonS3Client s3Client = null;
    TransferUtility transferUtility =null;


    public CognitoCachingCredentialsProvider getcredentials (Context context){
         credentialsProvider = new CognitoCachingCredentialsProvider(context,
           // getApplicationContext(), // Context
            "us-west-2:2951ad88-e3a3-4e1b-956f-42590104349b", // Identity pool ID
            Regions.US_WEST_2 //
         );
        syncManager = new CognitoSyncManager (context, Regions.US_WEST_2, credentialsProvider);
            Dataset dataset = syncManager.openOrCreateDataset("Mydataset");
               dataset.put("myKey","myValue");
               dataset.synchronize(new DefaultSyncCallback());
               return credentialsProvider;
        }
        public AmazonS3Client initializes3Client(Context context){
        if(credentialsProvider == null){
            getcredentials(context);
            s3Client = new AmazonS3Client(credentialsProvider);
            s3Client.setRegion(Region.getRegion(Regions.US_WEST_2));
        }

        return s3Client;
        }
        public TransferUtility checkTransferUtility(AmazonS3Client s3Client, Context context){
            if(transferUtility == null){
                 transferUtility = new TransferUtility(s3Client, context);
                 return transferUtility;
            }
            else {
                return transferUtility;
            }
        }
}
