/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.labo2.models

/**
 * Classe modélisant un livre d'autheur.
 * Une livre est caractérisé par un id (unique) et un titre.
 */
data class Book(val id: Int, val title: String) {}