package com.example.yummy


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class login : Fragment() {
    var  secold=0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var thread=myThread()
        thread.start()

    }





    inner class myThread: Thread() {
        override fun run() {

            try {
                while (!Thread.currentThread().isInterrupted()) {

                    //to make thread run all the time
                    Thread.sleep(1000)
                    //  Thread(Runnable {


                    //for make change to fast in something while the thread is run
                    var time = System.currentTimeMillis()
                    //   var min = (time / ((1000 * 60))) % 60
                    var sec = (time / 1000) / 60

                    if (sec.toDouble()<secold) {

                        check(sec)
                        break;
                    }
                    secold=sec.toDouble()+0.1


                }

            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }

        fun check(min: Long) {

           view!!.findNavController().navigate(R.id.logintoAcount)



        }}
}
