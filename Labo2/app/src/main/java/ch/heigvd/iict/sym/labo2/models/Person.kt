/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.labo2.models

import ch.heigvd.iict.sym.labo2.protobuf.DirectoryOuterClass
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.google.gson.Gson
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Classe Modélisant une personne
 */
@JacksonXmlRootElement
class Person(
    @JacksonXmlProperty
    val name: String,
    @JacksonXmlProperty
    val firstname: String,
    @JacksonXmlProperty
    val middlename: String,
    @JacksonXmlElementWrapper(useWrapping = false, localName = "phone")
    val phone: List<Phone>
) {

    override fun toString(): String {
        val s = StringBuilder()

        s.append(
            "Person name: " + name +
                    "\nPerson firstname: " + firstname +
                    "\nPerson middlename: " + middlename
        )

        for (phone in phone) {
            s.append(phone)
            s.append("\n")
        }
        return s.toString()
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }

    fun toXml(): String {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<!DOCTYPE directory SYSTEM \"http://mobile.iict.ch/directory.dtd\">" +
                XmlMapper().registerKotlinModule().writeValueAsString(Directory(this))
    }

    companion object {
        /**
         * Créé un objet Person pour le Protocol Buffer
         * en fonction des données de l'objet Person
         */
        private fun createProtobufPerson(person: Person): DirectoryOuterClass.Person {
            val protoPerson = DirectoryOuterClass.Person.newBuilder()
                .setName(person.name)
                .setFirstname(person.firstname)
                .setMiddlename(person.middlename)

            for (phone in person.phone) {
                protoPerson.addPhone(phone.createProtobufPhone())
            }

            return protoPerson.build()
        }

        /**
         * Prépare un ByteArray contenant les données de la personne
         * Le ByteArray est prêt à être envoyer
         */
        fun creatingByteArrayForProtobufData(person: Person): ByteArray {
            val directory = DirectoryOuterClass.Directory.newBuilder()

            directory.addResults(createProtobufPerson(person))

            return directory.build().toByteArray()
        }

        /**
         * Transforme un ByteArray en string en l'analysant
         */
        fun parsingDirectoryByteArrayData(byteArray: ByteArray): String {
            return DirectoryOuterClass.Directory.parseFrom(byteArray).toString()
        }

        fun fromJson(jsonStr: String): Person {
            return Gson().fromJson(jsonStr, Person::class.java)
        }

        fun fromXML(xmlStr: String): Person {
            val docb = DocumentBuilderFactory.newInstance()
            val doc = docb.newDocumentBuilder()
            val input = InputSource()
            input.setCharacterStream(StringReader(xmlStr))
            val d = doc.parse(input)
            val name = d.getElementsByTagName("name").item(0).textContent
            val middlename = d.getElementsByTagName("middlename").item(0).textContent
            val firstName = d.getElementsByTagName("firstname").item(0).textContent
            val phoneElements = d.getElementsByTagName("phone")

            var person = Person(
                name,
                firstName,
                middlename,
                mutableListOf(
                    Phone(
                        phoneElements.item(0).textContent,
                        Phone.Type.from(phoneElements.item(0).attributes.item(0).textContent)
                    ),
                    Phone(
                        phoneElements.item(1).textContent,
                        Phone.Type.from(phoneElements.item(1).attributes.item(0).textContent)
                    ),
                    Phone(
                        phoneElements.item(2).textContent,
                        Phone.Type.from(phoneElements.item(2).attributes.item(0).textContent)
                    )
                )
            )
            return person
        }
    }
}