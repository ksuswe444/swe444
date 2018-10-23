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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_loginto_acount.*



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class logintoAcount : Fragment() {
    lateinit var username:String
    lateinit var password:String
var ok =true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loginto_acount, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var masadDilog = ProgressDialog(context)
        masadDilog.setMessage("Checking...")
        sginup.setOnClickListener {
            masadDilog.show()

            username = oldemail.text.toString()
            password = oldpasw.text.toString()

            if (username.isEmpty() && password.isEmpty()){
                Toast.makeText(context, "Enter the Email and password vailds", Toast.LENGTH_LONG).show()

        }
            if (username.isEmpty() || password.isEmpty()) {

                masadDilog.dismiss()

                if (username.isEmpty()){

                        Toast.makeText(context, "Enter the Email vaild", Toast.LENGTH_LONG).show()}
                else{
                    Toast.makeText(context, "Enter the Password vaild", Toast.LENGTH_LONG).show()}

        } else {
                check(username, password, masadDilog)


            }


        }
        signuppage.setOnClickListener {
            view!!.findNavController().navigate(R.id.createacount)

        }


        forgotbut.setOnClickListener {
            if (oldemail.text.isEmpty())
                Toast.makeText(context, "Please Enter Email", Toast.LENGTH_LONG).show()
            else {
                var userEmail = oldemail.text.toString()
                val auth = FirebaseAuth.getInstance()
                auth.sendPasswordResetEmail(userEmail).addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        Toast.makeText(context, "Send IS successful to Email", Toast.LENGTH_LONG).show()
                    else {

                        Toast.makeText(context, "Send Is Not successful , please Enter Correct Email", Toast.LENGTH_LONG).show()

                    }
                }
            }
        }
    }
    //add type
    //chage bar
    //add dilog prossgaer
    private fun check(W: String, P: String,masadDilog:ProgressDialog) {
        val auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(W, P).addOnCompleteListener {task ->

            if (task.isSuccessful) {

                var  mAuth = FirebaseAuth.getInstance().currentUser!!.uid


                var database = FirebaseDatabase.getInstance().getReference("User")
                database.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(context, p0.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0!!.exists()) {
if(ok)
                            for (e in p0.children) {

                                var user = e.getValue(User::class.java) as User
//change
                                if (user!!.id.equals(mAuth)) {
                                    var type=user.type.toUpperCase()
                                    if((type.equals("FOOD PROVIDER"))){
                                        view!!.findNavController().navigate(R.id.manageAcountInformation)
                                        masadDilog.dismiss()
                                        ok=false

                                    }



                                    else{
                                        // new page
                                        masadDilog.dismiss()

                                        view!!.findNavController().navigate(R.id.displayList)}









                                }
                            }

                        }
                    }

                })
            }
            else
            {
                masadDilog.dismiss()
                Toast.makeText(context, ""+task.exception, Toast.LENGTH_LONG).show()

            }
        }
    }



}
