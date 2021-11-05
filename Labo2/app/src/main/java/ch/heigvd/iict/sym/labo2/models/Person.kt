package ch.heigvd.iict.sym.labo2.models

import ch.heigvd.iict.sym.labo2.protobuf.DirectoryOuterClass
import java.io.ByteArrayOutputStream
import java.io.FileInputStream;
import java.io.OutputStream

class Person(val name: String, val firstname:String, val middlename: String, val phones : MutableList<Phone>) {

    fun createProtobufPerson() : DirectoryOuterClass.Person {
        val person = DirectoryOuterClass.Person.newBuilder()
            .setName(name)
            .setFirstname(firstname)
            .setMiddlename(middlename)

        for (phone in phones) {
            person.addPhone(phone.createProtobufPhone())
        }

        return person.build()
    }

    fun createSendingData() : ByteArray {
        val directory = DirectoryOuterClass.Directory.newBuilder()

        directory.addResults(createProtobufPerson())

        //directory.copy { peopleList += createProtobufPerson() }

        //val os : OutputStream = ByteArrayOutputStream();

        //directory.build().writeTo(os)

        return directory.build().toByteArray()
    }

    fun receiveProtobuf(directory : DirectoryOuterClass.Directory) : String {
        return protobufToString(directory)
    }

    fun protobufToString(directory : DirectoryOuterClass.Directory) : String {

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