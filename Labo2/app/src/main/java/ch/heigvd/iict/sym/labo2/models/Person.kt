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
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */
@JacksonXmlRootElement
class Person(
    @JacksonXmlProperty
    val name: String,
    @JacksonXmlProperty
    val firstname: String,
    @JacksonXmlProperty
    val middlename: String,
    // Décoration permettant de ne pas wrapper les <phone> dans un élément <phones>
    @JacksonXmlElementWrapper(useWrapping = false, localName = "phone")
    val phone: List<Phone>
) {

    override fun toString(): String {
        val s = StringBuilder()

        s.append(
            "Person name: " + name +
                    "\nPerson firstname: " + firstname +
                    "\nPerson middlename: " + middlename + "\n"
        )

        for (phone in phone) {
            s.append(phone)
            s.append("\n")
        }
        return s.toString()
    }

    /**
     * Converti la Person en JSON
     */
    fun toJson(): String {
        return Gson().toJson(this)
    }

    /**
     * Converti la Person en XML
     */
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
         * Transforme un objet Directory de Protocol Buffer en String
         */
        private fun protobufDirectoryToString(directory : DirectoryOuterClass.Directory) : String {

            val s = StringBuilder()

            for (person in directory.resultsList) {

                val phonesToAdd = mutableListOf<Phone>()

                for (phoneNumber in person.phoneList) {
                    phonesToAdd.add(Phone(phoneNumber.number, Phone.getPhoneType(phoneNumber.type)))
                }

                val p = Person(person.name, person.firstname, person.middlename, phonesToAdd)
                s.append(p)
            }

            return s.toString()
        }

        /**
         * Transforme un ByteArray en string en l'analysant
         */
        fun parsingDirectoryByteArrayData(byteArray: ByteArray): String {
            return protobufDirectoryToString(DirectoryOuterClass.Directory.parseFrom(byteArray))
        }

        /**
         * Parse le JSON afin de recréer un objet Person
         */
        fun fromJson(jsonStr: String): Person {
            return Gson().fromJson(jsonStr, Person::class.java)
        }

        /**
         * Parse le XML afin de recréer un objet Person
         * Note : Obligé de parser à la main, car l'enum pose de gros problème en utilisant Jackson
         */
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