package com.example.yummy


import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.yummy.R.id.idlogin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_providerregister.*
import java.io.IOException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class providerregister : Fragment() {
    var logo=true
    lateinit var username: String
    lateinit var password: String
    lateinit var email: String
    lateinit var typeu: String
    lateinit var OpenTime: String
    lateinit var CloseTime: String
    //lateinit var uri:Uri
    lateinit var userId:String
    lateinit var mAuth: String
    //= "WNlt9902oqYyQxNdfR6CB62YDQS2"

     var URLforlogo=""
     var URLforrsturnt=""


    private var filePath: Uri? = null
    internal var storage: FirebaseStorage? = null
    internal var storageRef: StorageReference? = null
    private val REQUEST_PICK_IMAGE = 1234

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_providerregister, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var masadDilog = ProgressDialog(context)

        masadDilog.setMessage("Checking...")
        sginup2.setOnClickListener {


            username = usernamec22.text.toString().trim()//unique
            password = newpass2.text.toString()
            var phoneNumber = number11.text.toString()
            email = newemil22.text.toString().trim()
            typeu = "Food Provider"
            OpenTime = OPENTIME.text.toString()
            CloseTime = CLOSEDTIME.text.toString()




            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || OpenTime.isEmpty() || CloseTime.isEmpty() ||costC.text.isEmpty()) {
                masadDilog.dismiss()
                Toast.makeText(context, "Please, Enter all mandatory fields", Toast.LENGTH_SHORT).show()

            } else {
                    if (password.length < 8) {
                        masadDilog.dismiss()
                        Toast.makeText(context, "Please, Enter password more than 8 ", Toast.LENGTH_SHORT).show()}
                else {
                        if (phoneNumber.isEmpty()) {
                            if (URLforlogo.isNotEmpty() && URLforrsturnt.isNotEmpty()) {
                                addUser(username, email, " ", typeu, masadDilog)
                                //Toast.makeText(context, "Sign Up is successful", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Please,insert images  ", Toast.LENGTH_LONG).show()
                            }

                        } else {
                            //phoneNumber.toInt()
                            if (URLforlogo.isNotEmpty() && URLforrsturnt.isNotEmpty()) {
                                addUser(username, email, phoneNumber, typeu, masadDilog)
                            } else {
                                Toast.makeText(context, "Please,insert images  ", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
            }
        }


        backF.setOnClickListener {
            view.findNavController().navigate(R.id.createacount)
        }

        //*************
        uploadlogo.setOnClickListener {
            logo=true
            select()

            storage = FirebaseStorage.getInstance()
            storageRef = storage!!.reference


        }
        //*************
        uploadback.setOnClickListener {
            logo=false
            select()

            storage = FirebaseStorage.getInstance()
            storageRef = storage!!.reference

        }


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
            upload()
        }
    }


    private fun upload() {


        if (filePath != null) {

            //edit here
            if (logo) {
                val imageRers = storageRef!!.child("images/things/logo.jpg")
                imageRers.putFile(filePath!!).addOnSuccessListener {
                    // var UrlLogo =imageRers.downloadUrl.toString().trim()
                    imageRers.getDownloadUrl().addOnSuccessListener {

                        URLforlogo = it.toString().trim()//*****
                        //  ref.getDownloadUrl()
                    }

                }

            } else {
                val imageRers = storageRef!!.child("images/things/restaunt.jpg")
                imageRers.putFile(filePath!!).addOnSuccessListener {
                    // var UrlLogo =imageRers.downloadUrl.toString().trim()
                    imageRers.getDownloadUrl().addOnSuccessListener {

                        URLforrsturnt = it.toString().trim()
                    }
                }
            }

        }
    }
    fun addUser(username: String, email: String, number: String, type: String, masadDilog: ProgressDialog) {
        masadDilog.show()
        val mAuth = FirebaseAuth.getInstance()

//progressBar.setVisibility(view.VISIBLE)ADD TO SCREN
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                 userId = mAuth.uid!!
                val database = FirebaseDatabase.getInstance().getReference("User")
                val userkey = database.push().key
                val user = User(username, email, number, type, userId!!)
                database.child(userkey!!).setValue(user).addOnCompleteListener { task ->
                    if (task.isSuccessful) {





                        val database1 = FirebaseDatabase.getInstance().getReference("UserInfo")
                        val userkey1 = database.push().key
                        //val url=uri.toString()
                        //var OpenTime2=OpenTime.toInt()

                        val userinfo = UserInfo(OpenTime, username, CloseTime, costC.text.toString(), userId!!, URLforlogo, URLforrsturnt)
                        database1.child(userkey1!!).setValue(userinfo).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                masadDilog.dismiss()

                                Toast.makeText(context, "Sign Up is successful", Toast.LENGTH_SHORT).show()

                                view!!.findNavController().navigate(R.id.logintoAcount)

                            } else {
                                masadDilog.dismiss()
//change to erroer mags
                                Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }


                    } else {
                        masadDilog.dismiss()
                        Toast.makeText(context, "please, enter another Email", Toast.LENGTH_SHORT).show()

                    }
                }


            } else {
                masadDilog.dismiss()

                Toast.makeText(context, "please, Enter another Email", Toast.LENGTH_SHORT).show()

            }
        }
    }
}



