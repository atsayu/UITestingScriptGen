*** Variables ***
${password}	xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']
${username}	xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']
${login_button}	xpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']

*** Setting ***
Library	SeleniumLibrary

*** Test Cases ***
Test login 1
	Open Browser	https://www.saucedemo.com/	Chrome
	Maximize Browser Window
	Input Text	${password}	124124
	Input Text	${username}	123123
	Click Element	${login_button}
Test-login-1
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   NOT124124
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   NOT123123
   Click Element   NOTxpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-2
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   NOT124124
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   123123
   Click Element   NOTxpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-3
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   124124
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   NOT123123
   Click Element   NOTxpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-4
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   NOT124124
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   NOT123123
   Click Element   xpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-5
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   NOT124124
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   123123
   Click Element   xpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-6
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   124124
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   NOT123123
   Click Element   xpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-7
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   124124
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   123123
   Click Element   NOTxpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
