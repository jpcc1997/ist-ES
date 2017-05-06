package pt.ulisboa.tecnico.softeng.bank.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.bank.services.local.BankInterface;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.AccountData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ClientData;

@Controller
@RequestMapping(value = "/banks/{bankCode}/clients/{clientID}/accounts")
public class AccountController {
	private static Logger logger = LoggerFactory.getLogger(AccountController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String showAccounts(Model model,@PathVariable String bankCode ,@PathVariable String clientID ) {
		logger.info("showAccounts client:{}", clientID);

		ClientData clientData = BankInterface.getClientDataByID(clientID,bankCode, ClientData.CopyDepth.ACCOUNTS );
		BankData bankData = BankInterface.getBankDataByCode(bankCode , BankData.CopyDepth.CLIENTS);
		
		if (clientData == null) {
			model.addAttribute("error", "Error: it does not exist a client with the ID " + clientID);
			model.addAttribute("client", new ClientData());
			model.addAttribute("clients", BankInterface.getClients(bankCode));
			return "clients";
		} else {
			model.addAttribute("account", new AccountData());
			model.addAttribute("client", clientData);
			model.addAttribute("bank", bankData);
			return "accounts";
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String accountSubmit(Model model, @ModelAttribute AccountData accountData , @PathVariable String bankCode ,@PathVariable String clientID ){
		
		logger.info("bankSubmit code:{}, name:{}", accountData.getIBAN(), accountData.getBalance());
		
		try {
			BankInterface.createAccount(bankCode , clientID);
		} catch (BankException be) {
			model.addAttribute("error", "Error: it was not possible to create account");
			model.addAttribute("account", accountData);
			model.addAttribute("bank", BankInterface.getBankDataByCode(bankCode, BankData.CopyDepth.CLIENTS));
			model.addAttribute("client", BankInterface.getClientDataByID(clientID, bankCode, ClientData.CopyDepth.ACCOUNTS));
			return "accounts";
		}

		return "redirect:/banks/" + bankCode + "/clients/" + clientID + "/accounts";
	}
}
