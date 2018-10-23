package com.example.yummy


import android.app.Activity
import android.app.DownloadManager
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_manage_acount_information.*
import java.net.URL
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.provider.MediaStore
import com.example.yummy.R.id.chronometer
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import okhttp3.*
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class manageAcountInformation : Fragment() {
    var chnage=true
    var find = true
    lateinit var url: URL
    lateinit var mAuth: String
            //= "WNlt9902oqYyQxNdfR6CB62YDQS2"
    lateinit var UrlLogo:String

    private var filePath: Uri? = null
    internal var storage: FirebaseStorage? = null
    internal var storageRef: StorageReference? = null
    private val REQUEST_PICK_IMAGE = 1234



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_acount_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance().currentUser!!.uid

        load()
        setting.setOnClickListener {
            view!!.findNavController().navigate(R.id.editInformation)
        }
        logout.setOnClickListener {
            view!!.findNavController().navigate(R.id.logintoAcount)
        }

        idlogin.setOnClickListener {
            select()
            storage = FirebaseStorage.getInstance()
            storageRef = storage!!.reference
        }


        restsm.setOnClickListener {
                      view.findNavController().navigate(R.id.action_manageAcountInformation_to_displayfoods)
       }


    }


    private fun load() {
        userInfo()

    }





    private fun userInfo() {
        var database = FirebaseDatabase.getInstance().getReference("UserInfo")
        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, p0.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
if(chnage){
                if (p0!!.exists()) {

                    for (e in p0.children) {
                        var userinfo = e.getValue(UserInfo::class.java) as UserInfo


                        if (userinfo!!.id.equals(mAuth)) {

                            var uRL = userinfo.logUrl.trim()

                            Picasso.get().load(uRL).into(idlogin)

                            var open = userinfo!!.timeOpen.toString()
                            var close = userinfo!!.timeClose.toString()
                            timeclose.text = open + "-" + close + " AM"
                            moneny.text = userinfo!!.cost.toString() + " SAR"
                            nameRES.text=userinfo.username

                            chnage=false
                        }

                }

            }}}
        })
    }




    private fun select() {
        var PICK_IMAGE_REQUEST = 1234
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), REQUEST_PICK_IMAGE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data;
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, filePath)
                 idlogin.setImageBitmap(bitmap)
                 upload()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }



    private fun upload() {
        var find = true

        if (filePath != null) {

            //edit here
            val imageRers = storageRef!!.child("images/" + mAuth + "/logo.jpg")
            imageRers.putFile(filePath!!).addOnSuccessListener {
               // var UrlLogo =imageRers.downloadUrl.toString().trim()
                imageRers.getDownloadUrl().addOnSuccessListener {

                    var UrlLogo = it.toString().trim()
                    //  ref.getDownloadUrl()


                    var database = FirebaseDatabase.getInstance().getReference("UserInfo")
                    database.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            Toast.makeText(context, p0.message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            if (p0!!.exists()) {

                                for (e in p0.children) {
                                    var user = e.getValue(UserInfo::class.java) as UserInfo
//change
                                    if (user!!.id.equals(mAuth)) {
                                        if (find) {
                                            find = false
                                            var t = e.key
                                            var firebaseDel = FirebaseDatabase.getInstance().reference.child("UserInfo").child(t.toString())
                                            var keyuse = t.toString()
                                            var map = hashMapOf<String, String>()
                                            map.put("logUrl", UrlLogo)
                                            firebaseDel.updateChildren(map as Map<String, String>)
                                        }

                                    }
                                }
                            }
                        }
                    })
                }

            }
                        .addOnFailureListener {
                            Toast.makeText(context, "bad Internet", Toast.LENGTH_LONG).show()
                        }
                        .addOnProgressListener { taskSnapshot ->
                            val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        }

            }            }
        }







