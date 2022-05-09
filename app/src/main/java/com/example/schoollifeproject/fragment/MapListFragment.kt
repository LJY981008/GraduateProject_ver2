package com.example.schoollifeproject.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.schoollifeproject.R
import com.example.schoollifeproject.adapter.MapListAdapter
import com.example.schoollifeproject.databinding.FragmentSettingBinding
import com.example.schoollifeproject.model.APIS
import com.example.schoollifeproject.model.MapContacts
import com.example.schoollifeproject.model.MapModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 맵 게시판 Fragment
 * */
class MapListFragment : Fragment() {
    private val TAG = this.javaClass.toString()
    private var contactsList: MutableList<MapContacts> = mutableListOf()
    private val adapter = MapListAdapter(contactsList)
    private lateinit var binding: FragmentSettingBinding
    private lateinit var userID: String
    val api = APIS.create()

    fun newInstance(userID: String): MapListFragment {
        val args = Bundle()
        args.putString("userID", userID)

        val mapListFragment = MapListFragment()
        mapListFragment.arguments = args

        return mapListFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userID = arguments?.getString("userID").toString()

        binding = FragmentSettingBinding.inflate(inflater, container, false)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {})

        //user 아이디
        api.map_list(
            1
        ).enqueue(object : Callback<List<MapModel>> {
            override fun onResponse(call: Call<List<MapModel>>, response: Response<List<MapModel>>) {
                val list = mutableListOf<MapContacts>()
                for (i in response.body()!!) {
                    val contacts = (
                                MapContacts(
                                    i.getMapID()
                                )
                            )
                    list.add(contacts)
                    contactsList.add(contacts)
                }
                contactsList.clear()
                contactsList.addAll(list)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<MapModel>>, t: Throwable) {
            }

        })

        adapter.setOnMapListener { view, mapID ->
            val mindMapFragment = MindMapFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            Log.d("$TAG", "userIDSend: ${userID}, ${mapID}")

            transaction?.replace(R.id.frameLayout, mindMapFragment.newInstance(userID, mapID))?.commitAllowingStateLoss()
        }

        return binding.root
    }

}