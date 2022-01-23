package com.example.schoollifeproject

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.example.schoollifeproject.databinding.ActivityMindMapBinding
import me.jagar.mindmappingandroidlibrary.Views.Item
import me.jagar.mindmappingandroidlibrary.Views.ItemLocation
import me.jagar.mindmappingandroidlibrary.Views.MindMappingView
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import me.jagar.mindmappingandroidlibrary.Zoom.ZoomLayout
import java.util.*
import kotlin.collections.ArrayList

// 뷰 충돌 이벤트? + 삭제 기능 구현 + onefingerscroll

class MindMapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMindMapBinding
    private lateinit var mindMappingView: MindMappingView
    private lateinit var rootNode: Item
    private lateinit var freshman: Item
    private lateinit var sophomore: Item
    private lateinit var junior: Item
    private lateinit var senior: Item
    private lateinit var zoomLayout: ZoomLayout
    private var childNode = ArrayList<Item>()
    private var childInfo = HashMap<Item, Int>()
    private var childNodeNum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMindMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mindMappingView = binding.mindMappingView
        zoomLayout = binding.zoomLayout

        val displaymetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displaymetrics)
        val screenWidth = displaymetrics.widthPixels
        val screenHeight = displaymetrics.heightPixels

        mindMappingView.layoutParams.width = (screenWidth * 2.5).toInt()
        mindMappingView.layoutParams.height = (screenHeight * 2.5).toInt()

        addRoot()
        nodeEvent()
    }

    private fun addRoot() {
        rootNode = Item(this, "Root", "Hello", true)
        mindMappingView.addCentralItem(rootNode, false)

        freshman = Item(this, "1st", "Hello", true)
        sophomore = Item(this, "2nd", "Hello", true)
        junior = Item(this, "3rd", "Hello", true)
        senior = Item(this, "4st", "Hello", true)

        mindMappingView.addItem(
            freshman, rootNode, 200, 15,
            ItemLocation.TOP, true, null)
        mindMappingView.addItem(
            sophomore, rootNode, 200, 15,
            ItemLocation.LEFT, true, null)
        mindMappingView.addItem(
            junior, rootNode, 200, 15,
            ItemLocation.RIGHT, true, null)
        mindMappingView.addItem(
            senior, rootNode, 200, 15,
            ItemLocation.BOTTOM, true, null)
    }

    private fun nodeEvent() {
        rootNode.setOnClickListener {
            popupEventR(rootNode)
        }
        mindMappingView.setOnItemClicked { item ->
            if (item == freshman) {
                bottomEvent(item, ItemLocation.TOP)
            } else if (item == sophomore) {
                bottomEvent(item, ItemLocation.LEFT)
            } else if (item == junior) {
                bottomEvent(item, ItemLocation.RIGHT)
            } else if (item == senior) {
                bottomEvent(item, ItemLocation.BOTTOM)
            } else {
                childInfo[item]?.let { bottomEvent(item, it) }
                popupEvent(item)
            }
        }
    }

    private fun bottomEvent(node: Item, position: Int) {
        binding.bottomNavigationView.visibility = View.VISIBLE
        binding.bottomNavigationView.run {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.childNodeAdd -> {
                        childNode.add(Item(
                            this@MindMapActivity,
                            "Child",
                            "Hi",
                            true))
                        childInfo[childNode[childNodeNum]] = position
                        mindMappingView.addItem(
                            childNode[childNodeNum],
                            node,
                            150,
                            20,
                            position,
                            true,
                            null
                        )
                        nodePosition(node, position)
                        binding.bottomNavigationView.visibility = View.INVISIBLE
                        true
                    }
                    else -> {
                        true
                    }
                }
            }
        }
    }

    private fun nodePosition(parent: Item, position : Int) {
        childNode[childNodeNum].x -= rootNode.x
        if (position == 1 || position == 2) {
            when {
                parent.leftChildItems.size > 1 -> {
                    childNode!![childNodeNum].y -= rootNode.y
                }
                parent.rightChildItems.size > 1 -> {
                    childNode!![childNodeNum].y -= rootNode.y
                }
            }
        } else {
            childNode!![childNodeNum].y -= rootNode.y
        }
        childNodeNum++
    }

    private fun popupEventR(node: Item) {
        val popUp = PopupMenu(node!!.context, node) //v는 클릭된 뷰를 의미
        popUp.menuInflater.inflate(R.menu.popup_menu_root, popUp.menu)
        popUp.setOnMenuItemClickListener { items ->
            when (items.itemId) {
                R.id.popupMenuR1 -> { // 노드 내용 변경, Edit 버튼
                    editNode(node)
                }
                R.id.popupMenuR2 -> {
                }
                R.id.popupMenuR3 -> {
                }
                else -> {
                }
            }
            false
        }
        popUp.show() //Popup Menu 보이기

    }

    private fun popupEvent(node: Item) {
        val popUp = PopupMenu(node!!.context, node) //v는 클릭된 뷰를 의미
        popUp.menuInflater.inflate(R.menu.popup_menu, popUp.menu)
        popUp.setOnMenuItemClickListener { items ->
            when (items.itemId) {
                R.id.popupMenuR1 -> { // 노드 내용 변경, Edit 버튼
                    editNode(node)
                }
                R.id.popupMenu2 -> { // 노드 내용 삭제, Delete 버튼
                    deleteNode(node)
                }
                R.id.popupMenuR2 -> {
                }
                R.id.popupMenuR3 -> {
                }
                else -> {
                }
            }
            false
        }
        popUp.show() //Popup Menu 보이기
    }

    private fun editNode(node: Item) {
        binding.editNode.setText(node.title.text)
        binding.editNode.visibility = View.VISIBLE
        binding.editNode.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                //Enter key Action
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    //키패드 내리기
                    val imm: InputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(
                        binding.editNode.windowToken, 0
                    )
                    //처리
                    node.title.text = binding.editNode.text
                    binding.editNode.visibility = View.INVISIBLE
                    return true
                }
                return false
            }
        })
    }

    private fun deleteNode(node: Item) {
        node.removeAllViews()
    }

}