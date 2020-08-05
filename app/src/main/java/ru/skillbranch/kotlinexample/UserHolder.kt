package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting

object UserHolder {
    private val map = mutableMapOf<String, User>()
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder(){
        map.clear()
    }
    fun registerUser(
        fullName: String,
        email :String,
        password: String
    ): User{
        return User.makeUser(fullName, email = email, password = password)
            .also { user ->
                if(map.containsKey(user.login)) throw IllegalArgumentException("A user with this email already exists")
                map[user.login] = user
            }
    }

    fun registerUserByPhone(
        fullName: String,
        phone :String
    ): User{
        return User.makeUser(fullName, phone = phone)
            .also { user ->
                if(map.containsKey(user.login)) throw IllegalArgumentException("A user with this phone already exists")

                map[user.login] = user
            }
    }


    fun loginUser(login : String, password: String) : String?{
        var phoneLogin = preparePhone(login)
        val phoneRegEx = Regex("^[+][0-9]{11}$")
        var preparedLogin : String = ""
        if(phoneRegEx.matches(phoneLogin)){
            preparedLogin = phoneLogin
        }
        else{
            preparedLogin = login.trim()
        }
        return map[preparedLogin]?.run {
            if(checkPassword(password)) this.userInfo
            else null
        }
    }

    fun requestAccessCode(phone: String) {
        var phoneLogin = preparePhone(phone)
        map[phoneLogin]?.run {
            updateAccessCode()
        }
    }

    private fun preparePhone(phone: String) : String{
       return phone.replace("[^+\\d]".toRegex(), " ").replace(" ", "")

    }


}