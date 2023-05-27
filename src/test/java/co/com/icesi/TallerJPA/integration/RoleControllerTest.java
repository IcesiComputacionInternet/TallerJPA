package co.com.icesi.TallerJPA.integration;

import co.com.icesi.TallerJPA.dto.AssignRoleDTO;
import co.com.icesi.TallerJPA.dto.RoleCreateDTO;
import co.com.icesi.TallerJPA.dto.UserCreateDTO;
import co.com.icesi.TallerJPA.integration.config.TestConfigurationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
public class RoleControllerTest {

    @Autowired
    private MockMvc mocMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${security.token.admin}")
    private String tokenAdmin;
    @Value("${security.token.user}")
    private String tokenUser;
    @Value("${security.token.bank}")
    private String tokenBank;

    @Test
    void contextLoads() {

    }
    @Test
    public void testRolesEndpointWhenUserNotAuth() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/roles").content(
                                objectMapper.writeValueAsString(defaultAdminCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    //These test are to validate the creation of a role according to the User type that is authenticated
    //Besides these tests were done fulfilling the custom anotations and the input validations (@Valid) of RoleCreateDTO
    @Test
    public void testRolesEndpointWhenUserAuthAdminToCreateRole() throws Exception {
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/roles").content(
                                objectMapper.writeValueAsString(defaultRoleCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testRolesEndpointWhenUserAuthUserToCreateRole() throws Exception {
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/roles").content(
                                objectMapper.writeValueAsString(defaultRoleCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isForbidden())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testRolesEndpointWhenUserAuthBankToCreateRole() throws Exception {
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/roles").content(
                                objectMapper.writeValueAsString(defaultRoleCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isForbidden())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }


    @Test   //Test to fail the validation @NotBlank name of RoleCreateDTO
    public void testRolesEndpointWhenUserAuthAdminToCreateBadRole() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/roles").content(
                                objectMapper.writeValueAsString(badRole())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    //Tests to validate the assignation of roles to users
    /*
    @Test
    public void testAssignRoleEndpointWhenUserAuthAdminToAssignRole() throws Exception{
        defaultUserCreateDTO();
        System.out.println("Muestrate");
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/assignRole").content(
                                objectMapper.writeValueAsString(assignRole())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println("Resultado: "+result.getResponse().getContentAsString());

    }

     */



    @Test
    public void testAssignRoleEndpointWhenUserAuthUserToAssignRole() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/assignRole").content(
                                objectMapper.writeValueAsString(assignRole())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isForbidden())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
    @Test
    public void testAssignRoleEndpointWhenUserAuthBankToAssignRole() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/assignRole").content(
                                objectMapper.writeValueAsString(assignRole())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenBank))
                .andExpect(status().isForbidden())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

    }

    //Tests to validate the input validations of the AssignRoleDTo
    @Test
    public void testAssignRoleEndpointWhenUserAuthAdminToAssignRoleWitoutUsername() throws Exception{
        defaultUserCreateDTO();
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/assignRole").content(
                                objectMapper.writeValueAsString(badAssignRole1())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isNotFound())
                .andReturn();

        System.out.println("Resultado: "+result.getResponse().getContentAsString());

    }

    @Test
    public void testAssignRoleEndpointWhenUserAuthAdminToAssignRoleWitoutRoleName() throws Exception{
        defaultUserCreateDTO();
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/assignRole").content(
                                objectMapper.writeValueAsString(badAssignRole2())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isNotFound())
                .andReturn();

        System.out.println("Resultado: "+result.getResponse().getContentAsString());

    }

    private UserCreateDTO defaultAdminCreateDTO(){
        return UserCreateDTO.builder()
                .firstName("Andres")
                .lastName("Gomez")
                .email("test@hotmail.com")
                .phoneNumber("+573154620529")
                .password("1234")
                .role("ADMIN")
                .build();
    }

    private RoleCreateDTO defaultRoleCreateDTO() {
        return RoleCreateDTO.builder()
                .name("SUPERADMIN")
                .description("Ninguna")
                .build();
    }

    private RoleCreateDTO badRole() {
        return RoleCreateDTO.builder()
                .name("")
                .description("Ninguna")
                .build();
    }

    private void defaultUserCreateDTO(){
        UserCreateDTO.builder()
                .firstName("Carlos")
                .lastName("Gomez")
                .email("test1@hotmail.com")
                .phoneNumber("+573151220529")
                .password("1234")
                .role("USER")
                .build();
    }
    private AssignRoleDTO assignRole(){
        return AssignRoleDTO.builder()
                .username("test1@email.com")
                .roleName("ADMIN")
                .build();
    }

    private AssignRoleDTO badAssignRole1(){
        return AssignRoleDTO.builder()
                .username("")
                .roleName("ADMIN")
                .build();
    }
    private AssignRoleDTO badAssignRole2(){
        return AssignRoleDTO.builder()
                .username("test1@email.com")
                .roleName("")
                .build();
    }

}
