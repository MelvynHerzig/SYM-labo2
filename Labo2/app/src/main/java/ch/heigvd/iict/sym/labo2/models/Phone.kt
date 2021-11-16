package ch.heigvd.iict.sym.labo2.models

import ch.heigvd.iict.sym.labo2.protobuf.DirectoryOuterClass
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText
import java.lang.Exception

/**
 * Classe Modélisant un numéro de téléphone avec un certain "type"
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */
@JacksonXmlRootElement(localName = "phone")
class Phone(
    // Décoration pour ne pas avoir une balise <number> en XML
    @JacksonXmlText(value = true)
    val number: String,
    // Décoration pour spécifier que le type doit être spécifié en attribut de <phone>
    @JacksonXmlProperty(isAttribute = true)
    val type: Type
) {


    enum class Type(
        val type: String
    ) {
        @JsonProperty("home")
        HOME("home"),

        @JsonProperty("mobile")
        MOBILE("mobile"),

        @JsonProperty("work")
        WORK("work");

        override fun toString(): String {
            return type
        }

        /**
         * Récupère la bonne valeur de l'enum en fonction de la string envoyée
         */
        companion object {
            fun from(type: String?): Type = values().find { it.type == type } ?: Type.HOME
        }
    }

    override fun toString(): String {
        // La réception de l'enum avec "MODIFIED" ne permet pas le matching, et donc type peut être null
        val typeStr = if (type == null) "" else type.type
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
    }
}