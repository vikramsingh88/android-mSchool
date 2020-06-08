package com.vikram.school.ui.addfee.printer;

import com.vikram.school.ui.addfee.Fee;
import com.vikram.school.utility.Utility;

public class PrinterFormatter {
    public static String format(Fee fee, String studentName, String fatherName) {
        String formattedString = "";
        formattedString += "\n" +
                "*****************************" + "\n"+
                "SMT ROOPRANI VIDYA MANDIR" + "\n" +
                "LILAMBARPUR FATEHPUR" + "\n" +
                "Student name : " + studentName + "\n" +
                "Father name : " + fatherName + "\n" +
                "Fees month : " + Utility.formatDate(fee.getMonth()) + "\n" +
                "Amount Rs. : " + fee.getAmount() + "\n" +
                "Advance fees : " + fee.getAdvanceFee() + "\n" +
                "Remaining fees: " + fee.getRemainingFee() + "\n" +
                "Date time : " + Utility.formatDate(fee.getDate(), Utility.datePattern) + "\n" +
                "***********Thank you***********" + "\n"
                + "\n"
                + "\n" ;

        return formattedString;
    }
}
