package ch.heigvd.iict.sym.labo2.manipulations.adapter

/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.iict.sym.labo2.R
import ch.heigvd.iict.sym.labo2.manipulations.adapter.BookListAdapter.BookViewHolder
import ch.heigvd.iict.sym.labo2.models.Book

/**
 * Adapteur d'une liste de livre.
 * Adapté de: https://developer.android.com/guide/topics/ui/layout/recyclerview
 * @param books Liste de livres à adapter.
 */
class BookListAdapter(private val books: List<Book>) :  RecyclerView.Adapter<BookViewHolder>() {

    /**
     * Crée une nouvelle vue.
     * @param parent Vue parente
     * @param viewType Type de vue
     *
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)

        return BookViewHolder(view)
    }

    /**
     * Remplace le contenu d'une vue.
     * @param holder Vue à mettre à jour.
     * @param position Index de référence.
     */
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bookItemView.text = books[position].title
    }

    /**
     * Accède à la taille de la liste de livres.
     */
    override fun getItemCount() = books.size

    /**
     * Fourni une référence sur le type de vue utilisée.
     */
    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookItemView : TextView

       init {
           bookItemView = itemView.findViewById(R.id.textView)
       }
    }
}