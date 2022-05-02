package com.myexample.mymusicplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
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
        Toast.makeText(context,"PREV clicked",Toast.LENGTH_SHORT).show()
    }
    Utils.NEXT->{
        Toast.makeText(context,"NEXT clicked",Toast.LENGTH_SHORT).show()
    }
    Utils.EXIT->{
        PlayerActivity.musicservice!!.stopForeground(true)
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
}