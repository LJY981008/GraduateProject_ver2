package com.example.schoollifeproject

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.schoollifeproject.databinding.ActivityMindMapBinding
import me.jagar.mindmappingandroidlibrary.Views.Item
import me.jagar.mindmappingandroidlibrary.Views.MindMappingView
import me.jagar.mindmappingandroidlibrary.Zoom.ZoomLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


// itemId 설정 / parent 아이디 / item 아이디 따로 표기

class MindMapActivity : AppCompatActivity() {
    val api = APIS_login.create()

    private lateinit var binding: ActivityMindMapBinding
    private lateinit var mindMappingView: MindMappingView
    private lateinit var rootItem: Item
    private lateinit var grade1: Item
    private lateinit var grade2: Item
    private lateinit var grade3: Item
    private lateinit var grade4: Item
    private lateinit var mItem: Item

    private var childItem = ArrayList<Item>()
    private var childItemNum = ArrayList<Int>()
    private var childItemNumCount: Int = 0
    private var childItemNumMax: Int = 0

    private lateinit var zoomLayout: ZoomLayout
    private lateinit var detector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMindMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mindMappingView = binding.mindMappingView
        zoomLayout = binding.zoomLayout

        mindMappingView.layoutParams.width = 3000
        mindMappingView.layoutParams.height = 2000

        detector = GestureDetector(this,
            object : GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
                override fun onDown(event: MotionEvent): Boolean {
                    Log.d("DEBUG_TAG", "onDown")
                    return true
                }

                override fun onFling(
                    event1: MotionEvent,
                    event2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float,
                ): Boolean {
                    Log.d("DEBUG_TAG", "onFling: $event1 $event2")
                    return true
                }

                override fun onLongPress(event: MotionEvent) {
                    Log.d("DEBUG_TAG", "onLongPress: $event")
                    // 롱 클릭 시 드래그 이벤트
                    if (mItem != rootItem && mItem != grade1 &&
                        mItem != grade2 && mItem != grade3 && mItem != grade4
                    ) {
                        dragItem(mItem)
                    }
                }

                override fun onScroll(
                    event1: MotionEvent,
                    event2: MotionEvent,
                    distanceX: Float,
                    distanceY: Float,
                ): Boolean {
                    Log.d("DEBUG_TAG", "onScroll: $event1 $event2")
                    return true
                }

                override fun onShowPress(event: MotionEvent) {
                    Log.d("DEBUG_TAG", "onShowPress: $event")
                }

                override fun onSingleTapUp(event: MotionEvent): Boolean {
                    Log.d("DEBUG_TAG", "onSingleTapUp: $event")
                    return true
                }

                override fun onDoubleTap(event: MotionEvent): Boolean {
                    Log.d("DEBUG_TAG", "onDoubleTap: $event")
                    // 더블 클릭 시 팝업 메뉴
                    if (mItem != rootItem && mItem != grade1 &&
                        mItem != grade2 && mItem != grade3 && mItem != grade4) {
                        checkItem(mItem)
                    }
                    return true
                }

                override fun onDoubleTapEvent(event: MotionEvent): Boolean {
                    Log.d("DEBUG_TAG", "onDoubleTapEvent: $event")
                    return true
                }

                override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
                    Log.d("DEBUG_TAG", "onSingleTapConfirmed: ${mItem.itemId}")
                    // 한 번 클릭 시 하단 메뉴
                    if (mItem != rootItem) {
                        bottomEvent(mItem)
                    }
                    return true
                }
            })

        rootItem = Item(this, "Root", "root", null, true)
        mindMappingView.addCentralItem(rootItem, false)
        rootItem.setOnTouchListener { _, motionEvent ->
            mItem = rootItem
            detector.onTouchEvent(motionEvent)
            true
        }

        grade1 = Item(this, "1학년", "grade1", null, true)
        grade2 = Item(this, "2학년", "grade2", null, true)
        grade3 = Item(this, "3학년", "grade3", null, true)
        grade4 = Item(this, "4학년", "grade4", null, true)

        mindMappingView.addItem(
            grade1, rootItem, 200, 15,
            0, false, null)
        mindMappingView.addItem(
            grade2, rootItem, 200, 15,
            1, false, null)
        mindMappingView.addItem(
            grade3, rootItem, 200, 15,
            2, false, null)
        mindMappingView.addItem(
            grade4, rootItem, 200, 15,
            3, false, null)

        grade1.setOnTouchListener { _, motionEvent ->
            mItem = grade1
            detector.onTouchEvent(motionEvent)
            true
        }
        grade2.setOnTouchListener { _, motionEvent ->
            mItem = grade2
            detector.onTouchEvent(motionEvent)
            true
        }
        grade3.setOnTouchListener { _, motionEvent ->
            mItem = grade3
            detector.onTouchEvent(motionEvent)
            true
        }
        grade4.setOnTouchListener { _, motionEvent ->
            mItem = grade4
            detector.onTouchEvent(motionEvent)
            true
        }

        val userID = intent.getStringExtra("ID").toString()

        api.item_load(userID)
            .enqueue(object : Callback<PostModel> {
                override fun onResponse(
                    call: Call<PostModel>,
                    response: Response<PostModel>,
                ) {
                    response.body()?.itemID.toString()
                }

                override fun onFailure(call: Call<PostModel>, t: Throwable) {
                    TODO("Not yet implemented")
                    Log.d("d", t.message.toString())
                }
            })

        Log.d("DEBUG_TAG", "LoginID: ${userID}")
    }

    private fun dragItem(item: Item) {
        val dragItem = ClipData.Item(item.tag as? CharSequence)
        val dragData = ClipData(
            item.tag as? CharSequence,
            arrayOf(ClipDescription.MIMETYPE_TEXT_INTENT),
            dragItem
        )
        val myShadow = MyDragShadowBuilder(item)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            item.startDragAndDrop(
                dragData,
                myShadow,
                null, 0
            )
        }
        mindMappingView.setOnDragListener { view, dragEvent ->
            Log.d("DEBUG_TAG", "setOnDragListener")
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    Log.d("DEBUG_TAG", "DragEvent.ACTION_DRAG_STARTED")
                }
                DragEvent.ACTION_DROP -> { // 드롭 기능 구현해보자
                    val map = view as MindMappingView
                    for (index in 0 until map.childCount) {
                        val i = (map.getChildAt(index) as Item)

                        val x = dragEvent.x
                        val y = dragEvent.y

                        if ((x > i.x - i.width && x < i.x + i.width) && (y > i.y - i.height && y < i.y + i.height)) {
                            Log.d("DEBUG_TAG", "DragEvent.ACTION_DROP Item: ${i.title.text} ${i.itemId}")
                            val it = Item(this@MindMapActivity,
                                "${item.title.text}", "d", null, true)
                            mindMappingView.deleteItem(item)

                            if (i.location == 0 && item.location != 0) {
                                addItem(grade1, it)
                            } else if (i.location == 1 && item.location != 1) {
                                addItem(grade2, it)
                            } else if (i.location == 2 && item.location != 2) {
                                addItem(grade3, it)
                            } else if (i.location == 3 && item.location != 3) {
                                addItem(grade4, it)
                            }
                        }
                    }
                }
            }
            true
        }
    }

    private fun addItem(parent: Item, child: Item) { // mindMappingView 아이템 추가

        childItem.add(child)
        mindMappingView.addItem(child, parent, 150,
            20, parent.location, true, null)
        child.setOnTouchListener { _, motionEvent ->
            mItem = child
            detector.onTouchEvent(motionEvent)
            true
        }



        itemLocation(parent, child) // item 위치 설정
        binding.bottomNavigationView.visibility = View.INVISIBLE
    }

    private fun bottomEvent(node: Item) {
        if (node == rootItem || node == grade1 ||
            node == grade2 || node == grade3 || node == grade4) {
            binding.bottomNavigationView.menu.findItem(R.id.bottomMenu2).isVisible = false
        } else {
            binding.bottomNavigationView.menu.findItem(R.id.bottomMenu2).isVisible = true
        }

        binding.bottomNavigationView.visibility = View.VISIBLE
        binding.bottomNavigationView.run {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.bottomMenu1 -> {
                        setItem(node, false)
                        true
                    }
                    R.id.bottomMenu2 -> { // 노드 내용 변경, Edit 버튼
                        setItem(node, true)
                        true
                    }
                    R.id.bottomMenu3 -> { // 노드 내용 변경, Edit 버튼
                        mindMappingView.deleteItem(node)
                        true
                    }
                    else -> {
                        true
                    }
                }
            }
        }
    }

    private fun itemLocation(parent: Item, child: Item) { // item location 재설정
        child.x -= rootItem.x
        if (parent.location == 1 || parent.location == 2) {
            when {
                parent.leftChildItems.size > 1 -> {
                    child.y -= rootItem.y
                }
                parent.rightChildItems.size > 1 -> {
                    child.y -= rootItem.y
                }
            }
        } else {
            child.y -= rootItem.y
        }
    }

    private fun setItem(node: Item, modified: Boolean) {
        val setWindow: View =
            LayoutInflater.from(this@MindMapActivity).inflate(R.layout.window_item_set,null)
        val itemSetWindow = PopupWindow(
            setWindow,
            ((getApplicationContext().getResources().getDisplayMetrics().widthPixels) * 0.8).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        val setButton: Button = setWindow.findViewById(R.id.itemSetButton)
        val setTitle: EditText = setWindow.findViewById(R.id.setTitleView)
        val setContent: EditText = setWindow.findViewById(R.id.setContentView)
        if (modified) {
            setTitle.setText(node.title.text)
            setContent.setText(node.content)
            setButton.text = "수정"
        } else {
            setButton.text = "추가"
        }

        itemSetWindow.isFocusable =true
        itemSetWindow.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        itemSetWindow.update()
        itemSetWindow.showAtLocation(setWindow, Gravity.CENTER,0,0)

        itemSetWindow.isOutsideTouchable =true
        itemSetWindow.setTouchInterceptor {_, motionEvent ->
            if(motionEvent.action == MotionEvent.ACTION_OUTSIDE){
                itemSetWindow.dismiss()
            }
            false
        }

        setButton.setOnClickListener {
            val title = setTitle.text.toString()
            val content = setContent.text.toString()

            if (title != "" && content != "") {
                if(modified) {
                    node.title.text = title
                    node.content = content
                } else {
                    childItemNum.add(childItemNumMax)
                    val child =
                        Item(this@MindMapActivity, title,
                        "${node.itemId}_item${childItemNum[childItemNumMax++]}",
                        content,
                        true)

                    addItem(node, child)
                }

                itemSetWindow.dismiss()
            } else {
                Toast.makeText(
                    this, "제목(내용)이 비어있습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkItem(node: Item) {
        val checkWindow: View = LayoutInflater.from(this@MindMapActivity).inflate(R.layout.window_item_view,null)
        val itemCheckWindow = PopupWindow(
            checkWindow,
            ((getApplicationContext().getResources().getDisplayMetrics().widthPixels) * 0.8).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        val checkButton: Button = checkWindow.findViewById(R.id.itemCheckButton)
        val checkTitle: TextView = checkWindow.findViewById(R.id.checkTitleView)
        val checkContent: TextView = checkWindow.findViewById(R.id.checkContentView)

        checkTitle.text = node.title.text
        checkContent.text = node.content

        itemCheckWindow.isFocusable =true
        itemCheckWindow.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        itemCheckWindow.update()
        itemCheckWindow.showAtLocation(checkWindow, Gravity.CENTER,0,0)

        itemCheckWindow.isOutsideTouchable =true
        itemCheckWindow.setTouchInterceptor {_, motionEvent ->
            if(motionEvent.action == MotionEvent.ACTION_OUTSIDE){
                itemCheckWindow.dismiss()
            }
            false
        }

        checkButton.setOnClickListener {
            itemCheckWindow.dismiss()
        }
    }
}

private class MyDragShadowBuilder(v: View) : View.DragShadowBuilder(v) {

    private val shadow = ColorDrawable(Color.LTGRAY)

    // Defines a callback that sends the drag shadow dimensions and touch point
    // back to the system.
    override fun onProvideShadowMetrics(size: Point, touch: Point) {

        // Set the width of the shadow to half the width of the original View.
        val width: Int = view.width

        // Set the height of the shadow to half the height of the original View.
        val height: Int = view.height

        // The drag shadow is a ColorDrawable. This sets its dimensions to be the
        // same as the Canvas that the system provides. As a result, the drag shadow
        // fills the Canvas.
        shadow.setBounds(0, 0, width, height)

        // Set the size parameter's width and height values. These get back to
        // the system through the size parameter.
        size.set(width, height)

        // Set the touch point's location to be in the middle of the drag shadow.
        touch.set(width / 2, height / 2)
    }

    // Defines a callback that draws the drag shadow in a Canvas that the system
    // constructs from the dimensions passed to onProvideShadowMetrics().
    override fun onDrawShadow(canvas: Canvas) {

        // Draw the ColorDrawable on the Canvas passed in from the system.
        shadow.draw(canvas)
    }
}
