package mzc.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PcfResourceCalculController {
	
	@Autowired private PcfResourceCalculService service;
	
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getAppImageUrl(Model model) {
    	PcfResourceCalculVO resultVo = service.getPcfAuthInfo();
    	model.addAttribute("records", resultVo);
    	return "view";
    }
}
