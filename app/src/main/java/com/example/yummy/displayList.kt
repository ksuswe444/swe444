package com.example.yummy


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import kotlinx.android.synthetic.main.listfoodprivder.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class displayList : Fragment() {
    var ok:Boolean = false
    lateinit var userid:String
    var on=true
    lateinit var mAuth: String
    var myadapter:MsgAdapter?=null
    var listMag=ArrayList<UserInfo>()
    var find=true
    lateinit var intent:Intent
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_list, container, false)

        /*
        foodprivder.setOnClickListener{

            // intent1 =Intent(magView.context,::class.java)

            //// intent1.putExtra("Userid",userid)
            //magView.context.startActivity(intent1)

            intent1 =Intent(this.context, dispalymenu :: class.java)
            intent1.putExtra ("Userid",userid.toString())
            startActivity(intent1)

            view!!.findNavController().navigate(R.id.dispalymenu)
        }*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance().currentUser!!.uid
        ok = true
        loadMag()
        myadapter = MsgAdapter(this!!.activity!!, listMag)
        foodprivder.adapter = myadapter//pl
        profileResbutPself.setOnClickListener {
            view.findNavController().navigate(R.id.action_displayList_to_custmerpage)




        }

    }

    private fun loadMag(){//this shoud put in load page

        var oldMsg:UserInfo
        var newMs:UserInfo
        lateinit var  last: DataSnapshot



        var database = FirebaseDatabase.getInstance().getReference("UserInfo")
        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, p0.message, Toast.LENGTH_SHORT).show()}
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {
                    if (find) {
                        for (e in p0.children) {

                                var oldMsg = e.getValue(UserInfo::class.java) as UserInfo
                                listMag.add(oldMsg)//sort


                                // }
                                //lastone


                            }
                        myadapter!!.notifyDataSetChanged()

                        }
                       find=false





                }}})}


    inner class MsgAdapter: BaseAdapter  {
        var context: Context?=null
        var listOfMagsLocal=ArrayList<UserInfo>()
        constructor(context: Context, listOfMags:ArrayList<UserInfo>){
            listOfMagsLocal=listOfMags
            this.context=context
        }
        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var Masobject=listOfMagsLocal[position]
            var inflator=context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val magView=inflator.inflate(R.layout.listfoodprivder,null)
           magView.restTest.text=Masobject.username!!
            var cost=Masobject.cost.toString()!!+" SR"
            magView.restcost.text= cost

           var uRL1=Masobject.logUrl.trim()
                  var uRL2=Masobject.imageUrl.trim()

           //userid=listOfMagsLocal[position].id.trim()


            magView.restbig.setOnClickListener{

                val intent = Intent(magView.restbig.context, dispalymenu::class.java)

                intent.putExtra("userid",listOfMagsLocal[position].id.toString())

                context!!.startActivity(intent)
/*
                val mFragment = displayList()
                val mArgs = Bundle()
                mArgs.putString("userid", userid)
                mFragment.setArguments(mArgs)*/

                // intent=Intent(magView.context,dispalymenu::class.java)

               //intent.putExtra("userid",userid)
                //magView.context.startActivity(intent)

                //intent1 =Intent(magView.context, dispalymenu :: class.java)
                //intent1.putExtra ("Userid",userid.toString())
               // magView.context.startActivity(intent)

                //magView!!.navigate(R.id.men)
        }
           Picasso.get().load(uRL1).into(magView.restSmal)
            Picasso.get().load(uRL2).fit()
                    .into(magView.restbig)




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
