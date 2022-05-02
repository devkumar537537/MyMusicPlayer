package com.myexample.mymusicplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.myexample.mymusicplayer.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity(),ServiceConnection {

    companion object{
       lateinit var musicListPA:ArrayList<MusicModel>
       var songPostion:Int =0

var isPlaying:Boolean = false
        var musicservice:MusicService? = null
        lateinit var binding: ActivityPlayerBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent= Intent(this,MusicService::class.java)
        startService(intent)
        bindService(intent,this, BIND_AUTO_CREATE)


        intialiseLalyout()
        binding.playpausebtnPA.setOnClickListener {
if(isPlaying)
{
    pauseMusic()
}else
{
    playMusic()
}
    binding.previourbtnPA.setOnClickListener {
preNextSong(increment = false)
    }
    binding.nextbtnPA.setOnClickListener {
preNextSong(increment = true)
    }
}

    }
   private fun setLayout(){

        Glide.with(this).load(musicListPA.get(songPostion).artUri).
        apply(RequestOptions()
            .placeholder(R.drawable.splash_screen).centerCrop()).into(binding.songImgPA)
        binding.songNamePA.text = musicListPA.get(songPostion).title
    }

    private fun createMedaiaPlayer()
    {
        try {


            if (musicservice!!.mediaPlayer == null) {
                musicservice!!.mediaPlayer = MediaPlayer()
            } else {
                musicservice!!.mediaPlayer!!.reset()
                musicservice!!.mediaPlayer!!.setDataSource(musicListPA.get(songPostion).path)
                musicservice!!.mediaPlayer!!.prepare()
                musicservice!!.mediaPlayer!!.start()
                isPlaying = true
                binding.playpausebtnPA.setIconResource(R.drawable.pause_icon)
            }
        }catch (e:Exception)
        {
            return
        }
    }
    private fun intialiseLalyout()
    {

        songPostion = intent.getIntExtra("index",0)
        when(intent.getStringExtra("class")){
            "MusicAdapter" ->{
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.MusicListMA)
                setLayout()

            }
            "MainActivity"->{
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.MusicListMA)
                musicListPA.shuffle()
                setLayout()

            }
        }

    }
    private fun playMusic()
    {
            binding.playpausebtnPA.setIconResource(R.drawable.pause_icon)
        musicservice!!.showNotification(false)
        isPlaying = true
        musicservice!!.mediaPlayer!!.start()
    }
    private fun pauseMusic()
    {
        binding.playpausebtnPA.setIconResource(R.drawable.play_icon)
        musicservice!!.showNotification(true)
        isPlaying=false
        musicservice!!.mediaPlayer!!.pause()
    }
    private fun preNextSong(increment:Boolean)
    {
        if(increment)
        {
           setSongPostion(increment = true)
            setLayout()
            createMedaiaPlayer()
        }else{
            setSongPostion(increment = false)
            setLayout()
            createMedaiaPlayer()
        }
    }
    private fun setSongPostion(increment: Boolean){
        if(increment)
        {

            if(musicListPA.size -1 == songPostion)
            {
                songPostion =0
            }else
            {
                ++songPostion
            }


        }else
        {
            if(0 == songPostion)
            {
                songPostion = musicListPA.size-1
            }else
            {
                --songPostion
            }
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
      val binder = service as MusicService.MyBinder
        musicservice = binder.currentService()
        createMedaiaPlayer()


    }

    override fun onServiceDisconnected(name: ComponentName?) {
      musicservice = null
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(this)
        val intent = Intent(this@PlayerActivity,MusicService::class.java)
        stopService(intent)
    }
}