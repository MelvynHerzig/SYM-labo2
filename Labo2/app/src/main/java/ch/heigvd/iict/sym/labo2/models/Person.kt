/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.labo2.models

import ch.heigvd.iict.sym.labo2.protobuf.DirectoryOuterClass
import com.google.gson.Gson

/**
 * Classe Modélisant une personne
 */
class Person(val name: String, val firstname:String, val middlename: String, val phones : MutableList<Phone>) {

    override fun toString(): String {
        val s = StringBuilder()

        s.append("Person name: " + name +
                "\nPerson firstname: " + firstname +
                "\nPerson middlename: " + middlename)

        for (phone in phones) {
            s.append(phone)
            s.append("\n")
        }
        return s.toString()
    }

    fun toJson() : String {
        return Gson().toJson(this)
    }

    companion object {
        /**
         * Créé un objet Person pour le Protocol Buffer
         * en fonction des données de l'objet Person
         */
        private fun createProtobufPerson(person : Person) : DirectoryOuterClass.Person {
            val protoPerson = DirectoryOuterClass.Person.newBuilder()
                .setName(person.name)
                .setFirstname(person.firstname)
                .setMiddlename(person.middlename)

            for (phone in person.phones) {
                protoPerson.addPhone(phone.createProtobufPhone())
            }

            return protoPerson.build()
        }

        /**
         * Prépare un ByteArray contenant les données de la personne
         * Le ByteArray est prêt à être envoyer
         */
        fun creatingByteArrayForProtobufData(person : Person) : ByteArray {
            val directory = DirectoryOuterClass.Directory.newBuilder()

            directory.addResults(createProtobufPerson(person))

            return directory.build().toByteArray()
        }

        /**
         * Transforme un ByteArray en string en l'analysant
         */
        fun parsingDirectoryByteArrayData(byteArray : ByteArray) : String {
            return DirectoryOuterClass.Directory.parseFrom(byteArray).toString()
        }

        fun fromJson(jsonStr : String) : Person{
            return Gson().fromJson(jsonStr, Person::class.java)
        }
    }
}