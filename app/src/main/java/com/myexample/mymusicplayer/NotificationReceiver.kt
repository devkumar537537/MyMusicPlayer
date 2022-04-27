package com.myexample.mymusicplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotificationReceiver :BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
when(intent?.action){
    Utils.PLAY->{
        Toast.makeText(context,"Play clicked",Toast.LENGTH_SHORT).show()
    }
    Utils.PREV->{
        Toast.makeText(context,"PREV clicked",Toast.LENGTH_SHORT).show()
    }
    Utils.NEXT->{
        Toast.makeText(context,"NEXT clicked",Toast.LENGTH_SHORT).show()
    }
    Utils.EXIT->{
        Toast.makeText(context,"Exit clicked",Toast.LENGTH_SHORT).show()
    }

}
    }
}