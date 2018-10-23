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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_edit_information.*
import java.io.IOException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class editInformation : Fragment() {

    lateinit var mAuth: String
            //"WNlt9902oqYyQxNdfR6CB62YDQS2"
    private var filePath: Uri? = null
     lateinit var newuser:UserInfo
    //(2,"pasta",8,2,mAuth,"https://firebasestorage.googleapis.com/v0/b/yummy-b55d9.appspot.com/o/images%2FTJSmWa0DNfQbFeDDGyLaAkxasJt2%2Flogo.jpg?alt=media&token=1ec84f48-b8ac-4f1a-97bd-a1feaffca39d")
    internal var storage: FirebaseStorage? = null
    internal var storageRef: StorageReference? = null
    private val REQUEST_PICK_IMAGE = 1234


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


       mAuth = FirebaseAuth.getInstance().currentUser!!.uid





        addphoto.setOnClickListener {
            //loadImage()
            loadImg()

        }
        ok.setOnClickListener {

            if (opentoopen.text.isNotEmpty() || closeedit.text.isNotEmpty() || costedit.text.isNotEmpty() ||usenameedit.text.isNotEmpty()) {
                editUsernameInfo()
                Toast.makeText(context, "change is successful", Toast.LENGTH_SHORT).show()



                view!!.findNavController().navigate(R.id.action_editInformation_to_manageAcountInformation)

            }
            else
            Toast.makeText(context, "you dont enter any fields", Toast.LENGTH_SHORT).show()

        }
        Canccel.setOnClickListener {
            view!!.findNavController().navigate(R.id.manageAcountInformation)

        }
    }

    /*
private fun loadImage(){
    val userID="1SRFDYO"//change
    val phpto=1
        val storage=FirebaseStorage.getInstance()
    val storageLink=storage.getReferenceFromUrl("gs://yummy-b55d9.appspot.com")
    val format =SimpleDateFormat("ddMMyyHHmmss")
    val date=Date()
    val imgaePath=userID+"."+format.format(date)+"jpg"
    val imageRes=storageLink.child("userID").child("Res/"+imgaePath)

    /*var bitmapphor=phpto as BitmapDrawable
   bitmapphor=bitmapphor.Bitmap*/

    }*/
    private fun loadImg() {
        storage = FirebaseStorage.getInstance()
        storageRef = storage!!.reference
        select()


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
                // imageprofile.setImageBitmap(bitmap)
                   upload()
                // imageView11!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    private fun upload() {
        var find=true
      //  val mAuth = FirebaseAuth.getInstance().currentUser!!.uid

        if (filePath != null) {
            val possDia = ProgressDialog(context)
            possDia.setTitle("Uploading....")
            possDia.show()
            //edit here
            val imageRers = storageRef!!.child("images/" + mAuth + "/imageRes.jpg")
            imageRers.putFile(filePath!!).addOnSuccessListener {
                imageRers.getDownloadUrl().addOnSuccessListener {
                    var UrlLogo = it.toString().trim()

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
                                            map.put("imageUrl", UrlLogo)
                                            firebaseDel.updateChildren(map as Map<String, String>)

                                        }

                                    }
                                }
                            }
                        }
                    })

                possDia.dismiss()
                Toast.makeText(context, "Image Change sussful", Toast.LENGTH_LONG).show()

            }}
                    .addOnFailureListener {
                        possDia.dismiss()
                        Toast.makeText(context, "Image Change Not Sucssful", Toast.LENGTH_LONG).show()
                    }
                    .addOnProgressListener { taskSnapshot ->
                        val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        possDia.setMessage("Change " + progress.toInt() + "%...")
                    }

        }
    }

    private fun editUsername(username:String) {
var find=true
        var database = FirebaseDatabase.getInstance().getReference("User")
        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, p0.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0!!.exists()) {

                    for (e in p0.children) {
                        var user = e.getValue(User::class.java) as User
//change
                        if (user!!.id.equals(mAuth)) {
                            if(find){

                                find = false
                            var t = e.key
                            var firebaseDel = FirebaseDatabase.getInstance().reference.child("User").child(t.toString())
                            var keyuse = t.toString()
                                var map = hashMapOf<String, String>()
                                map.put("username", username)
                                firebaseDel.updateChildren(map as Map<String, String>)

                                Toast.makeText(context, "change is successful", Toast.LENGTH_SHORT).show()


                            }}
                    }
                }
            }
        })
    }

    private fun editUsernameInfo() {
        var ok=true
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
                            if (ok) {
                                ok = false

                                var t = e.key
                                var firebaseDel = FirebaseDatabase.getInstance().reference.child("UserInfo").child(t.toString())
                                var keyuse = t.toString()
                                if (usenameedit.text.isNotEmpty()) {
                                    var name = usenameedit.text.toString()
                                    var map = hashMapOf<String, String>()
                                    map.put("username", name)
                                    firebaseDel.updateChildren(map as Map<String, String>)
                                    editUsername(name)
                                }
                                if (opentoopen.text.isNotEmpty()) {
                                    var openTime = opentoopen.text.toString()
                                    var map = hashMapOf<String, String>()
                                    map.put("timeOpen", openTime)
                                    firebaseDel.updateChildren(map as Map<String, String>)

                                }
                                if (closeedit.text.isNotEmpty()) {
                                    var closeTime = closeedit.text.toString()
                                    var map = hashMapOf<String, String>()
                                    map.put("timeClose", closeTime)
                                    firebaseDel.updateChildren(map as Map<String, String>)

                                }
                                if (costedit.text.isNotEmpty()) {
                                    var closeTime = costedit.text.toString()
                                    var map = hashMapOf<String, String>()
                                    map.put("cost", closeTime)
                                    firebaseDel.updateChildren(map as Map<String, String>)

                                }


                            }
                        } }
                }


}

        })

    }

}





