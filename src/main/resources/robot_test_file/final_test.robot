*** Variables ***
${password}	xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']
${username}	xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']
${login_button}	xpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']

*** Setting ***
Library	SeleniumLibrary

*** Test Cases ***
Test Login 1
    Open Browser	https://www.saucedemo.com/	Chrome
	Maximize Browser Window
	Input Text	${password}	p2
	Input Text	${username}	u2
	Click Element	${login_button}
Test-Login-1
    Open Browser   https://www.saucedemo.com/   Edge
    Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   NOTu1
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   NOTp1
   Click Element   NOTxpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test Login 1
	Open Browser	https://www.saucedemo.com/	Chrome
	Maximize Browser Window
	Input Text	${password}	p2
	Input Text	${username}	u2
	Click Element	${login_button}
Test-Login-1
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   NOTu1
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   NOTp1
   Click Element   NOTxpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
   Test Login 1
   	Open Browser	https://www.saucedemo.com/	Chrome
   	Maximize Browser Window
   	Input Text	${password}	p2
   	Input Text	${username}	u2
   	Click Element	${login_button}
   Test-Login-1
      Open Browser   https://www.saucedemo.com/   Edge
      Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   NOTu1
      Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   NOTp1
      Click Element   NOTxpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
      Test Login 1
      	Open Browser	https://www.saucedemo.com/	Chrome
      	Maximize Browser Window
      	Input Text	${password}	p2
      	Input Text	${username}	u2
      	Click Element	${login_button}
      Test-Login-1
         Open Browser   https://www.saucedemo.com/   Edge
         Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   NOTu1
         Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   NOTp1
         Click Element   NOTxpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
         Test Login 1
         	Open Browser	https://www.saucedemo.com/	Chrome
         	Maximize Browser Window
         	Input Text	${password}	p2
         	Input Text	${username}	u2
         	Click Element	${login_button}
         Test-Login-1
            Open Browser   https://www.saucedemo.com/   Edge
            Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   NOTu1
            Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   NOTp1
            Click Element   NOTxpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']