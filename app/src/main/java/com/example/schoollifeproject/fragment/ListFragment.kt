package com.example.schoollifeproject.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.schoollifeproject.WriteNoticeActivity
import com.example.schoollifeproject.adapter.ContactsListAdapter
import com.example.schoollifeproject.databinding.FragmentListBinding
import com.example.schoollifeproject.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var contactsList: MutableList<Contacts> = mutableListOf()

    private val adapter = ContactsListAdapter(contactsList)

    private lateinit var getResult: ActivityResultLauncher<Intent>
    private lateinit var binding: FragmentListBinding

    private val api = APIS.create()
    private var id: String? = null
    private var countKey: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false)

        binding.recyclerView.adapter = adapter

        id = arguments?.getString("ID").toString()


        api.bbs_load(
            0
        ).enqueue(object : Callback<List<Bbs>> {
            override fun onResponse(call: Call<List<Bbs>>, response: Response<List<Bbs>>) {
                val list = mutableListOf<Contacts>()
                for (i in response.body()!!) {
                    if (i.getBbsAvailable() == 0 || id == i.getBbsWriter()) {
                        val contacts = (
                                Contacts(
                                    i.getBbsKey(),
                                    i.getBbsTitle(),
                                    i.getBbsWriter(),
                                    i.getBbsDate(),
                                    i.getBbsContent(),
                                    i.getBbsAvailable(),
                                    id!!
                                )
                                )
                        list.add(contacts)
                    }
                    countKey++

                }
                contactsList.clear()
                contactsList.addAll(list)
                adapter.notifyDataSetChanged()

            }

            override fun onFailure(call: Call<List<Bbs>>, t: Throwable) {

            }


        })


        getResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                Log.d("리절트", "111")
                val title = it.data?.getStringExtra("Title").toString()
                val contents = it.data?.getStringExtra("Contents").toString()
                val date = it.data?.getStringExtra("Date").toString()
                val key = it.data?.getIntExtra("key", 999999999)
                val contacts =
                    Contacts(
                        key!!,
                        title,
                        id!!,
                        contents,
                        date,
                        0,
                        id!!
                    )
                contactsList.add(contacts)


            }
            adapter.notifyDataSetChanged()
        }


        val addNote = binding.addNote

        addNote.setOnClickListener {

            val intent = Intent(context, WriteNoticeActivity::class.java)
            intent.apply {
                putExtra("ID", id)
            }
            getResult.launch(intent)
            Log.d("addNote", "11")
        }

        return binding.root
    }


}