package com.andreasgift.kutipanalkitab

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.andreasgift.kutipanalkitab.alarm.Alarm
import com.andreasgift.kutipanalkitab.ui.PagerAdapter
import com.andreasgift.kutipanalkitab.ui.ZoomOutPageTransformer
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)

        mPager = findViewById(R.id.pager)
        val mpagerAdapter = PagerAdapter(this)
        mPager.adapter = mpagerAdapter
        mPager.setPageTransformer(ZoomOutPageTransformer())
    }

    override fun onBackPressed() {
        if (mPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            mPager.currentItem = mPager.currentItem - 1
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.download -> downloadPage()
            //R.id.alarm -> alarmDialog()
            R.id.share -> sharePage()
        }
        return true
    }

    private fun downloadPage() {
        checkPermissionWriteExt()
    }

    private fun checkPermissionWriteExt() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            val bitmap = takeScreenshot()
            val imageURi = saveImage(bitmap)
            imageURi?.let {
                Toast.makeText(this, getString(R.string.download_success), Toast.LENGTH_LONG).show()
            }
        } else {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
    }

    private fun takeScreenshot(): Bitmap {
        val view = findViewById<View>(R.id.cl_fragment)
        val bitmap = Bitmap.createBitmap(
            view.measuredWidth,
            view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.layout(view.left, view.top, view.right, view.bottom)
        view.draw(canvas)
        return bitmap
    }

    private fun saveImage(bitmap: Bitmap): Uri? {
        var fos: OutputStream? = null
        var imageUri: Uri? = null

        val timestamp = SimpleDateFormat("yyyyMMdd_hhMMss").format(Date())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = applicationContext.contentResolver
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp)
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + "KutipanAlkitab")
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = resolver.openOutputStream(imageUri!!)
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM
            ).toString() + File.separator + "KutipanAlkitab"

            val file = File(imagesDir)
            if (!file.exists()) file.mkdir()
            val image = File(imagesDir, timestamp + ".png")
            imageUri = Uri.fromFile(image)
            fos = FileOutputStream(image)
        }

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos?.flush()
        fos?.close()
        return imageUri
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val bitmap = takeScreenshot()
                    val imageUri = saveImage(bitmap)
                    imageUri?.let {
                        Toast.makeText(
                            this,
                            getString(R.string.download_success),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.permission_denied_download),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            else -> {
            }
        }
    }

    private fun sharePage() {
        val bitmap = takeScreenshot()
        val imageUri = saveImage(bitmap)

        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, imageUri)
            type = "image/*"
        }
        startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.share)))
    }

    // Next versionimplementation
    private fun alarmDialog() {
        Alarm().setOneTimeAlarm(this)
        requestPermissionOverlay()
        //AlarmDialogFragment().show(supportFragmentManager, "ALARM_DIALOG")
    }

    // Next versionimplementation
    private fun requestPermissionOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Alarm().setOneTimeAlarm(this)
            } else {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + this.packageName)
                )
                startActivityForResult(intent, 21)
            }
        }
    }

    // Next version implementation
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this.applicationContext)) {
                    Toast.makeText(this, "Permission is denied", Toast.LENGTH_LONG).show()
                } else {
                    Alarm().setOneTimeAlarm(this.applicationContext)
                }
            }
        }
    }
}