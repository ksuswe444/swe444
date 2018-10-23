package com.example.yummy


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
import kotlinx.android.synthetic.main.fragment_displayfoods.*
import kotlinx.android.synthetic.main.listoffood.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class displayfoods : Fragment() {
    var ok:Boolean = false
    lateinit var mAuth: String
    var myadapter: displayfoods.MsgAdapter?=null
    var listMag=ArrayList<menuItemInfo>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_displayfoods, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance().currentUser!!.uid

        listadditem.setOnClickListener {
            view.findNavController().navigate(R.id.additem)
        }
        profileResbutPself.setOnClickListener {
            view.findNavController().navigate(R.id.action_displayfoods_to_manageAcountInformation)
        }




        ok=true
        loadMag()


        myadapter= MsgAdapter(this!!.activity!!,listMag)

        listOfItems.adapter=myadapter//pl




    }


    private fun loadMag(){

        var oldMsg:menuItemInfo
        var newMs:menuItemInfo
        lateinit var  last: DataSnapshot
        var database = FirebaseDatabase.getInstance().getReference("Foods")
        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, p0.message, Toast.LENGTH_SHORT).show()}
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {

                    for (e in p0.children) {
                        if(ok){

                            var oldMsg= e.getValue(menuItemInfo::class.java) as menuItemInfo
                            if (mAuth.equals(oldMsg.foodID)){
                                listMag.add(oldMsg)//sort }
                                //lastone
                            }}
                        last=e
                    }
                    // ok=false
                    if(!ok){
                        var newMs = last.getValue(menuItemInfo::class.java) as menuItemInfo
                        if (mAuth.equals(newMs.foodID)){
                            listMag.add(newMs)//sort
                        }}



                    myadapter!!.notifyDataSetChanged()

                }}})}


    inner class MsgAdapter: BaseAdapter {
        var context: Context?=null
        var listOfMagsLocal=ArrayList<menuItemInfo>()
        constructor(context: Context, listOfMags:ArrayList<menuItemInfo>){
            listOfMagsLocal=listOfMags
            this.context=context


            //hana's code
//
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var Masobject=listOfMagsLocal[position]
            var inflator=context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val magView=inflator.inflate(R.layout.
                    listoffood,null)


            magView.fName.text=Masobject.foodName!!
            magView.fPrice.text=Masobject.price!!
               var logUrl=Masobject.imageUrl
                  Picasso.get().load(logUrl).fit().into(magView.fImage)








            magView.cancelitem.setOnClickListener { //cahnge cancelitem to name id (-) of you put
                      val builder = AlertDialog.Builder(context)
                      builder.setTitle("Delete food  item ")
                       var nameitem=magView.fName.text.toString()

                      builder.setMessage("Do you want to delete the food item ?")
                     builder.setPositiveButton("YES"){dialog, which ->
                         delete(nameitem,position)//cahnge nameresCut to name id name food name
//// as magview.here put id for name of food item
                      }
                    builder.setNeutralButton("Cancel"){_,_ ->
                       Toast.makeText(context,"You cancelled.",Toast.LENGTH_SHORT).show()
                      }
                      val dialog: AlertDialog = builder.create()
                   dialog.show()
                   }





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


    //hana's code

    private fun delete(name:String,index:Int){

        var find=true
        var database = FirebaseDatabase.getInstance().getReference("Foods")//change to name of food database(as you write in db)
        database.addValueEventListener(
                object : ValueEventListener {

                    override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, p0.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0!!.exists()) {

                    for (e in p0.children) {
                        var user = e.getValue(menuItemInfo::class.java) as menuItemInfo //change User to db of item of food

                        if (user!!.foodName.equals(name)) {
                            if(find){

                                find = false
                                var t = e.key
                                var firebaseDel = FirebaseDatabase.getInstance().reference.child("Foods").child(t.toString())
                                firebaseDel.removeValue()
                                listMag.removeAt(index)
                                myadapter!!.notifyDataSetChanged()

                                Toast.makeText(context, "deleted is successful", Toast.LENGTH_SHORT).show()


                            }}
                    }
                }
            }
        })

    }


}





