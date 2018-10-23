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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_additem.*
import java.io.IOException
import java.net.URL




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class additem : Fragment() {
    //var chnage=true
    var find = true
    lateinit var mAuth: String
    //= "WNlt9902oqYyQxNdfR6CB62YDQS2"

    private var filePath: Uri? = null
    internal var storage: FirebaseStorage? = null
    internal var storageRef: StorageReference? = null
    private val REQUEST_PICK_IMAGE = 1234

     var foodName=""
     var foodPrice=""
     var descrip=""
//    var masadDilog = ProgressDialog(context)

    var urlImage =""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_additem, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance().currentUser!!.uid


        //masadDilog.setMessage("Adding...")
//        button.setOnClickListener {
//
//            masadDilog.show()





        fimage2.setOnClickListener {
            loadImg()
        }


        fButton.setOnClickListener {
            var masadDilog = ProgressDialog(context)
            masadDilog.setMessage("Adding...")

            foodName = tnamef.text.toString()
            foodPrice = tp.text.toString()
            descrip = defood.text.toString()

          if (foodName.isEmpty() || foodPrice.isEmpty() || descrip.isEmpty() || urlImage.isEmpty()) {

                if (foodName.isEmpty()) {
                    //                  masadDilog.dismiss()
                    Toast.makeText(context, "Enter the name", Toast.LENGTH_SHORT).show()
                }
                if (foodPrice.isEmpty()) {
                    //                masadDilog.dismiss()
                    Toast.makeText(context, "Enter the price", Toast.LENGTH_SHORT).show()
                }
               if (descrip.isEmpty()) {

              Toast.makeText(context, "Enter the description", Toast.LENGTH_SHORT).show()
               }
                if (urlImage.isEmpty()) {
                    //            masadDilog.dismiss()
                    Toast.makeText(context, "Upload the image", Toast.LENGTH_SHORT).show()
                }


            }
          else {


                load()
              view.findNavController().navigate(R.id.displayfoods)


                masadDilog.dismiss()



            }
        }

        restsm.setOnClickListener {
            view.findNavController().navigate(R.id.displayfoods)
        }

        profileResbut.setOnClickListener {
            view.findNavController().navigate(R.id.action_additem_to_manageAcountInformation)
        }
            //change error msg






    }

    private fun load() {
        addUser()

    }

    private fun loadImg(){
        storage= FirebaseStorage.getInstance()
        storageRef=storage!!.reference
        select()
    }

    private fun addUser() {
        //val mAuth = FirebaseAuth.getInstance()

     //   val userId = mAuth.uid
        val database = FirebaseDatabase.getInstance().getReference("Foods")
        val key = database.push().key
        val food = menuItemInfo(foodName,foodPrice,descrip,mAuth!!,urlImage)
        database.child(key!!).setValue(food).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //masadDilog.dismiss()
                Toast.makeText(context,"The item was added succufully", Toast.LENGTH_SHORT).show()

            }
            else
            {
               // masadDilog.dismiss()
                Toast.makeText(context,"The item was not added  succufully", Toast.LENGTH_SHORT).show()

            }

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
        var find = true


        if (filePath != null) {
            var masadDilog = ProgressDialog(context)

            masadDilog.setMessage("uploading the image")
            masadDilog.show()


            //edit here
            val imageRers = storageRef!!.child("images/" + mAuth + "/food.jpg")
            imageRers.putFile(filePath!!).addOnSuccessListener {
                // var UrlLogo =imageRers.downloadUrl.toString().trim()
               // imageRers.getDownloadUrl().addOnSuccessListener {

                imageRers.getDownloadUrl().addOnSuccessListener  {
                    urlImage=it.toString().trim()
                  //  Toast.makeText(context, "Added", Toast.LENGTH_LONG).show()
                    masadDilog.dismiss()


                }


                    //  ref.getDownloadUrl()



                }

            }
        else
            Toast.makeText(context, "bad Internet", Toast.LENGTH_LONG).show()


//                    .addOnFailureListener {
//                        Toast.makeText(context, "bad Internet", Toast.LENGTH_LONG).show()
//                    }
//                    .addOnProgressListener { taskSnapshot ->
//                        val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
//                    }

        }            }






