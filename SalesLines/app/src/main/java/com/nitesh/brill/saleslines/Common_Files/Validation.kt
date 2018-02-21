package com.nitesh.brill.saleslines.Common_Files


import android.content.Context
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.nitesh.brill.saleslines.R


class Validation(private val _context: Context) {


    /* return true if edit box is not empty otherwise return false */

    fun checkEmpty(edit: EditText, editName: String): Boolean {
        val str = edit.text.toString().trim { it <= ' ' }
        if (str.length == 0) {

         edit.error = editName + _context.getString(R.string.should_not_be_empty)
                    .toString()
            toastMsg(editName + " " + _context.getString(R.string.should_not_be_empty).toString())
            return true
        }
      //  edit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        return false
    }

    /* return true if edit box is contain spaces otherwise return false */

    fun checkSpaces(edit: EditText, editName: String): Boolean {
        val str = edit.text.toString().trim { it <= ' ' }
        if (str.contains(" ")) {


            edit.error = editName + " should not contain space"

            toastMsg(editName + " should not contain space");
            return true
        }
        return false
    }

    /* return true if email is valid otherwise return false */

    fun checkForEmail(edit: EditText, editName: String): Boolean {
        val str = edit.text.toString().trim()
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()) {
            edit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            return true
        }


       edit.error = editName + " "+_context.getString(R.string.is_not_valid)
               .toString()


        toastMsg(editName + " " + _context.getString(R.string.is_not_valid).toString());
        return false
    }

    /* return true if website is valid otherwise return false */

    fun checkForWebsite(edit: EditText, editName: String): Boolean {
        val str = edit.text.toString()
        if (android.util.Patterns.DOMAIN_NAME.matcher(str).matches()) {

            return true
        }
        edit.error = editName + " "              + _context.getString(R.string.is_not_valid).toString()

        toastMsg(editName + " " + _context.getString(R.string.is_not_valid).toString());
        return false
    }


    fun checkForPhone(edit: EditText, editName: String): Boolean {
        val str = edit.text.toString()
        if (android.util.Patterns.PHONE.matcher(str).matches()) {
            edit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            return true
        }

       edit.error = editName + " "           + _context.getString(R.string.is_not_valid).toString()

        toastMsg(editName + " " + _context.getString(R.string.is_not_valid).toString());

        return false
    }

    /* return true if space exist otherwise return false */
    fun checkForSpace(edit: EditText, editName: String): Boolean {
        val str = edit.text.toString().trim { it <= ' ' }
        if (str.contains(" ")) {

            edit.error = editName + " "                  + _context.getString(R.string.should_not_be_space).toString()

            toastMsg(editName + " " + _context.getString(R.string.should_not_be_space)
                    .toString());
            return true
        }
        return false
    }

//    /* return true if length is invalid otherwise return false */
//    fun checkForLength(edit: EditText, editName: String): Boolean {
//        val str = edit.text.toString().trim { it <= ' ' }
//        if (str.length < 8) {
//
//
//             edit.error = editName + " "                   + _context.getString(R.string.is_not_valid).toString()
//
//
//            toastMsg(editName + " " + _context.getString(R.string.is_not_valid).toString());
//            return true
//        }
//        return false
//    }

    /* return true if length is invalid otherwise return false */

    fun checkForLength(edit: EditText, editName: String): Boolean {
        val str = edit.text.toString().trim { it <= ' ' }
        if (str.length != 10 && str.length != 10) {

           edit.error = editName + " "                   + _context.getString(R.string.is_not_valid).toString()

            toastMsg(editName + " " + _context.getString(R.string.is_not_valid).toString());
            return true
        }
        return false
    }

    /* return true if integer is present otherwise return false */
    fun checkForInteger(edit: EditText, editName: String): Boolean {
        val str = edit.text.toString().trim { it <= ' ' }
        if (str.matches(".*\\d.*".toRegex())) {


          edit.error = editName + " "                   + _context.getString(R.string.is_not_valid).toString()


            toastMsg(editName + " " + _context.getString(R.string.is_not_valid).toString());
            return true
        }
        return false
    }

    // ======================================================================//

    fun checkSpecialChar(edit: EditText, editName: String): Boolean {
        val str = edit.text.toString().trim { it <= ' ' }
        if (!str.matches("[a-zA-Z.? ]*".toRegex())) {

           edit.error = editName + " "                   + _context.getString(R.string.is_not_valid).toString()

            toastMsg(editName + " " + _context.getString(R.string.is_not_valid).toString());
            return true
        }
        return false
    }

    // ======================================//


    fun toastMsg(msg: String) {


//        SnackBar.Builder((_context as Activity))
//
//                .withMessage(msg) // OR
//
//                .withTextColorId(R.color.white)
//                .withBackgroundColorId((R.color.red_))
//
//
//                .withDuration(SnackBar.SHORT_SNACK)
//                .show();


    }


}


