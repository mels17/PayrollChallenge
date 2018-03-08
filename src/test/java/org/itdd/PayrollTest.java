package test.java.org.itdd;

import main.kotlin.org.itdd.PayrollKt;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class PayrollTest {

    public static final String OUTPUT = "David  Rudd,2013-03-01 - 2013-04-01,5004,922,4082,450" + "\n" +
            "Ryan Chen,2013-02-15 - 2013-03-15,10000,2669,7331,1000\n";
    public static final String TEST_OUTPUT = "Rudd,Invalid date,Invalid salary,Invalid salary,Invalid salary,Invalid salary" +
     "\n" + "Ryan Chen,Date not entered,10000,2669,7331,1000" + "\n" +
    "John Doe,2013-03-15 - 2013-04-15,41667,16519,25148,Invalid super rate" + "\n";
    /*
        #----------------------------------#
        # Tests for GROSS INCOME function. #
        #----------------------------------#
    */
    @Test
    public void ValidInputReturnCalculatedGross( ) {
        assertThat( PayrollKt.print_get_gross_income( 24000 ) ).isEqualTo( 2000 );
    }

    @Test
    public void ValidInputReturnRoundedGross( ) {
        assertThat( PayrollKt.print_get_gross_income( 5000 ) ).isEqualTo( 417 );
    }

    @Test
    public void ZeroInputReturnZeroGross( ) {
        assertThat( PayrollKt.print_get_gross_income( 0 ) ).isEqualTo( 0 );
    }

    @Test
    public void NegativeInputReturnZeroGross( ) {
        assertThat( PayrollKt.print_get_gross_income( -25767 ) ).isEqualTo( 0 );
    }

    // Boundary case for rounding. >= 0.50 cents
    @Test
    public void BoundaryRoundGross( ) {
        assertThat( PayrollKt.print_get_gross_income( 26562 ) ).isEqualTo( 2214 );
    }



    /*
        #--------------------------------#
        # Tests for NET INCOME function. #
        #--------------------------------#
    */
    @Test
    public void ValidInputsReturnCalculatedNet( ) {
        assertThat( PayrollKt.print_get_net_income( 34000, 2500 ) ).isEqualTo( 31500 );
    }

    @Test
    public void ZeroInputsReturnZeroNet( ) {
        assertThat( PayrollKt.print_get_net_income( 0, 0 ) ).isEqualTo( 0 );
    }

    @Test
    public void NegativeInputReturnZeroNet( ) {
        assertThat( PayrollKt.print_get_net_income( -25767, -2586 ) ).isEqualTo( 0 );
    }

    @Test
    public void TaxGreaterThanGrossReturnZeroNet( ) {
        assertThat( PayrollKt.print_get_net_income( 5000, 25806 ) ).isEqualTo( 0 );
    }

    @Test
    public void TaxNegGrossPosThanGrossReturnZeroNet( ) {
        assertThat( PayrollKt.print_get_net_income( 5000, -25806 ) ).isEqualTo( 0 );
    }

    @Test
    public void TaxPosGrossNegThanGrossReturnZeroNet( ) {
        assertThat( PayrollKt.print_get_net_income( -5000, 25806 ) ).isEqualTo( 0 );
    }



    /*  #---------------------------#
        # Tests for SUPER function. #
        #---------------------------#
     */
    @Test
    public void ValidInputReturnCalculatedSuper( ) {
        assertThat( PayrollKt.print_get_super( 24000, BigDecimal.valueOf( 13.83 ) ) ).isEqualTo( 3319 );
    }

    @Test
    public void IntRateReturnSuper( ) {
        assertThat( PayrollKt.print_get_super( 24000, BigDecimal.valueOf( 13 ) ) ).isEqualTo( 3120 );
    }

    @Test
    public void ZeroInputsReturnZero( ) {
        assertThat( PayrollKt.print_get_super( 0, BigDecimal.valueOf( 0 ) ) ).isEqualTo( 0 );
    }

    @Test
    public void ZeroGrossReturnZeroSuper( ) {
        assertThat( PayrollKt.print_get_super( 0, BigDecimal.valueOf( 13 ) ) ).isEqualTo( 0 );
    }

    @Test
    public void ZeroRateReturnZeroSuper( ) {
        assertThat( PayrollKt.print_get_super( 24000, BigDecimal.valueOf( 0 ) ) ).isEqualTo( 0 );
    }

    @Test
    public void NegativeInputsReturnZero( ) {
        assertThat( PayrollKt.print_get_super( -24000, BigDecimal.valueOf( -0 ) ) ).isEqualTo( 0 );
    }

    @Test
    public void NegativeGrossReturnZero( ) {
        assertThat( PayrollKt.print_get_super( -24000, BigDecimal.valueOf( 13 ) ) ).isEqualTo( 0 );
    }

    @Test
    public void NegativeRateReturnZero( ) {
        assertThat( PayrollKt.print_get_super( 24000, BigDecimal.valueOf( -13 ) ) ).isEqualTo( 0 );
    }

    @Test
    public void BoundaryRoundReturnSuper( ) {
        assertThat( PayrollKt.print_get_super( 10000, BigDecimal.valueOf( 22.135 ) ) ).isEqualTo( 2214 );
    }

    @Test
    public void MoreDecimalsRate( ) {
        assertThat( PayrollKt.print_get_super( 5000, BigDecimal.valueOf( 1.34525667894534784534235454 ) ) ).isEqualTo( 67 );
    }

    /*
        #-------------------------------#
        # Tests for INCOME TAX function #
        #-------------------------------#
     */

    @Test
    public void ZeroSalaryZeroTax( ) {
        assertThat( PayrollKt.print_get_income_tax( 0 ) ).isEqualTo( 0 );
    }

    @Test
    public void BoundaryOneUpper( ) {
        assertThat( PayrollKt.print_get_income_tax( 18200 ) ).isEqualTo( 0 );
    }

    @Test
    public void BoundaryOneMiddle( ) {
        assertThat( PayrollKt.print_get_income_tax( 10000 ) ).isEqualTo( 0 );
    }

    @Test
    public void BoundaryTwoLower( ) {
        assertThat( PayrollKt.print_get_income_tax( 18201 ) ).isEqualTo( 0 );
    }

    @Test
    public void BoundaryTwoUpper( ) {
        assertThat( PayrollKt.print_get_income_tax( 37000 ) ).isEqualTo( 298 );
    }

    @Test
    public void BoundaryTwoMiddleRounding( ) {
        assertThat( PayrollKt.print_get_income_tax( 20000 ) ).isEqualTo( 29 );
    }

    @Test
    public void BoundaryThreeLower( ) {
        assertThat( PayrollKt.print_get_income_tax( 37001 ) ).isEqualTo( 298 );
    }

    @Test
    public void BoundaryThreeUpper( ) {
        assertThat( PayrollKt.print_get_income_tax( 87000 ) ).isEqualTo( 1652 );
    }

    @Test
    public void BoundaryThreeMiddle( ) {
        assertThat( PayrollKt.print_get_income_tax( 40000 ) ).isEqualTo( 379 );
    }

    @Test
    public void BoundaryFourLower( ) {
        assertThat( PayrollKt.print_get_income_tax( 87001 ) ).isEqualTo( 1652 );
    }

    @Test
    public void BoundaryFourUpper( ) {
        assertThat( PayrollKt.print_get_income_tax( 180000 ) ).isEqualTo( 4519 );
    }

    @Test
    public void BoundaryFourMiddleRound( ) {
        assertThat( PayrollKt.print_get_income_tax( 95000 ) ).isEqualTo( 1899 );
    }

    @Test
    public void BoundaryLastLower( ) {
        assertThat( PayrollKt.print_get_income_tax( 180001 ) ).isEqualTo( 4519 );
    }

    @Test
    public void BoundaryLastUpper( ) {
        assertThat( PayrollKt.print_get_income_tax( 350000 ) ).isEqualTo( 10894 );
    }

    @Test
    public void BoundaryLastMiddle( ) {
        assertThat( PayrollKt.print_get_income_tax( 500000 ) ).isEqualTo( 16519 );
    }





    /*  #--------------------------#
        # Tests for MAIN function. #
        #--------------------------#
    */
    @Test
    public void MainFileNotExist( ) {
        String[ ] para = { "payroll_data.csv" };
        assertThat( PayrollKt.main( para ) ).isEqualTo( "File Not Found" );
    }

    @Test
    public void TestMainWithoutFileExtension( ) {
         String[ ] para = { "123" };
         assertThat( PayrollKt.main( para ) ).isEqualTo( "File Not Found" );
     }

    @Test
    public void TestMainWithoutInput( ) {
         assertThat( PayrollKt.main( null ) ).isEqualTo( OUTPUT );
    }

    @Test
    public void TestMainWithTestData( ) {
        String[ ] para = { "test-data.csv" };
        assertThat( PayrollKt.main( para ) ).isEqualTo( TEST_OUTPUT );
    }
}