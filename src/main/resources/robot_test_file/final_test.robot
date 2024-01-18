*** Variables ***
${password}	xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']
${username}	xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']
${login}	xpath=//input[@data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']

*** Setting ***
Library	SeleniumLibrary

*** Test Cases ***
Test Normal Login 1
	Open Browser	https://www.saucedemo.com/	Chrome
	Maximize Browser Window
	Input Text	${password}	hellovn
	Input Text	${username}	bui the
	Click Element	${login}
Test-Normal Login-1
Test-Normal Login-2
Test-Normal Login-3
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   NOT12345
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   NOTstandard_user
   Click Element   NOTxpath=//input[@data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   NOT12345
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   NOTstandard_user
   Click Element   NOTxpath=//input[@data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   NOT12345
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   NOTstandard_user
   Click Element   NOTxpath=//input[@data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-1
   Open Browser   https://demoqa.com/login   Edge
   objects.action.InputText@38082d64
   objects.action.ClickElement@dfd3711
   Should Go To   https://demoqa.com/profile
Test-login-2
   Open Browser   https://demoqa.com/login   Edge
   objects.action.InputText@42d3bd8b
   objects.action.ClickElement@26ba2a48
   Should Go To   https://demoqa.com/profile
Test-login-3
   Open Browser   https://demoqa.com/login   Edge
   objects.action.InputText@5f2050f6
   objects.action.ClickElement@64616ca2
   Should Go To   https://demoqa.com/profile
Test-login-4
   Open Browser   https://demoqa.com/login   Edge
   objects.action.InputText@3b81a1bc
   objects.action.ClickElement@13fee20c
   Should Go To   https://demoqa.com/profile
Test-login-1
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Click Element   NOTxpath=//input[@id='lastName' and @placeholder='Last Name']
   Should Go To   https://demoqa.com/profile
Test-login-2
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Click Element   xpath=//input[@id='lastName' and @placeholder='Last Name']
   Should Go To   https://demoqa.com/profile
Test-login-3
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   a1
   Click Element   NOTxpath=//input[@id='lastName' and @placeholder='Last Name']
   Should Go To   https://demoqa.com/profile
Test-login-4
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   a2
   Click Element   NOTxpath=//input[@id='lastName' and @placeholder='Last Name']
   Should Go To   https://demoqa.com/profile
Test-login-1
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Click Element   NOTxpath=//input[@id='lastName' and @placeholder='Last Name']
   Should Go To   https://demoqa.com/profile
Test-login-2
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Click Element   xpath=//input[@id='lastName' and @placeholder='Last Name']
   Should Go To   https://demoqa.com/profile
Test-login-3
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   a1
   Click Element   NOTxpath=//input[@id='lastName' and @placeholder='Last Name']
   Should Go To   https://demoqa.com/profile
Test-login-4
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   a2
   Click Element   NOTxpath=//input[@id='lastName' and @placeholder='Last Name']
   Should Go To   https://demoqa.com/profile
Test-login-1
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
Test-login-1
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTnull
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
#LINE7   it2
#LINE9   it3
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
#LINE7   it2
#LINE9   it3
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
#LINE7   it2
#LINE9   it3
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
#LINE7   it2
#LINE9   it3
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
#LINE7   it2
#LINE9   it3
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
#LINE7   it2
#LINE9   it3
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
#LINE7   it2
#LINE9   it3
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
#LINE7   it2
#LINE9   it3
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   c1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   d1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   c1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   d1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   c1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   d1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   c1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   d1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   c1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   d1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   c1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   d1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   c1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   d1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   c1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   d1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   c1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   d1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   c1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   d1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   c1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   d1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   c1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   d1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   c1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   d1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
#LINE5   lsb1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
#LINE11   esc1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   c1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   d1
   Element Should Contain   xpath=//input[@id='postalCode' and @placeholder='Postal Code']   e
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   test_url
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
   Element Should Contain   xpath=//input[@id='postalCode' and @placeholder='Postal Code']   e1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   test_url
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
   Element Should Contain   xpath=//input[@id='postalCode' and @placeholder='Postal Code']   e2
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   c1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   d1
   Element Should Contain   xpath=//input[@id='postalCode' and @placeholder='Postal Code']   e
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   test_url
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
   Element Should Contain   xpath=//input[@id='postalCode' and @placeholder='Postal Code']   e1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   test_url
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
   Element Should Contain   xpath=//input[@id='postalCode' and @placeholder='Postal Code']   e2
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   c1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   d1
   Element Should Contain   xpath=//input[@id='postalCode' and @placeholder='Postal Code']   e
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   test_url
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
   Element Should Contain   xpath=//input[@id='postalCode' and @placeholder='Postal Code']   e1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   test_url
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
   Element Should Contain   xpath=//input[@id='postalCode' and @placeholder='Postal Code']   e2
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   c1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   d1
   Element Should Contain   xpath=//input[@id='postalCode' and @placeholder='Postal Code']   e
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   test_url
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
   Element Should Contain   xpath=//input[@id='postalCode' and @placeholder='Postal Code']   e1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   test_url
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
   Element Should Contain   xpath=//input[@id='postalCode' and @placeholder='Postal Code']   e2
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   url1
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   c1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   d1
   Element Should Contain   xpath=//input[@id='postalCode' and @placeholder='Postal Code']   e
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   test_url
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
   Element Should Contain   xpath=//input[@id='postalCode' and @placeholder='Postal Code']   e1
Test-login
   Open Browser   https://demoqa.com/login   Edge
   Input Text   xpath=//input[@id='firstName' and @placeholder='First Name']   NOTa1
   Location Should Be   test_url
   Input Text   xpath=//input[@id='email' and @placeholder='example@email.com']   NOTc1
   Input Text   xpath=//input[@id='city' and @placeholder='City']   NOTd1
   Element Should Contain   xpath=//input[@id='postalCode' and @placeholder='Postal Code']   e2
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
