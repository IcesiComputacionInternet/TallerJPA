package co.com.icesi.TallerJpa.validations;

import co.com.icesi.TallerJpa.validations.cellphonenumber.ColombianPhoneNumberValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintValidatorContext;

import static org.mockito.Mockito.mock;

public class ColombianPhoneNumberValidatorTest {

    private ConstraintValidatorContext context;

    @BeforeEach
    public void init(){
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    @DisplayName("Valid colombian phone number with plus, country code and blanc space.")
    public void testPhoneNumberValidationWithPlusCountryCodeBlankSpace() {
        String number = validPhoneNumberWithPlusCountryCodeBlankSpace();
        Assertions.assertTrue(new ColombianPhoneNumberValidator().isValid(number, context));
    }
    @Test
    @DisplayName("Valid colombian phone number with plus and country code.")
    public void testPhoneNumberValidationWithPlusCountryCode(){
        String number = validPhoneNumberWithPlusCountryCode();
        Assertions.assertTrue(new ColombianPhoneNumberValidator().isValid(number, context));
    }

    @Test
    @DisplayName("Valid colombian phone number with country code and blank space.")
    public void testPhoneNumberValidationWithCountryCodeBlankSpace(){
        String number = validPhoneNumberWithCountryCodeBlankSpace();
        Assertions.assertTrue(new ColombianPhoneNumberValidator().isValid(number, context));
    }

    @Test
    @DisplayName("Valid colombian phone number with country code.")
    public void testPhoneNumberValidationWithCountryCode(){
        String number = validPhoneNumberWithCountryCode();
        Assertions.assertTrue(new ColombianPhoneNumberValidator().isValid(number, context));
    }

    @Test
    @DisplayName("Valid colombian phone number only.")
    public void testPhoneNumberValidationOnly(){
        String number = validPhoneNumberOnly();
        Assertions.assertTrue(new ColombianPhoneNumberValidator().isValid(number, context));
    }

    @Test
    @DisplayName("Invalid colombian phone number with different country code.")
    public void testInvalidPhoneNumberWithDifferentCountryCode(){
        String number = invalidPhoneNumberWithDifferentCountryCode();
        Assertions.assertFalse(new ColombianPhoneNumberValidator().isValid(number, context));
    }
    @Test
    @DisplayName("Invalid colombian phone number with different start symbol.")
    public void testInvalidPhoneNumberWithDifferentSymbol(){
        String number = invalidPhoneNumberWithDifferentSymbol();
        Assertions.assertFalse(new ColombianPhoneNumberValidator().isValid(number, context));
    }
    @Test
    @DisplayName("Invalid colombian phone number with more blank spaces.")
    public void testInvalidPhoneNumberWithMoreBlankSpaces(){
        String number = invalidPhoneNumberWithMoreBlankSpaces();
        Assertions.assertFalse(new ColombianPhoneNumberValidator().isValid(number, context));
    }
    @Test
    @DisplayName("Invalid colombian phone number with more numbers.")
    public void testInvalidPhoneNumberWithMoreNumbers(){
        String number = invalidPhoneNumberWithMoreNumbers();
        Assertions.assertFalse(new ColombianPhoneNumberValidator().isValid(number, context));
    }
    @Test
    @DisplayName("Invalid colombian phone number with less numbers.")
    public void testInvalidPhoneNumberWithLessNumbers(){
        String number = invalidPhoneNumberWithLessNumbers();
        Assertions.assertFalse(new ColombianPhoneNumberValidator().isValid(number, context));
    }
    @Test
    @DisplayName("Invalid colombian phone number with start in zero.")
    public void testInvalidPhoneNumberWithStartZero(){
        String number = invalidPhoneNumberWithStartZero();
        Assertions.assertFalse(new ColombianPhoneNumberValidator().isValid(number, context));
    }
    @Test
    @DisplayName("Invalid colombian phone number with start in blank space.")
    public void testInvalidPhoneNumberWithStartBlankSpace(){
        String number = invalidPhoneNumberWithStartBlankSpace();
        Assertions.assertFalse(new ColombianPhoneNumberValidator().isValid(number, context));
    }

    private String validPhoneNumberWithPlusCountryCodeBlankSpace(){
        return "+57 3209867514";
    }
    private String validPhoneNumberWithPlusCountryCode(){
        return "+573209867514";
    }
    private String validPhoneNumberWithCountryCodeBlankSpace(){
        return "57 3209867514";
    }
    private String validPhoneNumberWithCountryCode(){
        return "573209867514";
    }
    private String validPhoneNumberOnly(){
        return "3209867514";
    }
    private String invalidPhoneNumberWithDifferentCountryCode(){
        return "+58 3209867514";
    }
    private String invalidPhoneNumberWithDifferentSymbol(){
        return "*57 3209867514";
    }
    private String invalidPhoneNumberWithMoreBlankSpaces(){
        return "+57  3209867514";
    }
    private String invalidPhoneNumberWithMoreNumbers(){
        return "32098675141";
    }
    private String invalidPhoneNumberWithLessNumbers(){
        return "320986751";
    }
    private String invalidPhoneNumberWithStartZero(){
        return "0209867514";
    }
    private String invalidPhoneNumberWithStartBlankSpace(){
        return " 3209867514";
    }
}
