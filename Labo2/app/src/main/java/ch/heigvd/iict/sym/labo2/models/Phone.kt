package ch.heigvd.iict.sym.labo2.models

import ch.heigvd.iict.sym.labo2.protobuf.DirectoryOuterClass

class Phone(val number: String, val type: Type) {
    enum class Type{
        HOME,
        MOBILE,
        WORK
    }

    fun createProtobufPhone() : DirectoryOuterClass.Phone {
        return DirectoryOuterClass.Phone.newBuilder()
            .setType(getDirectoryPhoneType(type))
            .setNumber(number)
            .build()
    }

    companion object {
        fun getDirectoryPhoneType(type : Type) : DirectoryOuterClass.Phone.Type {
            return when (type) {
                Type.HOME -> DirectoryOuterClass.Phone.Type.HOME
                Type.MOBILE -> DirectoryOuterClass.Phone.Type.MOBILE
                Type.WORK -> DirectoryOuterClass.Phone.Type.WORK
            }
        }

        fun getDirectoryPhoneType(type : DirectoryOuterClass.Phone.Type) : String {
            return when (type) {
                DirectoryOuterClass.Phone.Type.HOME -> "Mobile"
                DirectoryOuterClass.Phone.Type.MOBILE -> "Home"
                DirectoryOuterClass.Phone.Type.WORK -> "Work"
                else -> "Unknown Phone type"
            }
        }
    }
}