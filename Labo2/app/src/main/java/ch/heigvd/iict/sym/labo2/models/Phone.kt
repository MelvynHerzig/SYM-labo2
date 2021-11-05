/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.labo2.models

import ch.heigvd.iict.sym.labo2.protobuf.DirectoryOuterClass

/**
 * Classe Modélisant un numéro de téléphone avec un certain "type"
 */
class Phone(val number: String, val type: Type) {
    enum class Type{
        HOME,
        MOBILE,
        WORK
    }

    /**
     * Créé un objet Phone pour le Protocol Buffer
     * en fonction des données de l'objet Phone
     */
    fun createProtobufPhone() : DirectoryOuterClass.Phone {
        return DirectoryOuterClass.Phone.newBuilder()
            .setType(getDirectoryPhoneType(type))
            .setNumber(number)
            .build()
    }

    companion object {
        /**
         * Réalise la correspondance entre le Type du Protocol Buffer et le type du téléphone
         */
        fun getDirectoryPhoneType(type : Type) : DirectoryOuterClass.Phone.Type {
            return when (type) {
                Type.HOME -> DirectoryOuterClass.Phone.Type.HOME
                Type.MOBILE -> DirectoryOuterClass.Phone.Type.MOBILE
                Type.WORK -> DirectoryOuterClass.Phone.Type.WORK
            }
        }

        /**
         * Retourne un string en fonction Type de téléphone du Protocol Buffer
         */
        fun getDirectoryPhoneType(type : DirectoryOuterClass.Phone.Type) : String {
            return when (type) {
                DirectoryOuterClass.Phone.Type.HOME -> "Mobile"
                DirectoryOuterClass.Phone.Type.MOBILE -> "Home"
                DirectoryOuterClass.Phone.Type.WORK -> "Work"
                else -> "Unknown Phone type"
            }
        }
    }
}