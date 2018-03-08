package main.kotlin.org.itdd

import java.io.File
import java.io.InputStream
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

public val NO_OF_MONTHS = 12


/** Gross Income = Salary / 12 */
fun print_get_gross_income( salary: Int ): Int {
    if ( salary < 0 ) {
        println( "Salary Invalid.")
        return 0
    }

    var sbig = salary.toBigDecimal()
    var sal  = sbig.divide( NO_OF_MONTHS.toBigDecimal( ), 3, RoundingMode.HALF_UP )

    var gross_income = sal.setScale(0, RoundingMode.HALF_UP);

    return gross_income.intValueExact()
}


/** Net income = Gross income - Income tax - Both are rounded-off integers. */
fun print_get_net_income( gross_inc: Int, inc_tax: Int ): Int {

    if ( gross_inc >= 0 && inc_tax >= 0 ){
        var net_income = gross_inc - inc_tax

        if ( net_income < 0 ) { net_income = 0 }

        return net_income
    }
    return 0

}

/** Super = Gross Income * Super Rate / 100 */
fun print_get_super( gross_inc: Int, super_rt: BigDecimal ): Int {
    if ( gross_inc >= 0 && super_rt >= 0.toBigDecimal() ) {

        var sp  = ( gross_inc.toBigDecimal( ).multiply( super_rt ) ).divide( 100.toBigDecimal( ), 3, RoundingMode.HALF_UP )

        var spr = sp.setScale( 0, RoundingMode.HALF_UP );

        return spr.intValueExact()
    }
    return 0;
}



/** Income tax is based on the Tax_table.csv file. */
fun print_get_income_tax( salary: Int ): Int {

    val input_st: InputStream = File("/Users/malavika.vasudevan/IdeaProjects/PayrollChallenge/data/Tax_table.csv").inputStream()
    val line_list = mutableListOf<String>()

    input_st.bufferedReader().useLines { lines -> lines.forEach { line_list.add(it)} }
    line_list.forEach{ var arr = it.split( "," );
        try {

            var upper_lim = arr[ 1 ].toInt( )

            var lower_lim = arr[ 0 ].toInt( )


            if ( ( salary <= upper_lim ) && ( salary >= lower_lim ) ) {
                /** Salary falls in this range. */
                try {

                    var base = arr[ 2 ].toInt( )

                    var cents = arr[ 3 ].toFloat( )

                    var over = arr[ 4 ].toInt( )

                    /** Calculate income tax. */
                    var income_tax = ( base + ( salary - over ) * cents ) / NO_OF_MONTHS

                    /** Round decimal point */
                    var int_income_tax = Math.round( income_tax )
                    return int_income_tax
                } catch ( e: NumberFormatException ) {
                }
            }
        } catch( e: NumberFormatException ) {
        } }
    return 0
}


fun read_data( filename: String ): String {

    var output = ""
    val inputStream: InputStream = File(filename).inputStream()
    val lineList = mutableListOf<String>()

    /** String output variables. */
    var full_name = ""
    var period = ""
    var gross_income_str = ""
    var income_tax_str = ""
    var net_income_str = ""
    var super_str = ""
    inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it) } }
    /** Iterate over each line **/
    lineList.forEach {
        var toggle = 0
        var arr = it.split(",");
        if ( arr[ 0 ] != "" && arr[ 1 ] != "" ) {
            full_name = arr[0] + " " + arr[1]
        } else if ( arr[ 0 ] == "" && arr[ 1 ] == "" ) {
            full_name = "Name not entered."
        } else if ( arr[ 1 ] == "" ){
            full_name = arr[ 0 ]
        } else {
            full_name = arr[ 1 ]
        }


        /** Format y-M-d or yyyy-MM-d: https://www.programiz.com/kotlin-programming/examples/string-date */
        val date_str = arr[4];

        /** Date parsing exceptions */
        try {
            if ( arr[4] == "" ) {
                period = "Date not entered"
            } else {
                var start_date = LocalDate.parse(date_str, DateTimeFormatter.ISO_DATE);
                var end_date = start_date.plusMonths(1);

                period = start_date.toString( ) + " - " + end_date.toString( )
            }
        } catch ( e: DateTimeParseException ) {
            period = "Invalid date"
        }

        /** Salary parsing exceptions. */
        try {
            var salary = arr[ 2 ].toInt( );
            toggle = 1
            var gross_inc = print_get_gross_income(salary)
            gross_income_str = gross_inc.toString( )

            var income_tax = print_get_income_tax(salary)
            income_tax_str = income_tax.toString( )

            net_income_str = print_get_net_income(gross_inc, income_tax).toString( )


            var super_rt = arr[ 3 ].toBigDecimal( )
            super_str = print_get_super(gross_inc, super_rt).toString( )

        } catch ( e: NumberFormatException ) {
            var invalid_mess = ""
            if ( toggle == 0 ) {
                if ( arr[ 2 ] == "" ) {
                    invalid_mess = "Salary not entered"
                } else {
                    invalid_mess = "Invalid salary"
                }
                gross_income_str = invalid_mess
                income_tax_str = invalid_mess
                net_income_str = invalid_mess
                super_str = invalid_mess
            } else {
                if ( arr[ 3 ] == "" ) {
                    invalid_mess = "Super rate was not entered"
                } else {
                    invalid_mess = "Invalid super rate"
                }
                super_str = invalid_mess
            }


        }
        output = output + full_name + "," + period +
                "," + gross_income_str + "," + income_tax_str + "," +
                net_income_str + "," + super_str + "\n"
    }
    println( output )
    /** Write to file */
    File("/Users/malavika.vasudevan/IdeaProjects/PayrollChallenge/data/Output.txt").printWriter().use { out -> out.println( output ) }
    return output
}

fun main(args: Array<String>? = null) : String {
    var filename = ""
    if ( args != null) {
        if ( args.size != 0 ) {
            var user_filename = "/Users/malavika.vasudevan/IdeaProjects/PayrollChallenge/data/" + args[ 0 ]
            val file = File( user_filename )
            if ( file.exists() && !file.isDirectory ) {
                filename = user_filename
            } else {
                println("File Not Found!");
                return "File Not Found";
            }

        }
    } else {
        filename = "/Users/malavika.vasudevan/IdeaProjects/PayrollChallenge/data/data.csv"
    }
    return read_data(filename)
}
