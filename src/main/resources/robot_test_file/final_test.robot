Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   c1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   d1
   Element Should Contain   xpath=//input[@id='postalCode' and @placeholder='Postal Code']   e1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
   Element Should Contain   xpath=//input[@id='postalCode' and @placeholder='Postal Code']   e1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
   Element Should Contain   xpath=//input[@id='postalCode' and @placeholder='Postal Code']   e2
