package com.example.createloginapp_kim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class StudentMenu extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_menue);
    }

    /*@Override
    public void onReceive(Context context, Intent intent) {

        int notificationid = intent.getIntExtra("notificationid" ,0);
        String ms = intent.getStringExtra("todo");

        Intent mainIntent = new Intent(context,MainActivity.class);

        PendingIntent content = PendingIntent.getActivity(context,0,mainIntent, 0);

        NotificationManager mynotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);





        Notification.Builder bld = new Notification.Builder(context);
        bld.setSmallIcon(R.drawable.ic_launcher).
                setContentTitle("something happened!").
                setContentText(ms).
                setWhen(System.currentTimeMillis()).
                setAutoCancel(true).
                setContentIntent(content);
               // setPriority(Notification.PRIORITY_MAX).
               // setDefaults(Notification.DEFAULT_ALL);

        mynotifyManager.notify(notificationid, bld.build());*/

    }
//}
