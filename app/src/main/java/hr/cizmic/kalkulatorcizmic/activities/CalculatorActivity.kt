package hr.cizmic.kalkulatorcizmic.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import hr.cizmic.kalkulatorcizmic.R
import hr.cizmic.kalkulatorcizmic.databinding.ActivityCalculatorBinding
import hr.cizmic.kalkulatorcizmic.polygon.Polygon
import hr.cizmic.kalkulatorcizmic.polygon.Vertex
import hr.cizmic.kalkulatorcizmic.utils.Util


class CalculatorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalculatorBinding
    private var max_vertices: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Util.onActivityCreateSetTheme(this)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        max_vertices = binding.verticesList.childCount
        setNewVertexListeners()
        setCalculateListener()

    }

    private fun setNewVertexListeners() {
        for ((i, view) in binding.verticesList.children.withIndex()) {
            val x: EditText = view.findViewById(R.id.coord_x)
            val y: EditText = view.findViewById(R.id.coord_y)

            var condX: Boolean = false
            var condY: Boolean = false

            x.addTextChangedListener {
                if (i < max_vertices - 1) {
                    condX = !it.isNullOrEmpty()
                    if (condX and condY)
                        binding.verticesList.getChildAt(i + 1).visibility = View.VISIBLE
                    else
                        binding.verticesList.getChildAt(i + 1).visibility = View.GONE
                }
            }

            y.addTextChangedListener {
                if (i < max_vertices - 1) {
                    condY = !it.isNullOrEmpty()
                    if (condX and condY)
                        binding.verticesList.getChildAt(i + 1).visibility = View.VISIBLE
                    else
                        binding.verticesList.getChildAt(i + 1).visibility = View.GONE
                }
            }
        }
    }

    private fun setCalculateListener() {
        binding.btnCalculate.setOnClickListener {
            val newVertices: ArrayList<Vertex> = ArrayList()
            for ( view in binding.verticesList.children) {
                val x: EditText = view.findViewById(R.id.coord_x)
                val y: EditText = view.findViewById(R.id.coord_y)
                if (!x.text.isNullOrEmpty() and !y.text.isNullOrEmpty()) {
                    newVertices.add(Vertex(x.text.toString().toDouble(), y.text.toString().toDouble()))
                }
            }

            val polygon = Polygon(newVertices)
            val area = polygon.area()
            binding.draw.setPath(polygon)
            Toast.makeText(this, getString(R.string.poly_msg_1)+newVertices.size+getString(R.string.poly_msg_2)+"\n"+getString(
                R.string.poly_msg_3)+String.format("%.3f", area)+getString(R.string.poly_msg_4), Toast.LENGTH_LONG).show()

            val inputManager: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            inputManager.hideSoftInputFromWindow(
                currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )

        }
    }
}
