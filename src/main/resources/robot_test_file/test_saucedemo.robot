Test-login-1
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   NOTwrong_pass
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   NOTwrong_user
   Click Element   NOTxpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
   Should Go To   https://www.saucedemo.com/inventory.html
Test-login-2
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   NOTwrong_pass
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   wrong_user
   Click Element   NOTxpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
   Should Go To   https://www.saucedemo.com/inventory.html
Test-login-3
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   wrong_pass
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   NOTwrong_user
   Click Element   NOTxpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
   Should Go To   https://www.saucedemo.com/inventory.html
Test-login-4
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   user1
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   pass1
   Click Element   NOTxpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
   Should Go To   https://www.saucedemo.com/inventory.html
Test-login-5
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   user2
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   pass2
   Click Element   NOTxpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
   Should Go To   https://www.saucedemo.com/inventory.html
Test-login-6
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   user3
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   pass3
   Click Element   NOTxpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
   Should Go To   https://www.saucedemo.com/inventory.html
Test-login-7
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   NOTwrong_pass
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   NOTwrong_user
   Click Element   xpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
   Should Go To   https://www.saucedemo.com/inventory.html
Test-login-8
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   NOTwrong_pass
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   wrong_user
   Click Element   xpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
   Should Go To   https://www.saucedemo.com/inventory.html
Test-login-9
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   xpath=//input[@placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password']   wrong_pass
   Input Text   xpath=//input[@placeholder='Username' and @data-test='username' and @id='user-name' and @name='user-name']   NOTwrong_user
   Click Element   xpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
   Should Go To   https://www.saucedemo.com/inventory.html
