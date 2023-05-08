package com.example.pushnotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import android.widget.RemoteViews
import android.widget.RemoteViews.RemoteView
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage


const val channelId = "notofication_id"
const val channelName = "com.example.pushnotification"


class MyFirebaseMessagingService : FirebaseMessagingService() {

    //generate the notification
    //attach the notification create with custom layout
    //show the notification


    override fun onMessageReceived(message: RemoteMessage) {
        if(message.getNotification() != null){
            generateNotification(message.notification!!.title!!, message.notification!!.body!!)
        }
    }

    private fun getRemoteView(title: String, message: String): RemoteViews? {
        val remoteViews = RemoteViews("com.example.pushnotification",R.layout.notification)

        remoteViews.setTextViewText(R.id.title, title)
        remoteViews.setTextViewText(R.id.message, message)
        remoteViews.setImageViewResource(R.id.image,R.drawable.college)

        return remoteViews
    }



    fun generateNotification(title: String, message : String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT)

        //channel id, channel name

        var builder:NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.college)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title,message))

        val notificationManager  = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notoficationchannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notoficationchannel)
        }

        notificationManager.notify(0,builder.build())
    }


}