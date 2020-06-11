package com.telespecialists.telecare.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.telespecialists.telecare.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*


class CloudMessaging : FirebaseMessagingService() {
    private val CHANNEL_ID: String = "id_channel"
    private val CHANNEL_NAME: String = "name_channel"
    private val CHANNEL_DESCRIPTION: String = "description_channel"
    private var notificationManager: NotificationManager? = null

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e("remote", remoteMessage.toString())
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager)
        }
        notification()

    }

    private fun notification() {
        val defaultSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationID = Random().nextInt(3000)
        val largeIcon = BitmapFactory.decodeResource(
            resources,
            R.mipmap.ic_launcher
        )
        val bigView = RemoteViews(packageName, R.layout.custom_notification_large)
        val smallView = RemoteViews(packageName, R.layout.custom_notification_small)

        val notificationComp = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setLargeIcon(largeIcon)
            .setAutoCancel(false)
            .setSound(defaultSound)
            .setWhen(System.currentTimeMillis())
            .setCustomBigContentView(bigView).setCustomContentView(smallView)

        notificationManager!!.notify(notificationID, notificationComp.build())
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels(
        notificationManager: NotificationManager?
    ) {

        val adminChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        adminChannel.description = CHANNEL_DESCRIPTION
        adminChannel.enableLights(true)
        adminChannel.enableVibration(true)
        adminChannel.lightColor = Color.GREEN
        adminChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        try {
            notificationManager?.createNotificationChannel(adminChannel)
        } catch (e: Exception) {
        }
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.e("token", s)
    }
}