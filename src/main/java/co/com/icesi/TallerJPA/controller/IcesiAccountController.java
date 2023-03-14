package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.dto.IcesiAccountDTO;
import co.com.icesi.TallerJPA.service.IcesiAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/accounts")
public class IcesiAccountController {
    private final IcesiAccountService service;

    @PostMapping
    public String createAccount(@RequestBody IcesiAccountDTO dto){
        return service.save(dto) ?  "Account created successfully" : "";
    }

    @GetMapping
    public List<IcesiAccountDTO> getAccount(){
        return service.getAccounts();
    }

    /**
     * The map must use de following key
     * id: The id of the account that you want tu enable
     */
    @PatchMapping("/enable")
    public String enableAccount(@RequestBody Map<String,String> data){
        return service.manageAccount(data.get("id"),"enable") ? "Account enabled" :"Something went wrong, please check the id or if the account was already disabled";

    }

    /**
     * The map must use de following key
     * id: The id of the account that you want tu disable
     */

    @PatchMapping("/disable")
    public String disableAccount(@RequestBody Map<String,String> data){
        return service.manageAccount(data.get("id"),"disable") ? "Account disabled" :"Something went wrong, please check the id or make sure the balance of the account is  0";

    }

    /**
     * The map must use de following keys
     * account: The account number where the money will be given
     * amount: The amount of money that will be transferred
     */
    @PatchMapping("/withdrawal")
    public String withdrawal(@RequestBody Map<String,String> data){
        return service.withdrawalMoney(data.get("account"),Long.parseLong(data.get("amount"))) ? "withdrawal succeed!" : "Something went wrong, please check the account number or the amount of money";
    }

    /**
     * The map must use de following keys
     * account: The account where the money will be given
     * amount: The amount of money that will be transferred
     */

    @PatchMapping("/deposit")
    public String deposit(@RequestBody Map<String,String> data){
        return service.depositMoney(data.get("account"),Long.parseLong(data.get("amount"))) ? "deposit succeed!" : "Something went wrong, please check the account number or the amount of money";
    }

    /**
     * The map must use de following keys
     * source: The account number where the money will be taken
     * destiny: The account number where the money will be taken
     * amount: The amount of money that will be transferred
     */

    @PatchMapping("/transfer")
    public String  transfer(@RequestBody Map<String,String> data){
        return service.transferMoney(data.get("source"),data.get("destiny"),Long.parseLong(data.get("amount"))) ? "The action was made successfully" : "please check the account numbers or the amount of money";

    }




}
