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
                s.append("Person name: " + person.name +
                        "\nPerson firstname: " + person.firstname +
                        "\nPerson middlename: " + person.middlename)

                for (phoneNumber in person.phoneList) {
                    s.append(Phone.getDirectoryPhoneType(phoneNumber.type) +
                            " phone #: "+ phoneNumber.number + "\n")
                }
            }

            return s.toString()
        }
    }
}