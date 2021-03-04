package com.example.fizzup_test

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.util.*

class BaseAdapter2(
    context: Context,
    myList: ArrayList<listItem>
) : BaseAdapter() {
    var myList = ArrayList<listItem>()
    var context: Context

    // retourne le nombre d'objet présent dans notre liste
    override fun getCount(): Int {
        return myList.size
    }

    // retourne un élément de notre liste en fonction de sa position
    override fun getItem(position: Int): listItem {
        return myList[position]
    }

    // retourne l'id d'un élément de notre liste en fonction de sa position
    override fun getItemId(position: Int): Long {
        return myList.indexOf(getItem(position)).toLong()
    }

    // retourne la vue d'un élément de la liste
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View? {
        var convertView = convertView
        var mViewHolder: MyViewHolder? = null

        // au premier appel ConvertView est null, on inflate notre layout
        if (convertView == null) {
            val mInflater = context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = mInflater.inflate(R.layout.listitem, parent, false)

            // nous placons dans notre MyViewHolder les vues de notre layout
            mViewHolder = MyViewHolder()



            mViewHolder.textViewName = convertView
                .findViewById<View>(R.id.textViewName) as ImageView

            mViewHolder.textViewAge = convertView
                .findViewById<View>(R.id.textViewScore) as TextView

            // nous attribuons comme tag notre MyViewHolder a convertView
            convertView.tag = mViewHolder
        } else {
            // convertView n'est pas null, nous récupérons notre objet MyViewHolder
            // et évitons ainsi de devoir retrouver les vues �achaque appel de getView
            mViewHolder = convertView.tag as MyViewHolder
        }

        // nous récupèrons l'item de la liste demandé par getView
        val listItem = getItem(position)

        // nous pouvons attribuer a nos vues les valeurs de l'élément de la liste
        DownloadImageFromInternet(mViewHolder.textViewName!!).execute(listItem.name)

        mViewHolder.textViewAge!!.text = listItem.score.toString() + ""

        // nous retournos la vue de l'item demandé
        return convertView
    }


    @SuppressLint("StaticFieldLeak")
    @Suppress("DEPRECATION")
    private inner class DownloadImageFromInternet(var imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
        init { /*Toast.makeText(context,"Please wait, it may take a few minute...",     Toast.LENGTH_SHORT).show()*/ }
        override fun doInBackground(vararg urls: String): Bitmap? {
            val imageURL = urls[0]
            var image: Bitmap? = null
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
            }
            catch (e: Exception) {
                Log.e("Error Message", e.message.toString())
                e.printStackTrace()
            }
            return image
        }
        override fun onPostExecute(result: Bitmap?) {
            imageView.setImageBitmap(result)
        }
    }


    // MyViewHolder va nous permettre de ne pas devoir rechercher
    // les vues � chaque appel de getView, nous gagnons ainsi en performance
    private inner class MyViewHolder {
        var textViewName: ImageView? = null
        var textViewAge: TextView? = null
    }

    // on passe le context afin d'obtenir un LayoutInflater pour utiliser notre
    // row_layout.xml
    // on passe les valeurs de notre a l'adapter
    init {
        this.myList = myList
        this.context = context
    }
}