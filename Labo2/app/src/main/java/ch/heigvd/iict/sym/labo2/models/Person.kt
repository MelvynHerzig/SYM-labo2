/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.labo2.models

import ch.heigvd.iict.sym.labo2.protobuf.DirectoryOuterClass

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

    /**
     * Créé un objet Person pour le Protocol Buffer
     * en fonction des données de l'objet Person
     */
    private fun createProtobufPerson() : DirectoryOuterClass.Person {
        val person = DirectoryOuterClass.Person.newBuilder()
            .setName(name)
            .setFirstname(firstname)
            .setMiddlename(middlename)

        for (phone in phones) {
            person.addPhone(phone.createProtobufPhone())
        }

        return person.build()
    }

    /**
     * Prépare un ByteArray contenant les données de la personne
     * Le ByteArray est prêt à être envoyer
     */
    fun creatingByteArrayForProtobufData() : ByteArray {
        val directory = DirectoryOuterClass.Directory.newBuilder()

        directory.addResults(createProtobufPerson())

        return directory.build().toByteArray()
    }

    companion object {
        /**
         * Transforme un ByteArray en string en l'analysant
         */
        fun parsingProtobufByteArrayData(byteArray : ByteArray) : String {
            return protobufToString(DirectoryOuterClass.Directory.parseFrom(byteArray))
        }

        /**
         * Transforme un objet Directory de Protocol Buffer en String
         */
        private fun protobufToString(directory : DirectoryOuterClass.Directory) : String {
            val s = StringBuilder()

            for (person in directory.resultsList) {
                val phonesToAdd = mutableListOf<Phone>()
                for (phoneNumber in person.phoneList) {
                    phonesToAdd.add(Phone(phoneNumber.number, Phone.getPhoneType(phoneNumber.type)))
                }
                s.append(Person(person.name, person.firstname, person.middlename, phonesToAdd))
            }

            return s.toString()
        }
    }
}