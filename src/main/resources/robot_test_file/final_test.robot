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
