*** Variables ***
${password}	xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']
${username}	xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']
${login}	xpath=//input[@data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']

*** Setting ***
Library	SeleniumLibrary

*** Test Cases ***
Test Login 1
	Open Browser	https://www.saucedemo.com/	Chrome
	Maximize Browser Window
	Input Text	${password}	hello
	Input Text	${username}	standard_user
	Click Element	${login}
Test-Login-1
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   NOThello
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   NOTstandard_user
   Click Element   NOTxpath=//input[@data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
