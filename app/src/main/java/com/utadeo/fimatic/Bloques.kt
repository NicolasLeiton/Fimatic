package com.utadeo.fimatic

import android.content.ClipData
import android.content.ClipDescription
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.material.snackbar.Snackbar



const val ARG_PARAM1 = "param1"
const val ARG_PARAM2 = "param2"

class Bloques : Fragment() {

    private var name: String? = null
    private var num_bloques: String? = null

    private var list_blocks : MutableMap<Int,String > = mutableMapOf()
    private var id_count:Int = 0
    private lateinit var stackArea: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(ARG_PARAM1)
            num_bloques = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bloques, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Bloques.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Bloques().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Referencias a los bloques (imágenes) y al área de apilado
        val block1: ImageView = view.findViewById(R.id.Img_whiteBlock)


        stackArea = view.findViewById(R.id.stackArea)

        // Establecer la escucha de arrastre para el área de apilado
        stackArea.setOnDragListener(DragListener())
        if (num_bloques=="2"){
            val block2: ImageView = view.findViewById(R.id.Img_rightBlock)
            block2.setVisibility(View.VISIBLE)
            block2.setOnTouchListener(TouchListener())
        }
        // Habilitar arrastrar para cada bloque
        block1.setOnTouchListener(TouchListener())

    }

    // Listener para manejar el evento de arrastre cuando se toca el bloque
    private inner class TouchListener : View.OnTouchListener {
        override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
            if (motionEvent?.action == MotionEvent.ACTION_DOWN) {
                // Crear datos de arrastre
                val clipText = "This is a block"
                val item = ClipData.Item(clipText)
                val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
                val dragData = ClipData(clipText, mimeTypes, item)

                // Crear una sombra del bloque durante el arrastre
                val dragShadow = View.DragShadowBuilder(view)

                // Iniciar el arrastre
                view?.startDragAndDrop(dragData, dragShadow, view, 0)
                return true
            }
            return false
        }
    }

    fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
    // Listener para manejar el evento de soltar el bloque en el área de apilado
    private inner class DragListener : View.OnDragListener {
        override fun onDrag(view: View?, event: DragEvent?): Boolean {
            when (event?.action) {
                DragEvent.ACTION_DRAG_STARTED -> return true
                DragEvent.ACTION_DRAG_ENTERED -> return true
                DragEvent.ACTION_DRAG_EXITED -> return true
                DragEvent.ACTION_DROP -> {
                    // Aquí se maneja cuando el bloque se suelta en el área de apilado
                    val draggedView = event?.localState as View

                    // Crear una copia del bloque arrastrado
                    val newBlock = ImageView(requireContext())

                    list_blocks [id_count] = (draggedView.contentDescription.toString()) //Añadir al map


                    newBlock.setImageDrawable((draggedView as ImageView).drawable)
                    newBlock.setId(id_count)
                    id_count++

                    newBlock.layoutParams = LinearLayout.LayoutParams(
                        120.dpToPx(),
                        60.dpToPx()
                    )

                    // Ajustar márgenes para separar los bloques apilados
                    val layoutParams = newBlock.layoutParams as LinearLayout.LayoutParams
                    layoutParams.setMargins(0, -16, 0, 0)
                    newBlock.layoutParams = layoutParams

                    // Añadir funcionalidad de eliminación por toque largo
                    newBlock.setOnLongClickListener {
                        list_blocks.remove(newBlock.getId()) //Eliminar del map
                        // Eliminar el bloque del área de apilado
                        stackArea.removeView(newBlock)
                        Snackbar.make(stackArea, "Bloque eliminado", Snackbar.LENGTH_SHORT).show()
                        true
                    }

                    // Añadir el nuevo bloque al área de apilado
                    stackArea.addView(newBlock)
                    return true
                }
                DragEvent.ACTION_DRAG_ENDED -> return true
                else -> return false
            }
        }
    }
}