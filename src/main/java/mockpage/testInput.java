package mockpage;

//import static org.example.newSolve.changDomAndCreateMockPage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static mockpage.newSolve.changDomAndCreateMockPage;

public class testInput {
  public static void main(String[] args) throws IOException {
    String[] input = {"https://thinking-tester-contact-list.herokuapp.com/addContact", "firstName", "lastName", "email", "city", "postalCode"
    ,"stateProvince", "phone", "dateBirth", "streetAddress1", "streetAddress2", "submitButton", "country"};
//    String[] input = {"https://demoqa.com/login", "username", "password", "login_button"};
//
    Map<String, String> mapLocatorVariableAndValueVariable = new HashMap<>();
//    mapLocatorVariableAndValueVariable.put("username", "username_value");
//    mapLocatorVariableAndValueVariable.put("password", "password_value");
    //mapLocatorVariableAndValueVariable.put("login_button", "login_value");
    mapLocatorVariableAndValueVariable.put("firstName", "first_name");
    mapLocatorVariableAndValueVariable.put("lastName", "last_name");
    mapLocatorVariableAndValueVariable.put("email", "valid_email");
    mapLocatorVariableAndValueVariable.put("city", "valid_city");
    mapLocatorVariableAndValueVariable.put("postalCode", "postal_code");
    mapLocatorVariableAndValueVariable.put("stateProvince", "state_province");
    mapLocatorVariableAndValueVariable.put("phone", "valid_phone");
    mapLocatorVariableAndValueVariable.put("dateBirth", "date_birth");
    mapLocatorVariableAndValueVariable.put("streetAddress1", "street_address_1");
      mapLocatorVariableAndValueVariable.put("streetAddress2", "street_address_2");
      mapLocatorVariableAndValueVariable.put("country", "valid_country");

//    Map<String, Vector<String>> mp = getDataFromCSV("C:\\Users\\PC\\Downloads\\data.csv");
//    fillInCSV("C:\\Users\\PC\\Downloads\\data_thinktester.csv","C:\\Users\\PC\\Downloads\\temp.csv", mp);
//    Vector<String> result = getLocator(input);
//    System.out.println(result);
  }
}
