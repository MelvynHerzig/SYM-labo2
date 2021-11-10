/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.labo2.models

import ch.heigvd.iict.sym.labo2.protobuf.DirectoryOuterClass
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText
import java.lang.Exception

/**
 * Classe Modélisant un numéro de téléphone avec un certain "type"
 */
@JacksonXmlRootElement(localName = "phone")
class Phone(
    @JacksonXmlText(value=true)
    val number: String,
    @JacksonXmlProperty(isAttribute = true)
    val type: Type) {


    enum class Type(
        val type: String) {
        @JsonProperty("home")
        HOME("home"),
        @JsonProperty("mobile")
        MOBILE("mobile"),
        @JsonProperty("work")
        WORK("work");

        override fun toString(): String {
            return type
        }
    }

    override fun toString(): String {

        val typeStr = if (type == null)  "" else getStringPhoneType(type)
        return "Phone #: $number $typeStr"
    }

    /**
     * Créé un objet Phone pour le Protocol Buffer
     * en fonction des données de l'objet Phone
     */
    fun createProtobufPhone(): DirectoryOuterClass.Phone {
        return DirectoryOuterClass.Phone.newBuilder()
            .setType(getDirectoryPhoneType(type))
            .setNumber(number)
            .build()
    }

    companion object {
        /**
         * Réalise la correspondance entre le Type du Protocol Buffer et le type du téléphone
         */
        fun getDirectoryPhoneType(type: Type): DirectoryOuterClass.Phone.Type {
            return when (type) {
                Type.HOME -> DirectoryOuterClass.Phone.Type.HOME
                Type.MOBILE -> DirectoryOuterClass.Phone.Type.MOBILE
                Type.WORK -> DirectoryOuterClass.Phone.Type.WORK
            }
        }

        /**
         * Retourne le Phone Type en fonction Type de téléphone du Protocol Buffer
         */
        fun getPhoneType(type: DirectoryOuterClass.Phone.Type): Type {
            return when (type) {
                DirectoryOuterClass.Phone.Type.HOME -> Type.HOME
                DirectoryOuterClass.Phone.Type.MOBILE -> Type.MOBILE
                DirectoryOuterClass.Phone.Type.WORK -> Type.WORK
                else -> throw Exception("Bad Phone Type")
            }
        }

        /**
         * Retourne un string en fonction Type de téléphone du Protocol Buffer
         */
        fun getStringPhoneType(type: Type): String {
            return when (type) {
                Type.HOME -> "Home"
                Type.MOBILE -> "Mobile"
                Type.WORK -> "Work"
            }
        }
    }
}