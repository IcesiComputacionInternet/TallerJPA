package co.com.icesi.TallerJpa.util;

import co.com.icesi.TallerJpa.dto.IcesiUserRequestDTO;
import co.com.icesi.TallerJpa.dto.TransactionDTO;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TestItemsBuilder {

    private static PasswordEncoder encoder;

    @Autowired
    public TestItemsBuilder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public static IcesiUserRequestDTO defaultUserIcesiUserDTO() {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String firstName = faker.name().firstName();
        String phoneNumber = "+57" + faker.numerify("320#######");

        return IcesiUserRequestDTO.builder()
                .firstName(firstName)
                .lastName("Trujillo")
                .email(email)
                .phoneNumber(phoneNumber)
                .password(encoder.encode("password"))
                .role("USER")
                .build();
    }

    public static IcesiUserRequestDTO defaultBankIcesiUserDTO() {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String firstName = faker.name().firstName();
        String phoneNumber = "+57" + faker.numerify("320#######");

        return IcesiUserRequestDTO.builder()
                .firstName(firstName)
                .lastName("Trujillo")
                .email(email)
                .phoneNumber(phoneNumber)
                .password(encoder.encode("password"))
                .role("BANK")
                .build();
    }
    public static IcesiUserRequestDTO defaultAdminIcesiUserDTO() {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String firstName = faker.name().firstName();
        String phoneNumber = "+57" + faker.numerify("320#######");

        return IcesiUserRequestDTO.builder()
                .firstName(firstName)
                .lastName("Trujillo")
                .email(email)
                .phoneNumber(phoneNumber)
                .password(encoder.encode("password"))
                .role("ADMIN")
                .build();
    }
    public static TransactionDTO defaultTranferDTO(){
        return TransactionDTO.builder()
                .accountNumberOrigin("573-338604-81")
                .accountNumberDestiny("573-338604-83")
                .amount(1L)
                .build();
    }
    public static TransactionDTO defaultDepositDTO(){
        return TransactionDTO.builder()
                .accountNumberOrigin("573-338604-81")
                .amount(1L)
                .build();
    }
    public static TransactionDTO defaultWithdrawDTO(){
        return TransactionDTO.builder()
                .accountNumberOrigin("573-338604-81")
                .amount(1L).build();
    }
    public static String user1Normal(){
        return "573-338604-81";
    }
    public static String user1Deposit(){
        return "573-338604-82";
    }
    public static String user2Normal(){
        return "573-338604-83";
    }
    public static String user2Deposit(){
        return "573-338604-84";
    }
}
