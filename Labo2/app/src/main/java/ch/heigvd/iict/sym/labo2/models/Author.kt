/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.labo2.models

/**
 * Classe représentant un autheur.
 * Un autheur a une identifiant unique ainsi qu'un nom.
 */
data class Author(val id: Int, val name: String) {
    override fun toString(): String {
        return "${id}) ${name}"
    }
}
