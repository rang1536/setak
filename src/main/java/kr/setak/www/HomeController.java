package kr.setak.www;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.setak.www.domain.Order;
import kr.setak.www.domain.OrderItem;
import kr.setak.www.domain.User;
import kr.setak.www.service.SetakService;


@Controller
public class HomeController {
	
	@Autowired
	private SetakService setakService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		//세탁상품조회
		List<OrderItem> itemList = setakService.readAllItemServ();
		
		//스탭조회
		List<User> staffList = setakService.readStaffAllServ();
		
		//모든세탁물 조회
		List<Order> orderList = setakService.readAllOrderServ();
		
		model.addAttribute("orderList", orderList);
		model.addAttribute("itemList", itemList);
		model.addAttribute("staffList", staffList);
		
		return "index";
	}
	
}
