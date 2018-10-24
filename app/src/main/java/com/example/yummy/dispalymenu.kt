
package com.example.yummy


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_display_list.*
import kotlinx.android.synthetic.main.fragment_displayfoods.*
import kotlinx.android.synthetic.main.listfoodprivder.view.*
import kotlinx.android.synthetic.main.listoffood.view.*
import android.content.Intent
import android.content.Intent.getIntent
import com.example.yummy.R.drawable.name
import android.graphics.Color
import android.os.PersistableBundle
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import android.content.Intent.getIntent
import android.widget.TextView
import android.content.Intent.getIntent
import android.content.Intent.getIntent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.menu88.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@Suppress("DEPRECATION")
/**
 * A simple [Fragment] subclass.
 *
 */
class dispalymenu : AppCompatActivity() {

    var ok:Boolean = true
    lateinit var mAuth: String
    var myadapter: dispalymenu.MsgAdapter?=null
    var listMag=ArrayList<menuItemInfo>()
    lateinit var id: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu88)

        this.mAuth = FirebaseAuth.getInstance().currentUser!!.uid


        ok = true
        loadMag()


        myadapter = MsgAdapter(this, listMag)
//&&&&&&&&&&7
        menu11.adapter = myadapter//pl

        profileResbutPself2.setOnClickListener {
            //findNavController().navigate(R.id.action_menu_to_custmerpage)
            val fragmentTransaction: FragmentTransaction
            val fragmentManager: FragmentManager

            val fragmentClass: Class<*>? = null
            val mFragment: Fragment

            val fragment: Custmerpage  // fragment instance of current fragmentk

            fragmentTransaction = supportFragmentManager.beginTransaction()

            mFragment = Custmerpage() // CreateNewNote is fragment you want to display

            fragmentTransaction.replace(R.id.frameLayout5, mFragment)  // content_fragment is id of FrameLayout(XML file) where fragment will be displayed


        }


    }

/*

        var args = getArguments()
        id = args!!.getString("userid", "")
 */


        // id = getIntent(displayList.userid).toString())


        //////////////////////on click

    private fun loadMag(){

        var oldMsg:menuItemInfo
       // var newMs:menuItemInfo
        lateinit var  last: DataSnapshot
        val database = FirebaseDatabase.getInstance().getReference("Foods")
        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@dispalymenu, p0.message, Toast.LENGTH_SHORT).show()}
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {

                    if (ok) {
                        for (e in p0.children) {

                            val oldMsg= e.getValue(menuItemInfo::class.java) as menuItemInfo
                            if (intent.getStringExtra("userid").equals(oldMsg.foodID)) {
                                listMag.add(oldMsg)//sort }
                                //lastone
                            }
                    }}
                     ok=false




                    myadapter!!.notifyDataSetChanged()

                }

            }})}


    inner class MsgAdapter: BaseAdapter {
        var context: Context?=null
        var listOfMagsLocal=ArrayList<menuItemInfo>()
        constructor(context: Context, listOfMags:ArrayList<menuItemInfo>){
            listOfMagsLocal=listOfMags
            this.context=context



//
        }
        @SuppressLint("ViewHolder", "InflateParams")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val Masobject=listOfMagsLocal[position]
            val inflator=context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val magView=inflator.inflate(R.layout.
                    listofmenu,null)


            magView.fName.text= Masobject.foodName
            magView.fPrice.text= Masobject.price
            val logUrl=Masobject.imageUrl
            Picasso.get().load(logUrl).fit().into(magView.fImage)



            return magView
        }

        override fun getItem(position: Int): Any {
            return listOfMagsLocal[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listOfMagsLocal.size
        }

    }


}





