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
import kotlinx.android.synthetic.main.fragment_createacount.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class createacount : Fragment() {
    lateinit var username:String
    lateinit var password:String
    lateinit var email:String
    lateinit var typeu:String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_createacount, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RegisterCustomer.setOnClickListener {

            view.findNavController().navigate(R.id.cusromerregister)

        }

        RegisterFoodProvider.setOnClickListener {

            view.findNavController().navigate(R.id.providerregister)

        }


        loginpage.setOnClickListener {
            view.findNavController().navigate(R.id.logintoAcount)
        }
    }
}

