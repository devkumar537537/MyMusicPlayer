package com.myexample.mymusicplayer

import android.media.MediaMetadataRetriever
import java.util.concurrent.TimeUnit

data class MusicModel(
    val id:String,
    val title:String,
    val album:String,
    val artist:String,
    val duration:Long=0,
    val path:String,
    val artUri:String
) {
}
fun formatDuration(duration: Long):String{
    val minutes = TimeUnit.MINUTES.convert(duration,TimeUnit.MILLISECONDS)
    val seconds = (TimeUnit.SECONDS.convert(duration,TimeUnit.MILLISECONDS)-minutes*TimeUnit.SECONDS.convert(1,TimeUnit.MINUTES))
return String.format("%02d:%02d",minutes,seconds)
}
fun getImageArt(path: String):ByteArray?
{
    val retriever  = MediaMetadataRetriever()
    retriever.setDataSource(path)
    return retriever.embeddedPicture
}

 fun setSongPostion(increment: Boolean){
     if(!PlayerActivity.repeat){
         if(increment)
         {

             if(PlayerActivity.musicListPA.size -1 == PlayerActivity.songPostion)
             {
                 PlayerActivity.songPostion =0
             }else
             {
                 ++PlayerActivity.songPostion
             }


         }else
         {
             if(0 == PlayerActivity.songPostion)
             {
                 PlayerActivity.songPostion = PlayerActivity.musicListPA.size-1
             }else
             {
                 --PlayerActivity.songPostion
             }
         }
     }

}
