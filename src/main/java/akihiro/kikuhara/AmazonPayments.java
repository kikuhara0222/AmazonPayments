package akihiro.kikuhara;

import org.springframework.stereotype.Component;

import com.amazon.payments.paywithamazon.Client;
import com.amazon.payments.paywithamazon.Config;
import com.amazon.payments.paywithamazon.impl.PaymentsClient;
import com.amazon.payments.paywithamazon.impl.PaymentsConfig;
import com.amazon.payments.paywithamazon.types.CurrencyCode;
import com.amazon.payments.paywithamazon.types.Region;

@Component
public class AmazonPayments {
	private String merchantId = "A2CA5WV3JBBY6V";
	private String accessKey = "AKIAIBMIW2JFGJLVFCAQ";
	private String secretKey = "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";
	private String clientId = "amzn1.application-oa2-client.1d1a190dbea14fa1af5adc490e902b3d";
	
	private	Config config = new PaymentsConfig()
				.withSellerId(merchantId)
				.withAccessKey(accessKey)
				.withSecretKey(secretKey)
				.withCurrencyCode(CurrencyCode.JPY)
				.withSandboxMode(true)
				.withRegion(Region.JP);
	
	private Client client = new PaymentsClient(config);

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}
