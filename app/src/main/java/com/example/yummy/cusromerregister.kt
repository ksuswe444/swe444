package com.example.yummy


import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_cusromerregister.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class cusromerregister : Fragment() {
    lateinit var username:String
    lateinit var password:String
    lateinit var email:String
    lateinit var typeu:String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cusromerregister, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var masadDilog = ProgressDialog(context)

        masadDilog.setMessage("Checking...")



        sginup2.setOnClickListener {

            masadDilog.show()
            username = usernamec22.text.toString().trim()
            email= newemil22.text.toString().trim()//unique
            password = newpass2.text.toString().trim()
            var phoneNumber = number11.text.toString()
            //phoneNumber.toInt()
            //var password2=password.toInt()
            typeu = "Customer"


            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                masadDilog.dismiss()
                Toast.makeText(context, "Please, Enter all mandatory fields", Toast.LENGTH_SHORT).show()

            } else {
                if (password.length < 8) {
                    masadDilog.dismiss()
                    Toast.makeText(context, "Please, Enter password more than 8 ", Toast.LENGTH_SHORT).show()
                } else {
                    if (phoneNumber.isEmpty()) {
                        addUser(email, username, " ", typeu, masadDilog)
                        //Toast.makeText(context, "Sign Up is successful", Toast.LENGTH_SHORT).show()
                    } else {
                        //phoneNumber.toInt()

                        addUser(email, username, phoneNumber, typeu, masadDilog)

                        //Toast.makeText(context, "Sign Up is successful", Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }
       backC.setOnClickListener {
            view.findNavController().navigate(R.id.createacount)
        }


    }


    fun addUser(email: String, username: String, number:String, type: String,masadDilog: ProgressDialog) {
        val mAuth = FirebaseAuth.getInstance()

//progressBar.setVisibility(view.VISIBLE)ADD TO SCREN
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //Toast.makeText(context, mAuth.uid, Toast.LENGTH_SHORT).show()
                val userId = mAuth.uid
                val database = FirebaseDatabase.getInstance().getReference("User")
                val userkey = database.push().key
                val user = User(username,email,number,type,userId!!)
                database.child(userkey!!).setValue(user).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, " Sign Up is successful", Toast.LENGTH_SHORT).show()
                        masadDilog.dismiss()
                        view!!.findNavController().navigate(R.id.logintoAcount)


                    }
                    else{
                        masadDilog.dismiss()
                        Toast.makeText(context, "pleas, Enter another Email", Toast.LENGTH_SHORT).show()

                    }
                }


            }

            else{
                masadDilog.dismiss()

                Toast.makeText(context, "pleas, Enter another Email", Toast.LENGTH_SHORT).show()

            }
        }
    }



}
