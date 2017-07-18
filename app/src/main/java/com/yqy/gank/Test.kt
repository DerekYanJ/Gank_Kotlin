package com.yqy.gank

/**
 *
 * Created by DerekYan on 2017/7/17.
 */
class Test {

    /*
    fun req() {
        val params = HashMap<String, String>()
        params.put("service", "User.Login")
        HttpRequest.getLogin(
                ProgressSubscriber<LoginBean>(this, this, 0,
                        getString(R.string.str_progress_msg_load)), params)
    }
    */

    inner class B {
        fun Int.foo(){
            val a =  this@Test
            val b = this@B
            val c = this
            val c1 = this@foo
            val funList = lambda@ fun String.(){
                val d = this
            }
        }
    }

}