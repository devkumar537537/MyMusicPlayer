package com.myexample.mymusicplayer

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MusicService :Service() {
     val CHANNAL_ID ="channel1"
   private var myBinder=MyBinder()
    var mediaPlayer:MediaPlayer? = null
//private lateinit var mediaSession:MediaSessionCompat

    override fun onBind(intent: Intent?): IBinder? {
     return myBinder
    }
    inner class MyBinder:Binder(){
        fun currentService():MusicService{
            return this@MusicService
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
     //  mediaSession = MediaSessionCompat(baseContext,"My music")
        showNotification()
        return START_STICKY
    }
    fun showNotification(){


        val prevInt = Intent(baseContext,NotificationReceiver::class.java).setAction(Utils.PREV)
        val prevpendingIntent = PendingIntent.getBroadcast(baseContext,0,prevInt,PendingIntent.FLAG_UPDATE_CURRENT)

        val playInt = Intent(baseContext,NotificationReceiver::class.java).setAction(Utils.PLAY)
        val playpendingIntent = PendingIntent.getBroadcast(baseContext,1,playInt,PendingIntent.FLAG_UPDATE_CURRENT)

        val nextInt = Intent(baseContext,NotificationReceiver::class.java).setAction(Utils.NEXT)
        val nextpendingIntent = PendingIntent.getBroadcast(baseContext,2,nextInt,PendingIntent.FLAG_UPDATE_CURRENT)

        val exitInt = Intent(baseContext,NotificationReceiver::class.java).setAction(Utils.EXIT)
        val exitpendingIntent = PendingIntent.getBroadcast(baseContext,3,exitInt,PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.O)
        {
            val notificationChannel = NotificationChannel(CHANNAL_ID,"Now Playing Song",NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.description="This is important channel for playing song"
            notificationManager.createNotificationChannel(notificationChannel)
        }
  val notification = NotificationCompat.Builder(this,CHANNAL_ID)
      .setContentTitle(PlayerActivity.musicListPA[PlayerActivity.songPostion].title)
      .setContentText(PlayerActivity.musicListPA[PlayerActivity.songPostion].artist)
      .setSmallIcon(R.drawable.playlisticon)
      .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.splash_screen))
     // .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
      .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
      .setOnlyAlertOnce(true)
      .setDefaults(Notification.DEFAULT_ALL)
      .addAction(R.drawable.previous_icon,"Previous",prevpendingIntent)
      .addAction(R.drawable.play_icon,"Play",playpendingIntent)
      .addAction(R.drawable.next_icon,"Next",nextpendingIntent)
      .addAction(R.drawable.exit_icon,"Exit",exitpendingIntent)
      .build()
        startForeground(12,notification)

    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
    }
}