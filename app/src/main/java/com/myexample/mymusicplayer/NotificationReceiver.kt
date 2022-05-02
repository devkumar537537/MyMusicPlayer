package com.myexample.mymusicplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlin.system.exitProcess

class NotificationReceiver :BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
when(intent?.action){
    Utils.PLAY->{
        if(PlayerActivity.isPlaying)
        {
            pauseMusic()
        }else
        {
            playMusic()
        }
    }
    Utils.PREV->{
        prevNextSong(false,context!!)
    }
    Utils.NEXT->{
     prevNextSong(true,context!!)
    }
    Utils.EXIT->{
        PlayerActivity.musicservice!!.stopForeground(true)
        PlayerActivity.musicservice!!.mediaPlayer!!.release()
           PlayerActivity.musicservice = null
        exitProcess(1)

    }
}
    }
    private fun playMusic(){
        PlayerActivity.isPlaying = true
        PlayerActivity.musicservice!!.mediaPlayer!!.start()
        PlayerActivity.musicservice!!.showNotification(false)
        PlayerActivity.binding.playpausebtnPA.setIconResource(R.drawable.pause_icon)
    }
    private fun pauseMusic(){
        PlayerActivity.isPlaying = false
        PlayerActivity.musicservice!!.mediaPlayer!!.pause()
        PlayerActivity.musicservice!!.showNotification(true)
        PlayerActivity.binding.playpausebtnPA.setIconResource(R.drawable.play_icon)
    }
    private fun prevNextSong(increment:Boolean,context: Context)
    {
        setSongPostion(increment)
      PlayerActivity.musicservice!!.createMedaiaPlayer()
        Glide.with(context).load(PlayerActivity.musicListPA.get(PlayerActivity.songPostion).artUri).
        apply(RequestOptions()
            .placeholder(R.drawable.splash_screen).centerCrop()).into(PlayerActivity.binding.songImgPA)
        PlayerActivity.binding.songNamePA.text = PlayerActivity.musicListPA.get(PlayerActivity.songPostion).title

   playMusic()

    }

}