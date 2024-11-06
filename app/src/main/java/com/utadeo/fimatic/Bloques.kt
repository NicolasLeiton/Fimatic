package com.utadeo.fimatic

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar

import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.getkeepsafe.taptargetview.TapTargetView


const val ARG_PARAM1 = "param1"
const val ARG_PARAM2 = "param2"

class Bloques : Fragment() {

    private var nivel: String? = null
    private var num_bloques: Int? = null

    private var list_blocks : MutableMap<Int,String > = mutableMapOf()
    private var id_count:Int = 0
    private lateinit var stackArea: LinearLayout

    private lateinit var viewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            nivel = it.getString(ARG_PARAM1)
            num_bloques = it.getInt(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bloques, container, false)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireActivity() //Contexto de la activity
        // Referencias a los bloques (imágenes) y al área de apilado
        val block1: ImageView = view.findViewById(R.id.Img_whiteBlock)
        val block2: ImageView = view.findViewById(R.id.Img_rightBlock)
        val block3: ImageView = view.findViewById(R.id.Img_leftBlock)
        val blockinicio: ImageView = view.findViewById(R.id.Img_inicio)
        val startBut: ImageButton = view.findViewById(R.id.Start_Button)

        // Obtener el ViewModel compartido
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        // Actualizar los datos cuando sea necesario
        startBut.setOnClickListener {
            viewModel.setData(crear_instrucciones())
        }


        stackArea = view.findViewById(R.id.stackArea)

        // Establecer la escucha de arrastre para el área de apilado
        stackArea.setOnDragListener(DragListener())

        // Habilitar arrastrar para cada bloque
        block1.setOnTouchListener(TouchListener())

        if (num_bloques!! >=2){ //Verificar cuantos bloques necesita la activity
            block2.visibility = View.VISIBLE
            block2.setOnTouchListener(TouchListener())
        }
        if (num_bloques!! >=3){
            block3.visibility = View.VISIBLE
            block3.setOnTouchListener(TouchListener())
        }

        if (nivel=="Level1"){

            val img_car:ImageView = context.findViewById(R.id.carImg)
            val img_end:ImageView = context.findViewById(R.id.img_destino)
            TapTargetSequence(context)
                .targets(
                    TapTarget.forView(img_car, "Este es tu carrito :)", "Será el vehículo que manejes")
                        .outerCircleColor(R.color.cyan)  // Color del círculo exterior
                        .targetCircleColor(R.color.white)       // Color del círculo interior
                        .titleTextSize(20)                      // Tamaño del texto del título
                        .descriptionTextSize(18)                // Tamaño del texto de la descripción
                        .transparentTarget(true)  ,

                    TapTarget.forView(img_end, "Destino", "Tu objetivo sera llevar tu carro hasta este punto")
                        .outerCircleColor(R.color.cyan_2)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(20)
                        .descriptionTextSize(18)
                        .transparentTarget(true),

                    TapTarget.forView(block1, "Bloque mover adelante", "Arrastra este bloque hasta la parte de 'Tu lógica' para darle la instrucción al carro")
                        .outerCircleColor(R.color.cyan)
                        .targetCircleColor(R.color.purpura)
                        .titleTextSize(20)
                        .descriptionTextSize(18)
                        .transparentTarget(true)  ,

                    TapTarget.forView(blockinicio, "Quitar un bloque", "Para quitar un bloque que hayas agregado, basta con mantenerlo presionado unos segundos")
                        .outerCircleColor(R.color.cyan_2)
                        .targetCircleColor(R.color.purpura_3)
                        .titleTextSize(20)
                        .descriptionTextSize(18)
                        .transparentTarget(true)
                )
                .start()
        }
        else if (nivel=="Level2"){
            TapTargetView.showFor(context,
                TapTarget.forView(block2, "Giro a la derecha", "¡Bien ya tienes un nuevo bloque! Tendrás que hacer tu carro girar si quieres llegar al objetivo esta vez :3")
                .outerCircleColor(R.color.cyan)
                .targetCircleColor(R.color.white)
                .titleTextSize(20)
                .descriptionTextSize(18)
                .transparentTarget(true)
            )
        }
        else if (nivel=="Level3"){
            TapTargetView.showFor(context,
                TapTarget.forView(block3, "Giro a la izquierda", "¡Bien ya puedes girar a la izquierda! \nEso de tener el volante bloqueado me estaba aburriendo UnU...")
                    .outerCircleColor(R.color.cyan)
                    .targetCircleColor(R.color.cyan_3)
                    .titleTextSize(20)
                    .descriptionTextSize(18)
                    .transparentTarget(true)
            )
        }



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

    fun crear_instrucciones(): String {
        var instrucciones:String =""
        for (i in list_blocks.values){
            instrucciones += i + " "
        }
        instrucciones= instrucciones.dropLast(1)
        return instrucciones

    }
}