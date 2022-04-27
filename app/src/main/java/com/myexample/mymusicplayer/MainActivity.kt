package com.myexample.mymusicplayer

import android.Manifest
import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.myexample.mymusicplayer.databinding.ActivityMainBinding
import com.myexample.mymusicplayer.databinding.ActivityPlayerBinding
import java.io.File
import kotlin.system.exitProcess
@SuppressLint("Range")
class MainActivity : AppCompatActivity() {
 companion object{
    lateinit var MusicListMA : ArrayList<MusicModel>
 }
    private lateinit var binding:ActivityMainBinding
    private lateinit var toggle:ActionBarDrawerToggle
    lateinit var musicAdapter:MusicAdapter

    @RequiresApi(Build.VERSION_CODES.P)
    var permissions = arrayOf(
        permission.ACCESS_MEDIA_LOCATION,
        permission.WRITE_EXTERNAL_STORAGE,
        permission.RECORD_AUDIO,
        permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.FOREGROUND_SERVICE
    )
    var permissioncode = 124
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(permissions, permissioncode)
            }else{
                intialisetheLayout()
            }
        }
       // MusicListMA = ArrayList()
        binding.suffleBtn.setOnClickListener {
            val intent = Intent(this@MainActivity,PlayerActivity::class.java)
            intent.putExtra("index",0)
            intent.putExtra("class","MainActivity")
            startActivity(intent)
        }
        binding.favouriteBtn.setOnClickListener {
        val intent = Intent(this@MainActivity,FavouriteActivity::class.java)
            startActivity(intent)
        }

        binding.playlistBtn.setOnClickListener {
            val intent = Intent(this@MainActivity,PlayListActivity::class.java)
            startActivity(intent)
        }


        binding.navigationview.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.navFeedBack->{
                    Toast.makeText(applicationContext,"This is feedback",Toast.LENGTH_SHORT).show()
                }
                R.id.navSetting->{
                    Toast.makeText(applicationContext,"This is Setting",Toast.LENGTH_SHORT).show()
                }
                R.id.navAbout->{
                    Toast.makeText(applicationContext,"This is About",Toast.LENGTH_SHORT).show()
                }
                R.id.navExit->{
                  exitProcess(1)
                }
            }
            true
        }
    }
    private fun requestRuntimePermissions()
    {
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this, permissions,13)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
        {
             return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun intialisetheLayout()
    {

        setTheme(R.style.coolPinkNav)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(this,binding.root,R.string.open,R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
       MusicListMA= getAllAudio()

        binding.musicMV.setHasFixedSize(true)
        binding.musicMV.setItemViewCacheSize(13)
        binding.musicMV.layoutManager = LinearLayoutManager(this@MainActivity)
        musicAdapter = MusicAdapter(this@MainActivity, MusicListMA)
        binding.musicMV.adapter = musicAdapter
        binding.totalSongs.text ="Total Songs: "+musicAdapter.itemCount
    }
    override fun onResume() {
        super.onResume()
        if (!Utils.isPermissionGranted(this)) {
           requestRuntimePermissions()
        } else {

            Toast.makeText(
                applicationContext,
                "permission already granted here",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun takepermssion() {
        if (VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                //  startActivityForResult(intent,1122);
                requestpermissionlanucher.launch(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                requestpermissionlanucher.launch(intent)
                // startActivityForResult(intent,1122);
            }
        } else {
            if (VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.RECORD_AUDIO
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(permissions, permissioncode)
                }
            }
        }
    }

    var requestpermissionlanucher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            Toast.makeText(applicationContext, "permission granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0) {
            if (requestCode == 101) {
                val readExt = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (!readExt) {
                    takepermssion()
                }
            }
        }
    }

private fun getAllAudio():ArrayList<MusicModel>{

    var templist = ArrayList<MusicModel>()
    val selection = MediaStore.Audio.Media.IS_MUSIC+ " != 0"
    val projection = arrayOf(MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ALBUM,
    MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.DURATION,MediaStore.Audio.Media.DATE_ADDED,MediaStore.Audio.Media.DATA,
    MediaStore.Audio.Media.ALBUM_ID
        )
        val cursor =this.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,
        MediaStore.Audio.Media.DATE_ADDED+" DESC",null)
    if(cursor!= null)
    {
        if(cursor.moveToFirst()){
            do{
                val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                val albumC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                val artistC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val durationC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                val albumIdC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
                val uri = Uri.parse("content://media/external/audio/albumart")
                val artUriC = Uri.withAppendedPath(uri,albumIdC).toString()
                val music = MusicModel(idC,titleC,albumC,artistC,durationC,pathC,artUriC)
                val file = File(music.path)
                if(file.exists())
                {
                    templist.add(music)
                }
            }while (cursor.moveToNext())
            cursor.close()
        }
    }

    return templist
}
}