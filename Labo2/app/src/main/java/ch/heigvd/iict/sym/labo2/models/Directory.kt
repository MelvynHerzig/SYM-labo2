package ch.heigvd.iict.sym.labo2.models

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement
data class Directory(
    @JacksonXmlProperty
    val person : Person) {
}