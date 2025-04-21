package project1;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SendSMS {
	public static final String ACCOUNT_SID = "ACb1ec7b3887a66249dfb6124e010798fe";
	public static final String AUTH_TOKEN = "ce4dc10da06085fb0f2b251d0b7b0c07";

	public static void main(String[] args) {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

		Message message = Message.creator(new PhoneNumber("+970 599 800 141"), // Recipient's phone number
				new PhoneNumber("+12515122537"), // Twilio number
				"Hello from Java!").create();

		System.out.println("Sent message SID: " + message.getSid());
	}
}
