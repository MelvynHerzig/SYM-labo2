package ch.heigvd.iict.sym.labo2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ch.heigvd.iict.sym.labo2.manipulations.AsyncActivity

/**
 * Activité principale permettant de lancer les 5 activités
 * permettant de tester dufférents protocoles de communication applicatifs.
 * Respectivement: asynchrone, retardé, sérialisé, compressé et graphQL.
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */
class MainActivity : AppCompatActivity() {

    // Référence sur le bouton asynchrone.
    protected lateinit var asyncButton: Button

    // Référence sur le bouton retardé.
    protected lateinit var delayedButton: Button

    // Référence sur le bouton sérialisé.
    protected lateinit var serializedButton: Button

    // Référence sur le bouton compressé.
    protected lateinit var compressedButton: Button

    // Référence sur le bouton graphQL
    protected lateinit var graphqlButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Linkage des boutons selon layout
        asyncButton      = findViewById(R.id.main_btn_async)
        delayedButton    = findViewById(R.id.main_btn_delayed)
        serializedButton = findViewById(R.id.main_btn_serialized)
        compressedButton = findViewById(R.id.main_btn_compressed)
        graphqlButton    = findViewById(R.id.main_btn_graphql)

        // Mise en place des listeners
        asyncButton.setOnClickListener{
            startActivity(Intent(this, AsyncActivity::class.java))
        }

        delayedButton.setOnClickListener{
            startActivity(Intent(this, AsyncActivity::class.java))
        }

        serializedButton.setOnClickListener{
            startActivity(Intent(this, AsyncActivity::class.java))
        }

        compressedButton.setOnClickListener{
            startActivity(Intent(this, AsyncActivity::class.java))
        }

        graphqlButton.setOnClickListener{
            startActivity(Intent(this, AsyncActivity::class.java))
        }
    }
}