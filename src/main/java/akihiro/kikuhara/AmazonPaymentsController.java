package akihiro.kikuhara;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.amazon.payments.paywithamazon.Client;
import com.amazon.payments.paywithamazon.exceptions.AmazonServiceException;
import com.amazon.payments.paywithamazon.request.AuthorizeRequest;
import com.amazon.payments.paywithamazon.request.CloseOrderReferenceRequest;
import com.amazon.payments.paywithamazon.request.ConfirmOrderReferenceRequest;
import com.amazon.payments.paywithamazon.request.GetOrderReferenceDetailsRequest;
import com.amazon.payments.paywithamazon.request.SetOrderReferenceDetailsRequest;
import com.amazon.payments.paywithamazon.response.parser.AuthorizeResponseData;
import com.amazon.payments.paywithamazon.types.CurrencyCode;

@Controller
public class AmazonPaymentsController {

	@Autowired
	AmazonPayments amazonPayments;

	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("amazon", amazonPayments);
		return "index";
	}

	@RequestMapping("/next")
	public String next(Model model) {
		model.addAttribute("amazon", amazonPayments);
		return "next";
	}

	@RequestMapping(value = "/placeOrder", method = RequestMethod.POST)
	public String place(@RequestParam("amazonId") String amazonId, Model model) throws AmazonServiceException {

		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		String sellerOrderId = String.valueOf(hour + min + sec);
		Client client = amazonPayments.getClient();

		try {
			SetOrderReferenceDetailsRequest setOrderReferenceDetailsRequest = new SetOrderReferenceDetailsRequest(
					amazonId, "100.00");
			// set optional parameters
			setOrderReferenceDetailsRequest.setOrderCurrencyCode(CurrencyCode.JPY); // 言語？
			setOrderReferenceDetailsRequest.setSellerNote("my_note"); // 不明
			setOrderReferenceDetailsRequest.setSellerOrderId(sellerOrderId); // 注文番号
			setOrderReferenceDetailsRequest.setStoreName("store_name"); // ストア名

			client.setOrderReferenceDetails(setOrderReferenceDetailsRequest);

			ConfirmOrderReferenceRequest confirmRequest = new ConfirmOrderReferenceRequest(amazonId);
			client.confirmOrderReference(confirmRequest);

			// uniqueは注文のオーダー番号
			AuthorizeRequest authorizeRequest = new AuthorizeRequest(amazonId,
					setOrderReferenceDetailsRequest.getSellerOrderId(), "100.00");
			authorizeRequest.setAuthorizationCurrencyCode(CurrencyCode.JPY);
			authorizeRequest.setSellerAuthorizationNote("Your Authorization Note");
			authorizeRequest.setTransactionTimeout("0"); // Set to 0 for
															// synchronous mode
			authorizeRequest.setCaptureNow(true); // オーソリ後にキャプチャーのAPIを呼び出す設定

			// Call Authorize API
			AuthorizeResponseData response = client.authorize(authorizeRequest);
			client.authorize(authorizeRequest);

			System.out.println(response.getDetails());

			// Close the order reference once your one time
			CloseOrderReferenceRequest closeRequest = new CloseOrderReferenceRequest(amazonId);
			client.closeOrderReference(closeRequest);

			System.out.println("=============================:END");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return "end";
	}

}
