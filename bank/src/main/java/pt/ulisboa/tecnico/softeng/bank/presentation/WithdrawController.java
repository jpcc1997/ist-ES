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
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ClientData;

@Controller
@RequestMapping(value = "/banks/{bankCode}/clients/{clientID}/accounts/{iban}/withdraw")
public class WithdrawController {
	private static Logger logger = LoggerFactory.getLogger(WithdrawController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String showBalance(Model model,@PathVariable String bankCode, @PathVariable String clientID, @PathVariable String iban ) {
		logger.info("showBalance IBAN:{}", iban);

		ClientData clientData = BankInterface.getClientDataByID(clientID,bankCode, ClientData.CopyDepth.ACCOUNTS );
		BankData bankData = BankInterface.getBankDataByCode(bankCode , BankData.CopyDepth.CLIENTS);
		AccountData accountData = BankInterface.getAccountDataByIBAN(iban, clientID, bankCode);
		
		if (accountData == null) {
			model.addAttribute("error", "Error: it does not exist an account with the IBAN " + iban);
			model.addAttribute("account", new AccountData());
			model.addAttribute("accounts", BankInterface.getAccounts(clientID, bankCode));
			return "accounts";
		} else {
			model.addAttribute("operation", new BankOperationData());
			model.addAttribute("account", accountData);
			model.addAttribute("client", clientData);
			model.addAttribute("bank", bankData);
			return "withdraw";
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String withdrawSubmit(Model model, @ModelAttribute BankOperationData operationData,
			@PathVariable String bankCode, @PathVariable String clientID, @PathVariable String iban ){
		
		logger.info("withdrawSubmit");
		
		try {
			BankInterface.withdraw(bankCode, clientID, iban, operationData.getValue());
		} catch (BankException be) {
			model.addAttribute("error", "Error: could not withdraw specified ammount");
			model.addAttribute("operation", new BankOperationData());
			model.addAttribute("account", BankInterface.getAccountDataByIBAN(iban, clientID, bankCode));
			model.addAttribute("bank", BankInterface.getBankDataByCode(bankCode, BankData.CopyDepth.CLIENTS));
			model.addAttribute("client", BankInterface.getClientDataByID(clientID, bankCode, ClientData.CopyDepth.ACCOUNTS));
			return "withdraw";
		}

		return "redirect:/banks/" + bankCode + "/clients/" + clientID + "/accounts/" + iban + "/withdraw";
	}
}
