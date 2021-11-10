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
                    "\nPerson middlename: " + middlename + "\n"
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

        fun fromJson(jsonStr: String): Person {
            return Gson().fromJson(jsonStr, Person::class.java)
        }
    }
}