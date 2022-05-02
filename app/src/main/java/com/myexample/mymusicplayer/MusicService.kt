package com.myexample.mymusicplayer

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.*
import android.support.v4.media.session.MediaSessionCompat
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MusicService :Service() {
     val CHANNAL_ID ="channel1"
   private var myBinder=MyBinder()
    var mediaPlayer:MediaPlayer? = null
//private lateinit var mediaSession:MediaSessionCompat
private lateinit var  runnable :Runnable
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
        showNotification(false)
        return START_STICKY
    }

    fun showNotification(playPauseButton:Boolean){

var playpauetext = "some"
        if(playPauseButton)
        {
            playpauetext="PLay"
        }else
        {
            playpauetext="Pause"
        }
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

    var remoteViews = RemoteViews(packageName,R.layout.custom_media_view)

        val imgArt = getImageArt(PlayerActivity.musicListPA[PlayerActivity.songPostion].path)
       val image = if(imgArt != null)
        {
            BitmapFactory.decodeByteArray(imgArt,0,imgArt.size)
        }else{
            BitmapFactory.decodeResource(resources,R.drawable.splash_screen)
        }
  val notification = NotificationCompat.Builder(this,CHANNAL_ID)
      .setContentTitle(PlayerActivity.musicListPA[PlayerActivity.songPostion].title)
      .setContentText(PlayerActivity.musicListPA[PlayerActivity.songPostion].artist)
      .setStyle(NotificationCompat.DecoratedCustomViewStyle())
      .setCustomContentView(remoteViews)
      .setSmallIcon(R.drawable.playlisticon)

   .setLargeIcon(image)
     // .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
      .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
      .setOnlyAlertOnce(true)
      .setDefaults(Notification.DEFAULT_ALL)

        remoteViews.setOnClickPendingIntent(R.id.prev_custom_btn,prevpendingIntent)
        remoteViews.setOnClickPendingIntent(R.id.next_custom_btn,nextpendingIntent)
        remoteViews.setOnClickPendingIntent(R.id.play_custom_btn,playpendingIntent)
        remoteViews.setOnClickPendingIntent(R.id.exit_custom_btn,exitpendingIntent)







        startForeground(12,notification.build())

    }

     fun createMedaiaPlayer()
    {
        try {


            if (PlayerActivity.musicservice!!.mediaPlayer == null) {
                PlayerActivity.musicservice!!.mediaPlayer = MediaPlayer()
            } else {
                PlayerActivity.musicservice!!.mediaPlayer!!.reset()
                PlayerActivity.musicservice!!.mediaPlayer!!.setDataSource(
                    PlayerActivity.musicListPA.get(
                        PlayerActivity.songPostion
                    ).path)
                PlayerActivity.musicservice!!.mediaPlayer!!.prepare()

                PlayerActivity.binding.playpausebtnPA.setIconResource(R.drawable.pause_icon)
                PlayerActivity.musicservice!!.showNotification(false)
                PlayerActivity.binding.tvSeekBarstart.text = formatDuration(PlayerActivity.musicservice!!.mediaPlayer!!.currentPosition.toLong())
                PlayerActivity.binding.tvseekbarend.text = formatDuration(PlayerActivity.musicservice!!.mediaPlayer!!.duration.toLong())
                PlayerActivity.binding.seekBarPA.progress = 0
                PlayerActivity.binding.seekBarPA.max = PlayerActivity.musicservice!!.mediaPlayer!!.duration
            }
        }catch (e:Exception)
        {
            return
        }
    }
    fun seekBarSetup()
    {
        runnable = Runnable{
            PlayerActivity.binding.tvSeekBarstart.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
            PlayerActivity.binding.seekBarPA.progress = mediaPlayer!!.currentPosition
        Handler(Looper.getMainLooper()).postDelayed(runnable,200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable,0)

    }
}