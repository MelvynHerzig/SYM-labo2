package ch.heigvd.iict.sym.labo2.models

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

/**
 * Classe utile pour la sérialisation en XML afin de respecter la DTD
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */
@JacksonXmlRootElement(localName = "directory")
data class Directory(
    @JacksonXmlProperty
    val person : Person) {
}